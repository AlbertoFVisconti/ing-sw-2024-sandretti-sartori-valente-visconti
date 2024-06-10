package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the Server sends to a Client to inform it that one of the boards has changed.
 */
public class PlayersBoardUpdateMessage extends ServerMessage {
    private final String nickname;
    private final CardSlot placedCardSlot;
    private final CardLocation location;

    /**
     * Builds a PlayersBoardUpdateMessage.
     *
     * @param playerNickname the nickname of the player whose board has changed.
     * @param placedCardSlot the cardSlot that contains the card that was place on the board.
     * @param location       the location where the card was placed.
     */
    public PlayersBoardUpdateMessage(String playerNickname, CardSlot placedCardSlot, CardLocation location) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.nickname = playerNickname;
        this.placedCardSlot = placedCardSlot;
        this.location = location;
    }


    /**
     * Updates the board on the client's view
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.placeCardOnPlayersBoard(nickname, placedCardSlot, location);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
