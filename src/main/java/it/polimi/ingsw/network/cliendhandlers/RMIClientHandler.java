package it.polimi.ingsw.network.cliendhandlers;

import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.network.rmi.GameControllerWrapper;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Implementation of the ClientHandler interface that allows to handle clients that communicates with the server
 * through RMI (Remote Method Invocation).
 */
public class RMIClientHandler extends ClientHandler {
    private VirtualView view;

    /**
     * Constructs a RMIClientHandler object for the client whose view (possibly a remote object)
     * is passed.
     *
     * @param view the Client's View (possibly a remote object)
     */
    public RMIClientHandler(VirtualView view) {
        super();
        this.view = view;
    }

    /**
     * Changes the clients view with a new one.
     *
     * @param view the new client's View.
     */
    public void setView(VirtualView view) {
        this.view = view;
    }

    @Override
    public void sendMessage(ServerMessage message) {
        try {
            message.updateView(view);
        } catch (RuntimeException e) {
            this.forceDisconnection();
        }
    }

    /**
     * Allows to hand a client the remote object that represents the controller.
     *
     * @param gameControllerWrapper the GameControllerWrapper object that contains a reference to the game the client is playing.
     */
    @Override
    public void linkController(GameControllerWrapper gameControllerWrapper) {
        try {
            view.setController(gameControllerWrapper);
        } catch (RemoteException e) {
            this.forceDisconnection();
        }
    }
}
