package it.polimi.ingsw.model.events.messages.updates;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class PlayersBoardUpdateMessage implements ModelUpdateMessage{
    private final Card placedCard;
    private final CardLocation location;

    public PlayersBoardUpdateMessage(Card placedCard, CardLocation location) {
        this.placedCard = placedCard;
        this.location = location;
    }


    @Override
    public void updateView(VirtualView view) {
        try {
            view.placeCardOnPlayersBoard(placedCard, location);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
