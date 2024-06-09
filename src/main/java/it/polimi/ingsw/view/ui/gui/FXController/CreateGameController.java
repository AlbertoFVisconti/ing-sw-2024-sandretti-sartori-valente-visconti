package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.network.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class CreateGameController implements GUIScene {

    @FXML
    private Button CreateButton;
    private final Integer[] expectedplayersarray = {2, 3, 4};

    @FXML
    private ChoiceBox<Integer> ExpectedPlayersComboBox = new ChoiceBox<>();
    @FXML
    private Button GoBackButton;

    @FXML
    private Label InsertNicknameLabel;

    @FXML
    private TextField NicknameField;

    @FXML
    private Label NumberofPlayersLabel;

    @FXML
    private ImageView codexLogo;
    @FXML
    private ImageView BackArrow;

    @FXML
    private ImageView codexWallpaper;

    @FXML
    public void initialize() {

    }

    @FXML
    void CreateGameFX(ActionEvent event) throws IOException {
        Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(0, true, ExpectedPlayersComboBox.getValue(), NicknameField.getText()));
    }

    @FXML
    void GoBackToLobby(MouseEvent event) throws IOException {
        Client.getInstance().getView().getUserInterface().setStartingScene();
    }

    @Override
    public void reportError(RuntimeException exception) {

    }


    @Override
    public void setup() {
        ExpectedPlayersComboBox.getItems().addAll(expectedplayersarray);
    }

    @Override
    public void update() {

    }
}