package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class RMIClient {
    private static String playerIdentifier;
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1234);

        VirtualMainController virtualMainController = (VirtualMainController) registry.lookup("MainController");
        VirtualController virtualGameController = (VirtualController) registry.lookup("GameController");

        VirtualView view = new View();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Select color (1 -> RED, 2 -> YELLOW, 3 -> GREEN, 4 ->BLUE): ");
        PlayerColor color = switch (scanner.nextInt()) {
            case 1 -> PlayerColor.RED;
            case 2 -> PlayerColor.YELLOW;
            case 3 -> PlayerColor.GREEN;
            case 4 -> PlayerColor.BLUE;
            default -> throw new RuntimeException("Invalid color code");
        };

        System.out.println("Insert nickname: ");
        scanner.nextLine();
        String nickName = scanner.nextLine();


        List<Integer> availableGames = virtualMainController.getAvailableGames();

        System.out.println("List of available games:");
        for(Integer game : availableGames) {
            System.out.println("\t" + game);
        }
        System.out.println("Select game to join (insert -1 to create game): ");

        int selectedGame = scanner.nextInt();

        if(selectedGame == -1){
            System.out.println("How many players do you expect? ");
            int expectedPlayers = scanner.nextInt();

            RMIClient.playerIdentifier = virtualMainController.createGame(view, expectedPlayers, color, nickName);
        }
        else {
            if(!availableGames.contains(selectedGame)) {
                throw new IllegalArgumentException("There's no game with that ID");
            }

            RMIClient.playerIdentifier = virtualMainController.joinGame(view, selectedGame, color, nickName);
        }

        System.err.println("Successfully joined the game. Identifier: " + RMIClient.playerIdentifier);

        System.out.println("Select your private goal: ");
        int t = scanner.nextInt();

        virtualGameController.selectPrivateGoal(RMIClient.playerIdentifier,t );

        System.out.println("Select start card side (0 -> front, 1 -> back): ");
        t = scanner.nextInt();

        virtualGameController.placeStartCard(RMIClient.playerIdentifier, t==1);

        int x,y ,side;
        while(true) {
            System.out.println("Select card to place: ");
            t = scanner.nextInt();

            System.out.println("Select start card side (0 -> front, 1 -> back): ");
            side = scanner.nextInt();

            System.out.println("Where to you want to place the card?\n\t X=");
            x = scanner.nextInt();

            System.out.println("\tY=");
            y = scanner.nextInt();

            virtualGameController.placeCard(RMIClient.playerIdentifier, t, side==1,new CardLocation(x,y));


            System.out.println("Do you want to pick up a visible cards or to draw from a deck? (0 -> visible cards, 1-> decks): ");
            t = scanner.nextInt();

            if(t == 0) {
                System.out.println("Provide the index of the card you want to pick up: ");
                t = scanner.nextInt();

                virtualGameController.drawCard(RMIClient.playerIdentifier, 2+t);
            }
            else {
                System.out.println("Select the deck you want to draw from (0 -> resource, 1 -> gold):  ");
                t = scanner.nextInt();

                virtualGameController.drawCard(RMIClient.playerIdentifier, t);
            }
        }
    }
}
