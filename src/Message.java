import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Message {
    private static final Alert alert = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.CLOSE);

    public static void youWin(long time, int moves) {
        alert.setContentText("Congratulations! You've solved Puzzle15! \n" +
                "It took " + time + " secs. \n" +
                "You did " + moves + " moves. \n" +
                "Press space for new game.");
        alert.show();
    }
}