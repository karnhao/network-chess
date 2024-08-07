package ku.cs;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class AbstractClientHandler extends Thread {
    protected final InputStream in;
    protected final ObjectInputStream reader;
    protected final OutputStream output;
    protected final ObjectOutputStream writer;
    protected final Socket socket;
    
    public AbstractClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = socket.getInputStream();
        reader = new ObjectInputStream(in);
        output = socket.getOutputStream();
        writer = new ObjectOutputStream(output);
    }

    public AbstractClientHandler(AbstractClientHandler clientHandler) throws IOException {
        this.in = clientHandler.in;
        this.reader = clientHandler.reader;
        this.output = clientHandler.output;
        this.writer = clientHandler.writer;
        this.socket = clientHandler.socket;
    }

    public void close() throws IOException {
        writer.close();
        reader.close();
        socket.close();
    }
}
