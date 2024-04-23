package it.polimi.ingsw.model.events.messages.updates;

import it.polimi.ingsw.model.events.messages.Message;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class PublicGoalsUpdateMessage implements Message {
    private final Goal[] goals;

    public PublicGoalsUpdateMessage(Goal[] goals) {
        this.goals = goals;
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.setPublicGoal(goals);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
