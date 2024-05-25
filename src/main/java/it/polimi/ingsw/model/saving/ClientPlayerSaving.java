package it.polimi.ingsw.model.saving;

import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.utils.ItemCollection;

import java.util.Map;

public class ClientPlayerSaving extends PlayerSaving {
    /**
     * this class contains all the basic information that is needed to recreate a player object if the server crashes
     *
     * @param nick
     * @param playerColor
     * @param board
     * @param playerCards
     * @param privateGoal
     * @param inventory
     */
    public ClientPlayerSaving(String nick, PlayerColor playerColor, Map<CardLocation, CardSlot> board, PlayCard[] playerCards, Goal privateGoal, ItemCollection inventory) {
        super(nick, playerColor, board, playerCards, privateGoal, inventory);
    }
}
