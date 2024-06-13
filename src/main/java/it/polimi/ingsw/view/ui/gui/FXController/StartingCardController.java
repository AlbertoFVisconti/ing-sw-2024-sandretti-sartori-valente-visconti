package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.PlaceStartCardMessage;
import it.polimi.ingsw.network.Client;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class StartingCardController extends GUIScene {

    public ImageView startingcardback;
    public ImageView startingcardfront;
    public AnchorPane chatContainer;
    boolean selected = false;
    @FXML
    private AnchorPane TablePane;

    @FXML
    public void initialize()  {
        try {
            startingcardfront.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getStartCard().getFrontpath())).toString()
            ));
            startingcardback.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getStartCard().getBackpath())).toString()
            ));
        }
        catch (NullPointerException ignored){}
    }
    @FXML
    void SelectFront(MouseEvent event) throws IOException {
        selected = true;
        Client.getInstance().getView().setSelectedside(0);
        Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(false));
    }
    @FXML
    void SelectBack(MouseEvent event) throws IOException {
        selected = true;
        Client.getInstance().getView().setSelectedside(1);
        Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(true));
    }

    @Override
    public void update() {
        this.initialize();
    }
    @Override
    protected AnchorPane getChatContainer() {
        return chatContainer;
    }
}
