package it.polimi.ingsw.view.FXController;
import it.polimi.ingsw.events.messages.client.GameListRequestMessage;
import it.polimi.ingsw.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.UserInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Objects;

public class JoinGameController extends UserInterface{

    public ChoiceBox<Integer> selectGame= new ChoiceBox<>();
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
    public void JoinGameX(ActionEvent actionEvent) {
        Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(selectGame.getValue(), false, 0, nicknameField.getText()));
    }
    @FXML
    public void initialize() {
        UserInterface gui= Client.getInstance().getUserInterface();
        Client.getInstance().getServerHandler().sendMessage(new GameListRequestMessage());
        try {
            // TODO: find a better way (UserInterface.update())
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        HashSet<Integer> availableGames= gui.getAvailableGames();
        if(availableGames!=null){
            for (Integer gameID : availableGames) {
                selectGame.getItems().add(gameID);
            }
        }else{
            selectGame.setVisible(false);
            joinButton.setVisible(false);
            insertNickname.setVisible(false);
            nicknameField.setVisible(false);
            noGamesAvailable.setVisible(true);

        }
    }
    public void RefreshGame(ActionEvent actionEvent) {
        selectGame.getItems().removeAll();
        this.initialize();
    }

    @Override
    public void reportError(RuntimeException exception) throws RemoteException {

    }

    @Override
    protected void update() {

    }


}
