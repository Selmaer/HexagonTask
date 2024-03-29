package tile;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.LinkedList;
import java.util.List;

public class Hexagon extends Polygon {
    private LinkedList<Hexagon> neighbors;
    private final Point2D centerPoint;
    private final int radius;
    private final double innerRadius;

    public Hexagon(Point2D centerPoint, int radius) {
        this.radius = radius;
        this.centerPoint = centerPoint;

        this.innerRadius = Math.sqrt(0.75 * Math.pow(radius, 2));
        setPoints(centerPoint);
    }

    public double getDistance(Point2D point) {
        double x1 = centerPoint.getX();
        double y1 = centerPoint.getY();
        double x2 = point.getX();
        double y2 = point.getY();

        return Math.hypot(x1 - x2, y1 - y2);
    }

    public LinkedList<Hexagon> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Hexagon> hexList) {
        neighbors = new LinkedList<>();
        Point2D[] neighborsCenterPoints = calculatePossibleNeighbors();
        for (Point2D point : neighborsCenterPoints) {
            for (Hexagon hex : hexList) {
                if (hex.contains(point.getX(), point.getY())) {
                    neighbors.add(hex);
                }
            }
        }
    }

    public void fillHexagon(GraphicsContext gc, Color color) {
        gc.setFill(color);
        gc.fillPolygon(getXPoints(), getYPoints(), 6);
    }

    public void strokeHexagon(GraphicsContext gc, Color color) {
        gc.setStroke(color);
        gc.strokePolygon(getXPoints(), getYPoints(), 6);
    }

    private Point2D[] calculatePossibleNeighbors() {
        double centerPointX = centerPoint.getX();
        double centerPointY = centerPoint.getY();
        double shiftBetweenRows = 1.5 * radius;
        return new Point2D[]{
                new Point2D(centerPointX + innerRadius, centerPointY - shiftBetweenRows),
                new Point2D(centerPointX + (innerRadius * 2), centerPointY),
                new Point2D(centerPointX + innerRadius, centerPointY + shiftBetweenRows),
                new Point2D(centerPointX - innerRadius, centerPointY + shiftBetweenRows),
                new Point2D(centerPointX - (innerRadius * 2), centerPointY),
                new Point2D(centerPointX - innerRadius, centerPointY - shiftBetweenRows),
        };
    }

    /*This method sets all perimeter points of hexagon using its central point*/
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

    public Point2D getCenterPoint() {
        return centerPoint;
    }
}
