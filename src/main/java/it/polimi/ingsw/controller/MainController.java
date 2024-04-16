package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.GameSelector;

import java.io.IOException;
import java.util.*;
public class MainController {
    public static void main(String[] args) throws IOException {
        System.out.println("select a game to join or create one");
        GameSelector gameSelector = new GameSelector();
        //TODO  change with a message from client
        Scanner scanner=new Scanner(System.in);
        int idGame = scanner.nextInt();
        while (!gameSelector.getAvailableGames().contains(idGame)){
            for(int j: gameSelector.getAvailableGames()){
                System.out.println(j);
            }
            System.out.println("inserto the number of player you want in your game:");
            int j= scanner.nextInt();
            gameSelector.CreateGame(j);
            idGame = scanner.nextInt();
        }
        while(true){
            System.out.println("now select your color:\n 1-red \n 2-yellow \n 3-green \n 4-blue");
            int color= scanner.nextInt();
            System.out.println("now enter your nickname and remember, it must be unique!");
            String nick= scanner.nextLine();
            try {
                gameSelector.JoinGame(idGame, color, nick);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
