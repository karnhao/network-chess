package ku.cs;

import java.io.IOException;

public class ClientUpdateHandler  extends AbstractClientHandler{

    public ClientUpdateHandler(AbstractClientHandler clientHandler) throws IOException {
        super(clientHandler);
    }

    @Override
    public void run() {
        try {
            writer.writeObject(Server.getGame().getClientsGameState());
            writer.writeObject(Server.getGame().getCurrentFactionTurn().name());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
