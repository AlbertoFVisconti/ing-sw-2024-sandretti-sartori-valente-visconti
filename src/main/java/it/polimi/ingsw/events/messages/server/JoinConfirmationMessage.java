package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class JoinConfirmationMessage extends ServerMessage {
    private final String nickname;

    /**
     * Builds the message.
     *
     * @param nickname the player's nickname.
     */
    public JoinConfirmationMessage(String nickname) {
        super(MessageType.CONNECT_JOIN_MESSAGE);
        this.nickname = nickname;
    }

    /**
     * Updates the client's view with its player identifier
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.confirmJoin(this.nickname);
        } catch (
                RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
