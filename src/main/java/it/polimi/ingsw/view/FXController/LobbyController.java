package it.polimi.ingsw.view.FXController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LobbyController {

    @FXML
    private Button CreateGameButton;

    @FXML
    private Button JoinGameButton;

    @FXML
    private ImageView codexLogo;

    @FXML
    private ImageView codexWallpaper;

    private Stage window;
    private Parent nextPageParent;

    //CreateGamefx allows the user to change Pane to CreateGame.fxml
    @FXML
    void CreateGamefx(ActionEvent event) throws IOException {
        nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/CreateGame.fxml")));
        window = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    //JoinGamefx allows the user to change Pane to JoinGame.fxml
    @FXML
    void JoinGamefx(ActionEvent event) throws IOException {
        nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/JoinGame.fxml")));
        window = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

}
