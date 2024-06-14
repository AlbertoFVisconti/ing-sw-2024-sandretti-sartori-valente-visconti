package it.polimi.ingsw.view.ui.gui.FXController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RulesPage3Controller extends GUIScene {
    @FXML
    void goBackToLobby(MouseEvent event) throws IOException {
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Lobby.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    @FXML
    void nextPage(ActionEvent event) throws IOException {
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/RulesPage4.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    @FXML
    void previousPage(ActionEvent event) throws IOException {
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/RulesPage2.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    @Override
    public void update() {

    }
    @Override
    protected AnchorPane getChatContainer() {
        return null;
    }
}
