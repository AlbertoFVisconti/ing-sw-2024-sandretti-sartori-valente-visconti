package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.SelectColorMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ColorController {

    @FXML
    private ImageView blue;

    @FXML
    private ImageView codexWallpaper;

    @FXML
    private ImageView green;

    @FXML
    private ImageView red;

    @FXML
    private ImageView yellow;

    public void initialize() {
        if (!Client.getInstance().getUserInterface().getAvailableColors().contains(PlayerColor.RED)) {
            red.setOpacity(0.1);
            red.setOnMouseClicked(null);
        } else if (!Client.getInstance().getUserInterface().getAvailableColors().contains(PlayerColor.BLUE)) {
            blue.setOpacity(0.1);
            blue.setOnMouseClicked(null);
        } else if (!Client.getInstance().getUserInterface().getAvailableColors().contains(PlayerColor.GREEN)) {
            green.setOpacity(0.1);
            green.setOnMouseClicked(null);
        } else if (!Client.getInstance().getUserInterface().getAvailableColors().contains(PlayerColor.YELLOW)) {
            yellow.setOpacity(0.1);
            yellow.setOnMouseClicked(null);
        }
    }


    public void SelectRed(MouseEvent mouseEvent) throws IOException {
        red.setOpacity(0.5);
        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.RED));
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/GameView.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    public void SelectBlue(MouseEvent mouseEvent) throws IOException {
        blue.setOpacity(0.5);
        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.BLUE));
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/GameView.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    public void SelectYellow(MouseEvent mouseEvent) throws IOException {
        yellow.setOpacity(0.5);
        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.YELLOW));
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/GameView.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }

    public void SelectGreen(MouseEvent mouseEvent) throws IOException {
        green.setOpacity(0.5);
        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.GREEN));
        Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/GameView.fxml")));
        Stage window = (Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(new Scene(nextPageParent));
        window.show();
    }
}


