package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.Message;
import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

/**
 * Abstract message that the Server sends to the Client.
 * Allows to update the client's view.
 */
public abstract class ServerMessage extends Message {
    // identifier of the player that needs to receive the message.
    // can be set to null if the message needs to be broadcast
    private final String addresseeIdentifier;

    /**
     * Builds a ServerMessage with a specified type.
     * The message has a specified recipient.
     *
     * @param messageType         the type of the message
     * @param addresseeIdentifier if the message is for a single client only, provide its identifier
     */
    public ServerMessage(MessageType messageType, String addresseeIdentifier) {
        super(messageType);
        this.addresseeIdentifier = addresseeIdentifier;
    }

    /**
     * Builds a ServerMessage with a specified type.
     *
     * @param messageType the type of the message
     */
    public ServerMessage(MessageType messageType) {
        super(messageType);
        this.addresseeIdentifier = null;
    }

    /**
     * Retrieves the message addressee's identifier.
     *
     * @return addressee's playerIdentifier, {@code null} if the message has no specific addressee
     */
    public String getAddresseeIdentifier() {
        return addresseeIdentifier;
    }

    /**
     * Updates the View with the content of the message.
     *
     * @param view the client's view that needs to be updated.
     */
    abstract public void updateView(VirtualView view);
}
