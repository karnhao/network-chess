package ku.cs.game.pieces;

import ku.cs.game.Board;
import ku.cs.game.utils.Pos;

public class Knight extends Piece{

    public Knight(Board board, PieceFaction pieceFaction) {
        super(board, pieceFaction);
    }

    @Override
    public boolean moveRules(Pos oldPos, Pos newPos) {
        int dx = Math.abs(oldPos.getX() - newPos.getX());
        int dy = Math.abs(oldPos.getY() - newPos.getY());
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2); // L-shaped move
    }

    @Override
    public boolean eatRules(Pos oldPos, Pos newPos) {
        return moveRules(oldPos, newPos);
    }
    
}
