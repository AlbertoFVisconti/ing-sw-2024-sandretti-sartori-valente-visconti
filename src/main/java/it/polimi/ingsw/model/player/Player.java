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
    private final PlayCard[] playerCards;
    private Goal privateGoal;

    private final HashMap<Point, Card> board;
    private final ItemCollection inventory;

    /**
     * Constructs a new Player object.
     *
     * @param name String containing the player's nickname
     * @param playerColor Color value representing the unique color assigned to the player
     */
    public Player(String name, PlayerColor playerColor) {
        nickName=name;
        color=playerColor;
        startCard=null;
        board=new HashMap<>();
        playerCards=new PlayCard[3];
        inventory = new ItemCollection();
    }

    /**
     * Sets player's starting cards. The player will get to choose which side
     * to put place the card on.
     *
     * @param startCard the player's StartCard.
     */
    public void setStartCard(StartCard startCard) {
        this.startCard = startCard;
    }

    /**
     * Allows to add a card to the "hand" of the player.
     *
     * @param card the card you want to be placed
     * @param index the index you want the card to be placed (0<=index<=2)
     * @throws InvalidParameterException throws InvalidParamException if the card is null, the index is too big or there already is a card at that index
     */
    public void setPlayerCard(PlayCard card, int index) throws InvalidParameterException {
        if (index<0||index>2|| playerCards[index]!=null) throw new InvalidParameterException("cannot draw a card here");
        else if(card==null) throw new InvalidParameterException("please select a card first");
        else playerCards[index]=card;

    }
    /**
     * Allows to place a card from the hand of a player to his board, the position the card was is then set to null,
     *
     * @param index the index of the card you want to place (<=2)
     * @param location the location(expressed as point) where the card needs to be placed
     * @throws InvalidParameterException if there already is a card in the selected location or index is too big
     */
    public void placeCard(int index, Point location) throws InvalidParameterException{
        if (this.getPlacedCard(location)!=null|| index>2) throw new InvalidParameterException();
        else {
            board.put(location,playerCards[index]);
            playerCards[index]=null;
        }

    }

    /**
     * Places the player's StartCard in the origin of the board.
     * It's assumed that the player already decided the orientation of the card.
     *
     * @throws Exception if the players has no StartCard to place
     */
    public void placeStartingCard() throws Exception{
        if (startCard==null) throw new Exception("no start card found");

        // before we put the card in the board, we make it immutable to prevent later changes
        startCard.place(0);

        board.put(new Point(0,0),startCard);
        startCard=null;
    }

    /**
     * Retrieves the player's private goal
     *
     * @return the player's private goal
     */
    public Goal getPrivateGoal() {
        return privateGoal;
    }

    /**
     * Sets the player's private goal
     *
     * @param privateGoal the new private goal
     */
    public void setPrivateGoal(Goal privateGoal) {
        this.privateGoal = privateGoal;
    }

    /**
     * Retrieves the Card placed in a specified location
     * <p>
     * The reference returned is a reference to the actual stored object.
     * There's no problem since the card il placed, thus immutable (un-flippable).
     *
     * @param point Point representing the location to get the card from
     * @return a reference to the card in the desired location, {@code null} if there's no card
     */
    public Card getPlacedCard(Point point) {
        return this.board.get(point);
    }

    /**
     * Provides the HashMap representing the Player's board.
     * The Map is returned using {@code Collections.unmodifiableMap()} that allows
     * to safely expose the board without the need to clone and build it from scratch.
     * <p>
     * This method should only be used when there's a necessity to compute a massive amount of queries (es. PatterGoal.evaluate)
     * or if other kind of queries are needed; otherwise rely on the getPlacedCard() method
     *
     * @return an unmodifiable view of the board
     */
    public HashMap<Point, Card> getBoard() {
        return (HashMap<Point, Card>) Collections.unmodifiableMap(board);
    }

    /**
     * Retrieves all the Items in the players inventory.
     * <p>
     * An item is in the player's inventory if a card placed on the board has a corner
     * containing such item on the face that's currently facing up and if the corner
     * isn't covered by another card.
     * <p>
     * A corner is covered if there's a card in the same direction as the Corner
     * (es. for a top-left corner, you would need to check the top-left neighbouring card)
     * and that cards was placed later in the game.
     *
     * @return ItemCollection with the number of visible corner in the player's board
     */
    public ItemCollection getInventory() {
        return new ItemCollection(inventory);
    }

    /**
     * Adds a set of Items to the player's inventory
     *
     * @param itemCollection ItemCollection whose content needs to be added to the player's inventory
     */
    public void addItems(ItemCollection itemCollection) {
        this.inventory.add(itemCollection);
    }

    /**
     * Adds a single item to the player's inventory
     *
     * @param corner the item to add to the player's inventory
     */
    public void addItem(Corner corner) {
        this.inventory.add(corner);
    }

    /**
     * Removes a set of Items from the player's inventory
     *
     * @param itemCollection ItemCollection whose content needs to be removed from the player's inventory
     */
    public void removeItems(ItemCollection itemCollection) {
        this.inventory.sub(itemCollection);
    }

    /**
     * Removes a single item from the player's inventory
     *
     * @param corner the item to remove from the player's inventory
     */
    public void removeItem(Corner corner) {
        this.inventory.sub(corner);
    }
}
