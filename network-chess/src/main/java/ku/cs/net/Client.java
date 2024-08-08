package ku.cs.net;

import javafx.application.Platform;
import ku.cs.controllers.BoardController;
import ku.cs.controllers.MainGameScreenController;
import ku.cs.services.RootService;
import ku.cs.utils.TimeString;

import javax.crypto.Cipher;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Client extends Thread {
    private static Client client = null;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private String latestData;
    private final String serverIp;
    private final short serverPort;
    private String accessToken;

    private BoardController boardController = null;
    private PublicKey serverPublicKey;
    private PublicKey clientPublickey;
    private PrivateKey clientPrivateKey;
    private MainGameScreenController gameController;
    private String faction;

    public static void init(String ip, short port) throws IOException, ClassNotFoundException {
        if (client != null) return;

        Socket socket = new Socket(ip, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        out.writeObject("init");
        out.flush();

        String text = (String) in.readObject();
        System.out.println(text);

        PublicKey clientPublicKey = null;
        PrivateKey clientPrivateKey = null;
        PublicKey serverPublicKey = (PublicKey) in.readObject();
        System.out.println(serverPublicKey);
        String token = null;
        String faction = "";
        if (text.split(" ")[0].equalsIgnoreCase("player")) {

            // Generate Key pair
            System.out.println("Generating Client Key");
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(512);

                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                clientPublicKey = keyPair.getPublic();
                clientPrivateKey = keyPair.getPrivate();

                System.out.println("Public Key: " + clientPublicKey);
                System.out.println("Private Key: " + clientPrivateKey);

                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);

                out.writeObject(cipher.doFinal(clientPublicKey.getEncoded()));
                out.flush();

                cipher.init(Cipher.DECRYPT_MODE, clientPrivateKey);
                System.out.println("Decrypting Access Token...");
                token = new String(cipher.doFinal((byte[]) in.readObject()), StandardCharsets.UTF_8);
                System.out.println("Access Token = " + token);

                faction = (String) in.readObject();
                System.out.println("Faction = " + faction);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } // else viewer


        in.close();
        out.close();
        socket.close();

        if (Client.client == null) {
            Client.client = new Client(ip, port);
            Client.client.accessToken = token;
            Client.client.serverPublicKey = serverPublicKey;
            Client.client.clientPublickey = clientPublicKey;
            Client.client.clientPrivateKey = clientPrivateKey;
            Client.client.faction = faction;
        }
    }

    public static void close() {
        if (client == null) return;
        System.out.println("Closing Connection...");
        client.closeConnection();
        client.interrupt();
    }
    public void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception ignored) {}
    }

    public static Client getClient() {
        return client;
    }

    private Client(String ip, short port) {
        this.serverIp = ip;
        this.serverPort = port;
        this.start();
    }

    public String getLatestData() {
        return latestData;
    }

    @Override
    public void run() {
        System.out.println("Client Thread Start");
        while(true) {
            if (Thread.interrupted()) break;
            try {

                socket = new Socket(this.serverIp, this.serverPort);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                out.writeObject("update");
                this.latestData = (String) in.readObject();
                String toMove = (String) in.readObject();

                this.closeConnection();
                System.out.println(this.latestData);
                System.out.println("Update Since " + TimeString.getCurrentTimeString());

                Platform.runLater(() -> this.updateUIController(this.latestData, toMove));

                Thread.sleep(100);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                this.closeConnection();
                break;
            } catch (InterruptedException e) {
                this.closeConnection();
                break;
            }
        }
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void move(int fromX, int fromY, int toX, int toY) {
        try (Socket moveSocket = new Socket(this.serverIp, this.serverPort)) {
            ObjectOutputStream out = new ObjectOutputStream(moveSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(moveSocket.getInputStream());

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, this.serverPublicKey);

            String sendMessage = String.format("move %d %d %d %d", fromX, fromY, toX, toY);

            out.writeObject(sendMessage);
            out.writeObject(cipher.doFinal(this.accessToken.getBytes()));
            out.flush();

            String outputMessage = (String) in.readObject();
            System.out.println(outputMessage);
            RootService.showBar(outputMessage);
            this.gameController.addTextAreaText("Send : " + sendMessage);
            this.gameController.addTextAreaText("Response Message : " + outputMessage);
        } catch (ClassNotFoundException e) {
            RootService.showErrorBar(e.getClass().getSimpleName() + " " + e.getMessage());
            this.gameController.addTextAreaText("Error : " + e.getClass().getSimpleName() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGameController(MainGameScreenController mainGameScreenController) {
        this.gameController = mainGameScreenController;
    }

    private void updateUIController(String chessData, String toMove) {
        if (this.boardController != null) this.boardController.update(chessData);
        if (this.gameController != null) {
            this.gameController.setTurnLabelText(toMove + " to move.");
            this.gameController.setPlayerFaction(this.faction);
        }

    }
}
