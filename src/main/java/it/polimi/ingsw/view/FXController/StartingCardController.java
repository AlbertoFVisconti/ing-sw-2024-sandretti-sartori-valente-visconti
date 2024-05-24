package it.polimi.ingsw.view.FXController;

import it.polimi.ingsw.events.messages.client.PlaceStartCardMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.FXGraphicalUserInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StartingCardController {

    public ImageView startingcardback;
    public ImageView startingcardfront;
    @FXML
    private AnchorPane TablePane;
    @FXML
    public void initialize()  {
        startingcardfront= new ImageView(new Image(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getFrontpath()));
        startingcardback= new ImageView(new Image(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getBackpath()));
    }
    @FXML
    void SelectFront(MouseEvent event) throws IOException {
        Client.getInstance().getUserInterface().setSelectedside(0);
        Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(false));

    }
    @FXML
    void SelectBack(MouseEvent event) throws IOException {
        Client.getInstance().getUserInterface().setSelectedside(1);
        Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(true));

    }

}
