import exceptions.NoPathException;
import path.Path;
import tile.Hexagon;
import tile.Tilemap;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;
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
            hex.fillHexagon(gc, Color.DARKCYAN);
        }

        ArrayList<Hexagon> hexToFindPath = new ArrayList<>(2);


        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Hexagon hex = map.getClosestHexagon(new Point2D(e.getSceneX(), e.getSceneY()));

            System.out.println(hex.getCenterPoint());
            if(hex.contains(e.getSceneX(), e.getSceneY())) {
                if (hexToFindPath.size() == 2) {
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    for (Hexagon hex1 : hexList) {
                        hex1.fillHexagon(gc, Color.DARKCYAN);
                    }
                    hexToFindPath.clear();
                }
                hex.fillHexagon(gc, Color.CORAL);
                hexToFindPath.add(hex);
                if (hexToFindPath.size() == 2) {
                    System.out.println("full");
                    try {
                        Path pf = new Path();

                        List<Hexagon> path = pf.find(hexToFindPath.get(0), hexToFindPath.get(1), hexList);
                        System.out.println(path.size());
                        for(Hexagon hexy : path) {

                            hexy.fillHexagon(gc, Color.CHOCOLATE);
                        }

                    } catch (NoPathException ex) {
                        System.out.println(ex.getMessage());
                    }
//                    List<Hexagon> neighs = Path.sortByProximity(hexToFindPath.get(0).getNeighbors(), hexToFindPath.get(1));
//
//                    for (Hexagon hex2 : neighs) {
//                        System.out.println(hex2.getCenterPoint());
//                    }
                }
            }
        });
    }
}