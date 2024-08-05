package ku.cs.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static Client client = null;

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public static void init(String ip, short port) throws IOException {
        if (client != null) return;
        Client.client = new Client(ip, port);
    }

    public static void close() throws IOException {
        if (client == null) return;
        System.out.println("Closing Connection...");
        client.closeConnection();
    }
    public void closeConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    private Client(String ip, short port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("init");

        System.out.println(in.readLine());
    }
}
