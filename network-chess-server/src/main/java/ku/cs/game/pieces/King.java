package ku.cs.game.pieces;

import ku.cs.game.Board;
import ku.cs.game.utils.Pos;

public class King extends Piece {

    private boolean moved = false;

    public King(Board board, PieceFaction pieceFaction) {
        super(board, pieceFaction);
    }

    @Override
    public boolean moveRules(Pos oldPos, Pos newPos) {
        if (oldPos.equals(newPos)) return false;
        if (this.getBoard().isSquareUnderAttack(newPos, getFaction() == PieceFaction.BLACK ? PieceFaction.WHITE : PieceFaction.BLACK)) return false;

        int dx = Math.abs(oldPos.getX() - newPos.getX());
        int dy = Math.abs(oldPos.getY() - newPos.getY());
        return (dx <= 1 && dy <= 1); // One square in any direction
    }

    public boolean nonRecursiveEatRules(Pos oldPos, Pos newPos) {
        if (oldPos.equals(newPos)) return false;
        int dx = Math.abs(oldPos.getX() - newPos.getX());
        int dy = Math.abs(oldPos.getY() - newPos.getY());
        return (dx <= 1 && dy <= 1); // One square in any direction
    }

    @Override
    public boolean eatRules(Pos oldPos, Pos newPos) {
        return moveRules(oldPos, newPos);
    }

    @Override
    public void onMove(Pos oldPos, Pos newPos) {
        this.moved = true;
    }

    public boolean isValidCastlingMove(Pos kingPos, Pos kingDestinationPos, Rook rook) {
        if (this.moved || rook.isMoved()
            || this.getBoard().isSquareUnderAttack(kingPos, getFaction() == PieceFaction.BLACK ? PieceFaction.WHITE : PieceFaction.BLACK)) return false;
        System.out.println("a");
        int dx = kingDestinationPos.getX() - kingPos.getX();
        if (Math.abs(dx) != 2) return false;
        System.out.println("b");
        int step = dx > 0 ? 1 : -1;
        for (int x = kingPos.getX() + step; x != kingDestinationPos.getX(); x += step) {
            if (this.getBoard().getPiece((byte) x, kingPos.getY()) != null
                || this.getBoard().isSquareUnderAttack(new Pos((byte) x, kingPos.getY()), getFaction() == PieceFaction.BLACK ? PieceFaction.WHITE : PieceFaction.BLACK)) {
                return false;
            }
        }
        System.out.println("c");
        // Check final position of the king
        if (this.getBoard().isSquareUnderAttack(kingDestinationPos, getFaction() == PieceFaction.BLACK ? PieceFaction.WHITE : PieceFaction.BLACK)) return false;
        return true;
    }
    
}
