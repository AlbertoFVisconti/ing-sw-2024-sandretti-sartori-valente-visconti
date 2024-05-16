package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.RemoteException;

/**
 * Message that the Client sends to require to place a cards on its board
 */
public class PlaceCardMessage extends ClientMessage {
    private final int index;
    private final boolean onBackSide;
    private final CardLocation location;

    /**
     * Builds the message. Requires the player's identifier to recognise who is sending the message.
     *
     * @param index the index that represents the card that the client want to place.
     * @param onBackSide {@code true} if the cards needs to placed with the back side up, {@code false} otherwise.
     * @param location the location where the card needs to be placed.
     */
    public PlaceCardMessage(int index, boolean onBackSide, CardLocation location) {
        super(MessageType.PLAYER_MESSAGE);
        this.index = index;
        this.onBackSide = onBackSide;
        this.location = location;
    }

    /**
     * Tries to place the selected card.
     *
     * @param selector the GameSelector instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(VirtualMainController selector, VirtualController controller) {
        try {
            controller.placeCard(this.getPlayerIdentifier(), index, onBackSide, location);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
