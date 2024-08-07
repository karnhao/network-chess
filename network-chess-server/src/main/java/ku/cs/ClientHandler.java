package ku.cs;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ClientHandler extends AbstractClientHandler {

    public ClientHandler(Socket socket) throws IOException {
        super(socket);
    }

    public void run() {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String[] args = ((String) reader.readObject()).split(" ");
            System.out.println(dateTime.format(formatter) + " Received: " + Arrays.toString(args));
            switch (args[0]) {
                case "init":
                    ClientInitHandler clientJoinHandler = new ClientInitHandler(this);
                    clientJoinHandler.start();
                    clientJoinHandler.join();
                    break;
                case "move":
                    ClientMoveHandler clientMoveHandler = new ClientMoveHandler(this, args);
                    clientMoveHandler.start();
                    clientMoveHandler.join();
                    break;
                case "update":
                    ClientUpdateHandler clientUpdateHandler = new ClientUpdateHandler(this);
                    clientUpdateHandler.start();
                    clientUpdateHandler.join();
                    break;
                default:
                    break;
        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close(); // CLOSE CONNECTION!
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
