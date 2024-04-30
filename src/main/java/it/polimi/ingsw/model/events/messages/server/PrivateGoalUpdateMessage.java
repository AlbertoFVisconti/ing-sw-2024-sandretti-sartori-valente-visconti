package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the Server sends to a Client to inform it that its private goals were updated.
 * It's used to send the player's available private goals and also to send the definitive one.
 */
public class PrivateGoalUpdateMessage extends ServerMessage {
    private final boolean definitive;
    private final Goal[] goals;

    /**
     * Builds the message for available goals.
     *
     * @param goals an array of goal objects.
     */
    public PrivateGoalUpdateMessage(Goal[] goals) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.goals = goals;
        definitive = false;
    }

    /**
     * Builds the message for the definitive goal
     *
     * @param goal a goal object representing the player's definitive private goal.
     */
    public PrivateGoalUpdateMessage(Goal goal) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.goals = new Goal[]{goal};
        definitive = true;
    }

    /**
     * Updates the private goals in the client's view.
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        if(definitive) {
            try {
                view.setDefinitivePrivateGoal(goals[0]);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                view.setAvailablePrivateGoals(goals);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
