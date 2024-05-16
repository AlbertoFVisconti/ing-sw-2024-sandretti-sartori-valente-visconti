package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class RMIConnectionPseudoMessage extends ClientMessage{
    private final VirtualView view;

    /**
     * Builds a RMIConnectionPseudoMessage with a specified virtual View.
     * Requires the player's identifier in order to recognize the player.
     *
     */
    public RMIConnectionPseudoMessage(VirtualView view) {
        super(MessageType.CONNECT_JOIN_MESSAGE);
        this.view = view;
    }

    @Override
    public void execute(VirtualMainController selector, VirtualController controller) {
        try {
            selector.connect(view);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
