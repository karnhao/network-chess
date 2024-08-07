package ku.cs.game.pieces;

import ku.cs.game.Board;
import ku.cs.game.utils.Pos;

public abstract class Piece {

    public enum PieceFaction {
        BLACK, WHITE
    }

    private final Board board;
    private final PieceFaction faction;
    
    public Piece(Board board, PieceFaction faction) {
        this.board = board;
        this.faction = faction;
    }

    protected Board getBoard() {
        return this.board;
    }

    public PieceFaction getFaction() {
        return this.faction;
    }

    public void onMove(Pos oldPos, Pos newPos) {

    }

    public abstract boolean moveRules(Pos oldPos, Pos newPos);
    public abstract boolean eatRules(Pos oldPos, Pos newPos);
}
