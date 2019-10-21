package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tile.Hexagon;
import tile.Tilemap;

import java.util.List;

public class JSONParser {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String toJSON (Tilemap map, List<Hexagon> selectedHexagons) {
        SavedData savedData = new SavedData(map, selectedHexagons);
        String json = GSON.toJson(savedData);
        return json;
    }

    public static SavedData fromJSON (String json) {
        SavedData savedData = GSON.fromJson(json, SavedData.class);
        return savedData;
    }
}
