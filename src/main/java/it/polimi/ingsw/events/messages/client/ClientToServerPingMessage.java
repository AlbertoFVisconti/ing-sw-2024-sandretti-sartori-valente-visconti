package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

public class ClientToServerPingMessage extends ClientMessage {
    private final boolean isAnswer;

    public ClientToServerPingMessage(boolean isAnswer) {
        super(MessageType.PING_MESSAGE);
        this.isAnswer = isAnswer;
    }

    @Override
    public void execute(VirtualMainController selector, VirtualController controller) {
        try {
            selector.ping(this.getPlayerIdentifier(), isAnswer);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
