package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.Message;
import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

/**
 * Abstract message that the Client sends to the Server.
 * Allows to update execution an operation through the server's controller.
 */
public abstract class ClientMessage extends Message {
    private String playerIdentifier;

    /**
     * Builds a ServerMessage with a specified type.
     * Requires the player's identifier in order to recognize the player.
     *
     * @param messageType the type of the message
     */
    public ClientMessage(MessageType messageType) {
        super(messageType);
        this.playerIdentifier = null;
    }

    /**
     * Sets the message playerIdentifier.
     * Used by ServerHandlers that encapsulate the playerIdentifier in
     * every outgoing message.
     *
     * @param playerIdentifier the sender's identifier
     */
    public void setPlayerIdentifier(String playerIdentifier) {
        this.playerIdentifier = playerIdentifier;
    }

    /**
     * Retrieves the player's identifier from the message content
     *
     * @return the player's identifier
     */
    public String getPlayerIdentifier() {
        return playerIdentifier;
    }

    /**
     * Allows the server to perform the operation required by the message given the controllers
     *
     * @param mainController   the MainController instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    public abstract void execute(VirtualMainController mainController, VirtualController controller);
}