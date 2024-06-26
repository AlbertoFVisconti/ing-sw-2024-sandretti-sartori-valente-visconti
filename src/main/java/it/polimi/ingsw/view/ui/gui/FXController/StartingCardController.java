package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.PlaceStartCardMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.gui.MediaManager;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * StartingCardController handles the GUI scene that allows the GUI user to
 * place the starting card once the game starts.
 */
public class StartingCardController extends GUIScene {
    // ImageView that contains the back side of the player's starting card
    public ImageView startingCardBack;

    // ImageView that contains the front side of the player's starting card
    public ImageView startingCardFront;

    // AnchorPane that can contain the chat
    public AnchorPane chatContainer;

    /**
     * When the interface is loaded its setup the two private clickable cards
     */
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

    /**
     * Triggered when the user clicks on the front side of the starting card.
     * It sends a message to the server with the user selection.
     */
    @FXML
    void SelectFront() {
        Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(false));
    }

    /**
     * Triggered when the user clicks on the back side of the starting card.
     * It sends a message to the server with the user selection.
     */
    @FXML
    void SelectBack() {
        Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(true));
    }

    /**
     * Allows to view to request the interface to update
     * the displayed content. This interface contains a dynamic element (the starting card).
     */
    @Override
    public void update() {
        this.initialize();
    }

    /**
     * Allows the GUI manager to retrieve the AnchorPane where
     * the chat can be put.
     * Since this interface supports chat, an AnchorPane is provided.
     *
     * @return AnchorPane where the chat can be put
     */
    @Override
    protected AnchorPane getChatContainer() {
        return chatContainer;
    }
}
