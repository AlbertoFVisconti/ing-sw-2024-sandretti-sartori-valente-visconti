package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class PlayersBoardUpdateMessage extends ServerMessage {
    private final String nickname;
    private final Card placedCard;
    private final CardLocation location;

    public PlayersBoardUpdateMessage(String playerNickname, Card placedCard, CardLocation location) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.nickname = playerNickname;
        this.placedCard = placedCard;
        this.location = location;
    }


    @Override
    public void updateView(VirtualView view) {
        try {
            view.placeCardOnPlayersBoard(nickname, placedCard, location);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
