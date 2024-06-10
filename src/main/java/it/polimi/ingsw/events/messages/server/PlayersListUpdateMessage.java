package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the server sends to the clients when a players join, leave or change color (lobby only).
 */
public class PlayersListUpdateMessage extends ServerMessage {
    private final String[] nicknames;
    private final PlayerColor[] colors;

    /**
     * Builds a new PlayerListUpdateMessage
     *
     * @param nicknames a list of nicknames representing the connected players
     * @param colors a list of PlayerColor
     */
    public PlayersListUpdateMessage(String[] nicknames, PlayerColor[] colors) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.nicknames = nicknames;
        this.colors = colors;

    }

    /**
     * Updates the client's View calling the method matching the message
     *
     * @param view the client's view that needs to be updated.
     */
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
