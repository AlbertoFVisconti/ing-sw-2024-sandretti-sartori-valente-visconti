package it.polimi.ingsw.model.events.messages.updates;

import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class PrivateGoalUpdateMessage implements ModelUpdateMessage{
    private final boolean definitive;
    private final Goal[] goals;

    public PrivateGoalUpdateMessage(Goal[] goals) {
        this.goals = goals;
        definitive = false;
    }
    public PrivateGoalUpdateMessage(Goal goal) {
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
