package it.polimi.ingsw.network;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.network.serverhandlers.RMIServerHandler;
import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.network.serverhandlers.SocketServerHandler;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class Client {
    private final ServerHandler serverHandler;
    private final Scanner scanner;

    private Client(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;

        this.scanner = new Scanner(System.in);
    }

    private void joinGame() {
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


        int selectedGame=-2;
        do {
            if(selectedGame == -2)
                serverHandler.getAvailableGames();

            System.out.println("Select game to join (insert -1 to create game, -2 to reload): ");

            selectedGame = scanner.nextInt();
        } while(selectedGame <= -2);

        if(selectedGame == -1){
            int expectedPlayers;
            do {
                System.out.println("How many players do you expect? ");
                expectedPlayers = scanner.nextInt();

                if(expectedPlayers <= 0) {
                    System.err.println("Invalid number of players!");
                }
            } while (expectedPlayers <= 0);


            serverHandler.createGame(expectedPlayers, color, nickName);

        }
        else {
            serverHandler.joinGame(selectedGame, color, nickName);
        }
    }

    private void playGameTurn() {
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
                serverHandler.placeCard( t, side == 1, new CardLocation(x, y));
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
                    serverHandler.drawCard(2 + t);
                }
                catch(RuntimeException e){
                    System.err.println(e.getMessage());
                    success = false;
                }
            } else {
                System.out.println("Select the deck you want to draw from (0 -> resource, 1 -> gold):  ");
                t = scanner.nextInt();

                try {
                    serverHandler.drawCard(t);
                }
                catch (RuntimeException e) {
                    System.err.println(e.getMessage());
                    success = false;
                }
            }
        } while(!success);
    }

    private void playGameSetup() {
        boolean success;
        int t;
        do {
            success = true;
            System.out.println("Select your private goal: ");
            t = scanner.nextInt();

            try {
                serverHandler.selectPrivateGoal(t);
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
                serverHandler.placeStartCard(t == 1);
            }
            catch (RuntimeException e) {
                System.err.println(e.getMessage());
                success = false;
            }
        } while(!success);
    }

    private void run() throws InterruptedException {

        try {
            this.joinGame();
        }
        catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }


        while(serverHandler.getClientView().getPlayerIdentifier() == null) Thread.sleep(100);

        System.err.println("Successfully joined the game. Identifier: " + serverHandler.getClientView().getPlayerIdentifier());

        playGameSetup();

        while(true) {
            playGameTurn();
        }
    }

    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        int sel;
        do {
            System.out.println("Select protocol (1 -> socket, 2 -> RMI): ");
            sel = scanner.nextInt();
        } while(sel != 1 && sel != 2);

        ServerHandler handler;
        if(sel == 1) {
            handler = new SocketServerHandler(new View(), "127.0.0.1", 1235);
        }
        else {
            handler = new RMIServerHandler(new View(), "127.0.0.1", 1234);
        }

        Client client = new Client(handler);
        client.run();

    }
}
