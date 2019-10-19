import Tile.Coordinate;
import Tile.Hexagon;
import Tile.Tilemap;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {
    private final int WINDOW_WIDTH = 1400;
    private final int WINDOW_HEIGHT = 800;
    private final int SPACE_BETWEEN_TILES = 3;
    private final int HEXAGON_RADIUS = 20;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hexagon Map");
        Group root = new Group();
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMap(gc, canvas);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void drawMap(GraphicsContext gc, Canvas canvas) {
        gc.setFill(Color.DARKCYAN);
        gc.setStroke(Color.CORAL);
        gc.setLineWidth(5);

        Tilemap map = new Tilemap(WINDOW_WIDTH, WINDOW_HEIGHT, HEXAGON_RADIUS, SPACE_BETWEEN_TILES);

        List<Hexagon> hexList = map.createMap();
        for (Hexagon hex : hexList) {
            hex.fillHexagon(gc);
        }

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Hexagon hex = map.getClosestHexagon(new Coordinate(e.getSceneX(), e.getSceneY()));
            if(hex.contains(e.getSceneX(), e.getSceneY())) {
                hex.strokeHexagon(gc);
            }
        });
    }
}