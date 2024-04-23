package it.polimi.ingsw.model.events.messages.updates;

import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.events.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class DeckUpdateMessage implements Message {
    private final Resource resourceOnTop;
    private final int deckIdentifier;

    public DeckUpdateMessage(Resource resourceOnTop, int deckIdentifier) {
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
