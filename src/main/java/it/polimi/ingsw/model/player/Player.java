package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.events.Observable;
import it.polimi.ingsw.model.events.Observer;
import it.polimi.ingsw.model.events.messages.updates.PlayersBoardUpdateMessage;
import it.polimi.ingsw.model.events.messages.updates.PlayersHandUpdateMessage;
import it.polimi.ingsw.model.events.messages.updates.PrivateGoalUpdateMessage;
import it.polimi.ingsw.model.events.messages.updates.StartCardUpdateMessage;
import it.polimi.ingsw.utils.ItemCollection;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.utils.CardLocation;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Player extends Observable {
    public final String nickName;
    public String identifier;
    private Observer viewWrapper;
    public final PlayerColor color;
    private StartCard startCard;
    private final PlayCard[] playerCards;
    private Goal privateGoal;
    private Goal[] availableGoals;

    private final HashMap<CardLocation, Card> board;
    private final ItemCollection inventory;
    private boolean Disconnected;
    /**
     * Constructs a new Player object.
     *
     * @param playerIdentifier a string that is uniquely associated to this player
     * @param name String containing the player's nickname
     * @param playerColor Color value representing the unique color assigned to the player
     * @param viewWrapper player's view wrapper
     */
    public Player(String playerIdentifier , String name, PlayerColor playerColor, Observer viewWrapper) {
        this.identifier = playerIdentifier;
        this.nickName=name;
        this.color=playerColor;
        this.viewWrapper = viewWrapper;
        this.startCard=null;
        this.board=new HashMap<>();
        this.playerCards=new PlayCard[3];
        this.inventory = new ItemCollection();

        if(viewWrapper != null)
            this.subscribe(this.viewWrapper);
    }

    public void setViewWrapper(Observer viewWrapper) {
        if(this.viewWrapper != null)
            this.unsubscribe(this.viewWrapper);
        this.viewWrapper = viewWrapper;
        if(this.viewWrapper != null)
            this.subscribe(this.viewWrapper);
    }
    public Observer getViewWrapper() {
        return viewWrapper;
    }

    /**
     * Sets player's starting cards. The player will get to choose which side
     * to put place the card on.
     *
     * @param startCard the player's StartCard.
     */
    public void setStartCard(StartCard startCard) {
        this.startCard = startCard;
        this.notifyObservers(new StartCardUpdateMessage(startCard));
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

        this.notifyObservers(new PlayersHandUpdateMessage(this.nickName,card, index));
    }

    public void setAvailableGoals(Goal[] availableGoals) {
        this.availableGoals = availableGoals.clone();

        this.notifyObservers(new PrivateGoalUpdateMessage(availableGoals.clone()));
    }

    /**
     * Allows to place a card from the hand of a player to his board, the position the card was is then set to null,
     *
     * @param index      the index of the card you want to place (<=2)
     * @param onBackSide {@code true} if the cards needs to be place with the back side up, {@code} false otherwise
     * @param location   the CardLocation where the card needs to be placed
     * @throws InvalidParameterException if there already is a card in the selected location or index is too big
     */
    public void placeCard(int index, boolean onBackSide, CardLocation location) throws InvalidParameterException{
        if (this.getPlacedCard(location)!=null|| index>2) throw new InvalidParameterException();
        else {
            if(playerCards[index].isOnBackSide() != onBackSide) {
                playerCards[index].flip();
            }

            playerCards[index].place(-1 /*TODO*/);
            board.put(location,playerCards[index]);
            playerCards[index]=null;
        }

        this.notifyObservers(new PlayersBoardUpdateMessage(this.nickName, board.get(location), location));
    }

    /**
     * Places the player's StartCard in the origin of the board.
     * The orientation of the cards is also required
     *
     * @param onBackside {@code true} if the card should show the back side, {@code false} otherwise
     *
     * @throws Exception if the players has no StartCard to place
     */
    public void placeStartingCard(boolean onBackside) throws Exception{
        if (startCard==null) throw new Exception("no start card found");

        if(startCard.isOnBackSide() != onBackside) startCard.flip();

        // before we put the card in the board, we make it immutable to prevent later changes
        startCard.place(0);

        board.put(new CardLocation(0,0),startCard);
        startCard=null;

        this.notifyObservers(new PlayersBoardUpdateMessage(this.nickName,board.get(new CardLocation(0,0)), new CardLocation(0,0)));
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

        this.notifyObservers(new PrivateGoalUpdateMessage(privateGoal));
    }

    /**
     * Retrieves the Card placed in a specified location
     * <p>
     * The reference returned is a reference to the actual stored object.
     * There's no problem since the card il placed, thus immutable (un-flippable).
     *
     * @param cardLocation CardLocation representing the location to get the card from
     * @return a reference to the card in the desired location, {@code null} if there's no card
     */
    public Card getPlacedCard(CardLocation cardLocation) {
        return this.board.get(cardLocation);
    }

    /**
     * Provides the Map representing the Player's board.
     * The Map is returned using {@code Collections.unmodifiableMap()} that allows
     * to safely expose the board without the need to clone and build it from scratch.
     * <p>
     * This method should only be used when there's a necessity to compute a massive amount of queries (es. PatterGoal.evaluate)
     * or if other kind of queries are needed; otherwise rely on the getPlacedCard() method
     *
     * @return an unmodifiable view of the board
     */
    public Map<CardLocation, Card> getBoard() {
        return Collections.unmodifiableMap(board);
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
    /**
     * with no param returns if a player is disconnected, with a bool set its value to it
     * **/
    private boolean disconnected(){
        return this.Disconnected;
    }
    private void disconnected(boolean b){
        this.Disconnected=b;
    }

    public void addPlayerCard(PlayCard draw) throws Exception {
        for(int i = 0; i < playerCards.length; i++) {
            if(playerCards[i] == null) {
                playerCards[i] = draw;

                this.notifyObservers(new PlayersHandUpdateMessage(this.nickName,draw, i));
                return;
            }
        }

        throw new Exception("Player's hand is full");
    }

    public Goal[] getAvailableGoals() {
        return availableGoals;
    }

    public StartCard getStartCard() {
        return this.startCard;
    }

    public PlayCard[] getPlayerCards() {
        return playerCards;
    }
    
    public String getNickname() {
        return nickName;
    }
}
