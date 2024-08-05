package ku.cs.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;

import java.io.IOException;

public class BoardController {

    private static final int SIZE = 8;
    public GridPane gridPane;
    public VBox vBox;

    public void initialize() {

        VBox.setVgrow(vBox, Priority.ALWAYS);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/ku/cs/views/boardCell.fxml"));
                Pane cellPane = null;
                try {
                    cellPane = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String color;
                if ((row + col) % 2 == 0) {
                    color = "-dark-green-color";
                } else {
                    color = "-light-gray-color";
                }
                cellPane.setStyle("-fx-background-color: " + color + ";");
                gridPane.add(cellPane, col, row);
            }
        }
    }
}
