package it.polimi.ingsw.model.events.messages.updates;

import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.events.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class StartCardUpdateMessage implements Message {
    private final StartCard card;

    public StartCardUpdateMessage(StartCard card) {
        this.card = card;
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.setStartingCard(card);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
