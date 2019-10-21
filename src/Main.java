import exceptions.NoPathException;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import json.JSONParser;
import json.SavedData;
import path.PathFinder;
import tile.Hexagon;
import tile.Tilemap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main extends Application {
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;
    private final Integer SPACE_BETWEEN_TILES = 3;
    private final int HEXAGON_RADIUS = 20;

    private final Color HEXAGON_GRID_COLOR = Color.DARKCYAN;
    private final Color START_HEXAGON_COLOR = Color.DARKGREEN;
    private final Color FINISH_HEXAGON_COLOR = Color.DARKRED;
    private final Color PATH_COLOR = Color.DARKGOLDENROD;

    private Tilemap map;
    private List<Hexagon> selectedHexagons = new ArrayList<>(2);

    private Button newButton;
    private Button saveButton;
    private Button loadButton;

    private Canvas mapCanvas;
    private Canvas actionCanvas;

    private String json;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hexagon Map");
        Group root = new Group();

        mapCanvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        actionCanvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);

        setButtons();

        drawMap();
        responseToActivity();

        root.getChildren().addAll(mapCanvas, actionCanvas, newButton, saveButton, loadButton);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void setButtons() {
        newButton = new Button("New map");
        saveButton = new Button("Save to JSON");
        loadButton = new Button("Load from JSON");
        newButton.setMinSize(100, 20);
        saveButton.setMinSize(100, 20);
        loadButton.setMinSize(100, 20);
        saveButton.setDisable(true);
        loadButton.setDisable(true);
        saveButton.setLayoutX(100);
        loadButton.setLayoutX(200);
    }

    private void drawMap() {
        clearCanvas(mapCanvas);
        clearCanvas(actionCanvas);
        GraphicsContext mapGC = mapCanvas.getGraphicsContext2D();

        map = new Tilemap(mapCanvas, HEXAGON_RADIUS, SPACE_BETWEEN_TILES);
        map.drawMap(mapGC);
    }

    private void drawMap(SavedData savedData) {
        clearCanvas(mapCanvas);
        clearCanvas(actionCanvas);

        map = new Tilemap(savedData);
        map.drawMap(mapCanvas.getGraphicsContext2D());
    }

    private void responseToActivity() {
        GraphicsContext actionGC = actionCanvas.getGraphicsContext2D();
        actionCanvas.toFront();
        actionGC.setFill(HEXAGON_GRID_COLOR);

        newButton.setOnMouseClicked(e -> {
            saveButton.setDisable(true);
            selectedHexagons.clear();
            drawMap();
        });

        saveButton.setOnMouseClicked(e -> {
            json = JSONParser.toJSON(map, selectedHexagons);
            System.out.println(json);
            loadButton.setDisable(false);
        });

        loadButton.setOnMouseClicked(e -> {
            SavedData savedData = JSONParser.fromJSON(json);
            drawMap(savedData);
            List<Hexagon> hexagons = new LinkedList<>();
            hexagons.add(map.getClosestHexagon(savedData.SELECTED_HEXAGON_1));
            hexagons.add(map.getClosestHexagon(savedData.SELECTED_HEXAGON_2));
            showPath(hexagons);
        });

        actionCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Point2D mousePoint = new Point2D(e.getSceneX(), e.getSceneY());
            Hexagon hex = map.getClosestHexagon(mousePoint);

            if (hex.contains(mousePoint)) {
                if (selectedHexagons.size() == 2) {
                    clearCanvas(actionCanvas);
                    selectedHexagons.clear();
                    saveButton.setDisable(true);
                }
                hex.fillHexagon(actionGC, START_HEXAGON_COLOR);
                hex.strokeHexagon(actionGC, START_HEXAGON_COLOR);
                selectedHexagons.add(hex);
                if (selectedHexagons.size() == 2) {
                    showPath(selectedHexagons);
                }
            }
        });
    }

    private void clearCanvas(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void showPath(List<Hexagon> selectedHexagons) {
        GraphicsContext actionGC = actionCanvas.getGraphicsContext2D();

        List<Hexagon> pathList;
        try {
            pathList = PathFinder.findPath(selectedHexagons.get(0), selectedHexagons.get(1));

            Hexagon startHex = pathList.remove(pathList.size() - 1);
            startHex.fillHexagon(actionGC, START_HEXAGON_COLOR);
            startHex.strokeHexagon(actionGC, START_HEXAGON_COLOR);

            Hexagon finishHex = pathList.remove(0);
            finishHex.fillHexagon(actionGC, FINISH_HEXAGON_COLOR);
            finishHex.strokeHexagon(actionGC, FINISH_HEXAGON_COLOR);

            for (Hexagon hexagon : pathList) {
                hexagon.strokeHexagon(actionGC, PATH_COLOR);
            }
            saveButton.setDisable(false);
        } catch (NoPathException ex) {
            System.out.println(ex.getMessage());
        }
    }
}