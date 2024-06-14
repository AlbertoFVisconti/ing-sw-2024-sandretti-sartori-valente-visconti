package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.SelectGoalMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.gui.MediaManager;
import javafx.fxml.FXML;
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
    }
    @FXML
    public void selectGoal2() {
        selected = true;
        Client.getInstance().getServerHandler().sendMessage(new SelectGoalMessage(1));
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
