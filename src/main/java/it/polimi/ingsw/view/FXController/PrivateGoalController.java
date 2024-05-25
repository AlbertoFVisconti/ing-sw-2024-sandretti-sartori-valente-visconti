package it.polimi.ingsw.view.FXController;

import it.polimi.ingsw.events.messages.client.SelectGoalMessage;
import it.polimi.ingsw.network.Client;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PrivateGoalController {
    public ImageView privategoal1;
    public ImageView privategoal2;

    public void initialize()  {
        privategoal1= new ImageView(new Image(Client.getInstance().getUserInterface().getLocalPlayer().getAvailableGoals()[0].getPath()));
        privategoal2= new ImageView(new Image(Client.getInstance().getUserInterface().getLocalPlayer().getAvailableGoals()[1].getPath()));
    }
    @FXML
    public void SelectGoal1() {
        Client.getInstance().getServerHandler().sendMessage(new SelectGoalMessage(0));
    }
    @FXML
    public void SelectGoal2() {
        Client.getInstance().getServerHandler().sendMessage(new SelectGoalMessage(1));
    }
}
