package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class PrivateGoalUpdateMessage extends ServerMessage {
    private final boolean definitive;
    private final Goal[] goals;

    public PrivateGoalUpdateMessage(Goal[] goals) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.goals = goals;
        definitive = false;
    }
    public PrivateGoalUpdateMessage(Goal goal) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.goals = new Goal[]{goal};
        definitive = true;
    }

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
