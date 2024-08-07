package ku.cs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import ku.cs.game.Game;

public class Server {
    public final static String HOST = "localhost";
    public final static short PORT = 25565;
    private static Game game = null;

    private static PublicKey PUBLIC_KEY;
    private static PrivateKey PRIVATE_KEY;
    public static void main(String[] args) {

        System.out.println("Generating keypair...");
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PUBLIC_KEY = keyPair.getPublic();
            PRIVATE_KEY = keyPair.getPrivate();

            System.out.println("Public Key: " + PUBLIC_KEY.getEncoded());
            System.out.println("Private Key: " + PRIVATE_KEY.getEncoded());
        } catch (Exception e) {

        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PrivateKey getPrivateKey() {
        return PRIVATE_KEY;
    }

    public static PublicKey getPublicKey() {
        return PUBLIC_KEY;
    }

    public static Game getGame() {
        if (game == null) Server.game = new Game();
        return game;
    }
}