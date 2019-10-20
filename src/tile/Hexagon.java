package tile;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.LinkedList;
import java.util.List;

public class Hexagon extends Polygon {
    private LinkedList<Hexagon> neighbors;
    private final int radius;
    private final Point2D centerPoint;

    private final double innerRadius;

    private double calculateInnerRadius(int radius) {
        return Math.sqrt(0.75 * Math.pow(radius, 2));
    }

    public double getDistance(Point2D point) {
        double x1 = centerPoint.getX();
        double y1 = centerPoint.getY();
        double x2 = point.getX();
        double y2 = point.getY();

        return Math.hypot(x1 - x2, y1 - y2);
    }

    private void setPoints(Point2D centerPoint) {
        double centerPointX = centerPoint.getX();
        double centerPointY = centerPoint.getY();

        getPoints().addAll(
                centerPointX, centerPointY - radius,
                centerPointX + innerRadius, centerPointY - (radius / 2),
                centerPointX + innerRadius, centerPointY + (radius / 2),
                centerPointX, centerPointY + radius,
                centerPointX - innerRadius, centerPointY + (radius / 2),
                centerPointX - innerRadius, centerPointY - (radius / 2)
        );
    }

    public LinkedList<Hexagon> getNeighbors() {
        return neighbors;
    }

    public Hexagon (Point2D centerPoint, int radius) {
        this.radius = radius;
        this.centerPoint = centerPoint;

        this.innerRadius = calculateInnerRadius(radius);
        setPoints(centerPoint);
    }

    private double[] getXPoints() {
        double[] pointsX = new double[6];
        for (int i = 0; i < pointsX.length; i++) {
            pointsX[i] = getPoints().get(i * 2);
        }
        return pointsX;
    }
    private double[] getYPoints() {
        double[] pointsY = new double[6];
        for (int i = 0; i < pointsY.length; i++) {
            pointsY[i] = getPoints().get(i * 2 + 1);
        }
        return pointsY;
    }
    public void setNeighbors (List<Hexagon> hexList, int spaceBetweenTiles) {
        neighbors = new LinkedList<>();
        Point2D[] neighborsCenterPoints = calculatePossibleNeighbors(spaceBetweenTiles);
        for (Point2D point : neighborsCenterPoints) {
            for (Hexagon hex : hexList) {
                if (hex.contains(point.getX(), point.getY())) {
                    neighbors.add(hex);
                }
            }
        }
    }

    private Point2D[] calculatePossibleNeighbors (double spaceBetweenTiles) {
        double centerPointX = centerPoint.getX();
        double centerPointY = centerPoint.getY();
        double yAxisShiftBetweenTiles = Math.sqrt(0.75) * spaceBetweenTiles + 1.5 * radius;

        Point2D[] neighborsCenterPoints = new Point2D[6];
        neighborsCenterPoints[0] = new Point2D(
                centerPointX + ((spaceBetweenTiles / 2) + innerRadius),
                centerPointY - yAxisShiftBetweenTiles);
        neighborsCenterPoints[1] = new Point2D(
                centerPointX + (spaceBetweenTiles + (innerRadius * 2)),
                centerPointY);
        neighborsCenterPoints[2] = new Point2D(
                centerPointX + ((spaceBetweenTiles / 2) + innerRadius),
                centerPointY + yAxisShiftBetweenTiles);
        neighborsCenterPoints[3] = new Point2D(
                centerPointX - ((spaceBetweenTiles / 2) + innerRadius),
                centerPointY + yAxisShiftBetweenTiles);
        neighborsCenterPoints[4] = new Point2D(
                centerPointX - (spaceBetweenTiles + (innerRadius * 2)),
                centerPointY);
        neighborsCenterPoints[5] = new Point2D(
                centerPointX - ((spaceBetweenTiles / 2) + innerRadius),
                (centerPointY - yAxisShiftBetweenTiles));
        return neighborsCenterPoints;
    }

    public Point2D getCenterPoint() {
        return centerPoint;
    }

    public void fillHexagon(GraphicsContext gc, Color color) {
        gc.setFill(color);
        gc.fillPolygon(getXPoints(), getYPoints(), 6);
    }

    public void strokeHexagon(GraphicsContext gc, Color color) {
        gc.setStroke(color);
        gc.strokePolygon(getXPoints(), getYPoints(), 6);
    }
}
