package Tile;

public class Coordinate {
    private final double xCoord;
    private final double yCoord;

    public Coordinate(double xCoord, double yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public double getXCoord() {
        return xCoord;
    }

    public double getYCoord() {
        return yCoord;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "xCoord=" + (int)xCoord +
                ", yCoord=" + (int)yCoord +
                '}';
    }
}