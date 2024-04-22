package it.polimi.ingsw.model.events.messages.updates;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class PlayersHandUpdateMessage implements ModelUpdateMessage{
    private final PlayCard card;
    private final int cardSlot;

    public PlayersHandUpdateMessage(PlayCard card, int cardSlot) {
        this.card = card;
        this.cardSlot = cardSlot;
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.setPlayersCard(card, cardSlot);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
