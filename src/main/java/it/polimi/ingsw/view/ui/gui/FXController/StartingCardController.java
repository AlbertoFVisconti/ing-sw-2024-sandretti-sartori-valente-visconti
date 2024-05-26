package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.PlaceStartCardMessage;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

public class StartingCardController extends UserInterface {

    public ImageView startingcardback;
    public ImageView startingcardfront;
    boolean selected = false;
    @FXML
    private AnchorPane TablePane;

    @FXML
    public void initialize()  {
        FXGraphicalUserInterface.currentInterface = this;
//        System.out.println(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getFrontpath());
//        System.out.println(Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getFrontpath())).toString());

        startingcardfront.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getFrontpath())).toString()
        ));
        startingcardback.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getBackpath())).toString()
        ));
    }
    @FXML
    void SelectFront(MouseEvent event) throws IOException {
        selected = true;
        Client.getInstance().getUserInterface().setSelectedside(0);
        Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(false));
    }
    @FXML
    void SelectBack(MouseEvent event) throws IOException {
        selected = true;
        Client.getInstance().getUserInterface().setSelectedside(1);
        Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(true));
    }

    @Override
    public void update() {
        if (selected) {
            selected = false;
            Platform.runLater(() -> {
                try {
                    Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/PrivateGoal.fxml")));
                    Stage window = new Stage();
                    window.setScene(new Scene(nextPageParent));
                    window.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TablePane.getScene().getWindow().hide();
            });
        }
    }

    @Override
    public void reportError(RuntimeException exception) throws RemoteException {

    }
}
