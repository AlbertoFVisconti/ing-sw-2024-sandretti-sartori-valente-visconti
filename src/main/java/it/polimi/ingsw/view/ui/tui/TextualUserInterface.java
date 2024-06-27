package it.polimi.ingsw.view.ui.tui;


import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.events.messages.client.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.view.ui.tui.TUIscenes.*;

import java.security.InvalidParameterException;
import java.util.Scanner;

/**
 * TextualUserInterface allows to present the game's data to the User (and catch the user's input)
 * through the console.
 */
public class TextualUserInterface implements UserInterface {
    // currently displayed TUI scene
    private TUIScene currentScene;

    // current status message
    private String statusMessage;

    // current error message
    private String errorMessage;

    /**
     * Builds a new TextualUserInterface that reads user input from a provided scanner
     *
     * @param scanner Scanner where the user input are read
     */
    public TextualUserInterface(Scanner scanner) {
        CommandParser commandParser = new CommandParser(scanner, this);
        loadCommands(commandParser);
        commandParser.start();

        this.statusMessage = "";
        Client.getInstance().confirmUIInitialized();
    }

    /**
     * Allows to register command in the provided command parser.
     *
     * @param commandParser CommandParser where the commands need to be registered
     */
    private static void loadCommands(CommandParser commandParser) {
        commandParser.setContextualCommandExecutor(
                TextualUserInterface::forwardInputToScene
        );

        commandParser.registerCommand("!goals",
                (userInterface, tokens) -> {
                    if(Client.getInstance().getView().getLocalPlayer() == null) throw new UnsupportedOperationException("You need to be playing a game to view goals");
                    userInterface.displayGoals();
                }
        );
        // leave command
        commandParser.registerCommand("!leave",
                (userInterface, tokens) -> {
                    Client.getInstance().getServerHandler().sendMessage(new LeaveGameMessage());
                    userInterface.setMainScene();
                    Client.getInstance().getView().reset();
                }
            );


        // join command -> sets the "join game" scene
        commandParser.registerCommand("!join",
                (userInterface, tokens) -> userInterface.setJoinGameScene()
        );

        // create command -> sets the "create game" scene
        commandParser.registerCommand("!create",
                (userInterface, tokens) -> userInterface.setCreateGameScene()
        );

        // place_start command -> sets the "place starting card" scene
        commandParser.registerCommand("!place_start",
                (userInterface, tokens) -> userInterface.setPlaceStartScene()
        );

        // sel_goal command -> sets the "select goal" scene
        commandParser.registerCommand("!sel_goal",
                (userInterface, tokens) -> userInterface.setSelectGoalScene()
        );

        // set_color command -> sets the "lobby" scene
        commandParser.registerCommand("!set_color",
                (userInterface, tokens) -> userInterface.setWaitPlayersScene()
        );

        // board command -> sets the "board" scene
        commandParser.registerCommand("!board",
                (userInterface, tokens) -> {
                    if(tokens.length == 1) {
                        userInterface.setPlayerBoardScene(Client.getInstance().getView().getLocalPlayer());
                    }
                    else {
                        for(Player p : Client.getInstance().getView().getGameModel().getPlayers()) {
                            if(p.nickname.equals(tokens[1])) {
                                userInterface.setPlayerBoardScene(p);
                                return;
                            }
                        }
                        throw new InvalidParameterException("Unknown player");
                    }
                }
        );

        // draw command -> sets the "draw" scene
        commandParser.registerCommand("!draw",
                (userInterface, tokens) -> userInterface.setDrawScene()
        );

        // score command -> sets the "scoreboard" scene
        commandParser.registerCommand("!score",
                (userInterface, tokens) -> userInterface.setScoreScene()
        );

        // chat command -> sets the "chat" scene
        commandParser.registerCommand("!chat",
                (userInterface, tokens) -> {
                    if(tokens.length == 1) {
                        userInterface.setChatScene(null);
                    }
                    else {
                        for(Player p : Client.getInstance().getView().getGameModel().getPlayers()) {
                            if(p.nickname.equals(tokens[1])) {
                                userInterface.setChatScene(p);
                                return;
                            }
                        }
                        throw new InvalidParameterException("Unknown player");
                    }
                }
        );
    }

    /**
     * Allows to display goals
     */
    private synchronized void displayGoals() {
        this.currentScene = new GoalsTUIScene();
        this.update();
    }

    /**
     * Allows to forward user input to the currently displayed scene
     * input processing method.
     *
     * @param inputTokens user input that needs to be forwarded
     */
    public synchronized void forwardInputToScene(String[] inputTokens) {
        this.currentScene.processInput(inputTokens);
    }

    /**
     * Sets the TUI scene that allows the user to connect to the server
     */
    @Override
    public synchronized void setConnectionScene() {
        this.currentScene = new ProtocolTUIScene();
        this.update();
    }

