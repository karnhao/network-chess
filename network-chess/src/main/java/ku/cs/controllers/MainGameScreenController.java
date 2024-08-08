package ku.cs.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ku.cs.net.Client;

import java.io.IOException;

public class MainGameScreenController {

    public VBox leftPane;
    public VBox rightPane;
    public SplitPane splitPane;
    public VBox mainGameScreenVBox;
    public Label turnLabel;
    public TextArea text_area;
    public Label playerFactionLabel;

    public void initialize() {
        Client.getClient().setGameController(this);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ku/cs/views/board.fxml"));
        try {
            Pane pane = loader.load();
            leftPane.getChildren().add(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTurnLabelText(String text) {
        this.turnLabel.setText(text);
    }

    public void setTextAreaText(String text) {
        this.text_area.setText(text);
    }

    public void addTextAreaText(String textToAdd) {
        this.text_area.setText(this.text_area.getText() + "\n" + textToAdd);
    }

    public void setPlayerFaction(String faction) {
        this.playerFactionLabel.setText("You play " + faction + " pieces.");
    }
}
