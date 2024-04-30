package it.polimi.ingsw.model.events.messages.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.player.Player;

/**
 * Message that the Client sends to require to select one of the available private goals
 */
public class SelectGoalMessage extends ClientMessage {
    private final int index;

    /**
     * Builds the message. Requires the player's identifier to recognise who is sending the message.
     *
     * @param playerIdentifier the player's identifier
     * @param index the index that represent the chosen private goal among the available ones
     */
    public SelectGoalMessage(String playerIdentifier, int index) {
        super(MessageType.PLAYER_MESSAGE, playerIdentifier);
        this.index = index;
    }

    /**
     * Tries to select the private goal.
     *
     * @param selector the GameSelector instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(GameSelector selector, GameController controller) {
        Player player = selector.getPlayer(this.getPlayerIdentifier());
        controller.selectPrivateGoal(player, index);
    }
}
