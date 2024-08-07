package ku.cs.game.utils;

public class Pos {
    private byte x;
    private byte y;

    public Pos(byte x, byte y) {
        this.x = x;
        this.y = y;
    }

    public byte getX() {
        return x;
    }

    public void setX(byte x) {
        this.x = x;
    }

    public byte getY() {
        return y;
    }

    public void setY(byte y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) return false;
        return this.x == ((Pos) obj).getX() && this.y == ((Pos) obj).getY();
    }

    @Override
    public String toString() {
        return "Pos(" + x + ", " + y + ")";
    }

}
