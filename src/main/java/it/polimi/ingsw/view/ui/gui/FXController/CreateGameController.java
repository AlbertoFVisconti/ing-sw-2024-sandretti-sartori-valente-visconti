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
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CreateGameController extends GUIScene {

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
    void CreateGameFX(ActionEvent event) throws IOException {
        Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(0, true, ExpectedPlayersComboBox.getValue(), NicknameField.getText()));
    }

    @FXML
    void GoBackToLobby(MouseEvent event) throws IOException {
        Client.getInstance().getView().getUserInterface().setStartingScene();
    }

    @FXML
    public void initialize() {
        ExpectedPlayersComboBox.getItems().addAll(expectedplayersarray);
    }

    @Override
    protected AnchorPane getChatContainer() {
        return null;
    }

    @Override
    public void update() {

    }
}