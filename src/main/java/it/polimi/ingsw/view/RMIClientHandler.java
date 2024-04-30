package it.polimi.ingsw.view;

import it.polimi.ingsw.model.events.messages.server.ServerMessage;
import it.polimi.ingsw.network.rmi.GameControllerWrapper;

import java.rmi.RemoteException;

/**
 * Implementation of the ClientHandler interface that allows to handle clients that communicates with the server
 * through RMI (Remote Method Invocation).
 */
public class RMIClientHandler implements ClientHandler {
    private VirtualView view;

    /**
     * Constructs a RMIClientHandler object for the client whose view (possibly a remote object)
     * is passed.
     *
     * @param view the Client's View (possibly a remote object)
     */
    public RMIClientHandler(VirtualView view) {
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

    /**
     * Triggered when an observed object notify its subscribers.
     * Can also be used to send messages to the client.
     *
     * @param message the message that needs to be delivered to the client.
     */
    @Override
    public void onUpdate(ServerMessage message) {
        message.updateView(view);
    }

    /**
     * Allows to hand a client the remote object that represents the controller.
     *
     * @param gameControllerWrapper the GameControllerWrapper object that contains a reference to the game the client is playing.
     */
    @Override
    public void setController(GameControllerWrapper gameControllerWrapper) {
        try {
            view.setController(gameControllerWrapper);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
