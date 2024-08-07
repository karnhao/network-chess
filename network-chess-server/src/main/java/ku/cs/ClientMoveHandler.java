package ku.cs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;

public class ClientMoveHandler extends AbstractClientHandler {

    private String[] args;

    public ClientMoveHandler(AbstractClientHandler clientHandler, String[] args) throws IOException {
        super(clientHandler);
        this.args = args;
    }

    @Override
    public synchronized void start() {
        try {
            byte fromX, fromY, toX, toY;
            String accessToken;
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, Server.getPrivateKey());

            fromX = Byte.parseByte(args[1]);
            fromY = Byte.parseByte(args[2]);
            toX = Byte.parseByte(args[3]);
            toY = Byte.parseByte(args[4]);
            accessToken = new String(cipher.doFinal((byte[]) reader.readObject()), StandardCharsets.UTF_8);

            Server.getGame().move(fromX, fromY, toX, toY, accessToken);
            writer.writeObject("ok");
            writer.flush();
        } catch (Exception e) {
            try {
                e.printStackTrace();
                writer.writeObject("error : " + e.getClass().getSimpleName() + " " + e.getMessage());
                writer.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
