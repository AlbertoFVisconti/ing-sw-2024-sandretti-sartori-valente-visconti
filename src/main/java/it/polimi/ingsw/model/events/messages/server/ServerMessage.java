package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.events.messages.Message;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

public abstract class ServerMessage extends Message {
    public ServerMessage(MessageType messageType) {
        super(messageType);
    }


    abstract public void updateView(VirtualView view);
}
