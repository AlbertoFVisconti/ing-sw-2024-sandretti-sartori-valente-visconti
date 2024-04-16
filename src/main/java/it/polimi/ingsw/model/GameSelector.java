package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.decks.GoalDeckLoader;
import it.polimi.ingsw.model.decks.PlayCardDeckLoader;
import it.polimi.ingsw.model.decks.StartCardDeckLoader;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

import java.io.IOException;
import java.util.*;


public class GameSelector {
    private Map<Integer, GameController> Games;
    private int curr=0;
    public GameSelector(){
        this.Games= new HashMap<Integer, GameController>() ;
    }


    /**
     * returns true if there are no player with the same nickname in the selected game
     * @param game: the Game you want to check
     * @param Nickname: the nick you want to check
     * **/
    public boolean isAvailable(Game game, String Nickname){
        List<Player> temp=game.getPlayers();
        for(Player p: temp){
            if(p.nickName.equals(Nickname))  return false;
        }
        return true;
    }
    public void RemoveGame(int idGame){
        Games.remove(idGame);
    }
    /**
     * @param expectedPlayers: the number of player expected for that game
     * **/
    public int CreateGame(int expectedPlayers) throws IOException {
        //TODO sostituire con i valori effettivi
        GoalDeckLoader goalDeckLoader= new GoalDeckLoader("src/main/resources/json/goals.json");
        PlayCardDeckLoader resourceCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/resourcecards.json");
        PlayCardDeckLoader goldCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/goldcards.json");
        StartCardDeckLoader startCardDeckLoader= new StartCardDeckLoader("src/main/resources/json/cards/startcards.json");


        Game g=new Game(goldCardDeckLoader,resourceCardDeckLoader,startCardDeckLoader,goalDeckLoader, curr,expectedPlayers );

        GameController controller = new GameController(g);

        Games.put(curr,controller);

        return curr++;
    }

    public GameController getGame(int gameId) {
        return this.Games.get(gameId);
    }

    public void JoinGame(int idGame, int color, String Nickname) throws Exception{
        Game game=Games.get(idGame).getGame();
        if(game.getPlayers().size()==game.getExpectedPlayers()) throw new Exception("Game already full");
        if (!isAvailable(game,Nickname)) throw new Exception("Nick already taken");
        PlayerColor playerColor;
        switch (color) {
            case 1:
                playerColor = PlayerColor.RED;
                break;
            case 2:
                playerColor = PlayerColor.YELLOW;
                break;
            case 3:
                playerColor = PlayerColor.GREEN;
                break;
            default:
                playerColor = PlayerColor.BLUE;
        }
        if (!game.getAvailableColor().contains(playerColor)) throw new Exception("Color not available, choose another one");
        game.addPlayer(new Player(Nickname,playerColor));
    }
    public Set<Integer> getAvailableGames(){
        return Games.keySet();
    }

}
