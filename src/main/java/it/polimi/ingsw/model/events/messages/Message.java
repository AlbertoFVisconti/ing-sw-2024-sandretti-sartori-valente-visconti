package it.polimi.ingsw.model.events.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    final public MessageType messageType;

    public Message(MessageType messageType) {
        this.messageType = messageType;
    }
}
