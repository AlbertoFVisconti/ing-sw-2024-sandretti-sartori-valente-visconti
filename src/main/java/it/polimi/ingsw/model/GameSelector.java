package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GameSelector {
    private Map<Integer,Game> Games;
    private int curr=0;

    /**
     * returns true if there are no player with the same nickname in the selected game
     * @param game: the Game you want to check
     * @param Nickname: the nick you want to check
     * **/
    public boolean isAvailable(Game game, String Nickname){
        List<Player> temp=game.getPlayers();
        for(Player p: temp){
            if(p.getNickName().equals(Nickname))  return false;
        }
        return true;
    }
    public void RemoveGame(int idGame){
        Games.remove(idGame);
    }

    public void CreateGame(int expectedPlayers) throws IOException {
        Game g=new Game(null,null,null,null, curr,expectedPlayers ); //TODO sostituire con i valori effettivi
        Games.put(curr,g);
        curr++;

    }

    public void JoinGame(int idGame) throws Exception{
        Game game=Games.get(idGame);
        if(game.getPlayers().size()==game.getExpectedPlayers()){
            throw new Exception("Game already full");
        }


    }









}
