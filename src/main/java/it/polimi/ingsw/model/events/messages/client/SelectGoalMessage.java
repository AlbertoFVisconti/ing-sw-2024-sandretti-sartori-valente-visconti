package it.polimi.ingsw.model.events.messages.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.player.Player;

public class SelectGoalMessage extends ClientMessage {
    private final int index;
    public SelectGoalMessage(String playerIdentifier, int index) {
        super(MessageType.PLAYER_MESSAGE, playerIdentifier);
        this.index = index;
    }

    @Override
    public void execute(GameSelector selector, GameController controller) {
        Player player = selector.getPlayer(playerIdentifier);
        controller.selectPrivateGoal(player, index);
    }
}
