package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class PublicGoalsUpdateMessage extends ServerMessage {
    private final Goal[] goals;

    public PublicGoalsUpdateMessage(Goal[] goals) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
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
