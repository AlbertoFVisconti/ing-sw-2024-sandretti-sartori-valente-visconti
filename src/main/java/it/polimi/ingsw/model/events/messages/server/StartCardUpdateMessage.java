package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the Server sends to a Client to provide the Starting card.
 */
public class StartCardUpdateMessage extends ServerMessage {
    private final StartCard card;

    /**
     * Builds the message
     *
     * @param card the StartCard object that represents the player's starting card.
     */
    public StartCardUpdateMessage(StartCard card) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.card = card;
    }

    /**
     * Updates the Starting card in the client's view.
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.setStartingCard(card);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
