package ku.cs;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import ku.cs.game.Player;

public class ClientInitHandler extends AbstractClientHandler {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();


    public ClientInitHandler(AbstractClientHandler clientHandler) throws IOException {
        super(clientHandler);
    }

    public ClientInitHandler(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void run() {
        try {
            String role = Server.getGame().isAcceptPlayer() ? "player" : "viewer";
            writer.writeObject(role);
            writer.writeObject(Server.getPublicKey());
            writer.flush();

            if (role.equalsIgnoreCase("player")) {
                // Send encrypted player access token

                // Decrypt player public key
                Cipher decryptKeyCipher = Cipher.getInstance("RSA");
                decryptKeyCipher.init(Cipher.DECRYPT_MODE, Server.getPrivateKey());
    
                byte[] encryptedClientKey = (byte[]) reader.readObject();
    
                byte[] decryptedKey = decryptKeyCipher.doFinal(encryptedClientKey);
                PublicKey clientPublicKey = KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decryptedKey));
                    
                // Encrypt access token with client public key
                String accessToken = generateToken();

                System.out.println("Generated access token: " + accessToken);
    
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
                byte[] encryptedToken = cipher.doFinal(accessToken.getBytes());
    
                writer.writeObject(encryptedToken);
                writer.flush();

                // Add player to the game
                Player player = new Player();
                player.setAccessToken(accessToken);
                Server.getGame().addPlayer(player);
            }

            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateToken() {
        byte[] randomBytes = new byte[8];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
