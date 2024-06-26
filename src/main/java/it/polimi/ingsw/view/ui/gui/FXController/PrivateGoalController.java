package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.SelectGoalMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.gui.MediaManager;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * PrivateGoalController handles the GUI scene that allows the GUI user to
 * select the private goals once the game starts.
 */
public class PrivateGoalController extends GUIScene {
    // first available private goal
    public ImageView privateGoal1;
    // second available private goal
    public ImageView privateGoal2;

    // AnchorPane where the chat can be put by the GUI manager
    public AnchorPane chatContainer;

    /**
     * When the interface is loaded its setup the two private clickable cards
     */
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

    /**
     * Triggered when the user clicks on the first available goal.
     * It sends a message to the server with the user selection.
     */
    @FXML
    public void selectGoal1() {
        Client.getInstance().getServerHandler().sendMessage(new SelectGoalMessage(0));
    }

    /**
     * Triggered when the user clicks on the second available goal.
     * It sends a message to the server with the user selection.
     */
    @FXML
    public void selectGoal2() {
        Client.getInstance().getServerHandler().sendMessage(new SelectGoalMessage(1));
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

    /**
     * Allows to view to request the interface to update
     * the displayed content. This interface contains a dynamic element (the available goal).
     */
    @Override
    public void update() {
        this.initialize();
    }
}
