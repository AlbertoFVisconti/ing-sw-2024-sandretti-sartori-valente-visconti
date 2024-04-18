package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.GameSelector;

import java.io.IOException;
import java.util.*;
/**
 * this class manages the client before he joins a game and becomes a player, it allows him to either select or to create a game and to choose his Nickname and Color
 * **/
public class MainController {
    public static void main(String[] args) throws IOException {
        GameSelector gameSelector = new GameSelector();

        int numPlayers;

        Scanner scanner = new Scanner(System.in);

        System.out.println("insert the number of player you want in your game:");
        numPlayers= scanner.nextInt();
        int idGame = gameSelector.CreateGame(numPlayers);


        for(int i = 1; i <= numPlayers; i++) {
            System.out.println("Player #"+i);
            System.out.println("\tnow select your color:\n \t\t1-red \n \t\t2-yellow \n \t\t3-green \n \t\t4-blue");
            int color = scanner.nextInt();
            System.out.println("\tnow enter your nickname and remember, it must be unique!");
            scanner.nextLine();
            String nick = scanner.nextLine();

            try {
                gameSelector.JoinGame(idGame, color, nick);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                i--;
            }
        }


        GameController controller = gameSelector.getGame(idGame);
        controller.run();

    }
}
