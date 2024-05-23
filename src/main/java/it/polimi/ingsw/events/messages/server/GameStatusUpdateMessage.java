package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class GameStatusUpdateMessage extends ServerMessage {
    private final GameStatus gameStatus;
    private final TurnStatus turnStatus;
    private final String playersTurn;

    public GameStatusUpdateMessage(GameStatus gameStatus, TurnStatus turnStatus, String playersTurn) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.gameStatus = gameStatus;
        this.turnStatus = turnStatus;
        this.playersTurn = playersTurn;
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.updateGameStatus(gameStatus, turnStatus, playersTurn);
        } catch (
                RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
