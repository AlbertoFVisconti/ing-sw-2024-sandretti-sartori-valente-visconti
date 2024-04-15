package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.model.ScoreBoard;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * deals with all the logic after a game is selected and created
 *
 * **/
public class GameController {
    private Game game;
    private GameStatus currstatus;
    //TODO  create some lock to avoid a data race if we want ot give the same controller to diff games, otherwise keep it as is
    /**
     * @param game  the game you want to be controlled
     * **/public GameController(Game game){
        this.game=game;
        currstatus=GameStatus.LOBBY;
        this.waitPlayers();
    }
    private boolean checkTopRight(int x, int y, Player player){
        return (!player.getBoard().containsKey(new CardLocation(x + 1, y + 1))||
                (player.getBoard().get(new CardLocation(x + 1, y + 1)).getBottomLeftCorner()!=null));

    }
    private boolean checkTopLeft(int x, int y, Player player){
        return (!player.getBoard().containsKey(new CardLocation(x - 1, y + 1))||
                (player.getBoard().get(new CardLocation(x - 1, y + 1)).getBottomRightCorner()!=null));
    }
    private boolean checkBottomRight(int x, int y, Player player){
        return (!player.getBoard().containsKey(new CardLocation(x + 1, y - 1))||
                (player.getBoard().get(new CardLocation(x + 1, y -1)).getTopLeftCorner()!=null));
    }
    private boolean checkBottomLeft(int x, int y, Player player){
        return (!player.getBoard().containsKey(new CardLocation(x - 1, y + 1))||
                (player.getBoard().get(new CardLocation(x - 1, y + 1)).getTopRightCorner()!=null));
    }

    private void waitPlayers() {
        while (currstatus.equals(GameStatus.LOBBY)) {
            if (game.getPlayers().size() == game.getExpectedPlayers()) {
                //TODO change if we consider the client connected to the server as players or not, now it's just to avoid errors
                currstatus = GameStatus.GAME_CREATION;
            }
        }
        this.setUp();
    }
    private void setUp(){
         game.shufflePlayers();
         game.startGame();

         for(Player p: game.getPlayers()) {
             Goal t1= game.getGoal();
             Goal t2= game.getGoal();
             System.out.println("Select the starting card: \n");//TODO change this with a message from the client
             Scanner scanner=new Scanner(System.in);
             int i = scanner.nextInt();
             p.setPrivateGoal(i==1? t1:t2);
             try {
                 p.placeStartingCard();
             } catch (Exception e) {
                 throw new RuntimeException(e);
             }
         }
         currstatus=GameStatus.NORMAL_TURN;
         this.normalTurn();


    }
    private void normalTurn() {
         while (currstatus.equals(GameStatus.NORMAL_TURN)){
             for (Player player: game.getPlayers()){
                 System.out.println("select the index of the card and the point where to place the card");
                 Scanner scanner=new Scanner(System.in);
                 while (true) { // TODO maybe change this with a max n of tries after when the player is sent a tutorial
                     int i = scanner.nextInt();
                     int x=  scanner.nextInt();
                     int y= scanner.nextInt();// TODO change this to a message from the client
                     if(checkBottomLeft(x,y,player)&&checkBottomRight(x,y,player)&&checkTopLeft(x,y,player)&&checkTopRight(x,y,player)) {
                         try {
                             player.placeCard(i, new CardLocation(x, y));
                             break;
                         } catch (InvalidParameterException e) {
                             throw new RuntimeException(e);
                         }
                     }
                 }
             }
             ScoreBoard scoreBoard=this.game.getScoreBoard();
             for(Player player: game.getPlayers()){
                 if(scoreBoard.getScore(player)>=20 || this.game.emptyDecks()) {
                     currstatus = GameStatus.LAST_TURN;
                     lastTurn();
                 }

             }
         }
    }
    private void lastTurn(){
        for (Player player: game.getPlayers()){
            System.out.println("select the index of the card and the point where to place the card");
            Scanner scanner=new Scanner(System.in);
            while (true) { // TODO maybe change this with a max n of tries after when the player is sent a tutorial
                int i = scanner.nextInt();
                int x=  scanner.nextInt();
                int y= scanner.nextInt();// TODO change this to a message from the client
                if(checkBottomLeft(x,y,player)&&checkBottomRight(x,y,player)&&checkTopLeft(x,y,player)&&checkTopRight(x,y,player)) {
                    try {
                        player.placeCard(i, new CardLocation(x, y));
                        break;
                    } catch (InvalidParameterException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        currstatus=GameStatus.END;

    }
    private void end(){
         return;
    }
}
