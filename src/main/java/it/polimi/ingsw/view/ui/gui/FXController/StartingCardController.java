package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.PlaceStartCardMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.gui.MediaManager;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class StartingCardController extends GUIScene {
    public ImageView startingCardBack;
    public ImageView startingCardFront;
    public AnchorPane chatContainer;
    boolean selected = false;

    @FXML
    public void initialize()  {
        startingCardFront.getStyleClass().add("clickable");
        startingCardBack.getStyleClass().add("clickable");

        startingCardFront.setImage(MediaManager.getInstance().getImage(
                Client.getInstance().getView().getLocalPlayer().getStartCard().getFrontPath()
        ));
        startingCardBack.setImage(MediaManager.getInstance().getImage(
                Client.getInstance().getView().getLocalPlayer().getStartCard().getBackPath()
        ));
    }
    @FXML
    void SelectFront(MouseEvent event) {
        selected = true;
        Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(false));
    }
    @FXML
    void SelectBack(MouseEvent event) {
        selected = true;
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
