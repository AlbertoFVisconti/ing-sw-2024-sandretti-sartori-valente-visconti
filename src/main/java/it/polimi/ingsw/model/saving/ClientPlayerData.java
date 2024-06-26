package it.polimi.ingsw.model.saving;

import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.utils.ItemCollection;

import java.util.Map;

/**
 * ClientPlayerData class allows to create objects that contain
 * all the information that is needed to recreate a player object
 * on the client's View. It also makes sure that the data delivered
 * to the client only contains information that the player is allowed
 * to receive.
 */
public class ClientPlayerData extends PlayerData {
    /**
     * Creates an object that contains all the basic information that is needed to recreate a player object on the client's View.
     *
     * @param nickname nickname of the saved player
     * @param playerColor color of the saved player
     * @param board board of the saved player
     * @param playerCards hand of the saved player
     * @param privateGoal private goal of the saved player
     * @param inventory inventory of the saved player
     * @param startCard Starting card (if not placed) of the saved player
     * @param availableGoals available goals (if not chosen) of the saved player
     */
    public ClientPlayerData(String nickname, PlayerColor playerColor, Map<CardLocation, CardSlot> board, PlayCard[] playerCards, Goal privateGoal, ItemCollection inventory, StartCard startCard, Goal[] availableGoals) {
        super(nickname, playerColor, board, playerCards, privateGoal, inventory, startCard, availableGoals);
    }
}
