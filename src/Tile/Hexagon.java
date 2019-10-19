package Tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

public class Hexagon extends Polygon {
    private final int radius;
    private final Coordinate centerCoord;

    private final double innerRadius;

    private double calculateInnerRadius(int radius) {
        return Math.sqrt(0.75 * Math.pow(radius, 2));
    }

    public double getDistance(Coordinate coord) {
        double x1 = centerCoord.getXCoord();
        double y1 = centerCoord.getYCoord();
        double x2 = coord.getXCoord();
        double y2 = coord.getYCoord();

        return Math.hypot(x1 - x2, y1 - y2);
    }

    private void setPointsCoords(Coordinate centerCoord) {
        double centerXCoord = centerCoord.getXCoord();
        double centerYCoord = centerCoord.getYCoord();

        getPoints().addAll(
                centerXCoord, centerYCoord - radius,
                centerXCoord + innerRadius, centerYCoord - (radius / 2),
                centerXCoord + innerRadius, centerYCoord + (radius / 2),
                centerXCoord, centerYCoord + radius,
                centerXCoord - innerRadius, centerYCoord + (radius / 2),
                centerXCoord - innerRadius, centerYCoord - (radius / 2)
        );
    }

    private Coordinate[] calculatePossibleNeighbors (double spaceBetweenTiles) {
        double mainXCoord = centerCoord.getXCoord();
        double mainYCoord = centerCoord.getYCoord();
        double yAxisShiftBetweenTiles = Math.sqrt(0.75) * spaceBetweenTiles + 1.5 * radius;

        Coordinate[] dotsCoords = new Coordinate[6];
        dotsCoords[0] = new Coordinate(
                mainXCoord + ((spaceBetweenTiles / 2) + innerRadius),
                mainYCoord - yAxisShiftBetweenTiles);
        dotsCoords[1] = new Coordinate(
                mainXCoord + (spaceBetweenTiles + (innerRadius * 2)),
                mainYCoord);
        dotsCoords[2] = new Coordinate(
                mainXCoord + ((spaceBetweenTiles / 2) + innerRadius),
                mainYCoord + yAxisShiftBetweenTiles);
        dotsCoords[3] = new Coordinate(
                mainXCoord - ((spaceBetweenTiles / 2) + innerRadius),
                mainYCoord + yAxisShiftBetweenTiles);
        dotsCoords[4] = new Coordinate(
                mainXCoord - (spaceBetweenTiles + (innerRadius * 2)),
                mainYCoord);
        dotsCoords[5] = new Coordinate(
                mainXCoord - ((spaceBetweenTiles / 2) + innerRadius),
                (mainYCoord - yAxisShiftBetweenTiles));
        return dotsCoords;
    }

    public Hexagon (Coordinate centerCoord, int radius) {
        this.radius = radius;
        this.centerCoord = centerCoord;

        this.innerRadius = calculateInnerRadius(radius);
        setPointsCoords(centerCoord);
    }

    private double[] getXCoords() {
        double[] xCoords = new double[6];
        for (int i = 0; i < xCoords.length; i++) {
            xCoords[i] = getPoints().get(i * 2);
        }
        return xCoords;
    }
    private double[] getYCoords() {
        double[] yCoords = new double[6];
        for (int i = 0; i < yCoords.length; i++) {
            yCoords[i] = getPoints().get(i * 2 + 1);
        }
        return yCoords;
    }

    public Coordinate getCoord() {
        return centerCoord;
    }

    public Coordinate[] getPossibleNeighbors(double spaceBetweenTiles) {
        return calculatePossibleNeighbors(spaceBetweenTiles);
    }

    public void fillHexagon(GraphicsContext gc) {
        gc.fillPolygon(getXCoords(), getYCoords(), 6);
    }

    public void strokeHexagon(GraphicsContext gc) {
        gc.strokePolygon(getXCoords(), getYCoords(), 6);
    }
}
