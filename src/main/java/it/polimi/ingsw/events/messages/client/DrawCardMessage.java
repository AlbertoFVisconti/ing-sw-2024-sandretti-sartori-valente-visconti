package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.player.Player;

/**
 * Message that the Client sends to require to draw from a deck or pick up one of the visible cards
 */
public class DrawCardMessage extends ClientMessage {
    private final int index;

    /**
     * Builds the message. Requires the player's identifier to recognise who is sending the message.
     *
     * @param playerIdentifier the player's identifier
     * @param index the index that represents the card that the client want to pick up
     */
    public DrawCardMessage(String playerIdentifier, int index) {
        super(MessageType.PLAYER_MESSAGE, playerIdentifier);
        this.index = index;
    }

    /**
     * Tries to draw/pick up the selected card.
     *
     * @param selector the GameSelector instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(GameSelector selector, GameController controller) {
        Player player = selector.getPlayer(this.getPlayerIdentifier());
        controller.drawCard(player, index);
    }
}
