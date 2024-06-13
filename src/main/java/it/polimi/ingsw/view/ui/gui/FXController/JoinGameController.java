package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.GameListRequestMessage;
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
import java.util.HashSet;

public class JoinGameController extends GUIScene {

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
        Client.getInstance().getView().getUserInterface().setStartingScene();
    }

    @FXML
    public void JoinGameX(ActionEvent event) throws IOException {
        Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(selectGame.getValue(), false, 0, nicknameField.getText()));
    }

    public void RefreshGame(ActionEvent actionEvent) {
        Client.getInstance().getServerHandler().sendMessage(new GameListRequestMessage());
    }

    @FXML
    public void initialize() {
        Client.getInstance().getServerHandler().sendMessage(new GameListRequestMessage());
    }

    @Override
    public void update() {
        selectGame.getItems().clear();

        HashSet<Integer> availableGames = Client.getInstance().getView().getAvailableGames();
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
    @Override
    protected AnchorPane getChatContainer() {
        return null;
    }

}
