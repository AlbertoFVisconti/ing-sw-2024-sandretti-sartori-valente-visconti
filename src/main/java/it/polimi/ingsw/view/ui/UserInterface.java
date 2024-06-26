package it.polimi.ingsw.view.ui;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.model.player.Player;

/**
 * Interface that allows to define User Interfaces to present the
 * game data to the player and to process the player's input.
 */
public interface UserInterface {
    /**
     * Sets the interface that allows the user to connect to the server
     * by specifying the IP, the port and the protocol.
     */
    void setConnectionScene();

    /**
     * Sets the interface that represents the main scene of the game.
     */
    void setMainScene();

    /**
     * Sets the interface that allows the user to create a game by
     * providing their nickname (gameID) and the number of expected players
     */
    void setCreateGameScene();

    /**
     * Sets the interface that allows the user to join a game by
     * selecting one of the available games and by providing a nickname
     */
    void setJoinGameScene();

    /**
     * Sets the interface that represents the game Lobby.
     * The player can select their color and see the list of connected players
     */
    void setWaitPlayersScene();

    /**
     * Sets the interface that allows the user to place the starting card
     */
    void setPlaceStartScene();

    /**
     * Sets the interface that allows the user to select the private goal
     */
    void setSelectGoalScene();

    /**
     * Sets the interface that allows the user to pick up one of the available cards
     */
    void setDrawScene();

    /**
     * Sets the interface that displays the provided player's board.
     *
     * @param player Player whose board needs to be displayed.
     */
    void setPlayerBoardScene(Player player);

    /**
     * Sets the interface that displays the chat where the user can send
     * and receive message from other players (and from the server).
     *
     * @param player Player whose chat with the local player needs to be displayed, {@code null} to display the public chat
     */
    void setChatScene(Player player);

    /**
     * Sets the interface that displays the scoreboard
     */
    void setScoreScene();

    /**
     * Sets the interface that allows the user to read the game rules.
     */
    void setRuleScene();


    /**
     * Allows to update the displayed content on the user interface
     */
    void update();

    /**
     * Allows to report an error to the user through the user interface
     *
     * @param exception RuntimeException that carries the error data
     */
    void reportError(RuntimeException exception);

    /**
     * Allows to reset previously reported errors
     */
    void resetError();

    /**
     * Allows to provide the current game status data to the User interface
     *
     * @param gameStatus current GameStatus phase
     * @param turnStatus current TurnStatus (PLACE/DRAW)
     * @param playerTurn the nickname of the player that needs to play
     */
    void setGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playerTurn);
}