    /**
     * Sets the TUI scene that represents the main scene of the game
     */
    @Override
    public synchronized void setMainScene() {
        this.currentScene = new MainScreenTUIScene();
        this.update();
    }

    /**
     * Sets the TUI scene that allows to create a game
     */
    @Override
    public synchronized void setCreateGameScene() {
        this.currentScene = new CreateGameTUIScene();
        this.update();
    }

    /**
     * Sets the TUI scene that allows to join a game
     */
    @Override
    public synchronized void setJoinGameScene() {
        Client.getInstance().getServerHandler().sendMessage(new GameListRequestMessage());
        this.currentScene = new JoinGameTUIScene();
        this.update();
    }

    /**
     * Sets the TUI scene that allows the user to select color and view the list of connected players
     */
    @Override
    public synchronized void setWaitPlayersScene() {
        this.currentScene = new LobbyTUIScene();
        this.update();
    }

    /**
     *  Sets the TUI scene that allows the user to place the starting card
     */
    @Override
    public synchronized void setPlaceStartScene() {
        this.currentScene = new PlaceStartCardTUIScene();
        this.update();
    }

    /**
     * Sets the TUI scene that allows the user to select the private goal
     */
    @Override
    public synchronized void setSelectGoalScene() {
        this.currentScene = new SelectGoalTUIScene();
        this.update();
    }

    /**
     * Sets the TUI scene that allows the user to draw/pick up a card
     */
    @Override
    public synchronized void setDrawScene() {
        this.currentScene = new DrawTUIScene();
        this.update();
    }

    /**
     * Sets the TUI scene that allows to view the board of
     * one of the players playing the same game as the local player.
     *
     * @param player Player whose board needs to be displayed.
     */
    @Override
    public synchronized void setPlayerBoardScene(Player player) {
        this.currentScene = new PlayerBoardTUIScene(player);
        this.update();
    }

    /**
     * Sets the TUI scene that allows to read and send chat messages
     *
     * @param player Player whose chat with the local player needs to be displayed, {@code null} to display the public chat
     */
    @Override
    public synchronized void setChatScene(Player player) {
        this.currentScene = new ChatTUIScene(player);
        this.update();
    }

    /**
     * Sets the TUI scene that allows to view the game scoreboard
     */
    @Override
    public synchronized void setScoreScene() {
        this.currentScene = new GameResultsTUIScene();
        this.update();
    }

    /**
     * Sets the TUI scene that allows to read the game rules
     */
    @Override
    public synchronized void setRuleScene() {}

    /**
     * Allows view to request the YUI to be updated.
     * It forward the update request to the currently displayed scene
     */
    @Override
    public synchronized void update() {
        System.out.println(System.lineSeparator().repeat(50));
        this.currentScene.render(this.statusMessage + errorMessage);
    }

    /**
     * Allows to display an error through the TUI.
     * It forward the report request to the currently displayed scene
     *
     * @param exception RuntimeException that carries the error data
     */
    @Override
    public synchronized void reportError(RuntimeException exception) {
        this.errorMessage = "\n\nError: " + exception.getMessage();
        if(currentScene != null) currentScene.reset();
        this.update();
    }

    /**
     * Allows to reset the error message
     */
    @Override
    public synchronized void resetError() {
        this.errorMessage = "";
    }

    /**
     * Allows to provide the current game status data to the TUI.
     * This is mostly used to compose the status message.
     *
     * @param gameStatus current GameStatus phase
     * @param turnStatus current TurnStatus (PLACE/DRAW)
     * @param playerTurn the nickname of the player that needs to play
     */
    @Override
    public synchronized void setGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playerTurn) {
        Player p = Client.getInstance().getView().getLocalPlayer();
        boolean isLocalPlayersTurn = (p.nickname.equals(playerTurn));

        switch (gameStatus) {
            default:break;

            case LOBBY:
                statusMessage = "Waiting for players to join.";
                if(p.getColor() == null) statusMessage +=" You must select a color";
                break;

            case GAME_CREATION:
                statusMessage = "Game started, you need to:\n";
                if(p.getPlacedCardSlot(new CardLocation(0,0)) == null) {
                    statusMessage += "\t- place your starting card (!place_start)\n";
                }
                if(p.getPrivateGoal() == null) {
                    statusMessage += "\t- select your private goal (!sel_goal)\n";
                }
                statusMessage += "\t- wait";

                break;

            case LAST_TURN:
            case NORMAL_TURN:

                if(isLocalPlayersTurn) {
                    statusMessage = "It's your turn to ";
                }
                else {
                    statusMessage = "It's " + playerTurn + "'s turn to ";
                }

                if(turnStatus == TurnStatus.PLACE) {
                    statusMessage += "place a card";
                    if(isLocalPlayersTurn) statusMessage += " (!board)";
                }
                else {
                    statusMessage += "pick up a card";

                    if(isLocalPlayersTurn) statusMessage += " (!draw)";
                }

                break;
        }
    }
}
