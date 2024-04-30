package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the Server sends to a Client to inform it that one of the boards has changed.
 */
public class PlayersBoardUpdateMessage extends ServerMessage {
    private final String nickname;
    private final Card placedCard;
    private final CardLocation location;

    /**
     * Builds the message.
     *
     * @param playerNickname the nickname of the player whose board has changed.
     * @param placedCard the card that was placed on the board.
     * @param location the location where the card was placed.
     */
    public PlayersBoardUpdateMessage(String playerNickname, Card placedCard, CardLocation location) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.nickname = playerNickname;
        this.placedCard = placedCard;
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
            view.placeCardOnPlayersBoard(nickname, placedCard, location);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
