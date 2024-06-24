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

public class TextualUserInterface implements UserInterface {
    private TUIScene currentScene;
    private String statusMessage;
    private String errorMessage;

    public TextualUserInterface(Scanner scanner) {
        CommandParser commandParser = new CommandParser(scanner, this);
        loadCommands(commandParser);
        commandParser.start();

        this.statusMessage = "";
        Client.getInstance().confirmUIInitialized();
    }

    private static void loadCommands(CommandParser commandParser) {
        commandParser.setContextualCommandExecutor(
                TextualUserInterface::forwardInputToScene
        );

        commandParser.registerCommand("!join",
                (userInterface, tokens) -> userInterface.setJoinGameScene()
        );
        commandParser.registerCommand("!create",
                (userInterface, tokens) -> userInterface.setCreateGameScene()
        );
        commandParser.registerCommand("!place_start",
                (userInterface, tokens) -> userInterface.setPlaceStartScene()
        );
        commandParser.registerCommand("!sel_goal",
                (userInterface, tokens) -> userInterface.setSelectGoalScene()
        );
        commandParser.registerCommand("!set_color",
                (userInterface, tokens) -> userInterface.setWaitPlayersScene()
        );
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
        commandParser.registerCommand("!draw",
                (userInterface, tokens) -> userInterface.setDrawScene()
        );
        commandParser.registerCommand("!score",
                (userInterface, tokens) -> userInterface.setScoreScene()
        );
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


    public void forwardInputToScene(String[] inputString) {
        this.currentScene.processInput(inputString);
    }

    @Override
    public void setProtocolScene() {
        this.currentScene = new ProtocolTUIScene();
        this.update();
    }

    @Override
    public void setStartingScene() {
        this.currentScene = new MainScreenTUIScene();
        this.update();
    }

    @Override
    public void setCreateGameScene() {
        this.currentScene = new CreateGameTUIScene();
        this.update();
    }

    @Override
    public void setJoinGameScene() {
        Client.getInstance().getServerHandler().sendMessage(new GameListRequestMessage());
        this.currentScene = new JoinGameTUIScene();
        this.update();
    }

    @Override
    public void setWaitPlayersScene() {
        this.currentScene = new LobbyTUIScene();
        this.update();
    }

    @Override
    public void setPlaceStartScene() {
        this.currentScene = new PlaceStartCardTUIScene();
        this.update();
    }

    @Override
    public void setSelectGoalScene() {
        this.currentScene = new SelectGoalTUIScene();
        this.update();
    }

    @Override
    public void setDrawScene() {
        this.currentScene = new DrawTUIScene();
        this.update();
    }

    @Override
    public void setPlayerBoardScene(Player player) {
        this.currentScene = new PlayerBoardTUIScene(player);
        this.update();
    }

    @Override
    public void setChatScene(Player player) {
        this.currentScene = new ChatTUIScene(player);
        this.update();
    }

    @Override
    public void setScoreScene() {
        this.currentScene = new GameResultsTUIScene();
        this.update();
    }

    @Override
    public void update() {
        System.out.println(System.lineSeparator().repeat(50));
        this.currentScene.render(this.statusMessage + errorMessage);
    }

    @Override
    public void reportError(RuntimeException exception) {
        this.errorMessage = "\n\nError: " + exception.getMessage();
        if(currentScene != null) currentScene.reset();
        this.update();
    }

    @Override
    public void resetError() {
        this.errorMessage = "";
    }

    @Override
    public void setGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playerTurn) {
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
