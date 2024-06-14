package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.network.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LobbyController extends GUIScene{
    private Stage window;
    private Parent nextPageParent;

    //CreateGamefx allows the user to change Pane to CreateGame.fxml
    @FXML
    void createGamefx(ActionEvent event) throws IOException {
        Client.getInstance().getView().getUserInterface().setCreateGameScene();
    }

    //JoinGamefx allows the user to change Pane to JoinGame.fxml
    @FXML
    void joinGame(ActionEvent event) throws IOException {
        Client.getInstance().getView().getUserInterface().setJoinGameScene();
    }

    //GoToRules allows the user to change Pane to RulesPage1.fxml
    @FXML
    void goToRules(ActionEvent event) throws IOException {
        System.err.println("TODO" /* TODO */);
        nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/RulesPage1.fxml")));
        window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    @FXML
    public void initialize() {

    }

    @Override
    public void update() {

    }
    @Override
    protected AnchorPane getChatContainer() {
        return null;
    }
}
