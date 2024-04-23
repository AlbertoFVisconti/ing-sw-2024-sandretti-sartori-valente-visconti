package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 */
public interface VirtualView extends Remote {

    /**
     * Allows the server to give the player their starting card.
     *
     * @param card the StartCard object representing the player starting card.
     * @throws RemoteException in case of error with the remote communication
     */
    void setStartingCard(StartCard card) throws RemoteException;

    /**
     * Allows the server to give the player the public goals.
     *
     * @param goals a list of Goal objects that represents the public goals for the current game.
     * @throws RemoteException in case of error with the remote communication.
     */
    void setPublicGoal(Goal[] goals) throws RemoteException;

    /**
     * Allows the server to give the player the available private goals.
     *
     * @param goals a list of Goal object that represents the available private goals.
     * @throws RemoteException in case of error with the remote communication.
     */
    void setAvailablePrivateGoals(Goal[] goals) throws RemoteException;

    /**
     * Allows the server to give the player their definitive private goal.
     *
     * @param goal the Goal object representing the player definitive private Goal.
     * @throws RemoteException in case of error with the remote communication.
     */
    void setDefinitivePrivateGoal(Goal goal) throws RemoteException;

    /**
     * Allows the server to give the player one of the cards in their hand.
     *
     * @param playerNickname the nickname of the player whose board needs to be updated.
     * @param card the PlayCard object representing the new card to put in the player's hand.
     * @throws RemoteException in case of error with the remote communication.
     */
    void setPlayersCard(String playerNickname, PlayCard card, int index) throws RemoteException;

    /**
     * Allows the server to inform the player that one of the visible cards has changed.
     *
     * @param card the PlayCard object representing the new visible card.
     * @param index the index of the visible card to change.
     * @throws RemoteException in case of error with the remote communication.
     */
    void setVisibleCard(PlayCard card, int index) throws RemoteException;

    /**
     * Allows the server to inform the player that one of the decks has changed
     *
     * @param resource the Resource object representing the resource on the back of the card on top of the deck.
     * @param index the deckID that identify the deck that needs to be updated.
     * @throws RemoteException in case of error with the remote communication
     */
    void setDeckTopResource(Resource resource, int index) throws RemoteException;

    /**
     * Allows the server to inform the player that a card was placed on a board.
     *
     * @param playerNickName the nickname of the player whose board needs to be updated.
     * @param card the StartCard object representing the player starting card.
     * @throws RemoteException in case of error with the remote communication
     */
    void placeCardOnPlayersBoard(String playerNickName, Card card, CardLocation location) throws RemoteException;
}