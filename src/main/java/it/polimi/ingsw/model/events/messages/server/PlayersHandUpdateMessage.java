package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class PlayersHandUpdateMessage extends ServerMessage {
    private final String nickname;
    private final PlayCard card;
    private final int cardSlot;

    public PlayersHandUpdateMessage(String playerNickname,PlayCard card, int cardSlot) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.nickname = playerNickname;
        this.card = card;
        this.cardSlot = cardSlot;
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.setPlayersCard(nickname,card, cardSlot);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
