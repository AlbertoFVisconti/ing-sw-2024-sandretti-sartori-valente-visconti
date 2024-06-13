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

public class PlayerSaving implements Serializable {
    private final String Nick;
    private final PlayerColor playerColor;
    private final Map<CardLocation, CardSlot> board;
    private final PlayCard[] playerHand;
    private final Goal privateGoal;
    private final ItemCollection inventory;
    private final StartCard startCard;
    private final Goal[] availableGoal;


    /**
     * this class contains all the basic information that is needed to recreate a player object if the server crashes
     **/
    public PlayerSaving(String nick, PlayerColor playerColor,
                        Map<CardLocation, CardSlot> board, PlayCard[] playerCards, Goal privateGoal,
                        ItemCollection inventory, StartCard startCard, Goal[] availableGoal) {
        this.Nick = nick;
        this.playerColor = playerColor;
        this.board = board;
        this.playerHand = playerCards;

        this.privateGoal = privateGoal;
        this.inventory = inventory;
        this.startCard = startCard;
        this.availableGoal = availableGoal;
    }


    public String getNick() {
        return Nick;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public Map<CardLocation, CardSlot> getBoard() {
        return board;
    }

    public PlayCard[] getPlayerHand() {
        return playerHand;
    }

    public Goal getPrivateGoal() {
        return privateGoal;
    }

    public ItemCollection getInventory() {
        return inventory;
    }

    public Goal[] getAvailableGoal() {
        return availableGoal;
    }

    public StartCard getStartCard() {
        return startCard;
    }
}
