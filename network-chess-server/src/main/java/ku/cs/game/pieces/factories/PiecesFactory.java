package ku.cs.game.pieces.factories;

import ku.cs.game.Board;

public abstract class PiecesFactory {
    protected Board board;

    PiecesFactory(Board board) {
        this.board = board;
    }

    public abstract void createPieces();
}
