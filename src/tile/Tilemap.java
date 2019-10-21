package tile;

import exceptions.NoPathException;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import json.SavedData;
import path.PathFinder;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Tilemap {
    public final int SEED;
    private static final int PROBABILITY_INDEX = 4;
    public final int WINDOW_WIDTH;
    public final int WINDOW_HEIGHT;
    public final int HEXAGON_RADIUS;
    private final double HEXAGON_INNER_RADIUS;
    public final int SPACE_BETWEEN_TILES;
    private final int BUTTON_HEIGHT = 25;

    private final int MAX_ROWS;
    private final int MAX_TILES_PER_ROW;
    private final List<Hexagon> HEXAGON_LIST;

    public Tilemap(Canvas canvas, int tileSize, int spaceBetweenTiles) {
        this.SEED = (int) (Math.random() * 1000000);
        this.WINDOW_WIDTH = (int) canvas.getWidth();
        this.WINDOW_HEIGHT = (int) canvas.getHeight();
        this.HEXAGON_RADIUS = tileSize;
        this.SPACE_BETWEEN_TILES = spaceBetweenTiles;
        this.HEXAGON_INNER_RADIUS = HEXAGON_RADIUS * Math.sqrt(0.75);
        this.MAX_ROWS = calculateMaxRows();
        this.MAX_TILES_PER_ROW = calculateMaxTiles();
        this.HEXAGON_LIST = createMap();
    }

    public Tilemap(SavedData savedData) {
        this.SEED = savedData.SEED;
        this.WINDOW_WIDTH = savedData.WINDOW_WIDTH;
        this.WINDOW_HEIGHT = savedData.WINDOW_HEIGHT;
        this.HEXAGON_RADIUS = savedData.HEXAGON_RADIUS;
        this.SPACE_BETWEEN_TILES = savedData.SPACE_BETWEEN_TILES;
        this.HEXAGON_INNER_RADIUS = HEXAGON_RADIUS * Math.sqrt(0.75);
        this.MAX_ROWS = calculateMaxRows();
        this.MAX_TILES_PER_ROW = calculateMaxTiles();
        this.HEXAGON_LIST = createMap();
    }

    private List<Hexagon> createMap() {
        Random rand = new Random(SEED);
        List<Hexagon> hexList = new LinkedList<>();
        List<Point2D> possiblePoints = getAllPossibleCenters();
        for (Point2D point : possiblePoints) {
            int randomCoefficient = rand.nextInt((int) (Math.pow(PROBABILITY_INDEX, 2)));
            int creationProbability = getCreationProbability(point);
            if (randomCoefficient <= creationProbability) {
                hexList.add(new Hexagon(point, HEXAGON_RADIUS));
            }
        }
        for (Hexagon hex : hexList) {
            hex.setNeighbors(hexList, SPACE_BETWEEN_TILES);
        }
//TODO        removeSeparateHexagons();
        return hexList;
    }

    private int getCreationProbability(Point2D point) {
        int probX = (int) (Math.pow(PROBABILITY_INDEX, 2) -
                Math.pow((PROBABILITY_INDEX * point.getX() / WINDOW_WIDTH) - Math.sqrt(PROBABILITY_INDEX), PROBABILITY_INDEX));
        int probY = (int) (Math.pow(PROBABILITY_INDEX, 2) -
                Math.pow((PROBABILITY_INDEX * point.getY() / (WINDOW_HEIGHT - BUTTON_HEIGHT)) - Math.sqrt(PROBABILITY_INDEX), PROBABILITY_INDEX));
        return Math.min(probX, probY);
    }

    private int calculateMaxTiles() {
        return (int)
                ((WINDOW_WIDTH - HEXAGON_INNER_RADIUS) /
                        ((HEXAGON_INNER_RADIUS * 2) + SPACE_BETWEEN_TILES));
    }

    private int calculateMaxRows() {
        return (int)
                (WINDOW_HEIGHT - BUTTON_HEIGHT /
                        (Math.sqrt(0.75) * SPACE_BETWEEN_TILES + 1.5 * HEXAGON_RADIUS));
    }

    private List<Point2D> getAllPossibleCenters() {
        Point2D startingPoint = new Point2D(
                HEXAGON_RADIUS + SPACE_BETWEEN_TILES,
                HEXAGON_RADIUS + SPACE_BETWEEN_TILES + BUTTON_HEIGHT);
        double startingPointX = startingPoint.getX();
        double startingPointY = startingPoint.getY();
        List<Point2D> pointList = new LinkedList<>();
        for (int y = 0; y < MAX_ROWS; y++) {
            for (int x = 0; x < MAX_TILES_PER_ROW; x++) {
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
        for (Hexagon hex : HEXAGON_LIST) {
            PathFinder pathFinder = new PathFinder();
            try {
                pathFinder.findPath(hex, centerHex);
            } catch (NoPathException e) {
                HEXAGON_LIST.remove(hex);
            }
        }
    }

    private Hexagon getCenterHexagon () {
        Point2D point = new Point2D(WINDOW_WIDTH / 2, WINDOW_HEIGHT + BUTTON_HEIGHT / 2);
        return getClosestHexagon(point);
    }

    public Hexagon getClosestHexagon (Point2D point) {
        Hexagon hexagon = HEXAGON_LIST.get(0);
        double distance = WINDOW_WIDTH;
        for (Hexagon hex : HEXAGON_LIST) {
            double dist = hex.getDistance(point);
            if (dist < distance) {
                distance = dist;
                hexagon = hex;
            }
        }
        return hexagon;
    }

    public void drawMap(GraphicsContext gc) {
        for (Hexagon hex : HEXAGON_LIST) {
            hex.fillHexagon(gc, Color.DARKCYAN);
        }
    }
}
