package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

/**
 * Message that the server sends to a client to provide the available games list
 */
public class GameListMessage extends ServerMessage {
    private final HashSet<Integer> availableGames;

    /**
     * Builds a new GameListMessage ServerMessage with a specified set of available games
     *
     * @param availableGames the set of available games
     */
    public GameListMessage(Set<Integer> availableGames) {
        super(MessageType.GAME_LIST_MESSAGE);
        this.availableGames = new HashSet<>(availableGames);
    }

    /**
     * Updates the client's View calling the method matching the message
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.updateGameList(availableGames);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
