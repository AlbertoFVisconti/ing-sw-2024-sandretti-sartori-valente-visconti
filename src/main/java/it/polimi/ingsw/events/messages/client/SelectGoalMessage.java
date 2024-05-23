package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

/**
 * Message that the Client sends to require to select one of the available private goals
 */
public class SelectGoalMessage extends ClientMessage {
    private final int index;

    /**
     * Builds the message. Requires the player's identifier to recognise who is sending the message.
     *
     * @param index the index that represent the chosen private goal among the available ones
     */
    public SelectGoalMessage(int index) {
        super(MessageType.PLAYER_MESSAGE);
        this.index = index;
    }

    /**
     * Tries to select the private goal.
     *
     * @param selector   the GameSelector instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(VirtualMainController selector, VirtualController controller) {
        try {
            controller.selectPrivateGoal(this.getPlayerIdentifier(), index);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
