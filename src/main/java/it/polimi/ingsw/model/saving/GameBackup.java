package it.polimi.ingsw.model.saving;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;

import java.io.Serializable;

/**
 * GameBackup allows to store all the information needed to reload a game and
 * to make it looks like the game was never actually closed and reloaded.
 *
 * @param gameData game data
 * @param gameStatus GameStatus that describes the current phase of the game
 * @param turnStatus TurnStatus that describes the current phase of the current turn
 */
public record GameBackup(GameData gameData, GameStatus gameStatus, TurnStatus turnStatus) implements Serializable { }