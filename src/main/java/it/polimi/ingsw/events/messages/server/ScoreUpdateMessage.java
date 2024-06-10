package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the server sends to the clients when the scoreboard is updated
 */
public class ScoreUpdateMessage extends ServerMessage {
    private final ScoreBoard scoreBoard;

    /**
     * Builds a new ScoreUpdateMessage.
     *
     * @param scoreBoard the updated scoreboard
     */
    public ScoreUpdateMessage(ScoreBoard scoreBoard) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.scoreBoard = scoreBoard;
    }

    /**
     * Updates the client's View calling the method matching the message
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.updateScore(scoreBoard);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
