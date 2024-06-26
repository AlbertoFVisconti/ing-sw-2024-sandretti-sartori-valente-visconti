package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.network.Client;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * MainSceneController represent the standard interface that the GUI interface user
 * will see (upon connecting) when launching the game. This interface allows to
 * join a game or create a new one.
 */
public class MainSceneController extends GUIScene{


    /**
     * Triggered when the user clicks on the "create game" button.
     * It leads to the game creation interface where the user
     * can actually create a game.
     */
    @FXML
    void createGame() {
        Client.getInstance().getView().getUserInterface().setCreateGameScene();
    }

    /**
     * Triggered when the user clicks on the "join game" button.
     * It leads to the join game interface where the user
     * can actually join a game.
     */
    @FXML
    void joinGame() {
        Client.getInstance().getView().getUserInterface().setJoinGameScene();
    }

    /**
     * Triggered when the user clicks on the "rules" button.
     * It leads to the page where the user can read the
     * game rules.
     */
    @FXML
    void goToRules() {
        Client.getInstance().getView().getUserInterface().setRuleScene();
    }

    /**
     * Used by the View to request update of the displayed command.
     * This interface does not have any dynamic content.
     * Thus, this method does nothing.
     */
    @Override
    public void update() {}

    /**
     * Allows the GUI manager to put request an AnchorPane to place the chat.
     * This interface does not support chat.
     * Thus, this method return null.
     *
     * @return {@code null}
     */
    @Override
    protected AnchorPane getChatContainer() {
        return null;
    }
}
