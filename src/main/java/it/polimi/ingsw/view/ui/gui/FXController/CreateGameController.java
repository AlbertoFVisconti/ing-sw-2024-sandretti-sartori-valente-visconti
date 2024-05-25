package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.view.ui.gui.FXGraphicalUserInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

public class CreateGameController extends UserInterface {

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
    private FXGraphicalUserInterface fxGraphicalUserInterface;

    @FXML
    public void initialize() {
        FXGraphicalUserInterface.currentInterface = this;
        ExpectedPlayersComboBox.getItems().addAll(expectedplayersarray);
    }

    @FXML
    void CreateGameFX(ActionEvent event) throws IOException {
        Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(0, true, ExpectedPlayersComboBox.getValue(), NicknameField.getText()));
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/SelectColor.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    @FXML
    void GoBackToLobby(MouseEvent event) throws IOException {
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Lobby.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    @Override
    public void reportError(RuntimeException exception) throws RemoteException {

    }


    @Override
    public void update() {

    }
}