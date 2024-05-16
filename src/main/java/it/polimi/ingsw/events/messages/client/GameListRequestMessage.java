package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

public class GameListRequestMessage extends ClientMessage {
    /**
     * Builds a ClientMessage that asks the server for the available games list.
     */
    public GameListRequestMessage() {
        super(MessageType.CONNECT_JOIN_MESSAGE);
    }

    @Override
    public void execute(VirtualMainController selector, VirtualController controller) {
        try {
            selector.getAvailableGames(getPlayerIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
