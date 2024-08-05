package ku.cs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public final static String HOST = "localhost";
    public final static short PORT = 25565;
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New connection");
                System.out.println(socket.getPort());
                System.out.println(socket.getRemoteSocketAddress());
                new ClientHandler(socket).start();;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}