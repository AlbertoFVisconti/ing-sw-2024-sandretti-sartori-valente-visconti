package it.polimi.ingsw.view.ui.gui;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.view.ui.gui.FXController.GUIScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class GraphicalUserInterface extends Application implements UserInterface, Runnable {
    private GUIScene currentScene;

    private static Stage window;

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
                    try {
                        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxml)));
                        Parent newRoot = loader.load();
                        this.currentScene = loader.getController();
                        window.setScene(new Scene(newRoot));
                        window.show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public synchronized void setSetColorScene() {
        changeScene("/fxml/SelectColor.fxml");
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

    }

    @Override
    public synchronized void update() {
        if (currentScene != null) Platform.runLater(() -> currentScene.update());
    }

    @Override
    public synchronized void reportError(RuntimeException exception) {
        this.currentScene.reportError(exception);
    }

    @Override
    public void run() {
        Application.launch(GraphicalUserInterface.class);
    }
}

