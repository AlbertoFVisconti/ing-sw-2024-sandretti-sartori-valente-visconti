package it.polimi.ingsw.model.events.messages.updates;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class VisibleCardUpdateMessage implements ModelUpdateMessage{
    private final PlayCard visibleCard;
    private final int cardSlot;

    public VisibleCardUpdateMessage(PlayCard visibleCard, int cardSlot) {
        this.visibleCard = visibleCard;
        this.cardSlot = cardSlot;
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.setVisibleCard(visibleCard, cardSlot);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
