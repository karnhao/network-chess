package ku.cs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class AbstractClientHandler extends Thread {
    protected final InputStream in;
    protected final BufferedReader reader;
    protected final OutputStream output;
    protected final PrintWriter writer;
    protected final Socket socket;
    
    public AbstractClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(in));
        output = socket.getOutputStream();
        writer = new PrintWriter(output, true);
    }
}
