package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.View;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class RMIClient {
    private static String playerIdentifier;
    private static VirtualMainController virtualMainController;
    private static VirtualController virtualGameController;
    private static final Scanner scanner = new Scanner(System.in);
    private static View view;


    private static void joinGame() throws RemoteException {
        PlayerColor color;

        do {
            System.out.println("Select color (1 -> RED, 2 -> YELLOW, 3 -> GREEN, 4 ->BLUE): ");
            color = switch (scanner.nextInt()) {
                case 1 -> PlayerColor.RED;
                case 2 -> PlayerColor.YELLOW;
                case 3 -> PlayerColor.GREEN;
                case 4 -> PlayerColor.BLUE;
                default -> null;
            };

            if(color == null) {
                System.err.println("Invalid color code!");
            }
        } while(color == null);

        System.out.println("Insert nickname: ");
        scanner.nextLine();
        String nickName = scanner.nextLine();


        List<Integer> availableGames = virtualMainController.getAvailableGames();
        int selectedGame;

        do {
            System.out.println("List of available games:");
            for (Integer game : availableGames) {
                System.out.println("\t" + game);
            }
            System.out.println("Select game to join (insert -1 to create game, -2 to reload): ");

            selectedGame = scanner.nextInt();

            if(selectedGame == -2) {
                availableGames = virtualMainController.getAvailableGames();
            }
            else if(selectedGame != -1 && !availableGames.contains(selectedGame)) {
                System.err.println("Invalid selection!");
            }

        } while(selectedGame < -1 && !availableGames.contains(selectedGame));

        if(selectedGame == -1){
            int expectedPlayers;
            do {
                System.out.println("How many players do you expect? ");
                expectedPlayers = scanner.nextInt();

                if(expectedPlayers <= 0) {
                    System.err.println("Invalid number of players!");
                }
            } while (expectedPlayers <= 0);


            virtualMainController.createGame(view, expectedPlayers, color, nickName);

        }
        else {
            if(!availableGames.contains(selectedGame)) {
                throw new IllegalArgumentException("There's no game with that ID");
            }

            virtualMainController.joinGame(view, selectedGame, color, nickName);
        }
    }

    public static void playGameTurn() throws RemoteException {
        boolean success;
        int t;
        int side;
        int x,y;
        do {
            success = true;
            System.out.println("Select card to place: ");
            t = scanner.nextInt();

            System.out.println("Select start card side (0 -> front, 1 -> back): ");
            side = scanner.nextInt();

            System.out.println("Where to you want to place the card?\n\t X=");
            x = scanner.nextInt();

            System.out.println("\tY=");
            y = scanner.nextInt();

            try {
                virtualGameController.placeCard(playerIdentifier, t, side == 1, new CardLocation(x, y));
            }
            catch (RuntimeException e) {
                System.err.println(e.getMessage());
                success = false;
            }

        } while(!success);

        do {
            success = true;
            System.out.println("Do you want to pick up a visible cards or to draw from a deck? (0 -> visible cards, 1-> decks): ");
            t = scanner.nextInt();

            if (t == 0) {
                System.out.println("Provide the index of the card you want to pick up: ");
                t = scanner.nextInt();

                try {
                    virtualGameController.drawCard(playerIdentifier, 2 + t);
                }
                catch(RuntimeException e){
                    System.err.println(e.getMessage());
                    success = false;
                }
            } else {
                System.out.println("Select the deck you want to draw from (0 -> resource, 1 -> gold):  ");
                t = scanner.nextInt();

                try {
                    virtualGameController.drawCard(playerIdentifier, t);
                }
                catch (RuntimeException e) {
                    System.err.println(e.getMessage());
                    success = false;
                }
            }
        } while(!success);
    }

    public static void playGameSetup() throws RemoteException {
        boolean success;
        int t;
        do {
            success = true;
            System.out.println("Select your private goal: ");
            t = scanner.nextInt();

            try {
                virtualGameController.selectPrivateGoal(playerIdentifier, t);
            }
            catch (RuntimeException e) {
                System.err.println(e.getMessage());
                success = false;
            }
        } while(!success);

        do {
            success = true;
            System.out.println("Select start card side (0 -> front, 1 -> back): ");
            t = scanner.nextInt();

            try {
                virtualGameController.placeStartCard(playerIdentifier, t == 1);
            }
            catch (RuntimeException e) {
                System.err.println(e.getMessage());
                success = false;
            }
        } while(!success);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {
        Registry registry = LocateRegistry.getRegistry(1234);

        virtualMainController = (VirtualMainController) registry.lookup("MainController");

        view = new View();

        boolean success;
        do {
            success = true;
            try {
                joinGame();
            }
            catch (RuntimeException e) {
                System.err.println(e.getMessage());
                success = false;
            }
        } while(!success);

        while(view.getPlayerIdentifier() == null) Thread.sleep(100);
        playerIdentifier = view.getPlayerIdentifier();
        virtualGameController =  view.getController();
        System.err.println("Successfully joined the game. Identifier: " + playerIdentifier);

        playGameSetup();

        while(true) {
            playGameTurn();
        }
    }
}
