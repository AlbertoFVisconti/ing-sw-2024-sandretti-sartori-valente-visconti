package it.polimi.ingsw.view.FXController;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.FXGraphicalUserInterface;
import it.polimi.ingsw.view.ui.UserInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaitingForPlayersController extends UserInterface {

    @FXML
    private ImageView BackArrow;

    @FXML
    private ImageView codexWallpaper;

    @FXML
    private Label player1= new Label();

    @FXML
    private Label player2= new Label();

    @FXML
    private Label player3= new Label();

    @FXML
    void GoBackToLobby(MouseEvent event) throws IOException {
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Lobby.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    @FXML
    public void initialize() {
        FXGraphicalUserInterface.currentInterface = this;
        List<Player> players;
        players = Client.getInstance().getUserInterface().getPlayersList();
        for (Player player : players) {
            if (!player.getNickname().equals(Client.getInstance().getUserInterface().getLocalPlayerName())) {
                if (player1.getText().isEmpty()) {
                    player1.setText(player.getNickname());
                } else if (player2.getText().isEmpty()) {
                    player2.setText(player.getNickname());
                } else if (player3.getText().isEmpty()) {
                    player3.setText(player.getNickname());
                }
            }

        }
    }

    @Override
    protected void update() {
        List<Player> players;
        players = Client.getInstance().getUserInterface().getPlayersList();
        for (Player player : players) {
            if (!player.getNickname().equals(Client.getInstance().getUserInterface().getLocalPlayerName())) {
                if (player1.getText().isEmpty()) {
                    player1.setText(player.getNickname());
                } else if (player2.getText().isEmpty() && !player1.getText().equals(player.getNickname())) {
                    player2.setText(player.getNickname());
                } else if (player3.getText().isEmpty() && !player1.getText().equals(player.getNickname()) && !player2.getText().equals(player.getNickname())){
                    player3.setText(player.getNickname());
                }
            }

        }
        if(Client.getInstance().getUserInterface().getGameStatus().equals(GameStatus.GAME_CREATION)){
            Parent nextPageParent = null;
            try {
                nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/StartingCard.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage window = (Stage) BackArrow.getScene().getWindow();
            window.setScene(new Scene(nextPageParent));
            window.show();
        }

    }

    @Override
    public void reportError(RuntimeException exception) throws RemoteException {

    }
}
