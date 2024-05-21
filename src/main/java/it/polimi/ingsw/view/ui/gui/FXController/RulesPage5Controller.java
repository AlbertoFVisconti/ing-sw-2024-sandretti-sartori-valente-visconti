package it.polimi.ingsw.view.ui.gui.FXController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RulesPage5Controller {
    @FXML
    private Button GoBackButton;
    private ImageView RulesPage1;
    @FXML
    void GoBackToLobby(MouseEvent event) throws IOException {
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Lobby.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }
    @FXML
    void PreviousPage(ActionEvent event) throws IOException {
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/RulesPage4.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }
}
