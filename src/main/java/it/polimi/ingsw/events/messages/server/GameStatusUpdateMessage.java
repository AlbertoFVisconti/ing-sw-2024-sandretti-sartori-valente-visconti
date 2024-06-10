package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the server sends to the clients to update the current game status (turn status and current turn)
 */
public class GameStatusUpdateMessage extends ServerMessage {
    private final GameStatus gameStatus;
    private final TurnStatus turnStatus;
    private final String playersTurn;

    /**
     * Builds a new GameStatusUpdateMessage ServerMessage
     *
     * @param gameStatus the current GameStatus
     * @param turnStatus the current TurnStatus
     * @param playersTurn the nickname of the player who needs to play
     */
    public GameStatusUpdateMessage(GameStatus gameStatus, TurnStatus turnStatus, String playersTurn) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.gameStatus = gameStatus;
        this.turnStatus = turnStatus;
        this.playersTurn = playersTurn;
    }

    /**
     * Updates the client's View calling the method matching the message
     *
     * @param view the client's view that needs to be updated.
     */
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
