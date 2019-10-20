package tile;

import exceptions.NoPathException;
import javafx.geometry.Point2D;
import path.Path;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Tilemap {
    private final int SEED;
    private static final int PROBABILITY_INDEX = 4;
    private final int SPACE_BETWEEN_TILES;
    private final int WINDOW_WIDTH;
    private final int WINDOW_HEIGHT;
    private final int HEXAGON_RADIUS;
    private final double HEXAGON_INNER_RADIUS;

    private final int maxRows;
    private final int maxTilesPerRow;
    private List<Hexagon> hexagonList;

    public Tilemap(int windowWidth, int windowHeight, int tileSize, int spaceBetweenTiles) {
        SEED = (int) (Math.random() * 1000000);
        WINDOW_WIDTH = windowWidth;
        WINDOW_HEIGHT = windowHeight;
        HEXAGON_RADIUS = tileSize;
        SPACE_BETWEEN_TILES = spaceBetweenTiles;
        HEXAGON_INNER_RADIUS = HEXAGON_RADIUS * Math.sqrt(0.75);
        this.maxRows = calculateMaxRows();
        this.maxTilesPerRow = calculateMaxTiles();
    }

    public List<Hexagon> createMap() {
        Random rand = new Random(SEED);
        hexagonList = new LinkedList<>();
        List<Point2D> possiblePoints = getAllPossibleCenters();
        for (Point2D point : possiblePoints) {
            int randomCoefficient = rand.nextInt((int) (Math.pow(PROBABILITY_INDEX, 2)));
            int creationProbability = getCreationProbability(point);
            if (randomCoefficient <= creationProbability) {
                hexagonList.add(new Hexagon(point, HEXAGON_RADIUS));
            }
        }
        for (Hexagon hex : hexagonList) {
            hex.setNeighbors(hexagonList, SPACE_BETWEEN_TILES);
        }
//        removeSeparateHexagons();
        return hexagonList;
    }

    private int getCreationProbability(Point2D point) {
        int probX = (int) (Math.pow(PROBABILITY_INDEX, 2) -
                Math.pow((PROBABILITY_INDEX * point.getX() / WINDOW_WIDTH) - Math.sqrt(PROBABILITY_INDEX), PROBABILITY_INDEX));
        int probY = (int) (Math.pow(PROBABILITY_INDEX, 2) -
                Math.pow((PROBABILITY_INDEX * point.getY() / WINDOW_HEIGHT) - Math.sqrt(PROBABILITY_INDEX), PROBABILITY_INDEX));
        return Math.min(probX, probY);
    }

    private int calculateMaxTiles() {
        return (int)
                ((WINDOW_WIDTH - HEXAGON_INNER_RADIUS) /
                        ((HEXAGON_INNER_RADIUS * 2) + SPACE_BETWEEN_TILES));
    }

    private int calculateMaxRows() {
        return (int)
                (WINDOW_HEIGHT /
                        (Math.sqrt(0.75) * SPACE_BETWEEN_TILES + 1.5 * HEXAGON_RADIUS));
    }

    public List<Point2D> getAllPossibleCenters() {
        Point2D startingPoint = new Point2D(HEXAGON_RADIUS + SPACE_BETWEEN_TILES, HEXAGON_RADIUS + SPACE_BETWEEN_TILES);
        double startingPointX = startingPoint.getX();
        double startingPointY = startingPoint.getY();
        List<Point2D> pointList = new LinkedList<>();
        for (int y = 0; y < maxRows; y++) {
            for (int x = 0; x < maxTilesPerRow; x++) {
                double pointX = startingPointX + x * (HEXAGON_INNER_RADIUS * 2 + SPACE_BETWEEN_TILES) +
                        (y % 2) * (HEXAGON_INNER_RADIUS + ((double)SPACE_BETWEEN_TILES / 2));
                double pointY = startingPointY + y * (HEXAGON_RADIUS * 1.5 + Math.sqrt(0.75) * SPACE_BETWEEN_TILES);

                pointList.add(new Point2D(pointX, pointY));
            }
        }
        return pointList;
    }

    private void removeSeparateHexagons() {
        Hexagon centerHex = getCenterHexagon();
        for (Hexagon hex : hexagonList) {
            Path path = new Path();
            try {
                path.find(hex, centerHex, hexagonList);
            } catch (NoPathException e) {
                hexagonList.remove(hex);
            }
        }
    }

    private Hexagon getCenterHexagon () {
        Point2D point = new Point2D(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
        return getClosestHexagon(point);
    }

    public Hexagon getClosestHexagon (Point2D point) {
        Hexagon hexagon = hexagonList.get(0);
        double distance = WINDOW_WIDTH;
        for (Hexagon hex : hexagonList) {
            double dist = hex.getDistance(point);
            if (dist < distance) {
                distance = dist;
                hexagon = hex;
            }
        }
        return hexagon;
    }
}
