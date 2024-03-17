package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.goals.Goal;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashMap;

public class Player {
    public final String nickName;
    public final PlayerColor color;
    private StartCard startCard;
    private PlayCard[] playerCards;
    private Goal privateGoal;

    private HashMap<Point, Card> board;
    private ItemCollection inventory;


    public Player(String name, PlayerColor playerColor, StartCard startingCard) {
        nickName=name;
        color=playerColor;
        startCard=startingCard;
        board=new HashMap<Point,Card>();
        playerCards=new PlayCard[3];
    }

    /**
     allows to add a card to the "hand" of the player, throws InvalidParamException if: the card is null, the index is too big
     or there already is a card at that index
     @param card the card you want to be placed
     @param index the index you want the card to be placed (<=2)

     **/
    public void setPlayerCard(PlayCard card, int index) throws InvalidParameterException {
        if (index>2|| playerCards[index]!=null) throw new InvalidParameterException("cannot draw a card here");
        else if(card==null) throw new InvalidParameterException("please select a card first");
        else playerCards[index]=card;

    }
    /**
     * allows to place a card from the hand of a player to his board, the position the card was is then set to null,
     * throws an exception if there already is a card in the selected location or index is too big
     * @param index the index of the card you want to place (<=2)
     * @param location the location(expressed as point) where the card needs to be placed
     * **/
    public void placeCard(int index, Point location) throws InvalidParameterException{
        if (this.getPlacedCard(location)!=null|| index>2) throw new InvalidParameterException();
        else {
            board.put(location,playerCards[index]);
            playerCards[index]=null;
        }

    }

    public void placeStartingCard() throws Exception{
        if (startCard==null) throw new Exception("no start card found");
        board.put(new Point(0,0),startCard);
        startCard=null;
    }
    /**it does what you think it does**/
    public Goal getPrivateGoal() {
        return privateGoal;
    }

    /**it does what you think it does**/
    public void setPrivateGoal(Goal privateGoal) {
        this.privateGoal = privateGoal;
    }

    /**it does what you think it does**/
    public Card getPlacedCard(Point point) {
        return this.board.get(point);
    }

    /**returns an unmodifiable view of the board **/
    public HashMap<Point, Card> getBoard() {
        /*
         this allows to safely expose the hashmap without the need to clone and build it from scratch
         this method should only be used when there's a necessity to compute a massive amount of queries (es. PatterGoal.evaluate)
         or if other kind of queries are needed; otherwise rely on the getPlacedCard() method
        */
        return (HashMap<Point, Card>) Collections.unmodifiableMap(board);
    }
    /**return ItemCollection with the number of visible corner at runtime **/
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
