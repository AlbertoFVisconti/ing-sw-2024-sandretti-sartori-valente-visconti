package it.polimi.ingsw.model.chat;

import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * ChatMessage represents a message sent from a player to another (or broadcast)
 */
public class ChatMessage implements Serializable {
    // The text content of the message
    private final String text;

    // The timestamp when the message was created
    private final Long timestamp;

    // The nickname of the message sender
    private final String senderNick;

    // The nickname of the message receiver
    private final String receiverNick;

    private final double displayColorRed;
    private final double displayColorBlue;
    private final double displayColorGreen;

    /**
     * Constructs a new default (BLACK) ChatMessage object.
     *
     * @param text       The text content of the message.
     * @param senderNick The nickname of the message sender.
     * @param receiverNick The nickname of the message receiver.
     */
    public ChatMessage(String text, String senderNick, String receiverNick) {
        this.displayColorRed = 0;
        this.displayColorGreen = 0;
        this.displayColorBlue = 0;

        this.text = text;
        this.senderNick = senderNick;
        this.receiverNick = receiverNick;
        timestamp = System.currentTimeMillis();
    }

    /**
     * Constructs a new chat message with a certain color.
     * Used to display system messages in the chat.
     *
     * @param text       The text content of the message.
     * @param senderNick The nickname of the message sender.
     * @param receiverNick The nickname of the message receiver.
     */
    public ChatMessage(String text, String senderNick, String receiverNick, Color color) {
        this.displayColorRed = color.getRed();
        this.displayColorGreen = color.getGreen();
        this.displayColorBlue = color.getBlue();

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

    /**
     * Retrieves the nickname of the message receiver.
     *
     * @return nickname of the message receiver.
     */
    public String getReceiverNick() {
        return receiverNick;
    }

    /**
     * Retrieves the display color of the message
     *
     * @return message display color
     */
    public Color getDisplayColor() {
        return Color.color(displayColorRed, displayColorGreen, displayColorBlue);
    }
}
