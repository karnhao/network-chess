package ku.cs.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainGameScreen {

    public VBox leftPane;
    public VBox rightPane;
    public SplitPane splitPane;
    public VBox mainGameScreenVBox;

    public void initialize() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ku/cs/views/board.fxml"));
        try {
            Pane pane = loader.load();
            leftPane.getChildren().add(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
