package ku.cs.game.pieces;

import ku.cs.game.Board;
import ku.cs.game.utils.Pos;

public class Rook extends Piece {

    private boolean moved;

    public Rook(Board board, PieceFaction pieceFaction) {
        super(board, pieceFaction);
    }

    @Override
    public boolean moveRules(Pos oldPos, Pos newPos) {
        if (oldPos.equals(newPos)) return false;
        if (! ( oldPos.getX() == newPos.getX() || oldPos.getY() == newPos.getY())) return false;
        return !this.getBoard().isPathBlocked(oldPos, newPos);

    }

    @Override
    public boolean eatRules(Pos oldPos, Pos newPos) {
        return moveRules(oldPos, newPos);
    }

    @Override
    public void onMove(Pos oldPos, Pos newPos) {
        this.moved = true;
    }

    public boolean isMoved() {
        return this.moved;
    }
    
}
