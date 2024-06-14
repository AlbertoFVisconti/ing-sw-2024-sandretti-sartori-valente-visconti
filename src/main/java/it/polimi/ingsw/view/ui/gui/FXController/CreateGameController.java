package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.network.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class CreateGameController extends GUIScene {
    private final Integer[] expectedPlayersChoices = {2, 3, 4};

    @FXML
    private ChoiceBox<Integer> expectedPlayersChoiceBox = new ChoiceBox<>();


    @FXML
    private TextField nicknameFieldText;

    @FXML
    void createGame(ActionEvent event) {
        Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(null, true, expectedPlayersChoiceBox.getValue(), nicknameFieldText.getText()));
    }

    @FXML
    void goBackToLobby(MouseEvent event) {
        Client.getInstance().getView().getUserInterface().setStartingScene();
    }

    @FXML
    public void initialize() {
        expectedPlayersChoiceBox.getItems().addAll(expectedPlayersChoices);
    }

    @Override
    protected AnchorPane getChatContainer() {
        return null;
    }

    @Override
    public void update() {

    }
}