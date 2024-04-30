package it.polimi.ingsw.model.events.messages.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.player.Player;

/**
 * Message that the Client sends to require to place the starting card on its board.
 */
public class PlaceStartCardMessage extends ClientMessage {
    private final boolean onBackSide;

    /**
     * Builds the message. Requires the player's identifier to recognise who is sending the message.
     *
     * @param playerIdentifier the player's identifier
     * @param onBackSide {@code true} if the cards needs to be placed with the back side up, {@code false} otherwise
     */
    public PlaceStartCardMessage(String playerIdentifier, boolean onBackSide) {
        super(MessageType.PLAYER_MESSAGE, playerIdentifier);
        this.onBackSide = onBackSide;
    }

    /**
     * Tries to place the starting card.
     *
     * @param selector the GameSelector instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(GameSelector selector, GameController controller) {
        Player player = selector.getPlayer(this.getPlayerIdentifier());
        controller.placeStartCard(player, onBackSide);
    }
}
