package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

public class LeaveGameMessage extends ClientMessage{
    /**
     * Builds a ServerMessage with a specified type.
     * Requires the player's identifier in order to recognize the player.
     */
    public LeaveGameMessage() {
        super(MessageType.CONNECT_JOIN_MESSAGE);
    }

    @Override
    public void execute(VirtualMainController mainController, VirtualController controller) {
        try {
            mainController.leaveGame(this.getPlayerIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
