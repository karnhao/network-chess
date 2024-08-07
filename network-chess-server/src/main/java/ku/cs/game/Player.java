package ku.cs.game;

import ku.cs.game.pieces.Piece.PieceFaction;

public class Player extends Viewer {
    private String accessToken;
    private PieceFaction ownFaction;

    public Player() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public PieceFaction getOwnFaction() {
        return ownFaction;
    }

    public void setOwnFaction(PieceFaction ownFaction) {
        this.ownFaction = ownFaction;
    }
}
