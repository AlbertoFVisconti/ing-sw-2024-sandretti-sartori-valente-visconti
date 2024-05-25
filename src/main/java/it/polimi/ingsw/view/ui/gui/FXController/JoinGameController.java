package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.GameListRequestMessage;
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
import java.util.HashSet;
import java.util.Objects;

public class JoinGameController extends UserInterface {

    public ChoiceBox<Integer> selectGame = new ChoiceBox<>();
    public TextField nicknameField;
    public Label insertNickname;
    public Button joinButton;
    public Label noGamesAvailable;
    public Button refreshButton;
    @FXML
    private Label AvailableGamesLabel;

    @FXML
    private ImageView codexLogo;

    @FXML
    private ImageView codexWallpaper;
    @FXML
    private Button GoBackButton;

    @FXML
    void GoBackToLobby(MouseEvent event) throws IOException {
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Lobby.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    @FXML
    public void JoinGameX(ActionEvent event) throws IOException {
        Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(selectGame.getValue(), false, 0, nicknameField.getText()));
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/SelectColor.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    @FXML
    public void initialize() {
        FXGraphicalUserInterface.currentInterface = this;
        Client.getInstance().getServerHandler().sendMessage(new GameListRequestMessage());
    }

    public void RefreshGame(ActionEvent actionEvent) {
        Client.getInstance().getServerHandler().sendMessage(new GameListRequestMessage());
    }

    @Override
    public void reportError(RuntimeException exception) throws RemoteException {

    }

    @Override
    public void update() {
        selectGame.getItems().removeAll();

        HashSet<Integer> availableGames = Client.getInstance().getUserInterface().getAvailableGames();
        if (availableGames != null) {
            for (Integer gameID : availableGames) {
                selectGame.getItems().add(gameID);
            }
        } else {
            selectGame.setVisible(false);
            joinButton.setVisible(false);
            insertNickname.setVisible(false);
            nicknameField.setVisible(false);
            noGamesAvailable.setVisible(true);

        }
    }


}
