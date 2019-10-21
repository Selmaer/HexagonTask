package json;

import javafx.geometry.Point2D;
import tile.Hexagon;
import tile.Tilemap;

import java.util.List;

public class SavedData {
    public final int SEED;
    public final int WINDOW_WIDTH;
    public final int WINDOW_HEIGHT;
    public final int HEXAGON_RADIUS;
    public final int SPACE_BETWEEN_TILES;

    public final Point2D SELECTED_HEXAGON_1;
    public final Point2D SELECTED_HEXAGON_2;

    public SavedData(Tilemap map, List<Hexagon> selectedHexagons) {
        this.SEED = map.SEED;
        this.WINDOW_WIDTH = map.WINDOW_WIDTH;
        this.WINDOW_HEIGHT = map.WINDOW_HEIGHT;
        this.HEXAGON_RADIUS = map.HEXAGON_RADIUS;
        this.SPACE_BETWEEN_TILES = map.SPACE_BETWEEN_TILES;
        this.SELECTED_HEXAGON_1 = selectedHexagons.get(0).getCenterPoint();
        this.SELECTED_HEXAGON_2 = selectedHexagons.get(1).getCenterPoint();
    }
}
