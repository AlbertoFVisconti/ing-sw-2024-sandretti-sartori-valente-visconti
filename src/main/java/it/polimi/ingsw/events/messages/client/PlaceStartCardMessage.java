package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

/**
 * Message that the Client sends to require to place the starting card on its board.
 */
public class PlaceStartCardMessage extends ClientMessage {
    private final boolean onBackSide;

    /**
     * Builds the message. Requires the player's identifier to recognise who is sending the message.
     *
     * @param onBackSide {@code true} if the cards needs to be placed with the back side up, {@code false} otherwise
     */
    public PlaceStartCardMessage(boolean onBackSide) {
        super(MessageType.PLAYER_MESSAGE);
        this.onBackSide = onBackSide;
    }

    /**
     * Tries to place the starting card.
     *
     * @param selector the GameSelector instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(VirtualMainController selector, VirtualController controller) {
        try {
            controller.placeStartCard(this.getPlayerIdentifier(), onBackSide);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
