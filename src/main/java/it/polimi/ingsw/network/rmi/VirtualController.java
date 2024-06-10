package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 */
public interface VirtualController extends Remote {

    /**
     * Allows the player to place on of their cards on their board.
     * <p>
     * The server might answer by reporting an error in the following cases:
     * <ul>
     *     <li>The playerIdentifier is not recognised</li>
     *     <li>The player identified by playerIdentified is not expected to play (it is not their turn)</li>
     *     <li>The provided index exceed the player's hand size (or is negative number)</li>
     *     <li>The provided CardLocation is un valid (a card's already placed there, or one of the neighbours has an hidden corner that prevents placement)</li>
     *     <li>The current game phase doesn't expect the user to place a card</li>
     * </ul>
     *
     * @param playerIdentifier The string that the player received while joining/creating the game.
     * @param index            The index of the player's hand that contains the cards that needs to be placed.
     * @param onBackSide       {@code true} if the cards should be placed with the back side up, {@code false} otherwise.
     * @param location         The CardLocation that represents the desired placement location of the card.
     * @throws RemoteException  in case of errors with the remote communication.
     */
    void placeCard(String playerIdentifier, int index, boolean onBackSide, CardLocation location) throws RemoteException;

    /**
     * Allows the player to pick up one of the visible cards or to draw from one of the decks.
     * <p>
     * If the provided index is 0, the player is trying to draw from the first deck (resource cards),
     * if it is 1, the player is drawing from the second deck (gold cards).
     * If the provided index is greater or equal to 2, then the player is picking up the (index-2)th visible card.
     * <p>
     * The server might answer by reporting an error in the following cases:
     * <ul>
     *     <li>The playerIdentifier is not recognised</li>
     *     <li>The player identified by playerIdentified is not expected to play (it is not their turn)</li>
     *     <li>The provided index is out of bound </li>
     *     <li>The current game phase doesn't expect the player to draw/pick up a card</li>
     * </ul>
     *
     * @param playerIdentifier The string that the player received while joining/creating the game.
     * @param index            The number that represents what the player's trying to draw/pick up.
     * @throws RemoteException  in case of errors with remote communication.
     */
    void drawCard(String playerIdentifier, int index) throws RemoteException;

    /**
     * Allows the player to place the first card of the game on their board.
     * <p>
     * The server might answer by reporting an error in the following cases:
     * <ul>
     *     <li>The playerIdentifier is not recognised</li>
     *     <li>The player identified by playerIdentified already placed their starting card</li>
     *     <li>The current game phase doesn't expect the player to place the starting card</li>
     * </ul>
     *
     * @param playerIdentifier The string that the player received while joining/creating the game.
     * @param onBackSide       {@code true} if the cards should be placed with the back side up, {@code false} otherwise.
     * @throws RemoteException  in case of errors with the remote communication.
     */
    void placeStartCard(String playerIdentifier, boolean onBackSide) throws RemoteException;

    /**
     * Allows the player to place the first card of the game on their board.
     * <p>
     * The server might answer by reporting an error in the following cases:
     * <ul>
     *     <li>The playerIdentifier is not recognised</li>
     *     <li>The player identified by playerIdentified already placed their starting card</li>
     *     <li>The current game phase doesn't expect the player to select a private goal</li>
     * </ul>
     *
     * @param playerIdentifier The string that the player received while joining/creating the game.
     * @param index            The index of the available goal the player has chosen.
     * @throws RemoteException  in case of errors with the remote communication.
     */
    void selectPrivateGoal(String playerIdentifier, int index) throws RemoteException;

    /**
     * Allows the player to select their color.
     * The server might answer by reporting an error in the following cases:
     * <ul>
     *     <li>The playerIdentifier is not recognised</li>
     *     <li>The selected color was already taken</li>
     *     <li>The game already started</li>
     * </ul>
     *
     * @param playerIdentifier the string that the player received while joining/creating the game.
     * @param color the PlayerColor that represent the player's choice
     * @throws RemoteException in case of errors with the remote communication
     */
    void selectColor(String playerIdentifier, PlayerColor color) throws RemoteException;

    /**
     * Allows the player to send a message in the chat.
     *
     * @param playerIdentifier the string that the player received while joining/creating the game
     * @param message the message that the player want to send
     * @param addressee the nickname of the player that needs to receive this message, {@code null} for public messages
     * @throws RemoteException in case of errors with the remote communication
     */
    void sendChatMsg(String playerIdentifier, String message, String addressee) throws RemoteException;
}
