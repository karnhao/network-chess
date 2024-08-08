package ku.cs.game;

import ku.cs.game.exceptions.IllegalMoveException;
import ku.cs.game.pieces.King;
import ku.cs.game.pieces.Piece;
import ku.cs.game.pieces.Rook;
import ku.cs.game.pieces.Piece.PieceFaction;
import ku.cs.game.pieces.factories.PiecesDefaultFactory;
import ku.cs.game.utils.Pos;

public class Board {
    private static final byte DEFAULT_SIZE = 8;

    private Piece[][] pieces;
    private byte size;

    public Board(byte size) {
        this.size = size;
        this.pieces = new Piece[size][size];
        new PiecesDefaultFactory(this).createPieces();
    }

    public Board() {
        this(DEFAULT_SIZE);
    }

    /**
     * Set or replace piece in the current position of the board
     * 
     * @param piece
     * @param position
     */
    public void set(Piece piece, Pos position) {
        System.out.println("set piece " + piece + " " + position);
        set(piece, position.getX(), position.getY());
    }

    /**
     * Set or replace piece in the current position of the board
     * 
     * @param piece
     * @param x
     * @param y
     */
    public void set(Piece piece, byte x, byte y) {
        validatePosition(x, y);
        this.pieces[y][x] = piece;
    }

    /**
     * Set or replace piece in the current position of the board
     * 
     * @param piece
     * @param x
     * @param y
     */
    public void set(Piece piece, int x, int y) {
        this.set(piece, (byte) x, (byte) y);
    }

    public Piece getPiece(byte x, byte y) {
        validatePosition(x, y);
        return this.pieces[y][x];
    }

    public Piece getPiece(Pos position) {
        return this.getPiece(position.getX(), position.getY());
    }

    private void validatePosition(byte x, byte y) {
        if (x < 0 || y < 0
                || x >= this.size
                || y >= this.size)
            throw new IllegalArgumentException("Out of bounds");
    }

    public void move(byte fromX, byte fromY, byte toX, byte toY) throws IllegalMoveException {
        if (this.getPiece(fromX, fromY) == null)
            throw new IllegalMoveException("Source piece not found");

        Piece sourcePiece = this.getPiece(fromX, fromY);
        Piece destinationPiece = this.getPiece(toX, toY);

        Pos sourcePos = new Pos(fromX, fromY);
        Pos destinationPos = new Pos(toX, toY);

        int dx = destinationPos.getX() - sourcePos.getX(); // ไว้ดูว่าราชาจะไปทางซ้ายหรือขวา

        if (destinationPiece == null) {

            if (sourcePiece instanceof King && Math.abs(dx) == 2) {
                // Castling check
                Rook rook = (Rook) this.getPiece((byte) (dx < 0 ? 0 : 7), sourcePos.getY());
                if (!((King) sourcePiece).isValidCastlingMove(sourcePos, destinationPos, rook)) {
                    throw new IllegalMoveException("Invalid Castling move");
                }
                this.castling((King) sourcePiece, rook, sourcePos, destinationPos);
                return;
            } else if (!sourcePiece.moveRules(sourcePos, destinationPos))
                throw new IllegalMoveException("Cannot move to " + destinationPos.toString());

        } else {
            if (destinationPiece.getFaction().equals(sourcePiece.getFaction())) {

                if (sourcePiece instanceof King) {
                    // Castling check
                    Pos kingDestinationPos;
                    Rook rook;

                    if (destinationPiece instanceof Rook) {
                        int step = dx < 0 ? -2 : 2;
                        kingDestinationPos = new Pos((byte) (sourcePos.getX() + step), sourcePos.getY());
                        rook = (Rook) destinationPiece;
                    } else {
                        throw new IllegalMoveException(
                                "Cannot eat your own piece : " + destinationPiece.getClass().getName());
                    }

                    if (!((King) sourcePiece).isValidCastlingMove(sourcePos, kingDestinationPos, rook)) {
                        throw new IllegalMoveException("Invalid Castling move");
                    }

                    this.castling((King) sourcePiece, rook, sourcePos, kingDestinationPos);
                    return;
                } else
                    throw new IllegalMoveException(
                            "Cannot eat your own piece : " + destinationPiece.getClass().getName());

            }
            if (!sourcePiece.eatRules(sourcePos, destinationPos))
                throw new IllegalMoveException("Cannot eat at " + destinationPos.toString());
        }

        this.set(sourcePiece, destinationPos);
        this.set(null, sourcePos);
        sourcePiece.onMove(sourcePos, destinationPos);
    }

    public boolean isSquareUnderAttack(byte x, byte y, PieceFaction attacker) {

        for (byte i = 0; i < this.size; i++) {
            for (byte j = 0; j < this.size; j++) {
                Piece piece = this.getPiece(i, j);
                if (piece == null || !piece.getFaction().equals(attacker))
                    continue;

                if (piece instanceof King) {
                    if (((King) piece).nonRecursiveEatRules(new Pos(i, j), new Pos(x, y)))
                        return true;
                } else if (piece.eatRules(new Pos(i, j), new Pos(x, y)))
                    return true;
            }
        }

        return false;
    }

    public boolean isSquareUnderAttack(Pos pos, PieceFaction attacker) {
        return this.isSquareUnderAttack(pos.getX(), pos.getY(), attacker);
    }

    private void castling(King king, Rook rook, Pos kingPos, Pos kingDestinationPos) {
        int dx = kingDestinationPos.getX() - kingPos.getX();
        int rookStep = dx > 0 ? -1 : 1;

        // castling
        this.set(king, kingDestinationPos);
        this.set(rook, kingDestinationPos.getX() + rookStep, kingDestinationPos.getY());
        this.set(null, kingPos);
        this.set(null, new Pos((byte) (rookStep == -1 ? 7 : 0), kingPos.getY()));

    }

    public String getPieceString() {
        String output = "";
        for (byte i = 0; i < this.size; i++) {
            for (byte j = 0; j < this.size; j++) {
                Piece piece = this.pieces[i][j];
                output += piece == null ? "0,"
                        : (piece.getFaction().name() + "_" + piece.getClass().getSimpleName() + ",").toLowerCase();
            }
        }
        return output;
    }

    public boolean isPathBlocked(Pos oldPos, Pos newPos) {
        int dx = Integer.compare(newPos.getX(), oldPos.getX());
        int dy = Integer.compare(newPos.getY(), oldPos.getY());

        int x = oldPos.getX() + dx;
        int y = oldPos.getY() + dy;

        while (x != newPos.getX() || y != newPos.getY()) {
            if (getPiece(new Pos((byte) x, (byte) y)) != null) {
                return true; // Path is blocked
            }
            x += dx;
            y += dy;
        }
        return false;
    }
}
