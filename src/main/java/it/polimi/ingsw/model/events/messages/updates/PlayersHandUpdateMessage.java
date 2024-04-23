package it.polimi.ingsw.model.events.messages.updates;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.events.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class PlayersHandUpdateMessage implements Message {
    private final String nickname;
    private final PlayCard card;
    private final int cardSlot;

    public PlayersHandUpdateMessage(String playerNickname,PlayCard card, int cardSlot) {
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
