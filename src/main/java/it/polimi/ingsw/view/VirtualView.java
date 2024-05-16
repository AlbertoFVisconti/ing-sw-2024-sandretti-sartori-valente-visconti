package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

/**
 * Remote Interface that allows to define standard methods to update a player's view.
 */
public interface VirtualView extends Remote {

    void setController(VirtualController controller) throws RemoteException;

    void setPlayerIdentifier(String playerIdentifier) throws RemoteException;

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

    /**
     * Allows the server to hand the player the available games list.
     *
     * @param availableGames a set of integer, each integer represents the ID of an available game
     * @throws RemoteException in case of error with the remote communication
     */
    void updateGameList(Set<Integer> availableGames) throws RemoteException;

    /**
     * Allows the server to confirm that the client (whose view this is) successfully joined
     * the game with a certain nickname.
     *
     * @param nickname the nickname that the clients chose when they joined the game.
     * @throws RemoteException in case of error with the remote communication.
     */
    void confirmJoin(String nickname) throws RemoteException;

    /**
     * Allows the server to provide an updated version of the players list with nicknames and colors.
     * <p>
     * An update can be triggered by a player joining, a player selecting/changing their color and by the game
     * starting (shuffling).
     *
     * @param nicknames the array of nicknames of the players in the game.
     * @param colors the array of colors of the players in the game.
     * @throws RemoteException in case of error with the remote communication.
     */
    void updatePlayersList(String[] nicknames, PlayerColor[] colors) throws RemoteException;

    /**
     * Allows the server to inform the client when the game's status changes.
     *
     * @param gameStatus the current game phase
     * @param turnStatus the current turn status (either DRAW or PLACE)
     * @param playersTurn the nickname of the player that needs to play.
     * @throws RemoteException in case of error with the remote communication
     */
    void updateGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playersTurn) throws RemoteException;

    /**
     * When an exception is raised on the Server when processing a Client's request,
     * if the client is recognized, this method interface allows to send the raised exception to the
     * client.
     *
     * @param exception a RuntimeException containing the error that needs to be reported.
     * @throws RemoteException in case of error with the remote communication.
     */
    void reportError(RuntimeException exception) throws RemoteException;

    /**
     * Allows the server to provide an updated scoreboard to the client.
     *
     * @param scoreBoard the updated scoreboard
     * @throws RemoteException in case of error with the remote communication.
     */
    void updateScore(ScoreBoard scoreBoard) throws RemoteException;
}