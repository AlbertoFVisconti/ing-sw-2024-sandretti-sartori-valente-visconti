package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.events.messages.client.SelectGoalMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.gui.MediaManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class PrivateGoalController extends GUIScene {
    public ImageView privateGoal1;
    public ImageView privateGoal2;
    public AnchorPane chatContainer;
    boolean selected = false;
    @FXML
    public void initialize()  {
        privateGoal1.getStyleClass().add("clickable");
        privateGoal2.getStyleClass().add("clickable");

        privateGoal1.setImage(MediaManager.getInstance().getImage(
                Client.getInstance().getView().getLocalPlayer().getAvailableGoals()[0]
        ));
        privateGoal2.setImage(MediaManager.getInstance().getImage(
                Client.getInstance().getView().getLocalPlayer().getAvailableGoals()[1]
        ));
    }
    @FXML
    public void selectGoal1() {
        selected = true;
        Client.getInstance().getServerHandler().sendMessage(new SelectGoalMessage(0));
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(Client.getInstance().getView().getGameStatus().equals(GameStatus.GAME_CREATION)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText("Wait others players to select their private goal!");
            alert.show();
        }
    }
    @FXML
    public void selectGoal2() {
        selected = true;
        Client.getInstance().getServerHandler().sendMessage(new SelectGoalMessage(1));
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(Client.getInstance().getView().getGameStatus().equals(GameStatus.GAME_CREATION)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText("Wait others players to select their private goal!");
            alert.show();
        }
    }

    @Override
    protected AnchorPane getChatContainer() {
        return chatContainer;
    }
    @Override
    public void update() {
        this.initialize();
    }
}
