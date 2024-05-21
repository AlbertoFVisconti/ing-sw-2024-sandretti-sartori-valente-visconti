package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class ServerToClientPingMessage extends ServerMessage {
    private final boolean isAnswer;

    public ServerToClientPingMessage(boolean isAnswer) {
        super(MessageType.PING_MESSAGE);
        this.isAnswer = isAnswer;
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.ping(isAnswer);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
