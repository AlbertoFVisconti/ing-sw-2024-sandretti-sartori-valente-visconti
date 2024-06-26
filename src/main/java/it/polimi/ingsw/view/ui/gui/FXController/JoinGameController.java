package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.GameListRequestMessage;
import it.polimi.ingsw.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.network.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.HashSet;

/**
 * JoinGameController allows GUI interface users to join an existing game.
 */
public class JoinGameController extends GUIScene {
    // choice box that will contain the available games
    @FXML
    public ChoiceBox<String> selectGame = new ChoiceBox<>();

    // TextField that allows the user to input their nickname
    @FXML
    public TextField nicknameField;

    // label that asks the user to insert the nickname
    @FXML
    public Label insertNickname;

    // button that allows to user to join a game
    @FXML
    public Button joinButton;

    // label that tells the user that there are no available games
    @FXML
    public Label noGamesAvailable;

    // button that allows to refresh the list of available games
    @FXML
    public Button refreshButton;

    @FXML
    public Label avaiableGamesLabel;

    /**
     * Triggered when the user clicks on the arrows that allows to go back to the main scene
     */
    @FXML
    void goBackToLobby() {
        Client.getInstance().getView().getUserInterface().setMainScene();
    }

    /**
     * Triggered when the user clicks on the "join game" button.
     * It will send the join game message to the server.
     */
    @FXML
    public void joinGame() {
        Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(selectGame.getValue(), false, 0, nicknameField.getText()));
    }

    /**
     * Triggered when the user clicks on the "refresh" button.
     * It will send a game list request message to the server
     */
    public void refreshGame() {
        Client.getInstance().getServerHandler().sendMessage(new GameListRequestMessage());
    }

    /**
     * When the Join game interface is loaded, this method is called, and it
     * will send a game list request to the server.
     */
    @FXML
    public void initialize() {
        Client.getInstance().getServerHandler().sendMessage(new GameListRequestMessage());
    }

    /**
     * Allows the view to request an update of the displayed content: the list of available games.
     */
    @Override
    public void update() {
        selectGame.getItems().clear();

        HashSet<String> availableGames = Client.getInstance().getView().getAvailableGames();
        if (availableGames != null && !availableGames.isEmpty()) {
            selectGame.setVisible(true);
            joinButton.setVisible(true);
            insertNickname.setVisible(true);
            nicknameField.setVisible(true);
            noGamesAvailable.setVisible(false);
            avaiableGamesLabel.setVisible(true);

            for (String gameID : availableGames) {
                selectGame.getItems().add(gameID);
            }
        } else {
            selectGame.setVisible(false);
            joinButton.setVisible(false);
            insertNickname.setVisible(false);
            nicknameField.setVisible(false);
            noGamesAvailable.setVisible(true);
            avaiableGamesLabel.setVisible(false);
        }
    }

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
