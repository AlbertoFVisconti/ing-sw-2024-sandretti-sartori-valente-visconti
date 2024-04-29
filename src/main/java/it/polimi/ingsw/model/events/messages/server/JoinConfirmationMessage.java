package it.polimi.ingsw.model.events.messages.server;

import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class JoinConfirmationMessage extends ServerMessage {
    private final String playerIdentifier;
    public JoinConfirmationMessage(String playerIdentifier) {
        super(MessageType.CONNECT_JOIN_MESSAGE);
        this.playerIdentifier = playerIdentifier;
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.setPlayerIdentifier(playerIdentifier);
        } catch (
        RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
