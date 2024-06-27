package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

/**
 * LeaveGameMessage allows players to leave games without the need for the need for the
 * client di disconnect from the server.
 */
public class LeaveGameMessage extends ClientMessage{
    /**
     * Builds a ServerMessage with a specified type.
     * Requires the player's identifier in order to recognize the player.
     */
    public LeaveGameMessage() {
        super(MessageType.CONNECT_JOIN_MESSAGE);
    }

    /**
     * Calls the matching method on the MainController that
     * allows the player's to leave the game
     *
     * @param mainController   the MainController instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(VirtualMainController mainController, VirtualController controller) {
        try {
            mainController.leaveGame(this.getPlayerIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
