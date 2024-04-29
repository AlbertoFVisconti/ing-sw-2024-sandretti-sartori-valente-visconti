package it.polimi.ingsw.model.events.messages.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.player.Player;

public class PlaceStartCardMessage extends ClientMessage {
    private final boolean onBackSide;
    public PlaceStartCardMessage(String playerIdentifier, boolean onBackSide) {
        super(MessageType.PLAYER_MESSAGE, playerIdentifier);
        this.onBackSide = onBackSide;
    }

    @Override
    public void execute(GameSelector selector, GameController controller) {
        Player player = selector.getPlayer(playerIdentifier);
        controller.placeStartCard(player, onBackSide);
    }
}
