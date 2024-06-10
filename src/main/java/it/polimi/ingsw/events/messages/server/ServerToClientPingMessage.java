package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Allows the server to send a ping message to a client.
 * It can be used to answer to a previous ping, or to
 * ask the client to answer
 */
public class ServerToClientPingMessage extends ServerMessage {
    private final boolean isAnswer;

    /**
     * Creates a new ServerToClientPingMessage
     *
     * @param isAnswer {@code true} if the ping message is an answer to a previous ping message (from the client), {@code false} if the client is expecting the server to answer
     */
    public ServerToClientPingMessage(boolean isAnswer) {
        super(MessageType.PING_MESSAGE);
        this.isAnswer = isAnswer;
    }

    /**
     * Updates the client's View calling the method matching the message
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.ping(isAnswer);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
