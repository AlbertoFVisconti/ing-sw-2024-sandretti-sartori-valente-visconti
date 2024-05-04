package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the Server sends to a Client to provide the public goals
 */
public class PublicGoalsUpdateMessage extends ServerMessage {
    private final Goal[] goals;

    /**
     * Builds the message.
     *
     * @param goals an array of goal objects that represents the public goals for the current game.
     */
    public PublicGoalsUpdateMessage(Goal[] goals) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.goals = goals;
    }

    /**
     * Updates the public goals in the client's view.
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.setPublicGoal(goals);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
