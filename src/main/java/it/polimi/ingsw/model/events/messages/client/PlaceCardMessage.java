package it.polimi.ingsw.model.events.messages.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CardLocation;

public class PlaceCardMessage extends ClientMessage {
    private final int index;
    private final boolean onBackSide;
    private final CardLocation location;
    public PlaceCardMessage(String playerIdentifier, int index, boolean onBackSide, CardLocation location) {
        super(MessageType.PLAYER_MESSAGE, playerIdentifier);
        this.index = index;
        this.onBackSide = onBackSide;
        this.location = location;
    }

    @Override
    public void execute(GameSelector selector, GameController controller) {
        Player player = selector.getPlayer(playerIdentifier);
        controller.placeCard(player, index, onBackSide, location);
    }
}
