package it.polimi.ingsw.events.messages.client;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

public class SelectColorMessage extends ClientMessage {
    private final PlayerColor color;

    /**
     * Builds a ServerMessage with a specified type.
     * Requires the player's identifier in order to recognize the player.
     *
     * @param playerIdentifier the player's identifier
     */
    public SelectColorMessage(String playerIdentifier, PlayerColor color) {
        super(MessageType.PLAYER_MESSAGE, playerIdentifier);
        this.color = color;
    }

    @Override
    public void execute(GameSelector selector, GameController controller) {
        Player player = selector.getPlayer(this.getPlayerIdentifier());
        controller.selectColor(player, color);
    }
}
