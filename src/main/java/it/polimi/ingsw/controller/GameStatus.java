package it.polimi.ingsw.controller;

/**
 * Enum that represents the possible states for a Game
 */
public enum GameStatus {
    LOBBY,
    GAME_CREATION,
    NORMAL_TURN,
    LAST_TURN,
    END,

    DELETED
}
