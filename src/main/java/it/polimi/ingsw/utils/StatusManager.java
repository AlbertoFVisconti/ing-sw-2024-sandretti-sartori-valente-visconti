package it.polimi.ingsw.utils;

import it.polimi.ingsw.events.saving.GameSavingMessage;
import it.polimi.ingsw.events.saving.PlayerSavingMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;

public class StatusManager {

    public void saveGame(Game game) throws IOException {
        String filename = game.getIdGame() + ".ser";
        ArrayList<PlayerSavingMessage> playerSavingMessageArray = new ArrayList<>();
        for (Player p : game.getPlayers()) {
            PlayerSavingMessage playerSavingMessage = new PlayerSavingMessage(p.getNickname(), p.getColor(),
                    p.getBoard(), p.getPlayerCards(), p.getPrivateGoal(), p.getInventory());
            playerSavingMessageArray.add(playerSavingMessage);
        }
        GameSavingMessage gameSavingMessage = new GameSavingMessage(game.getExpectedPlayers(), playerSavingMessageArray,
                game.getIdGame(), game.getGoldCardsDeck(), game.getResourceCardsDeck(), game.getVisibleCards(),
                game.getScoreBoard(), game.getCommonGoals(), game.getStartCardsDeck(), game.getGoalsDeck());

        FileOutputStream file = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(gameSavingMessage);
        out.close();
        file.close();

    }

    public Game restoreGame(String gameName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(gameName);
        ObjectInputStream in = new ObjectInputStream(fileInputStream);
        GameSavingMessage resotredGameMessage = (GameSavingMessage) in.readObject();
        Game g = new Game(resotredGameMessage);
        in.close();
        fileInputStream.close();
        return g;
    }

}
