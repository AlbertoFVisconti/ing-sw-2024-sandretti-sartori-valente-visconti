package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.PlaceStartCardMessage;
import it.polimi.ingsw.network.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class StartingCardController {

    public ImageView startingcardback;
    public ImageView startingcardfront;
    @FXML
    private AnchorPane TablePane;
    @FXML
    public void initialize()  {
        System.out.println(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getFrontpath());
        System.out.println(Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getFrontpath())).toString());

        startingcardfront.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getFrontpath())).toString()
        ));
        startingcardback.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getBackpath())).toString()
        ));
    }
    @FXML
    void SelectFront(MouseEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                Client.getInstance().getUserInterface().setSelectedside(0);
                Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(false));
                Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/PrivateGoal.fxml")));
                Stage window = new Stage();
                window.setScene(new Scene(nextPageParent));
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    void SelectBack(MouseEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                Client.getInstance().getUserInterface().setSelectedside(1);
                Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(true));
                Parent nextPageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/PrivateGoal.fxml")));
                Stage window = new Stage();
                window.setScene(new Scene(nextPageParent));
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
