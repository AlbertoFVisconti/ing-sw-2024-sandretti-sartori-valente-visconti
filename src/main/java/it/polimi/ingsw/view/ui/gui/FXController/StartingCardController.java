package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.PlaceStartCardMessage;
import it.polimi.ingsw.network.Client;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

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
