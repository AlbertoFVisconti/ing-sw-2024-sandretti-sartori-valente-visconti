package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.network.Client;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * CreateGameController allows the GUI client player to create a game providing
 * the number of player and the nickname of the creating player.
 */
public class CreateGameController extends GUIScene {
    // possible number of players for a new game
    private final Integer[] expectedPlayersChoices = {2, 3, 4};

    // number of player choice box
    @FXML
    private ChoiceBox<Integer> expectedPlayersChoiceBox = new ChoiceBox<>();

    // text field to input game creator nickname (will also serve as gameID/lobby name)
    @FXML
    private TextField nicknameFieldText;

    /**
     * Triggered when the user clicks on the "create game" button.
     * Sends the game creation message to the server.
     */
    @FXML
    void createGame() {
        Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(null, true, expectedPlayersChoiceBox.getValue(), nicknameFieldText.getText()));
    }

    /**
     * Triggered when the user clicks on the arrows that allows to go back to the main scene
     */
    @FXML
    void goBackToLobby() {
        Client.getInstance().getView().getUserInterface().setMainScene();
    }

    /**
     * Initialize the interface by adding possible choices to the choice box
     */
    @FXML
    public void initialize() {
        expectedPlayersChoiceBox.getItems().addAll(expectedPlayersChoices);
    }

    /**
     * Used to retrieve the chat container from the controller.
     * Game creation interface doesn't support chat, thus this method simply returns null
     *
     * @return {@code null}
     */
    @Override
    protected AnchorPane getChatContainer() {
        return null;
    }

    /**
     * Used to update the interface when the server provides data.
     * Game creation interface doesn't support any dynamic element, thus this method does nothing
     */
    @Override
    public void update() {}
}