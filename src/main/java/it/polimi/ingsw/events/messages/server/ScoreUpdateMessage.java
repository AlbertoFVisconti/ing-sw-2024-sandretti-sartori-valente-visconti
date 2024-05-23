package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class ScoreUpdateMessage extends ServerMessage {
    private final ScoreBoard scoreBoard;

    public ScoreUpdateMessage(ScoreBoard scoreBoard) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.scoreBoard = scoreBoard;
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.updateScore(scoreBoard);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
