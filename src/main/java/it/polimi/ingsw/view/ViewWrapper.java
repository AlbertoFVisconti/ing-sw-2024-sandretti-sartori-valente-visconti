package it.polimi.ingsw.view;

import it.polimi.ingsw.model.events.Observer;
import it.polimi.ingsw.model.events.messages.updates.ModelUpdateMessage;

public class ViewWrapper implements Observer {
    private VirtualView view;

    public ViewWrapper(VirtualView view) {
        this.view = view;
    }

    public void setView(VirtualView view) {
        this.view = view;
    }

    @Override
    public void onUpdate(ModelUpdateMessage updateMessage) {
        updateMessage.updateView(view);
    }
}
