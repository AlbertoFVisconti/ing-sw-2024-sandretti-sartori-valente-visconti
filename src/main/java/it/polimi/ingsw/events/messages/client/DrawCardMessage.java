package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

/**
 * Message that the Client sends to require to draw from a deck or pick up one of the visible cards
 */
public class DrawCardMessage extends ClientMessage {
    private final int index;

    /**
     * Builds the message. Requires the player's identifier to recognise who is sending the message.
     *
     * @param index the index that represents the card that the client want to pick up
     */
    public DrawCardMessage(int index) {
        super(MessageType.PLAYER_MESSAGE);
        this.index = index;
    }

    /**
     * Tries to draw/pick up the selected card.
     *
     * @param selector   the GameSelector instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(VirtualMainController selector, VirtualController controller) {
        try {
            controller.drawCard(this.getPlayerIdentifier(), index);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
