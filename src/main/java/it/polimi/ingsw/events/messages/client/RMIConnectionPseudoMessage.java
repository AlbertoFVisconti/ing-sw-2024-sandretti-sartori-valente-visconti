package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * "Message" that the server uses internally in order to process RMI connection request
 * asynchronously.
 */
public class RMIConnectionPseudoMessage extends ClientMessage {
    private final VirtualView view;

    /**
     * Builds a RMIConnectionPseudoMessage with a specified virtual View.
     * Requires the player's identifier in order to recognize the player.
     */
    public RMIConnectionPseudoMessage(VirtualView view) {
        super(MessageType.CONNECT_JOIN_MESSAGE);
        this.view = view;
    }

    /**
     * Allows the server to perform the operation required by the message given the controllers
     *
     * @param mainController   the GameController instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(VirtualMainController mainController, VirtualController controller) {
        try {
            mainController.connect(view);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
