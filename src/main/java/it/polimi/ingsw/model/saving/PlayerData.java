package it.polimi.ingsw.model.saving;

import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.utils.ItemCollection;

import java.io.Serializable;
import java.util.Map;

/**
 * PlayerDAta allows to store and share information regarding a Player,
 * including secret data.
 */
public class PlayerData implements Serializable {
    // the player's nickname
    private final String nickname;

    // the player's color
    private final PlayerColor playerColor;

    // the player's board
    private final Map<CardLocation, CardSlot> board;

    // the player's hand
    private final PlayCard[] playerHand;

    // the player's private goal
    private final Goal privateGoal;

    // the player's inventory
    private final ItemCollection inventory;

    // the player's start card
    private final StartCard startCard;

    // the player's available goals
    private final Goal[] availableGoals;


    /**
     * Builds a PlayerData object that represents a player whose data is provided
     *
     * @param nickname the player's nickname
     * @param playerColor the player's color
     * @param board the player's board
     * @param playerCards the player's hand
     * @param privateGoal the player's private goal
     * @param inventory the player's inventory
     * @param startCard the player's starting card
     * @param availableGoals the player's available private goals
     */
    public PlayerData(String nickname, PlayerColor playerColor,
                      Map<CardLocation, CardSlot> board, PlayCard[] playerCards, Goal privateGoal,
                      ItemCollection inventory, StartCard startCard, Goal[] availableGoals) {
        this.nickname = nickname;
        this.playerColor = playerColor;
        this.board = board;
        this.playerHand = playerCards;

        this.privateGoal = privateGoal;
        this.inventory = inventory;
        this.startCard = startCard;
        this.availableGoals = availableGoals;
    }

    /**
     * Retrieves the player's nickname
     *
     * @return the player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Retrieves the player's color
     *
     * @return the player's color
     */
    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    /**
     * Retrieves the player's board
     *
     * @return the player's board
     */
    public Map<CardLocation, CardSlot> getBoard() {
        return board;
    }

    /**
     * Retrieves the player's hand
     *
     * @return the player's hand
     */
    public PlayCard[] getPlayerHand() {
        return playerHand;
    }

    /**
     * Retrieves the player's private goal
     *
     * @return the player's board
     */
    public Goal getPrivateGoal() {
        return privateGoal;
    }

    /**
     * Retrieves the player's inventory
     *
     * @return the player's inventory
     */
    public ItemCollection getInventory() {
        return inventory;
    }

    /**
     * Retrieves the player's available goals
     *
     * @return the player's available goals
     */
    public Goal[] getAvailableGoals() {
        return availableGoals;
    }

    /**
     * Retrieves the player's starting card
     *
     * @return the player's starting card
     */
    public StartCard getStartCard() {
        return startCard;
    }
}
