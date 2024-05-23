package it.polimi.ingsw.model.chat;

import java.io.Serializable;

/**
 * ChatMessage represents a message sent from a player to another (or broadcast)
 */
public class ChatMessage implements Serializable {
    private final String text; // The text content of the message
    private final Long timestamp; // The timestamp when the message was created
    private final String senderNick; // The nickname of the message sender
    private final String receiverNick; // The nickname of the message receiver

    /**
     * Constructs a new ChatMessage object.
     *
     * @param text       The text content of the message.
     * @param senderNick The nickname of the message sender.
     */
    public ChatMessage(String text, String senderNick, String receiverNick) {
        this.text = text;
        this.senderNick = senderNick;
        this.receiverNick = receiverNick;
        timestamp = System.currentTimeMillis();
    }

    /**
     * Returns the text content of the message.
     *
     * @return The text content of the message.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the timestamp when the message was created.
     *
     * @return The timestamp when the message was created.
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the nickname of the message sender.
     *
     * @return The nickname of the message sender.
     */
    public String getSenderNick() {
        return senderNick;
    }

    public String getReceiverNick() {
        return receiverNick;
    }
}
