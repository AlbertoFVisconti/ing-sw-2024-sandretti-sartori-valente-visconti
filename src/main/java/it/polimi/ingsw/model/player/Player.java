package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.goals.Goal;

import javax.smartcardio.Card;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;

public class Player {
    public final String nickName = null;
    public final PlayerColor color = null;

    private PlayCard[] playerCards;
    private Goal privateGoal;

    private HashMap<Point, Card> board;
    private ItemCollection inventory;


    public void setPlayerCard(PlayCard card, int index) {}
    public void placeCard(int index, Point location) {}

    public Goal getPrivateGoal() {
        return privateGoal;
    }

    public void setPrivateGoal(Goal privateGoal) {
        this.privateGoal = privateGoal;
    }


    public HashMap<Point, Card> getBoard() {
        // this allows to safely expose the hashmap without the need to clone and build it from scratch
        return (HashMap<Point, Card>) Collections.unmodifiableMap(board);
    }

    public ItemCollection getInventory() {
        return new ItemCollection(inventory);
    }

    public void addItems(ItemCollection itemCollection) {
        this.inventory.add(itemCollection);
    }



}
