package ku.cs.game;

public class Viewer {
    protected String address;
    protected short port;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }
}
