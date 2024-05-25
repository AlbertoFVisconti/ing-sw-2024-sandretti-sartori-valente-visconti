package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.Message;
import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

/**
 * Abstract message that the Server sends to the Client.
 * Allows to update the client's view.
 */
public abstract class ServerMessage extends Message {
    private final String addressee;

    /**
     * Builds a ServerMessage with a specified type.
     * The message has a specified recipient.
     *
     * @param messageType         the type of the message
     * @param addresseeIdentifier if the message is for a single client only, provide its identifier
     */
    public ServerMessage(MessageType messageType, String addresseeIdentifier) {
        super(messageType);
        this.addressee = addresseeIdentifier;
    }

    /**
     * Builds a ServerMessage with a specified type.
     *
     * @param messageType the type of the message
     */
    public ServerMessage(MessageType messageType) {
        super(messageType);
        this.addressee = null;
    }

    public String getAddresseeIdentifier() {
        return addressee;
    }

    /**
     * Updates the View with the content of the message.
     *
     * @param view the client's view that needs to be updated.
     */
    abstract public void updateView(VirtualView view);
}
