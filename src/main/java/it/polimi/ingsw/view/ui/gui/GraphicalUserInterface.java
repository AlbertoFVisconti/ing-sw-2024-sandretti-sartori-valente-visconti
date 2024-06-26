package it.polimi.ingsw.view.ui.gui;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.view.ui.gui.FXController.ChatController;
import it.polimi.ingsw.view.ui.gui.FXController.GUIScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * GraphicalUserInterface allows to present the game's information and to catch the player's input
 * through a javafx-based GUI application
 */
public class GraphicalUserInterface extends Application implements UserInterface, Runnable {
    // reference to the controller that handles the currently displayed scene
    private GUIScene currentScene;

    private static Stage window;

    // chat container and controller
    private AnchorPane chat = null;
    private ChatController chatController = null;

    private GUIScene scoreBoardScene = null;

    /**
     * Method launched when the javafx application is run.
     * Sets up the stage and notifies the Client that the
     * user interface is ready.
     *
     * @param stage default stage
     */
    @Override
    public void start(Stage stage) {
        window = stage;
        Client.getInstance().confirmUIInitialized();
    }

    /**
     * resetError does nothing in the GUI.
     * GUI reports errors through pop-up
     * Therefore, there's no need to reset.
     */
    @Override
    public void resetError() {}

    /**
     * GUI doesn't need the game status to be internally stored.
     * This method does nothing.
     *
     * @param gameStatus current GameStatus phase
     * @param turnStatus current TurnStatus (PLACE/DRAW)
     * @param playerTurn the nickname of the player that needs to play
     */
    @Override
    public synchronized void setGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playerTurn) {}

    /**
     * Displays the GUI scene that allows the player to connect to the server
     */
    @Override
    public void setConnectionScene() {
        changeScene("/fxml/Protocol.fxml");
    }

    /**
     * Displays the GUI scene that represents the main scene
     */
    @Override
    public synchronized void setMainScene() {
        changeScene("/fxml/Main.fxml");
    }

    /**
     * Displays the GUI scene that allows to create a game
     */
    @Override
    public synchronized void setCreateGameScene() {
        changeScene("/fxml/CreateGame.fxml");
    }

    /**
     * Displays the GUI scene that allows to join a game
     */
    @Override
    public synchronized void setJoinGameScene() {
        changeScene("/fxml/JoinGame.fxml");
    }

    /**
     * helper method that change that currently displayed scene
     *
     * @param fxml the resource path where the FXML of the scene can be found
     */
    private void changeScene(String fxml) {
        Platform.runLater(
                () -> {
                    if(this.chat == null) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/chat.fxml"));
                        try {
                            chat = fxmlLoader.load();
                            chatController = fxmlLoader.getController();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    try {
                        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxml)));
                        Parent newRoot = loader.load();
                        this.currentScene = loader.getController();

                        this.currentScene.addChat(chat);

                        window.setScene(new Scene(newRoot));
                        window.show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Displays the GUI scene where the user can select their color and view the connected players
     */
    @Override
    public synchronized void setWaitPlayersScene() {
        changeScene("/fxml/WaitingForPlayers.fxml");
    }

    /**
     * Displays the GUI scene that allows the user to place the starting card
     */
    @Override
    public synchronized void setPlaceStartScene() {
        changeScene("/fxml/StartingCard.fxml");
    }

    /**
     * Displays the GUI scene that allows the user to select the private goal
     */
    @Override
    public synchronized void setSelectGoalScene() {
        changeScene("/fxml/PrivateGoal.fxml");
    }

    /**
     * This method does nothing, since GUI doesn't support a scene
     * dedicated to the draw/pick up.
     * GUI merges the board and draw GUI scene in a single GUI scene.
     */
    @Override
    public synchronized void setDrawScene() {}

    /**
     * Displays the board of the local player (also the drawable cards).
     * The parameter is ignored since switching to another player's board
     * is an operation handled internally by the controller.
     *
     * @param player ignored.
     */
    @Override
    public synchronized void setPlayerBoardScene(Player player) {
        changeScene("/fxml/GameView.fxml");
    }

    /**
     * Unsupported operation, since the GUI doesn't support a scene dedicated to
     * the Chat alone
     *
     * @param player ignored
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public synchronized void setChatScene(Player player) {
        throw new UnsupportedOperationException("GUI does not implement a chat scene");
    }

    /**
     * Displays the scoreboard as a secondary window
     */
    @Override
    public synchronized void setScoreScene() {
        Platform.runLater( () -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ScoreBoard.fxml"));
            Parent parent;
            try {
                parent = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            scoreBoardScene = fxmlLoader.getController();

            stage.setTitle("Scoreboard");
            stage.setScene(new Scene(parent));
            stage.show();
        });
    }

    /**
     * Displays the GUI scene that allows the user to read the game rules
     */
    @Override
    public synchronized void setRuleScene() {
        changeScene("/fxml/RulesPage.fxml");
    }


    /**
     * Allows view to request the GUI to be updated.
     * It forward the update request to the currently displayed scene's controller
     */
    @Override
    public synchronized void update() {
        Platform.runLater( () -> {
                    if(scoreBoardScene != null) scoreBoardScene.update();
                    if(chat != null) chatController.updateChat();
                    if (currentScene != null) currentScene.update();
                }
        );
    }

    /**
     * Allows to display an error through the GUI.
     * It forward the report request to the currently displayed scene's controller
     *
     * @param exception RuntimeException that carries the error data
     */
    @Override
    public synchronized void reportError(RuntimeException exception) {
        if(currentScene != null) Platform.runLater(() -> this.currentScene.reportError(exception));
    }

    /**
     * Thread method that launches the javafx application
     */
    @Override
    public void run() {
        Application.launch(GraphicalUserInterface.class);
    }
}

