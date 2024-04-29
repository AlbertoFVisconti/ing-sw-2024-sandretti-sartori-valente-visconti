package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class DeckUpdateMessage extends ServerMessage {
    private final Resource resourceOnTop;
    private final int deckIdentifier;

    public DeckUpdateMessage(Resource resourceOnTop, int deckIdentifier) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.resourceOnTop = resourceOnTop;
        this.deckIdentifier = deckIdentifier;
    }
    @Override
    public void updateView(VirtualView view) {
        try {
            view.setDeckTopResource(resourceOnTop, deckIdentifier);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
