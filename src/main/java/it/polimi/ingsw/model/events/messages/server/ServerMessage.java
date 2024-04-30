package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.events.messages.Message;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

/**
 * Abstract message that the Server sends to the Client.
 * Allows to update the client's view.
 */
public abstract class ServerMessage extends Message {

    /**
     * Builds a ServerMessage with a specified type.
     *
     * @param messageType the type of the message
     */
    public ServerMessage(MessageType messageType) {
        super(messageType);
    }


    /**
     * Updates the View with the content of the message.
     *
     * @param view the client's view that needs to be updated.
     */
    abstract public void updateView(VirtualView view);
}
