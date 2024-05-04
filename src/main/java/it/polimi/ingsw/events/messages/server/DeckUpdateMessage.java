package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the Server sends to a Client to inform it that one of the deck has changed.
 */
public class DeckUpdateMessage extends ServerMessage {
    private final Resource resourceOnTop;
    private final int deckIdentifier;

    /**
     * Builds the message.
     *
     * @param resourceOnTop the resource that is on top of the deck.
     * @param deckIdentifier the identifier of the deck that needs to be updated.
     */
    public DeckUpdateMessage(Resource resourceOnTop, int deckIdentifier) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.resourceOnTop = resourceOnTop;
        this.deckIdentifier = deckIdentifier;
    }

    /**
     * Updates the deck on the client's view.
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.setDeckTopResource(resourceOnTop, deckIdentifier);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
