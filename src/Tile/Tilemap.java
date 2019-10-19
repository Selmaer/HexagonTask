package Tile;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Tilemap {
    private static final int PROBABILITY_INDEX = 4;
    private final int SPACE_BETWEEN_TILES;
    private final int WINDOW_WIDTH;
    private final int WINDOW_HEIGHT;
    private final int HEXAGON_RADIUS;
    private final double HEXAGON_INNER_RADIUS;

    private final int maxRows;
    private final int maxTilesPerRow;
    private List<Hexagon> hexagonList;

    public Hexagon getClosestHexagon (Coordinate coord) {
        Hexagon hexagon = hexagonList.get(0);
        double distance = WINDOW_WIDTH;
        for (Hexagon hex : hexagonList) {
            double dist = hex.getDistance(coord);
            if (dist < distance) {
                distance = dist;
                hexagon = hex;
            }
        }
        return hexagon;
    }

    public List<Hexagon> createMap() {
        Random rand = new Random();
        hexagonList = new LinkedList<>();
        List<Coordinate> possibleCoords = getAllPossibleCenters();
        for (Coordinate coord : possibleCoords) {
            int randomCoefficient = rand.nextInt((int) (Math.pow(PROBABILITY_INDEX, 2)));
            int creationProbability = getCreationProbability(coord);
            if (randomCoefficient <= creationProbability) {
                hexagonList.add(new Hexagon(coord, HEXAGON_RADIUS));
            }
        }
        return hexagonList;
    }

    private int getCreationProbability(Coordinate coord) {
        int probX = (int) (Math.pow(PROBABILITY_INDEX, 2) -
                Math.pow((PROBABILITY_INDEX * coord.getXCoord() / WINDOW_WIDTH) - Math.sqrt(PROBABILITY_INDEX), PROBABILITY_INDEX));
        int probY = (int) (Math.pow(PROBABILITY_INDEX, 2) -
                Math.pow((PROBABILITY_INDEX * coord.getYCoord() / WINDOW_HEIGHT) - Math.sqrt(PROBABILITY_INDEX), PROBABILITY_INDEX));
        return Math.min(probX, probY);
    }

    public Tilemap(int windowWidth, int windowHeight, int tileSize, int spaceBetweenTiles) {
        WINDOW_WIDTH = windowWidth;
        WINDOW_HEIGHT = windowHeight;
        HEXAGON_RADIUS = tileSize;
        SPACE_BETWEEN_TILES = spaceBetweenTiles;
        HEXAGON_INNER_RADIUS = HEXAGON_RADIUS * Math.sqrt(0.75);
        this.maxRows = calculateMaxRows();
        this.maxTilesPerRow = calculateMaxTiles();
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

    public List<Coordinate> getAllPossibleCenters() {
        Coordinate startingCoord = new Coordinate(HEXAGON_RADIUS + SPACE_BETWEEN_TILES, HEXAGON_RADIUS + SPACE_BETWEEN_TILES);
        double startingXCoord = startingCoord.getXCoord();
        double startingYCoord = startingCoord.getYCoord();
        List<Coordinate> coordinateList = new LinkedList<>();
        for (int y = 0; y < maxRows; y++) {
            for (int x = 0; x < maxTilesPerRow; x++) {
                double xCoord = startingXCoord + x * (HEXAGON_INNER_RADIUS * 2 + SPACE_BETWEEN_TILES) +
                        (y % 2) * (HEXAGON_INNER_RADIUS + ((double)SPACE_BETWEEN_TILES / 2));
                double yCoord = startingYCoord + y * (HEXAGON_RADIUS * 1.5 + Math.sqrt(0.75) * SPACE_BETWEEN_TILES);

                coordinateList.add(new Coordinate(xCoord, yCoord));
            }
        }
        return coordinateList;
    }
}
