package it.polimi.ingsw.view.ui.gui;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.model.player.Player;
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


public class GraphicalUserInterface extends Application implements UserInterface, Runnable {
    private GUIScene currentScene;

    private static Stage window;

    private AnchorPane chat = null;
    private ChatController chatController = null;

    @Override
    public void start(Stage stage) throws Exception {
        window = new Stage();
        this.setStartingScene();
    }

    @Override
    public void resetError() {

    }

    @Override
    public void init() {}

    @Override
    public synchronized void setGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playerTurn) {

    }

    @Override
    public synchronized void setStartingScene() {
        changeScene("/fxml/Lobby.fxml");
    }

    @Override
    public synchronized void setCreateGameScene() {
        changeScene("/fxml/CreateGame.fxml");
    }

    @Override
    public synchronized void setJoinGameScene() {
        changeScene("/fxml/JoinGame.fxml");
    }

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

    @Override
    public synchronized void setWaitPlayersScene() {
        changeScene("/fxml/WaitingForPlayers.fxml");
    }

    @Override
    public synchronized void setPlaceStartScene() {
        changeScene("/fxml/StartingCard.fxml");
    }

    @Override
    public synchronized void setSelectGoalScene() {
        changeScene("/fxml/PrivateGoal.fxml");
    }

    @Override
    public synchronized void setDrawScene() {

    }

    @Override
    public synchronized void setPlayerBoardScene(Player nickname) {
        changeScene("/fxml/GameView.fxml");
    }

    @Override
    public synchronized void setChatScene(Player player) {
        throw new UnsupportedOperationException("GUI does not implement a chat scene");
    }

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

            stage.setTitle("Scoreboard");
            stage.setScene(new Scene(parent));
            stage.show();
        });
    }


    @Override
    public synchronized void update() {
        if(chat != null) Platform.runLater(() -> chatController.updateChat());
        if (currentScene != null) Platform.runLater(() -> currentScene.update());
    }

    @Override
    public synchronized void reportError(RuntimeException exception) {
        if(currentScene != null) Platform.runLater(() -> this.currentScene.reportError(exception));
    }

    @Override
    public void run() {
        Application.launch(GraphicalUserInterface.class);
    }
}

