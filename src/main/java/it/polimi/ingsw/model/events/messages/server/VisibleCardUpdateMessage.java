package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class VisibleCardUpdateMessage extends ServerMessage {
    private final PlayCard visibleCard;
    private final int cardSlot;

    public VisibleCardUpdateMessage(PlayCard visibleCard, int cardSlot) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
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
