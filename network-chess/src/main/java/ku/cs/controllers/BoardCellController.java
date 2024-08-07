package ku.cs.controllers;

import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import ku.cs.net.Client;
import ku.cs.services.RootService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BoardCellController {

    private int x, y;

    private static List<Image> rook, knight, bishop, queen, king, pawn;
    public StackPane stackPane;

    public enum Piece {
        BLACK_ROOK, WHITE_ROOK,
        BLACK_KNIGHT, WHITE_KNIGHT,
        BLACK_BISHOP, WHITE_BISHOP,
        BLACK_QUEEN, WHITE_QUEEN,
        BLACK_KING, WHITE_KING,
        BLACK_PAWN, WHITE_PAWN,
    }

    public static void loadImageResource() {
        rook = loadPairImages("/ku/cs/images/icons/Chess_rdt60.png", "/ku/cs/images/icons/Chess_rlt60.png");
        knight = loadPairImages("/ku/cs/images/icons/Chess_ndt60.png", "/ku/cs/images/icons/Chess_nlt60.png");
        bishop = loadPairImages("/ku/cs/images/icons/Chess_bdt60.png", "/ku/cs/images/icons/Chess_blt60.png");
        queen = loadPairImages("/ku/cs/images/icons/Chess_qdt60.png", "/ku/cs/images/icons/Chess_qlt60.png");
        king = loadPairImages("/ku/cs/images/icons/Chess_kdt60.png", "/ku/cs/images/icons/Chess_klt60.png");
        pawn = loadPairImages("/ku/cs/images/icons/Chess_pdt60.png", "/ku/cs/images/icons/Chess_plt60.png");
    }

    public void initialize() {
        this.stackPane.setOnDragOver(event -> {
            // Check if the dragged item is not the drop target itself and if it contains string data
            if (event.getGestureSource() != this.stackPane && event.getDragboard().hasString()) {

                // Accept the transfer modes (COPY or MOVE in this case)
                event.acceptTransferModes(TransferMode.MOVE);

            }

            // Consume the event to indicate that it's being handled
            event.consume();
        });

        this.stackPane.setOnDragDropped(event -> {
            // Get the Dragboard containing the data
            Dragboard db = event.getDragboard();

            // Initialize a flag for the drop success
            boolean success = false;

            // Check if the Dragboard contains string data
            if (db.hasString()) {

                // Process the dropped data here
                String data = db.getString();

                // Perform necessary actions with the data
                System.out.println(data + " -> " + this.x + "," + this.y);
                String[] d = data.split(",");
                int fromX = Integer.parseInt(d[0]);
                int fromY = Integer.parseInt(d[1]);
                Client.getClient().move(fromX, fromY, this.x, this.y);


                // Set the success flag to true, indicating a successful drop
                success = true;

            }

            // Indicate whether the drop was completed successfully
            event.setDropCompleted(success);

            // Consume the event to indicate that it's being handled
            event.consume();
        });
    }

    private static List<Image> loadPairImages(String r1, String r2) {
        return Arrays.asList(
                new Image(Objects.requireNonNull(BoardCellController.class.getResource(r1)).toExternalForm()),
                new Image(Objects.requireNonNull(BoardCellController.class.getResource(r2)).toExternalForm())
        );
    }

    public Pane pieceImage;

    private void fillImage(Image image, Pane pane) {
        BackgroundImage bg = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1,1,true,true,false,true));
        pane.setBackground(new Background(bg));
    }

    public void setPiece(Piece piece) {
        switch (piece) {
            case BLACK_KING -> fillImage(BoardCellController.king.get(0), pieceImage);
            case WHITE_KING -> fillImage(BoardCellController.king.get(1), pieceImage);
            case BLACK_PAWN -> fillImage(BoardCellController.pawn.get(0), pieceImage);
            case WHITE_PAWN -> fillImage(BoardCellController.pawn.get(1), pieceImage);
            case BLACK_ROOK -> fillImage(BoardCellController.rook.get(0), pieceImage);
            case WHITE_ROOK -> fillImage(BoardCellController.rook.get(1), pieceImage);
            case BLACK_QUEEN -> fillImage(BoardCellController.queen.get(0), pieceImage);
            case WHITE_QUEEN -> fillImage(BoardCellController.queen.get(1), pieceImage);
            case BLACK_BISHOP -> fillImage(BoardCellController.bishop.get(0), pieceImage);
            case WHITE_BISHOP -> fillImage(BoardCellController.bishop.get(1), pieceImage);
            case BLACK_KNIGHT -> fillImage(BoardCellController.knight.get(0), pieceImage);
            case WHITE_KNIGHT -> fillImage(BoardCellController.knight.get(1), pieceImage);
            default -> {
                return;
            }
        }
        this.pieceImage.setOnDragDetected(event -> {
            // Start a drag-and-drop operation with COPY transfer mode
            Dragboard db = this.pieceImage.startDragAndDrop(TransferMode.MOVE);

            // Define the content to be transferred
            ClipboardContent content = new ClipboardContent();
            content.putString(this.x + "," + this.y);

            // Set the content for the dragboard
            db.setContent(content);

            // Consume the event to indicate that it's being handled
            event.consume();
        });
    }

    public void removePiece() {
        this.pieceImage.setOnDragDetected(null);
        this.pieceImage.setBackground(null);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
