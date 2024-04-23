package it.polimi.ingsw.view;

import it.polimi.ingsw.model.events.Observer;
import it.polimi.ingsw.model.events.messages.Message;

public class ViewWrapperRMI implements Observer {
    private VirtualView view;

    public ViewWrapperRMI(VirtualView view) {
        this.view = view;
    }

    public void setView(VirtualView view) {
        this.view = view;
    }

    @Override
    public void onUpdate(Message message) {
        message.updateView(view);
    }
}
