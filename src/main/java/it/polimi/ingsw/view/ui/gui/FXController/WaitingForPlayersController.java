package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.LeaveGameMessage;
import it.polimi.ingsw.events.messages.client.SelectColorMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;

public class WaitingForPlayersController extends GUIScene {
    public ImageView blue;
    public ImageView red;
    public ImageView yellow;
    public ImageView green;
    public AnchorPane chatContainer;

    @FXML
    private Label player1;

    @FXML
    private Label player2;

    @FXML
    private Label player3;

    @FXML
    private Label player4;

    private Label[] playerLabels;

    @FXML
    public void initialize() {
        playerLabels = new Label[]{player1, player2, player3, player4};

        this.update();
    }

    @FXML
    void goBackToLobby(MouseEvent event) {
        Client.getInstance().getServerHandler().sendMessage(new LeaveGameMessage());
        Client.getInstance().getView().getUserInterface().setStartingScene();
    }

    @Override
    protected AnchorPane getChatContainer() {
        return this.chatContainer;
    }

    @Override
    public void update() {
        List<Player> players;
        players = Client.getInstance().getView().getPlayersList();

        int labelIndex = 0;
        for (Player player : players) {
            if (Client.getInstance().getView().getLocalPlayer() != null && Client.getInstance().getView().getLocalPlayer().nickname.equals(player.nickname)) {
                playerLabels[labelIndex].setText(player.nickname + " (you)");
            }
            else {
                playerLabels[labelIndex].setText(player.nickname);
            }

            if(player.getColor() != null) {
                playerLabels[labelIndex].setTextFill(PlayerColor.playerColorToColor(player.getColor()));
            }

            labelIndex++;
        }
        for(int i = players.size(); i < playerLabels.length; i++) {
            playerLabels[i].setText("");
            playerLabels[i].setTextFill(Color.color(0,0,0));
        }


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

    public void selectRed(MouseEvent mouseEvent) {
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.RED)) {
            return;
        }
        red.setOpacity(0.5);

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.RED));
    }

    public void selectBlue(MouseEvent mouseEvent) {
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.BLUE)) {
            return;
        }
        blue.setOpacity(0.5);

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.BLUE));
    }

    public void selectYellow(MouseEvent mouseEvent) {
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.YELLOW)) {
            return;
        }
        yellow.setOpacity(0.5);

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.YELLOW));
    }

    public void selectGreen(MouseEvent mouseEvent) {
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.GREEN)) {
            return;
        }
        green.setOpacity(0.5);

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.GREEN));
    }
}
