package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class StartCardUpdateMessage extends ServerMessage {
    private final StartCard card;

    public StartCardUpdateMessage(StartCard card) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
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
