package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class GameSelector {

    private Set<Game> AvailableGames;

    private ArrayList<Game> Idtogame;

    public boolean isAvailable(Game game, String Nickname){

        List<Player> temp=game.getPlayers();

        for(Player p: temp){
            if(p.getNickName().equals(Nickname))  return false;
        }
        return true;
    }
    public void RemoveGame(Game game){
        AvailableGames.remove(game);
    }

    public void CreateGame() throws IOException {
        Game g=new Game("test", "test", "test", "test"); //TODO sostituire con i valori effettivi
        AvailableGames.add(g);
    }

    public void JoinGame(int Index){



    }









}
