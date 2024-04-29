package it.polimi.ingsw.network.socket;
import it.polimi.ingsw.model.events.messages.client.*;
import it.polimi.ingsw.model.events.messages.server.ServerMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient extends Thread {
    private static String playerIdentifier;

    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    private static final String IP = "127.0.0.1";
    private static final int PORT = 1235;

    private static final Scanner scanner = new Scanner(System.in);
    private static View view;



    private static void joinGame() throws IOException {
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



        int selectedGame;
        System.out.println("Select game to join (insert -1 to create game): ");
        selectedGame = scanner.nextInt();


        if(selectedGame == -1){
            int expectedPlayers;
            do {
                System.out.println("How many players do you expect? ");
                expectedPlayers = scanner.nextInt();

                if(expectedPlayers <= 0) {
                    System.err.println("Invalid number of players!");
                }
            } while (expectedPlayers <= 0);


            ClientMessage message = new JoinGameMessage(-1, true, expectedPlayers, nickName, color, null);
            outputStream.writeObject(message);

        }
        else {

            ClientMessage message = new JoinGameMessage(selectedGame, false, -1, nickName, color, null);
            outputStream.writeObject(message);
        }
    }

    public static void playGameTurn() throws IOException {
        int t;
        int side;
        int x,y;

        System.out.println("Select card to place: ");
        t = scanner.nextInt();

        System.out.println("Select start card side (0 -> front, 1 -> back): ");
        side = scanner.nextInt();

        System.out.println("Where to you want to place the card?\n\t X=");
        x = scanner.nextInt();

        System.out.println("\tY=");
        y = scanner.nextInt();


        ClientMessage message = new PlaceCardMessage(playerIdentifier, t, side == 1, new CardLocation(x,y));
        outputStream.writeObject(message);



        System.out.println("Do you want to pick up a visible cards or to draw from a deck? (0 -> visible cards, 1-> decks): ");
        t = scanner.nextInt();

        if (t == 0) {
            System.out.println("Provide the index of the card you want to pick up: ");
            t = scanner.nextInt();

            message = new DrawCardMessage(playerIdentifier, 2+t);
            outputStream.writeObject(message);
        } else {
            System.out.println("Select the deck you want to draw from (0 -> resource, 1 -> gold):  ");
            t = scanner.nextInt();

            message = new DrawCardMessage(playerIdentifier, t);
            outputStream.writeObject(message);

        }
    }

    public static void playGameSetup() throws IOException {
        ClientMessage message;
        int t;
        System.out.println("Select your private goal: ");
        t = scanner.nextInt();


        message = new SelectGoalMessage(playerIdentifier, t);
        outputStream.writeObject(message);

        System.out.println("Select start card side (0 -> front, 1 -> back): ");
        t = scanner.nextInt();

        message = new PlaceStartCardMessage(playerIdentifier, t==1);
        outputStream.writeObject(message);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket(IP, PORT);
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        view = new View();

        new SocketClient().start();

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
        System.err.println("Successfully joined the game. Identifier: " + playerIdentifier);

        playGameSetup();

        while(true) {
            playGameTurn();
        }
    }

    @Override
    public void run() {
        ServerMessage message;

        while(true) {
            try {
                message = (ServerMessage) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            message.updateView(view);
        }
    }
}
