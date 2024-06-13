package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.saving.GameSaving;
import it.polimi.ingsw.model.saving.PlayerSaving;

import java.io.*;
import java.util.ArrayList;

public class StatusManager {

    public void saveGame(Game game) throws IOException {
        String filename = game.getIdGame() + ".ser";
        ArrayList<PlayerSaving> playerSavingArray = new ArrayList<>();
        for (Player p : game.getPlayers()) {
            PlayerSaving playerSaving = new PlayerSaving(p.getNickname(), p.getColor(),
                    p.getBoard(), p.getPlayerCards(), p.getPrivateGoal(), p.getInventory(), p.getStartCard(), p.getAvailableGoals());
            playerSavingArray.add(playerSaving);
        }
        GameSaving gameSaving = new GameSaving(game.getExpectedPlayers(), playerSavingArray,
                game.getIdGame(), game.getGoldCardsDeck(), game.getResourceCardsDeck(), game.getVisibleCards(),
                game.getScoreBoard(), game.getCommonGoals(), game.getStartCardsDeck(), game.getGoalsDeck());

        FileOutputStream file = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(gameSaving);
        out.close();
        file.close();

    }

    public Game restoreGame(String gameName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(gameName);
        ObjectInputStream in = new ObjectInputStream(fileInputStream);
        GameSaving resotredGameMessage = (GameSaving) in.readObject();
        Game g = new Game(resotredGameMessage);
        in.close();
        fileInputStream.close();
        return g;
    }

}
