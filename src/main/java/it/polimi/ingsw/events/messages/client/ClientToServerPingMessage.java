package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

/**
 * Allows the client to send a ping message to the server.
 * It can be used to answer to a previous ping, or to
 * ask the server to answer
 */
public class ClientToServerPingMessage extends ClientMessage {
    private final boolean isAnswer;

    /**
     * Creates a new ClientToServerPingMessage
     *
     * @param isAnswer {@code true} if the ping message is an answer to a previous ping message (from the server), {@code false} if the client is expecting the server to answer
     */
    public ClientToServerPingMessage(boolean isAnswer) {
        super(MessageType.PING_MESSAGE);
        this.isAnswer = isAnswer;
    }

    /**
     * Allows the server to perform the operation required by the message given the controllers
     *
     * @param mainController the MainController instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(VirtualMainController mainController, VirtualController controller) {
        try {
            mainController.ping(this.getPlayerIdentifier(), isAnswer);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
