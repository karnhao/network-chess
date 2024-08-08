package ku.cs.game;

import ku.cs.game.exceptions.IllegalMoveException;
import ku.cs.game.pieces.Piece.PieceFaction;

public class Game {
    private Player[] players;
    private byte currentPlayerTurn;
    private Board board;

    public Game() {
        System.out.println("Initializing Game");

        this.players = new Player[2];
        this.board = new Board();

        this.currentPlayerTurn = 0;
    }

    private void nextPlayer() {
        currentPlayerTurn++;
        if (currentPlayerTurn >= players.length)
            currentPlayerTurn = 0;
    }

    public void move(byte fromX, byte fromY, byte toX, byte toY, String accessToken) throws IllegalMoveException {
        if (!accessToken.equals(this.players[currentPlayerTurn].getAccessToken()))
            throw new SecurityException("Invalid access token");
        
        if (this.board.getPiece(fromX, fromY).getFaction() != this.players[currentPlayerTurn].getOwnFaction())
            throw new IllegalMoveException("Cannot move that piece");
        
        this.board.move(fromX, fromY, toX, toY);
        nextPlayer();
    }

    public int addPlayer(Player player) {
        if (players[0] == null) {
            players[0] = player;
            player.setOwnFaction(PieceFaction.BLACK);
            return 0;
        } else if (players[1] == null) {
            players[1] = player;
            player.setOwnFaction(PieceFaction.WHITE);
            return 1;
        } else return -1;
    }

    public void removePlayer(int index) {
        this.players[index] = null;
    }

    public String getClientsGameState() {
        return this.board.getPieceString();
    }

    public PieceFaction getCurrentFactionTurn() {
        return this.currentPlayerTurn == 0 ? PieceFaction.BLACK : PieceFaction.WHITE;
    }

    public boolean isAcceptPlayer() {
        return this.players[0] == null || this.players[1] == null;
    }
}
