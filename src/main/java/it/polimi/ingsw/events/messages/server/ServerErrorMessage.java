package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the server sends to a client when an error occurs while processing one of
 * the client's messages in order to report such error.
 */
public class ServerErrorMessage extends ServerMessage {
    private final RuntimeException exception;

    /**
     * Builds a ServerErrorMessage with a provided RuntimeException
     *
     * @param exception RuntimeException that contains information regarding the error
     */
    public ServerErrorMessage(RuntimeException exception) {
        super(MessageType.SERVER_ERROR_MESSAGE);
        this.exception = exception;
    }

    /**
     * Updates the client's View calling the method matching the message
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.reportError(exception);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
