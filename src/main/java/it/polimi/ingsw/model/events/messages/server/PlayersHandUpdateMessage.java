package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the Server sends to a Client to inform it that one of the players received a new card.
 */
public class PlayersHandUpdateMessage extends ServerMessage {
    private final String nickname;
    private final PlayCard card;
    private final int cardSlot;

    /**
     * Builds the message.
     *
     * @param playerNickname the nickname of the player whose hand has changed
     * @param card the card that was inserted in the player's hand
     * @param cardSlot the index of the player hands where the card needs to be updated.
     */
    public PlayersHandUpdateMessage(String playerNickname,PlayCard card, int cardSlot) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.nickname = playerNickname;
        this.card = card;
        this.cardSlot = cardSlot;
    }

    /**
     * Updates the hand of the player in the client's view.
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.setPlayersCard(nickname,card, cardSlot);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
