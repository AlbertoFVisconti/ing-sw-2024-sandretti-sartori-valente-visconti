package it.polimi.ingsw.view.ui;


import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.RemoteException;
import java.util.*;

public class TextualUserInterface extends UserInterface{
    private final Scanner scanner;

    private String gameStatusMessage;
    private String lastCommand;

    public TextualUserInterface(Scanner scanner) {
        this.scanner = scanner;
        lastCommand = "";

        gameStatusMessage = """
                Connected to the server. Use:
                \t- !list                          to get a list of the available games
                \t- !create exp_players nickname   to create a new game
                \t- !join gameid nickname          to join one of the game
                \t- !help                          to get the list of commands""";

        System.out.println("\n\n\n\n\n\n\n\n\n"+gameStatusMessage);
    }

    @Override
    public void run() {
        while(true) {
            String command;
            do {
                command = scanner.nextLine();
            }while(command.isEmpty());

            executeCommand(command, true);
        }
    }

    private void executeCommand(String command, boolean newCommand) {
        lastCommand = "";
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        if(command.startsWith("!")) {
            String[] tokens = command.split(" ");

            Player player;
            int i;
            switch (tokens[0]) {
                case "!list":
                    lastCommand = command;
                    if(newCommand) {
                        this.getServerHandler().getAvailableGames();
                    }

                    System.out.println("Available games:\n");
                    if(this.availableGames != null) {
                        for (Integer gameID : this.availableGames) {
                            System.out.println("\t" + gameID);
                        }
                    }

                    break;
                case "!create":
                    this.getServerHandler().createGame(Integer.parseInt(tokens[1]), tokens[2]);
                    break;
                case "!join":
                    this.getServerHandler().joinGame(Integer.parseInt(tokens[1]), tokens[2]);
                    break;
                case "!help":
                    lastCommand = command;
                    System.out.println("WIP");
                    break;
                case "!colors":
                    lastCommand = command;
                    System.out.println("Available colors: " + this.gameModel.getAvailableColor());
                    break;
                case "!set_col":
                    this.getServerHandler().selectColor(PlayerColor.valueOf(tokens[1].toUpperCase()));
                    break;
                case "!players_list":
                    lastCommand = command;
                    System.out.println("Players:\n");
                    for(Player p : gameModel.getPlayers()) {
                        System.out.println("\t" + p.nickName + " - " + p.getColor());
                    }
                    break;
                case "!starting":
                    lastCommand = command;
                    System.out.println("Your starting card:\n");
                    System.out.println(this.getLocalPlayer().getStartCard());
                    break;
                case "!place_start":
                    this.getServerHandler().placeStartCard(Integer.parseInt(tokens[1]) == 1);
                    break;
                case "!private_goals":
                    lastCommand = command;

                    System.out.println("Your private goals:");

                    i = 0;
                    for(Goal g : this.getLocalPlayer().getAvailableGoals()) {
                        System.out.println("Card n°"+i);
                        System.out.println(g);
                        i++;
                    }
                    break;
                case "!sel_goal":
                    this.getServerHandler().selectPrivateGoal(Integer.parseInt(tokens[1]));
                    break;
                case "!board":
                    lastCommand = command;

                    player = null;
                    if (tokens.length == 1) player = this.getLocalPlayer();
                    else {
                        for(Player p : gameModel.getPlayers()) {
                            if (p.nickName.equals(tokens[1])) {
                                player = p;
                                break;
                            }
                        }
                    }

                    if(player == null) {
                        System.out.println("player not recognized");
                    }
                    else {
                        for(CardLocation cardLocation : player.getBoard().keySet()) {
                            System.out.println("Card in " + cardLocation + ":");

                            System.out.println(player.getPlacedCard(cardLocation));

                            System.out.println("\n\n");
                        }
                    }

                    break;
                case "!hand":
                    lastCommand = command;

                    player = null;
                    if (tokens.length == 1) player = this.getLocalPlayer();
                    else {
                        for(Player p : gameModel.getPlayers()) {
                            if (p.nickName.equals(tokens[1])) {
                                player = p;
                                break;
                            }
                        }
                    }

                    if(player == null) {
                        System.out.println("player not recognized");
                    }
                    else {
                        i = 0;
                        for(PlayCard card : player.getPlayerCards()) {
                            System.out.println(player.nickName+"'s card n°" + i + ":");

                            System.out.println(card);

                            System.out.println("\n\n");

                            i++;
                        }
                    }

                    break;
                case "!place":
                    this.getServerHandler().placeCard(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]) == 1, new CardLocation(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4])));
                    break;
                case "!decks":
                    lastCommand =command;
                    System.out.println("Decks:");
                    System.out.println("\tResource: " + this.gameModel.getResourceCardsDeck().getTopOfTheStack());
                    System.out.println("\tGolden: " + this.gameModel.getGoldCardsDeck().getTopOfTheStack());

                    break;
                case "!visible_cards":
                    lastCommand = command;

                    i = 0;
                    for(PlayCard card : gameModel.getVisibleCards()) {
                        System.out.println("Visible cards n°"+i+":");
                        System.out.println(card);
                        System.out.println();
                    }

                    break;
                case "!draw":
                    this.getServerHandler().drawCard(Integer.parseInt(tokens[1]));
                    break;

                case "!pick_up":
                    this.getServerHandler().drawCard(Integer.parseInt(tokens[1]) + 2);
                    break;
            }
        }

        System.out.println("\n\n");
        if(this.getLocalPlayer() != null) System.out.println("Playing as: " + getLocalPlayer().nickName);
        System.out.println(gameStatusMessage);
        System.out.print(">");
    }

    @Override
    protected void update() {
        executeCommand(lastCommand, false);
    }

    @Override
    public void updateGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playersTurn) throws RemoteException {
        super.updateGameStatus(gameStatus, turnStatus, playersTurn);


        switch (gameStatus) {
            default:
                break;
            case GameStatus.LOBBY:
                gameStatusMessage = """
                        Successfully connected to the game. You need to select a color. Use:
                        \t- !colors                get a list of the available colors
                        \t- !set_col color_id      to try to select the color
                        \t- !players_list          to print the list of connected players
                        \t- !help                  to get the list of commands""";
                break;
            case GameStatus.GAME_CREATION:
                gameStatusMessage = "Game started, you need to "+(this.getLocalPlayer().getPlacedCard(new CardLocation(0,0) ) == null ? "place starting and " : "")+(this.getLocalPlayer().getPrivateGoal() == null ? "select private goal and" : "")+" wait for others to complete this step. Use:\n" +
                        "\t- !starting              to print information about the starting card\n" +
                        "\t- !place_start [side]    to place the start card (0->front, 1->back)\n" +
                        "\t- !private_goals         to print the available private goals\n" +
                        "\t- !sel_goal [goal_id]    to select the private goal\n" +
                        "\t- !help                  to get the list of commands";
                break;
            case GameStatus.NORMAL_TURN:
            case GameStatus.LAST_TURN:
                if(this.getPlayersTurn().equals(this.getLocalPlayer().nickName)) {
                    gameStatusMessage = "It's your turn to ";
                    if (turnStatus == TurnStatus.PLACE) {
                        gameStatusMessage = gameStatusMessage + "place a card. Use:\n" +
                                "\t- !board [playername]        to print the board [of the specified player]\n" +
                                "\t- !hand [playername]         to print the hand [of the specified player]\n" +
                                "\t- !place card_num side x y   to place the card\n" +
                                "\t- !help                      to get the list of commands";
                    }
                    else {
                        gameStatusMessage = gameStatusMessage + "pick up a card. Use:\n" +
                                "\t- !board [playername]        to print the board [of the specified player]\n" +
                                "\t- !hand [playername]         to print the hand [of the specified player]\n" +
                                "\t- !decks                     to print info about the decks\n" +
                                "\t- !visible_cards             to print the visible cards\n" +
                                "\t- !draw deck_id              to draw from the specified deck\n" +
                                "\t- !pick_up card_num          to pick up the specified visible card\n" +
                                "\t- !help                      to get the list of commands";
                    }
                }
                else {
                    gameStatusMessage = "It's "+playersTurn+ "'s turn to "+ (turnStatus == TurnStatus.PLACE ? "place a card" : "pick up a card") +". Use:\n" +
                            "\t- !board [playername]        to print the board [of the specified player]\n" +
                            "\t- !hand [playername]         to print the hand [of the specified player]\n" +
                            "\t- !decks                     to print info about the decks\n" +
                            "\t- !visible_cards             to print the visible cards\n" +
                            "\t- !help                      to get the list of commands";
                }
                break;
            case GameStatus.END:



        }
    }
}
