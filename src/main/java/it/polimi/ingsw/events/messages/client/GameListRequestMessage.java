package it.polimi.ingsw.events.messages.client;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.events.messages.server.GameListMessage;

public class GameListRequestMessage extends ClientMessage {
    /**
     * Builds a ServerMessage with a specified type.
     * Requires the player's identifier in order to recognize the player.
     *
     * @param playerIdentifier the player's identifier
     */
    public GameListRequestMessage(String playerIdentifier) {
        super(MessageType.CONNECT_JOIN_MESSAGE, playerIdentifier);
    }

    @Override
    public void execute(GameSelector selector, GameController controller) {
        selector.getPlayersClientHandler(this.getPlayerIdentifier()).sendMessage(
                new GameListMessage(selector.getAvailableGames())
        );
    }
}
