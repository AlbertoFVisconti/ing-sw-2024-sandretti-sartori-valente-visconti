package it.polimi.ingsw.view.FXController;

import it.polimi.ingsw.view.ui.FXGraphicalUserInterface;
import it.polimi.ingsw.view.ui.UserInterface;
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
    private Stage window;
    private Parent nextPageParent;

    @FXML
    public void initialize() {
        ExpectedPlayersComboBox.getItems().addAll(expectedplayersarray);
    }

    @FXML
    void CreateGameFX(ActionEvent event) {
        this.getServerHandler().createGame(ExpectedPlayersComboBox.getValue(), NicknameField.getText());
    }

    @FXML
    void GoBackToLobby(ActionEvent event) throws IOException {
        nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Lobby.fxml")));
        window = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    @Override
    public void reportError(RuntimeException exception) throws RemoteException {

    }


    @Override
    protected void update() {

    }
}