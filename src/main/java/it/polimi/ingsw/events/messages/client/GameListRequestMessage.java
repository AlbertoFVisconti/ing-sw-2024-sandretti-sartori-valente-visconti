package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

/**
 * Message that the client can send to the server in order to request
 * the list of available games
 */
public class GameListRequestMessage extends ClientMessage {
    /**
     * Builds a ClientMessage that asks the server for the available games list.
     */
    public GameListRequestMessage() {
        super(MessageType.CONNECT_JOIN_MESSAGE);
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
            mainController.getAvailableGames(getPlayerIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
