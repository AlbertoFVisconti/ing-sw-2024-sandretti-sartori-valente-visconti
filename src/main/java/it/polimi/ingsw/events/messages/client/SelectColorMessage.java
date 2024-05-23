package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

public class SelectColorMessage extends ClientMessage {
    private final PlayerColor color;

    /**
     * Builds a ServerMessage with a specified type.
     * Requires the player's identifier in order to recognize the player.
     */
    public SelectColorMessage(PlayerColor color) {
        super(MessageType.PLAYER_MESSAGE);
        this.color = color;
    }

    @Override
    public void execute(VirtualMainController selector, VirtualController controller) {
        try {
            controller.selectColor(this.getPlayerIdentifier(), color);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
