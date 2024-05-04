package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.events.messages.Message;
import it.polimi.ingsw.events.messages.MessageType;

/**
 * Abstract message that the Client sends to the Server.
 * Allows to update execution an operation through the server's controller.

 */
public abstract class ClientMessage extends Message {
    private final String playerIdentifier;

    /**
     * Builds a ServerMessage with a specified type.
     * Requires the player's identifier in order to recognize the player.
     *
     * @param messageType the type of the message
     * @param playerIdentifier the player's identifier
     */
    public ClientMessage(MessageType messageType, String playerIdentifier) {
        super(messageType);
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
     * @param selector the GameSelector instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    public abstract void execute(GameSelector selector, GameController controller);
}