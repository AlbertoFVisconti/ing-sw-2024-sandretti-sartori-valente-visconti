package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class WaitingForPlayersController implements GUIScene {

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
        List<Player> players;
        players = Client.getInstance().getView().getPlayersList();
        for (Player player : players) {
            if (!player.getNickname().equals(Client.getInstance().getView().getLocalPlayerName())) {
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
    public void setup() {
    }

    @Override
    public void update() {
        Platform.runLater(
                () -> {
                    player1.setText("");
                    player2.setText("");
                    player3.setText("");

                    List<Player> players;
                    players = Client.getInstance().getView().getPlayersList();
                    for (Player player : players) {
                        if (!player.getNickname().equals(Client.getInstance().getView().getLocalPlayerName())) {
                            if (player1.getText().isEmpty()) {
                                player1.setText(player.getNickname());
                            } else if (player2.getText().isEmpty() && !player1.getText().equals(player.getNickname())) {
                                player2.setText(player.getNickname());
                            } else if (player3.getText().isEmpty() && !player1.getText().equals(player.getNickname()) && !player2.getText().equals(player.getNickname())){
                                player3.setText(player.getNickname());
                            }
                        }

                    }

                }
        );
    }

    @Override
    public void reportError(RuntimeException exception) {

    }
}
