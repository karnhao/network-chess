package ku.cs;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends AbstractClientHandler {

    public ClientHandler(Socket socket) throws IOException {
        super(socket);
    }

    public void run() {
        try {
            String text;
            while ((text = reader.readLine()) != null) {
                System.out.println("Received: " + text);
                writer.println("Echo: " + text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
