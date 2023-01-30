import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * JAVAFX DESCRIPTION:
 * For the correct work of program it is necessary download and install JavaFX application.
 * File - Project Structure - Libraries - From Maven, in the searching line type fx,
 * Choose from the list org.openjfx:javafx-fxml:11.0.2
 * Install in the folder with program.
 * In Run - Edit Configurations - VM options write
 * --module-path "/Users/pras/IdeaProjects/Wordle/lib" --add-modules javafx.controls,javafx.fxml
 */

public class Puzzle extends Application {

    static final int GRID_SIZE_X = 4;
    static final int GRID_SIZE_Y = 4;
    static int[][] grid = new int[GRID_SIZE_Y][GRID_SIZE_X];
    static final int[] offset_X = {0, 0, -1, 1};
    static final int[] offset_Y = {-1, 1, 0, 0};

    static Font font = Font.font("Arial", 30);
    private final ArrayList<Tile> arrayOfTiles = new ArrayList<>();
    static Pane pane = new Pane(10, 10);
    String shiftDirection;
    int moves = 0;
    long timeStart = 0;
    AnchorPane root;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {
        try {
            shuffleTheGridWithRandom();

            root = new AnchorPane();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Puzzle 15");
            stage.setWidth(350);
            stage.setHeight(375);
            stage.setResizable(false);
            addComponents(root);
            stage.show();

            scene.setOnKeyPressed((KeyEvent e) -> {
                switch (e.getCode()) {
                    case UP -> moveUp();
                    case DOWN -> moveDown();
                    case LEFT -> moveLeft();
                    case RIGHT -> moveRight();
                    case SPACE -> newGame();
                }

                if (moves == 0) {
                    timeStart = System.currentTimeMillis() / 1000;
                }

                if (isShiftDirectionPossible(shiftDirection)) {
                    moves++;
                    shift(shiftDirection);
                    addComponents(root);

                    if (isEndOfGame()) {
                        long timeFinish = System.currentTimeMillis() / 1000;
                        long time = timeFinish - timeStart;
                        Message.youWin(time, moves);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveUp() {
        shiftDirection = "w";
    }

    private void moveDown() {
        shiftDirection = "s";
    }

    private void moveLeft() {
        shiftDirection = "a";
    }

    private void moveRight() {
        shiftDirection = "d";
    }

    private void newGame() {
        moves = 0;
        timeStart = 0;
        shuffleTheGridWithRandom();
        addComponents(root);
    }


    private void addComponents(AnchorPane root) {
        root.getChildren().clear();
        pane.getChildren().clear();
        root.getChildren().add(pane);

        for (int i = 0; i < GRID_SIZE_X * GRID_SIZE_Y; i++) {
            Tile tile = new Tile();
            arrayOfTiles.add(tile);
        }

        int item = 0;
        for (int i = 0; i < GRID_SIZE_X; i++) {
            for (int j = 0; j < GRID_SIZE_Y; j++) {
                String tileContent = String.valueOf(grid[i][j]);
                String color = "gray";
                if (tileContent.equals("0")) {
                    tileContent = "";
                    color = "white";
                }
                pane.add(arrayOfTiles.get(item), j, i);
                arrayOfTiles.get(item).setText(tileContent);
                arrayOfTiles.get(item).setStyle("-fx-control-inner-background: " + color + ";");
                item++;
            }
        }

    }


    public static int[][] getGridWin() {
        return new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8,}, {9, 10, 11, 12}, {13, 14, 15, 0}};
    }


    public static boolean isEndOfGame() {
        return Arrays.deepEquals(grid, getGridWin());
    }


    public static void shuffleTheGridWithRandom() {
        grid = (getGridWin()).clone();

        String randomShiftDirection;
        for (int i = 0; i < 1000; i++) {
            randomShiftDirection = getRandomShiftDirection();
            if (isShiftDirectionPossible(randomShiftDirection)) {
                shift(randomShiftDirection);
            }
        }
    }


    public static String getRandomShiftDirection() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(0, 4));
    }


    public static int getOffsetIndex(String shiftDirection) {
        return switch (shiftDirection) {
            case "w", "0" -> 1;
            case "s", "1" -> 0;
            case "a", "2" -> 3;
            case "d", "3" -> 2;
            default -> -1;
        };
    }


    public static boolean isShiftDirectionPossible(String shiftDirection) {
        int[] emptyTileCoordinates = getEmptyTileCoordinates();
        int x;
        int y;
        int index = getOffsetIndex(shiftDirection);

        x = emptyTileCoordinates[0] + offset_X[index];
        y = emptyTileCoordinates[1] + offset_Y[index];

        return (y >= 0 && y <= 3) && (x >= 0 && x <= 3);
    }


    public static int[] getEmptyTileCoordinates() {
        for (int i = 0; i < GRID_SIZE_X; i++) {
            for (int j = 0; j < GRID_SIZE_Y; j++) {
                if (grid[i][j] == 0) {
                    return new int[]{j, i};
                }
            }
        }
        return new int[]{0, 0};
    }


    public static void shift(String shiftDirection) {
        int[] emptyTileCoordinates = getEmptyTileCoordinates();
        int x;
        int y;
        int index = getOffsetIndex(shiftDirection);

        x = emptyTileCoordinates[0] + offset_X[index];
        y = emptyTileCoordinates[1] + offset_Y[index];

        int shiftingTile = grid[y][x];
        grid[y][x] = 0;
        grid[emptyTileCoordinates[1]][emptyTileCoordinates[0]] = shiftingTile;
    }

}