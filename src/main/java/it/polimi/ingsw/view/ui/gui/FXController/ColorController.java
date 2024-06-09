package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.SelectColorMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ColorController implements GUIScene {

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


    public void SelectRed(MouseEvent mouseEvent) throws IOException {
        red.setOpacity(0.5);
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.RED)) {
            return;
        }

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.RED));
    }

    public void SelectBlue(MouseEvent mouseEvent) throws IOException {
        red.setOpacity(0.5);
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.BLUE)) {
            return;
        }

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.BLUE));
    }

    public void SelectYellow(MouseEvent mouseEvent) throws IOException {
        red.setOpacity(0.5);
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.YELLOW)) {
            return;
        }

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.YELLOW));
    }

    public void SelectGreen(MouseEvent mouseEvent) throws IOException {
        red.setOpacity(0.5);
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.GREEN)) {
            return;
        }

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.GREEN));
    }

    @Override
    public void setup() {
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.RED)) {
            red.setOpacity(0.1);
        }
        else red.setOpacity(1);


        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.BLUE)) {
            blue.setOpacity(0.1);
        }
        else blue.setOpacity(1);

        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.GREEN)) {
            green.setOpacity(0.1);

        }
        else green.setOpacity(1);

        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.YELLOW)) {
            yellow.setOpacity(0.1);
        }
        else yellow.setOpacity(1);
    }

    @Override
    public void update() {
        this.setup();
    }

    @Override
    public void reportError(RuntimeException exception) {

    }
}


