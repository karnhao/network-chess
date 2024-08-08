package ku.cs.game.pieces;

import ku.cs.game.Board;
import ku.cs.game.utils.Pos;

public class Pawn extends Piece {

    private boolean isMoved = false;
    private byte direction;

    public enum Direction {
        UP, DOWN
    }

    public Pawn(Board board, PieceFaction pieceFaction, Direction direction) {
        super(board, pieceFaction);
        if (direction == Direction.UP) this.direction = -1;
        if (direction == Direction.DOWN) this.direction = 1;
    }

    @Override
    public boolean moveRules(Pos oldPos, Pos newPos) {
        if (oldPos.getX() == newPos.getX() && newPos.getY() == oldPos.getY() + direction) return true; // Move forward
        if (!isMoved && oldPos.getX() == newPos.getX() && newPos.getY() == oldPos.getY() + direction * 2) return true; // Move 2 steps forward
        return false;
    }

    @Override
    public boolean eatRules(Pos oldPos, Pos newPos) {
        if (Math.abs(oldPos.getX() - newPos.getX()) == 1 && newPos.getY() == oldPos.getY() + direction) return true;
        return false;
    }

    @Override
    public void onMove(Pos oldPos, Pos newPos) {
        this.isMoved = true;
        if (newPos.getY() == ((direction == 1) ? 7 : 0)) {
            this.getBoard().set(new Queen(this.getBoard(), this.getFaction()), newPos); // Promote to queen
        }
        
    }
    
}
