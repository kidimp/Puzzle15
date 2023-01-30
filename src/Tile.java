import javafx.geometry.Pos;
import javafx.scene.control.TextField;

public class Tile extends TextField {
    private static final int SIZE = 75;
    public Tile() {
        setPrefWidth(SIZE);
        setPrefHeight(SIZE);
        setEditable(false);
        setAlignment(Pos.CENTER);
        setFont(Puzzle.font);
        setFocusTraversable(false);
    }
}