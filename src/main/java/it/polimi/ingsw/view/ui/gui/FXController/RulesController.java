package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.gui.MediaManager;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * RulesController handles the interface that allows to read the game rules.
 */
public class RulesController extends GUIScene{
    // ImageView that contains the currently displayed page
    @FXML
    public ImageView rulesImage;

    // ImageView that represents the "next page" button
    @FXML
    public ImageView nextPageButton;

    // ImageView that represents the "previous page" button
    @FXML
    public ImageView previousPageButton;

    // the number of rules page
    private final int NUM_OF_PAGES = 5;
    // the currently displayed rules page
    private int currentDisplayedPage;

    /**
     * When the interface is loaded, it set the currently displayed page as the
     * first one.
     */
    @FXML
    public void initialize() {
        this.currentDisplayedPage = 1;
        switchPage();
    }

    /**
     * Update the displayed content according to the currently displayed page.
     */
    private void switchPage() {
        if(currentDisplayedPage == 1) previousPageButton.setOpacity(0.5);
        else previousPageButton.setOpacity(1);
        if(currentDisplayedPage == NUM_OF_PAGES) nextPageButton.setOpacity(0.5);
        else nextPageButton.setOpacity(1);

        this.rulesImage.setImage(
                MediaManager.getInstance().getImage("/image/rules/CodexRules"+currentDisplayedPage+".png")
        );
    }

    /**
     * Triggered when the user clicks on the "go back to lobby" button, which
     * allows to go back to the main scene, where the user can then create/join a game.
     */
    @FXML
    void goBackToLobby() {
        Client.getInstance().getView().getUserInterface().setMainScene();
    }

    /**
     * Triggered when the user clicks on the "next page" button.
     * If there's a next page, it will be displayed.
     */
    @FXML
    void nextPage() {
        if(currentDisplayedPage != NUM_OF_PAGES) {
            currentDisplayedPage++;
            switchPage();
        }
    }

    /**
     * Triggered when the user clicks on the "previous page" button.
     * If there's a previous page, it will be displayed.
     */
    @FXML
    public void previousPage() {
        if(currentDisplayedPage != 1) {
            currentDisplayedPage--;
            switchPage();
        }
    }

    /**
     * Used to update the interface when the server provides data.
     * Rules interface doesn't support any dynamic element, thus this method does nothing
     */
    @Override
    public void update() {}

    /**
     * Used to retrieve the chat container from the controller.
     * Join game interface doesn't support chat, thus this method simply returns null
     *
     * @return {@code null}
     */
    @Override
    protected AnchorPane getChatContainer() {
        return null;
    }
}
