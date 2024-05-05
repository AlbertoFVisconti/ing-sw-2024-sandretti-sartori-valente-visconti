package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class PlayersListUpdateMessage extends ServerMessage{
    private final String[] nicknames;
    private final PlayerColor[] colors;

    public PlayersListUpdateMessage(String[] nicknames, PlayerColor[] colors) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.nicknames = nicknames;
        this.colors = colors;

    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.updatePlayersList(nicknames, colors);
        } catch (
                RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
