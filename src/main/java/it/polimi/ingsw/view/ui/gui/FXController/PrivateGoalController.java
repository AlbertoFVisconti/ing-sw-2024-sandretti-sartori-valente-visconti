package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.SelectGoalMessage;
import it.polimi.ingsw.network.Client;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class PrivateGoalController extends GUIScene {
    public ImageView privategoal1;
    public ImageView privategoal2;
    public AnchorPane chatContainer;
    boolean selected = false;

    @FXML
    public void initialize()  {
        try {
            privategoal1.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getAvailableGoals()[0].getPath())).toString()
            ));
            privategoal2.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getAvailableGoals()[1].getPath())).toString()
            ));
        }
        catch (NullPointerException ignored) {}
    }
    @FXML
    public void SelectGoal1() {
        selected = true;
        Client.getInstance().getServerHandler().sendMessage(new SelectGoalMessage(0));
    }
    @FXML
    public void SelectGoal2() {
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
