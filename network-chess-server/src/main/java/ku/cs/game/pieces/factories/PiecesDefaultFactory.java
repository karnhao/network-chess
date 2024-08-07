package ku.cs.game.pieces.factories;

import ku.cs.game.Board;
import ku.cs.game.pieces.Bishop;
import ku.cs.game.pieces.King;
import ku.cs.game.pieces.Knight;
import ku.cs.game.pieces.Pawn;
import ku.cs.game.pieces.Queen;
import ku.cs.game.pieces.Rook;
import ku.cs.game.pieces.Pawn.Direction;
import ku.cs.game.pieces.Piece.PieceFaction;

public class PiecesDefaultFactory extends PiecesFactory {

    public PiecesDefaultFactory(Board board) {
        super(board);
    }

    @Override
    public void createPieces() {

        // Black Piece
        createNotPawns(PieceFaction.BLACK, (byte) 0);
        createPawns(PieceFaction.BLACK, (byte) 1, Direction.DOWN);

        // White Piece
        createNotPawns(PieceFaction.WHITE, (byte) 7);
        createPawns(PieceFaction.WHITE, (byte) 6, Direction.UP);
    }

    private void createNotPawns(PieceFaction faction, byte y) {
        this.board.set(new Rook(board, faction), 0, y);
        this.board.set(new Knight(board, faction), 1, y);
        this.board.set(new Bishop(board, faction), 2, y);
        this.board.set(new Queen(board, faction), 3, y);
        this.board.set(new King(board, faction), 4, y);
        this.board.set(new Bishop(board, faction), 5, y);
        this.board.set(new Knight(board, faction), 6, y);
        this.board.set(new Rook(board, faction), 7, y);
    }

    private void createPawns(PieceFaction faction, byte y, Direction direction) {
        for (byte i = 0; i < 8; i++) this.board.set(new Pawn(board, faction, direction), i, y);
    }
    
}
