package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the Server sends to a Client to confirm that it was correctly connected to the server.
 * The message also holds the player's identifier that the client needs to perform further operations.
 */
public class ConnectionConfirmationMessage extends ServerMessage {
    private final String playerIdentifier;

    /**
     * Builds the message.
     *
     * @param playerIdentifier the player's identifier.
     */
    public ConnectionConfirmationMessage(String playerIdentifier) {
        super(MessageType.CONNECT_JOIN_MESSAGE);
        this.playerIdentifier = playerIdentifier;
    }

    /**
     * Updates the client's view with its player identifier
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.setPlayerIdentifier(playerIdentifier);
        } catch (
        RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
