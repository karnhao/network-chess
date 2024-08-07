package ku.cs.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import ku.cs.net.Client;

import java.io.IOException;

public class BoardController {

    private static final int SIZE = 8;
    public GridPane gridPane;
    public VBox vBox;

    private BoardCellController[][] boardCellControllers;

    public void initialize() {

        Client.getClient().setBoardController(this);
        boardCellControllers = new BoardCellController[SIZE][SIZE];
        VBox.setVgrow(vBox, Priority.ALWAYS);
        BoardCellController boardCellController;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/ku/cs/views/boardCell.fxml"));
                Pane cellPane;
                try {
                    cellPane = loader.load();
                    boardCellController = loader.getController();
                    boardCellController.setPosition(col, row);
                    boardCellControllers[row][col] = boardCellController;

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

    public void update(String data) {
        String[] d = data.split(",");
        String di;
        BoardCellController.Piece piece = null;
        for (int i = 0; i < 64; i++) {
            int x = i % 8;
            int y = i / 8;

            di = d[i];
            switch (di) {
                case "black_rook" -> piece = BoardCellController.Piece.BLACK_ROOK;
                case "black_knight" -> piece = BoardCellController.Piece.BLACK_KNIGHT;
                case "black_bishop" -> piece = BoardCellController.Piece.BLACK_BISHOP;
                case "black_queen" -> piece = BoardCellController.Piece.BLACK_QUEEN;
                case "black_king" -> piece = BoardCellController.Piece.BLACK_KING;
                case "black_pawn" -> piece = BoardCellController.Piece.BLACK_PAWN;
                case "white_rook" -> piece = BoardCellController.Piece.WHITE_ROOK;
                case "white_knight" -> piece = BoardCellController.Piece.WHITE_KNIGHT;
                case "white_bishop" -> piece = BoardCellController.Piece.WHITE_BISHOP;
                case "white_queen" -> piece = BoardCellController.Piece.WHITE_QUEEN;
                case "white_king" -> piece = BoardCellController.Piece.WHITE_KING;
                case "white_pawn" -> piece = BoardCellController.Piece.WHITE_PAWN;
                default -> piece = null;
            }

            if (piece == null) this.boardCellControllers[y][x].removePiece();
            else this.boardCellControllers[y][x].setPiece(piece);
        }

    }
}
