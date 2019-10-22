package json;

import javafx.geometry.Point2D;
import tile.Hexagon;
import tile.Tilemap;

import java.util.List;

/*This is the object I use to save data into JSON. For now there is no need to save more than seed and selected hexagons,
 * but to import to and then export data from external source all of the fields might be necessary*/
public class SavedData {
    public final int SEED;
    public final int WINDOW_WIDTH;
    public final int WINDOW_HEIGHT;
    public final int HEXAGON_RADIUS;

    public final Point2D SELECTED_HEXAGON_1;
    public final Point2D SELECTED_HEXAGON_2;

    public SavedData(Tilemap map, List<Hexagon> selectedHexagons) {
        this.SEED = map.SEED;
        this.WINDOW_WIDTH = map.WINDOW_WIDTH;
        this.WINDOW_HEIGHT = map.WINDOW_HEIGHT;
        this.HEXAGON_RADIUS = map.HEXAGON_RADIUS;
        this.SELECTED_HEXAGON_1 = selectedHexagons.get(0).getCenterPoint();
        this.SELECTED_HEXAGON_2 = selectedHexagons.get(1).getCenterPoint();
    }
}
