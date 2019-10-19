import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class Test extends Application {

    private final static int WINDOW_WIDTH = 800;
    private final static int WINDOW_HEIGHT = 600;

    private final static double r = 20; // the inner radius from hexagon center to outer corner
    private final static double n = Math.sqrt(r * r * 0.75); // the inner radius from hexagon center to middle of the axis
    private final static double TILE_HEIGHT = 2 * r;
    private final static double TILE_WIDTH = 2 * n;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        AnchorPane tileMap = new AnchorPane();
        Scene content = new Scene(tileMap, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(content);

        int rowCount = 14; // how many rows of tiles should be created
        int tilesPerRow = 16; // the amount of tiles that are contained in each row
        int xStartOffset = 40; // offsets the entire field to the right
        int yStartOffset = 40; // offsets the entire fiels downwards

        for (int x = 0; x < tilesPerRow; x++) {
            for (int y = 0; y < rowCount; y++) {
                double xCoord = x * TILE_WIDTH + (y % 2) * n + xStartOffset;
                double yCoord = y * TILE_HEIGHT * 0.75 + yStartOffset;

                Polygon tile = new Tile(xCoord, yCoord);
                tileMap.getChildren().add(tile);
            }
        }
        primaryStage.show();
    }

    private class Tile extends Polygon {
        Tile(double x, double y) {
            // creates the polygon using the corner coordinates
            getPoints().addAll(
                    x, y,
                    x, y + r,
                    x + n, y + r * 1.5,
                    x + TILE_WIDTH, y + r,
                    x + TILE_WIDTH, y,
                    x + n, y - r * 0.5
            );

            // set up the visuals and a click listener for the tile
            setFill(Color.ANTIQUEWHITE);
            setStrokeWidth(1);
            setStroke(Color.BLACK);
            setOnMouseClicked(e -> {
                if (this.getFill() == Color.AZURE) {
                    setFill(Color.ANTIQUEWHITE);
                } else {
                    this.setFill(Color.AZURE);
                }
                System.out.println("Clicked: " + this);
            });
            setOnMouseEntered(e -> {
                if (this.getFill() == Color.AZURE) {
                    setFill(Color.AQUA);
                } else {
                    this.setFill(Color.WHITESMOKE);
                }
            });
            setOnMouseExited(e -> {
                if (this.getFill() == Color.AQUA) {
                    setFill(Color.AZURE);
                } else {
                    this.setFill(Color.ANTIQUEWHITE);
                }
            });
        }
    }
}