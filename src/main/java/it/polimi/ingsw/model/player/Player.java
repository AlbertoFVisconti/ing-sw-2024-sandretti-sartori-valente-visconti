package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.goals.Goal;

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


    public Player(/* TODO */) {
        // TODO
    }

    public void setPlayerCard(PlayCard card, int index) {}
    public void placeCard(int index, Point location) {}

    public Goal getPrivateGoal() {
        return privateGoal;
    }

    public void setPrivateGoal(Goal privateGoal) {
        this.privateGoal = privateGoal;
    }


    public Card getPlacedCard(Point point) {
        return this.board.get(point);
    }
    public HashMap<Point, Card> getBoard() {
        /*
         this allows to safely expose the hashmap without the need to clone and build it from scratch
         this method should only be used when there's a necessity to compute a massive amount of queries (es. PatterGoal.evaluate)
         or if other kind of queries are needed; otherwise rely on the getPlacedCard() method
        */
        return (HashMap<Point, Card>) Collections.unmodifiableMap(board);
    }

    public ItemCollection getInventory() {
        return new ItemCollection(inventory);
    }

    public void addItems(ItemCollection itemCollection) {
        this.inventory.add(itemCollection);
    }
    public void addItem(Corner corner) {
        this.inventory.add(corner);
    }
    public void removeItems(ItemCollection itemCollection) {
        this.inventory.sub(itemCollection);
    }
    public void removeItem(Corner corner) {
        this.inventory.add(corner);
    }



}
