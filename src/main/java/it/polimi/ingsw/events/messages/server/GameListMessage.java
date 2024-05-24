package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class GameListMessage extends ServerMessage {
    private final HashSet<Integer> availableGames;

    public GameListMessage(Set<Integer> availableGames) {
        super(MessageType.GAME_LIST_MESSAGE);
        this.availableGames = new HashSet<>(availableGames);
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.updateGameList(availableGames);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
