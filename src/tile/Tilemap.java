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
    public final int SEED; //seed is useful while importing/exporting map to/from JSON
    public final int WINDOW_WIDTH;
    public final int WINDOW_HEIGHT;
    public final int HEXAGON_RADIUS;
    private final double HEXAGON_INNER_RADIUS;
    private final int BUTTON_HEIGHT = 25; //is used to avoid buttons placed over hexagons

    private final int MAX_ROWS;
    private final int MAX_TILES_PER_ROW;
    private final List<Hexagon> HEXAGON_LIST;

    public Tilemap(Canvas canvas, int tileSize) {
        this.SEED = (int) (Math.random() * 1000000);
        this.WINDOW_WIDTH = (int) canvas.getWidth();
        this.WINDOW_HEIGHT = (int) canvas.getHeight();
        this.HEXAGON_RADIUS = tileSize;
        this.HEXAGON_INNER_RADIUS = HEXAGON_RADIUS * Math.sqrt(0.75);
        this.MAX_ROWS = calculateMaxRows();
        this.MAX_TILES_PER_ROW = calculateMaxTiles();
        this.HEXAGON_LIST = createMap();
        removeSeparateHexagons();
    }

    public Tilemap(SavedData savedData) {
        this.SEED = savedData.SEED;
        this.WINDOW_WIDTH = savedData.WINDOW_WIDTH;
        this.WINDOW_HEIGHT = savedData.WINDOW_HEIGHT;
        this.HEXAGON_RADIUS = savedData.HEXAGON_RADIUS;
        this.HEXAGON_INNER_RADIUS = HEXAGON_RADIUS * Math.sqrt(0.75);
        this.MAX_ROWS = calculateMaxRows();
        this.MAX_TILES_PER_ROW = calculateMaxTiles();
        this.HEXAGON_LIST = createMap();
        removeSeparateHexagons();
    }

    public void drawMap(GraphicsContext gc) {
        gc.setLineWidth(0);
        for (Hexagon hex : HEXAGON_LIST) {
            hex.fillHexagon(gc, Color.DARKCYAN);
            hex.strokeHexagon(gc, Color.WHITE);
        }
    }

    /*This method returns hexagon that is closest to the point he got as an argument*/
    public Hexagon getClosestHexagon(Point2D point) {
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

    private List<Hexagon> createMap() {
        Random rand = new Random(SEED);
        List<Hexagon> hexList = new LinkedList<>();
        List<Point2D> possiblePoints = getAllPossibleCenters();
        for (Point2D point : possiblePoints) {
            int randomCoefficient = rand.nextInt(16);
            int creationProbability = getCreationProbability(point);
            if (randomCoefficient <= creationProbability) {
                hexList.add(new Hexagon(point, HEXAGON_RADIUS));
            }
        }
        for (Hexagon hex : hexList) {
            hex.setNeighbors(hexList);
        }
        return hexList;
    }

    /*To find the probability to create each hexagon I'm using this function: f(x) = -(x^2/z - 2)^4 + 16,
     * where x is a position of the hexagon on axis (X or Y) and z is a total length of the axis. The graph
     * of this function gives almost 100% probability (the index is 16) of hexagon creation near the center
     * of the canvas and rapid decrease of probability towards its edges. */
    private int getCreationProbability(Point2D point) {
        int probX = (int) (16 - Math.pow((4 * point.getX() / WINDOW_WIDTH) - 2, 4));
        int probY = (int) (16 - Math.pow((4 * point.getY() / (WINDOW_HEIGHT - BUTTON_HEIGHT)) - 2, 4));
        return Math.min(probX, probY);
    }

    private int calculateMaxTiles() {
        return (int) ((WINDOW_WIDTH - HEXAGON_INNER_RADIUS) / (HEXAGON_INNER_RADIUS * 2));
    }

    private int calculateMaxRows() {
        return (int) (WINDOW_HEIGHT - BUTTON_HEIGHT / (HEXAGON_RADIUS * 1.5));
    }

    /*This method returns center points of all possible hexagons on the canvas*/
    private List<Point2D> getAllPossibleCenters() {
        Point2D startingPoint = new Point2D(HEXAGON_RADIUS, HEXAGON_RADIUS + BUTTON_HEIGHT);
        double startingPointX = startingPoint.getX();
        double startingPointY = startingPoint.getY();
        List<Point2D> pointList = new LinkedList<>();
        for (int y = 0; y < MAX_ROWS; y++) {
            for (int x = 0; x < MAX_TILES_PER_ROW; x++) {
                double pointX = startingPointX + x * (HEXAGON_INNER_RADIUS * 2) +
                        (y % 2) * HEXAGON_INNER_RADIUS;
                double pointY = startingPointY + y * (HEXAGON_RADIUS * 1.5);

                pointList.add(new Point2D(pointX, pointY));
            }
        }
        return pointList;
    }

    /*This method removes all hexagons that are separated from the main map
     * and therefore there is no path to find them*/
    private void removeSeparateHexagons() {
        List<Hexagon> separateHexagons = findSeparateHexagons();
        for(Hexagon hex : separateHexagons) {
            HEXAGON_LIST.remove(hex);
        }
    }

    private List<Hexagon> findSeparateHexagons() {
        List<Hexagon> separateHexagons = new LinkedList<>();
        Hexagon centerHex = getCentralHexagon();
        for (Hexagon hex : HEXAGON_LIST) {
            try {
                PathFinder.findPath(hex, centerHex);
            } catch (NoPathException e) {
                separateHexagons.add(hex);
            }
        }
        return separateHexagons;
    }

    private Hexagon getCentralHexagon() {
        Point2D point = new Point2D(WINDOW_WIDTH / 2, (WINDOW_HEIGHT + BUTTON_HEIGHT) / 2);
        return getClosestHexagon(point);
    }
}