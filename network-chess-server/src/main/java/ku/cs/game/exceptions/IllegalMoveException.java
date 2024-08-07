package ku.cs.game.exceptions;

public class IllegalMoveException extends Exception {
    public IllegalMoveException(String message) {
        super(message);
    }

    public IllegalMoveException() {
        super();
    }
}
