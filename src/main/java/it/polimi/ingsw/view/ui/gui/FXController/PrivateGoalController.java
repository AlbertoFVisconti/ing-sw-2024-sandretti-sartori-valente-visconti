package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.SelectGoalMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.view.ui.gui.FXGraphicalUserInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

public class PrivateGoalController extends UserInterface {
    public ImageView privategoal1;
    public ImageView privategoal2;
    boolean selected = false;

    public void initialize()  {
        FXGraphicalUserInterface.currentInterface = this;
//        System.out.println(Client.getInstance().getUserInterface().getLocalPlayer().getAvailableGoals()[0].getPath());
//        System.out.println(Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getAvailableGoals()[0].getPath())).toString());

        privategoal1.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getAvailableGoals()[0].getPath())).toString()
        ));
        privategoal2.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getAvailableGoals()[1].getPath())).toString()
        ));
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
    public void reportError(RuntimeException exception) throws RemoteException {

    }

    @Override
    public void update() {
        if(selected) {
            selected = false;
            Platform.runLater(() -> {
                try {
                    Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/GameView.fxml")));
                    Stage window = new Stage();
                    window.setScene(new Scene(nextPageParent));
                    window.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
