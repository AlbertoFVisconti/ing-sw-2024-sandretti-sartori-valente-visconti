package it.polimi.ingsw.view.ui;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.model.player.Player;

public interface UserInterface {
    void setProtocolScene();
    void setStartingScene();
    void setCreateGameScene();
    void setJoinGameScene();

    void setWaitPlayersScene();
    void setPlaceStartScene();
    void setSelectGoalScene();
    void setDrawScene();
    void setPlayerBoardScene(Player nickname);
    void setChatScene(Player player);
    void setScoreScene();


    void update();
    void reportError(RuntimeException exception);
    void resetError();

    void setGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playerTurn);
}
