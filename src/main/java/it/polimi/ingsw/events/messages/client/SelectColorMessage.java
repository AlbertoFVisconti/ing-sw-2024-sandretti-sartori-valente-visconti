package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

/**
 * Message that the client sends to the server in order to request to change the player's color
 */
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

    /**
     * Allows the server to perform the operation required by the message given the controllers
     *
     * @param mainController   the MainController instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(VirtualMainController mainController, VirtualController controller) {
        try {
            controller.selectColor(this.getPlayerIdentifier(), color);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
