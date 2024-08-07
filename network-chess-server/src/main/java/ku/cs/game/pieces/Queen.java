package ku.cs.game.pieces;

import ku.cs.game.Board;
import ku.cs.game.utils.Pos;

public class Queen extends Piece {

    public Queen(Board board, PieceFaction pieceFaction) {
        super(board, pieceFaction);
    }

    @Override
    public boolean moveRules(Pos oldPos, Pos newPos) {
        if (oldPos.equals(newPos)) return false;
        int dx = Math.abs(oldPos.getX() - newPos.getX());
        int dy = Math.abs(oldPos.getY() - newPos.getY());

        if (!( dx == dy || oldPos.getX() == newPos.getX() || oldPos.getY() == newPos.getY() )) return false; // Diagonal, vertical, or horizontal
        return !this.getBoard().isPathBlocked(oldPos, newPos);
    }

    @Override
    public boolean eatRules(Pos oldPos, Pos newPos) {
        return moveRules(oldPos, newPos);
    }
    
}
