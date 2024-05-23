package it.polimi.ingsw.view.ui.tui;


import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.events.messages.client.*;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.ui.UserInterface;

import java.rmi.RemoteException;
import java.util.*;

public class TextualUserInterface extends UserInterface {
    private final Scanner scanner;

    private String gameStatusMessage;
    private String serverErrorMessage;
    private String syntaxErrorMessage;
    private String lastCommand;

    public TextualUserInterface(Scanner scanner) {
        this.scanner = scanner;
        lastCommand = "";

        serverErrorMessage = null;
        syntaxErrorMessage = null;

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

            this.serverErrorMessage = null;
            this.syntaxErrorMessage = null;

            executeCommand(command, true);
        }
    }

    private void executeCommand(String command, boolean newCommand) {
        lastCommand = "";
        // "clearing" the console
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        if(command.startsWith("!")) {

            String[] tokens = command.split(" ");

            Player player;
            PlayerColor color;
            int expectedPlayers;
            String nickname;
            int selection;
            int x,y;

            StringBuilder msgText;

            int i;
            switch (tokens[0]) {
                default:
                    syntaxErrorMessage = "unknown command";
                    break;
                case "!private_msg":
                    if(tokens.length < 3) {
                        syntaxErrorMessage = "invalid num of arguments";
                        break;
                    }

                    player = null;
                    for(Player p : gameModel.getPlayers()) {
                        if (p.nickName.equals(tokens[1])) {
                            player = p;
                            break;
                        }
                    }

                    if(player == null) {
                        syntaxErrorMessage = "selected player was not recognized";
                        break;
                    }

                    msgText = new StringBuilder();
                    for(int h = 2; h < tokens.length; h++) {
                        msgText.append(tokens[h]);
                        if(h<tokens.length-1) msgText.append(' ');
                    }

                    Client.getInstance().getServerHandler().sendMessage(new ClientChatMsgMessage(msgText.toString(), player.nickName));
                    break;

                case "!msg":
                    if(tokens.length < 2) {
                        syntaxErrorMessage = "invalid num of arguments";
                        break;
                    }

                    msgText = new StringBuilder();
                    for(int h = 1; h < tokens.length; h++) {
                        msgText.append(tokens[h]);
                        if(h<tokens.length-1) msgText.append(' ');
                    }

                    Client.getInstance().getServerHandler().sendMessage(new ClientChatMsgMessage(msgText.toString()));



                    break;

                case "!chat":
                    if(this.getLocalPlayer() == null) {
                        syntaxErrorMessage = "you must join a game to chat with other players";
                        break;
                    }
                    lastCommand = command;

                    nickname = null;
                    if(tokens.length != 1) {
                        nickname = tokens[1];
                    }

                    Printer.printChat(gameModel.getChat(), this.getLocalPlayer().nickName, nickname);

                    break;
                case "!list":
                    lastCommand = command;
                    if(newCommand) {
                        Client.getInstance().getServerHandler().sendMessage(new GameListRequestMessage());
                    }

                    System.out.println("Available games:\n");
                    if(this.availableGames != null) {
                        for (Integer gameID : this.availableGames) {
                            System.out.println("\t" + gameID);
                        }
                    }

                    break;
                case "!create":
                    if(tokens.length != 3) {
                        syntaxErrorMessage = "invalid num of arguments";
                        break;
                    }

                    nickname = tokens[2];

                    try {
                        expectedPlayers = Integer.parseInt(tokens[1]);
                    }
                    catch (NumberFormatException e) {
                        syntaxErrorMessage = "first argument is invalid";
                        break;
                    }

                    if(expectedPlayers < 2 || expectedPlayers > 4) {
                        syntaxErrorMessage = "a game cannot contain less then 2 players and more than 4";
                        break;
                    }


                    Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(-1, true, expectedPlayers, nickname));
                    break;
                case "!join":

                    if(tokens.length != 3) {
                        syntaxErrorMessage = "invalid num of arguments";
                        break;
                    }

                    nickname = tokens[2];

                    try {
                        selection = Integer.parseInt(tokens[1]);
                    }
                    catch (NumberFormatException e) {
                        syntaxErrorMessage = "first argument is invalid";
                        break;
                    }

                    if(this.availableGames == null || !this.availableGames.contains(selection)) {
                        syntaxErrorMessage = "the selected game was not found in the game list provided by the server";
                        break;
                    }

                    Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(selection, false, 0, nickname));
                    break;
                case "!help":
                    lastCommand = command;
                    syntaxErrorMessage = "WIP";
                    break;
                case "!colors":
                    lastCommand = command;
                    System.out.println("Available colors: " + this.gameModel.getAvailableColor());
                    break;
                case "!set_col":

                    if(tokens.length != 2) {
                        syntaxErrorMessage = "invalid num of arguments";
                        break;
                    }

                    try {
                        color = PlayerColor.valueOf(tokens[1].toUpperCase());
                    }
                    catch (IllegalArgumentException e) {
                        syntaxErrorMessage = "invalid color";
                        break;
                    }

                    Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(color));
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
                    System.out.println("\tFront:");
                    Printer.printCard(this.getLocalPlayer().getStartCard(), false);
                    System.out.println("\tBack");
                    Printer.printCard(this.getLocalPlayer().getStartCard(), true);
                    break;
                case "!place_start":

                    if(tokens.length != 2) {
                        syntaxErrorMessage = "invalid num of arguments";
                        break;
                    }

                    try {
                        selection = Integer.parseInt(tokens[1]);
                    }
                    catch (NumberFormatException e) {
                        syntaxErrorMessage = "first argument is invalid";
                        break;
                    }

                    Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(selection == 1));
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
                    if(tokens.length != 2) {
                        syntaxErrorMessage = "invalid num of arguments";
                        break;
                    }

                    try {
                        selection = Integer.parseInt(tokens[1]);
                    }
                    catch (NumberFormatException e) {
                        syntaxErrorMessage = "first argument is invalid";
                        break;
                    }

                    if(this.getLocalPlayer().getAvailableGoals().length <= selection || selection < 0) {
                        syntaxErrorMessage = "the selected goal was not found in the list provided by the server";
                        break;
                    }

                    Client.getInstance().getServerHandler().sendMessage(new SelectGoalMessage(selection));
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
                        syntaxErrorMessage = "selected player was not recognized";
                    }
                    else {
                        Printer.printBoard(player.getBoard());
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
                        Printer.printHand(player.getPlayerCards());
                    }

                    break;
                case "!score":
                    Printer.printScoreBoard(gameModel.getScoreBoard(), gameModel.getPlayers());
                    break;

                case "!place":
                    if(tokens.length != 5) {
                        syntaxErrorMessage = "invalid num of arguments";
                        break;
                    }

                    try {
                        i = Integer.parseInt(tokens[1]);
                        selection = Integer.parseInt(tokens[2]);
                        x = Integer.parseInt(tokens[3]);
                        y = Integer.parseInt(tokens[4]);
                    }
                    catch (NumberFormatException e) {
                        syntaxErrorMessage = "invalid arguments";
                        break;
                    }

                    if(this.getLocalPlayer().getPlayerCards().length <= i || i<0) {
                        syntaxErrorMessage = "the selected card index is out of bound";
                        break;
                    }

                    if(Math.abs(x)%2 != Math.abs(y)%2) {
                        syntaxErrorMessage = "invalid card location";
                        break;
                    }


                    Client.getInstance().getServerHandler().sendMessage(new PlaceCardMessage(i, selection==1, new CardLocation(x,y)));


                    break;
                case "!drawable":
                    lastCommand =command;
                    Printer.printDrawableCards(gameModel.getResourceCardsDeck(), gameModel.getGoldCardsDeck(), gameModel.getVisibleCards());

                    break;
                case "!draw":
                    if(tokens.length != 2) {
                        syntaxErrorMessage = "invalid num of arguments";
                        break;
                    }

                    try {
                        selection = Integer.parseInt(tokens[1]);
                    }
                    catch (NumberFormatException e) {
                        syntaxErrorMessage = "first argument is invalid";
                        break;
                    }

                    if(selection >= 2 || selection < 0) {
                        syntaxErrorMessage = "the selected deck does not exist";
                        break;
                    }

                    Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(selection));
                    break;

                case "!pick_up":
                    if(tokens.length != 2) {
                        syntaxErrorMessage = "invalid num of arguments";
                        break;
                    }

                    try {
                        selection = Integer.parseInt(tokens[1]);
                    }
                    catch (NumberFormatException e) {
                        syntaxErrorMessage = "first argument is invalid";
                        break;
                    }

                    if(selection >= this.gameModel.getVisibleCards().length || selection < 0) {
                        syntaxErrorMessage = "the selected visible card does not exist";
                        break;
                    }

                    Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(selection+2));
                    break;
            }
        }

        // displaying error messages
        if(serverErrorMessage != null) System.out.println("\033[0;31m" + "Server Error: " + serverErrorMessage + "\033[0m");
        if(syntaxErrorMessage != null) System.out.println("\033[0;33m" + "Syntax Error: " + syntaxErrorMessage + "\033[0m");

        System.out.println("\n\n");
        if(this.getLocalPlayer() != null) System.out.println("Playing as: " + getLocalPlayer().nickName);
        System.out.println(gameStatusMessage);
        System.out.print(">");
    }

    @Override
    public void update() {
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
                        "\t- |score                 to print the scoreboard\n" +
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
                                "\t- |score                 to print the scoreboard\n" +
                                "\t- !help                      to get the list of commands";
                    }
                    else {
                        gameStatusMessage = gameStatusMessage + "pick up a card. Use:\n" +
                                "\t- !board [playername]        to print the board [of the specified player]\n" +
                                "\t- !hand [playername]         to print the hand [of the specified player]\n" +
                                "\t- !drawable                  to print info about the decks and visible cards\n" +
                                "\t- !draw deck_id              to draw from the specified deck\n" +
                                "\t- !pick_up card_num          to pick up the specified visible card\n" +
                                "\t- |score                 to print the scoreboard\n" +
                                "\t- !help                      to get the list of commands";
                    }
                }
                else {
                    gameStatusMessage = "It's "+playersTurn+ "'s turn to "+ (turnStatus == TurnStatus.PLACE ? "place a card" : "pick up a card") +". Use:\n" +
                            "\t- !board [playername]        to print the board [of the specified player]\n" +
                            "\t- !hand [playername]         to print the hand [of the specified player]\n" +
                            "\t- !drawable                  to print info about the decks and visible cards\n" +
                            "\t- |score                 to print the scoreboard\n" +
                            "\t- !help                      to get the list of commands";
                }
                break;
            case GameStatus.END:
                    gameStatusMessage = "Game ended! Results:" + this.gameModel.getScoreBoard().toString();


        }
    }

    @Override
    public void reportError(RuntimeException exception) throws RemoteException {
        this.serverErrorMessage = exception.getMessage();
    }
}
