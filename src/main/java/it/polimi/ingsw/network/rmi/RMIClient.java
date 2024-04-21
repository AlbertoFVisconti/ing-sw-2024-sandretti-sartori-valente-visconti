package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.model.player.PlayerColor;

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

            RMIClient.playerIdentifier = virtualMainController.createGame(expectedPlayers, color, nickName);
        }
        else {
            if(!availableGames.contains(selectedGame)) {
                throw new IllegalArgumentException("There's no game with that ID");
            }

            RMIClient.playerIdentifier = virtualMainController.joinGame(selectedGame, color, nickName);
        }

        System.err.println("Successfully joined the game. Identifier: " + RMIClient.playerIdentifier);
    }
}
