package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class ServerErrorMessage extends ServerMessage{
    private final RuntimeException exception;
public ServerErrorMessage(RuntimeException exception) {
        super(MessageType.SERVER_ERROR_MESSAGE);
        this.exception = exception;
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.reportError(exception);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
