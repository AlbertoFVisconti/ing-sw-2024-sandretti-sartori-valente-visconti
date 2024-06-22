package it.polimi.ingsw.model.saving;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;

import java.io.Serializable;

public class GameBackup implements Serializable {
    private final GameSaving gameData;
    private final GameStatus gameStatus;
    private final TurnStatus turnStatus;

    public GameBackup(GameSaving gameData, GameStatus gameStatus, TurnStatus turnStatus) {
        this.gameData = gameData;
        this.gameStatus = gameStatus;
        this.turnStatus = turnStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public TurnStatus getTurnStatus() {
        return turnStatus;
    }

    public GameSaving getGameData() {
        return gameData;
    }
}

