package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.saving.GameData;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * JoinConfirmationMessage allows the server to confirm that a client joined a certain game.
 * It carries the nickname that the players used to join the game.
 * If the player's re-joining a game, the message will also contain a GameData object
 * that contains the game's data (only data that the player is authorized to receive).
 * This message is also used when a saved game needs to be reloaded. In this case the message
 * will be received twice, when all the players selects their colors.
 */
public class JoinConfirmationMessage extends ServerMessage {
    private final String nickname;

    private final GameData savings;


    /**
     * Builds a JoinConfirmationMessage with no game data (game has yet to start).
     *
     * @param nickname the player's nickname.
     */
    public JoinConfirmationMessage(String nickname) {
        super(MessageType.CONNECT_JOIN_MESSAGE);
        this.nickname = nickname;
        this.savings = null;
    }


    /**
     * Builds a JoinConfirmationMessage that contains game data
     *
     * @param nickname the nickname that the player used to join the game
     * @param savings the game data
     */
    public JoinConfirmationMessage(String nickname, GameData savings) {
        super(MessageType.CONNECT_JOIN_MESSAGE);
        this.nickname = nickname;
        this.savings = savings;
    }

    /**
     * Updates the client's view with its player identifier
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.confirmJoin(this.nickname, savings);
        } catch (
                RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
