package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the Server sends to a Client to inform it that one of the visible cards has changed.
 */
public class VisibleCardUpdateMessage extends ServerMessage {
    private final PlayCard visibleCard;
    private final int cardSlot;

    /**
     * Builds the message.
     *
     * @param visibleCard the PlayCard object that represents the new card that is placed in the visible card slot.
     * @param cardSlot the slot where the new card is located.
     */
    public VisibleCardUpdateMessage(PlayCard visibleCard, int cardSlot) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.visibleCard = visibleCard;
        this.cardSlot = cardSlot;
    }

    /**
     * Updates the visible cards in the client's view.
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.setVisibleCard(visibleCard, cardSlot);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
