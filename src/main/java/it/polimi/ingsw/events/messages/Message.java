package it.polimi.ingsw.events.messages;

import java.io.Serializable;

/**
 * Generic message that allows the server and clients to share data
 */
public abstract class Message implements Serializable {
    final public MessageType messageType;

    /**
     * Build a generic message with a specified type
     *
     * @param messageType The type of the message
     */
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }
}
