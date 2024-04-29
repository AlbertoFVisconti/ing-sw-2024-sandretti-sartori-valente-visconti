package it.polimi.ingsw.view;

import it.polimi.ingsw.model.events.messages.server.ServerMessage;

public class RMIClientHandler implements ClientHandler {
    private VirtualView view;

    public RMIClientHandler(VirtualView view) {
        this.view = view;
    }

    public void setView(VirtualView view) {
        this.view = view;
    }

    @Override
    public void onUpdate(ServerMessage message) {
        message.updateView(view);
    }
}
