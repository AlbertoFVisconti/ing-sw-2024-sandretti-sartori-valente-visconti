package it.polimi.ingsw.model.events.messages.saving;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.utils.ItemCollection;

import java.io.Serializable;
import java.util.Map;

public class PlayerSavingMessage implements Serializable {
    private final  String Nick;
    private final PlayerColor playerColor;
    private final Map<CardLocation, Card> board;
    private PlayCard[] playerHand;
    private final Goal privateGoal;
    private final ItemCollection inventory;
    /**
     * this class contains all the basic information that is needed to recreate a player object if the server crashes
     * **/
    public PlayerSavingMessage( String nick, PlayerColor playerColor,
                               Map<CardLocation, Card> board, PlayCard[] playerCards, Goal privateGoal,
                                ItemCollection inventory){
        this.Nick = nick;
        this.playerColor = playerColor;
        this.board = board;
        this.playerHand = playerCards;

        this.privateGoal = privateGoal;
        this.inventory = inventory;
    }


    public String getNick() {
        return Nick;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }
    public Map<CardLocation, Card> getBoard() {
        return board;
    }

    public PlayCard[] getPlayerHand() {
        return playerHand;
    }

    public Goal getPrivateGoal() {
        return privateGoal;
    }

    public ItemCollection getInventory() {return inventory;}
}
