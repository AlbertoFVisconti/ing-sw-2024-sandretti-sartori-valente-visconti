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

import java.util.List;

/**
 * WaitingForPlayersController handles the interface that the GUI user is
 * presented with upon joining/creating a game.
 * It allows the user to select/switch color, to see the connected players
 * and to chat with them.
 */
public class WaitingForPlayersController extends GUIScene {
    // ImageView that represents the button that the user can press to select BLUE as their color
    public ImageView blue;

    // ImageView that represents the button that the user can press to select RED as their color
    public ImageView red;

    // ImageView that represents the button that the user can press to select YELLOW as their color
    public ImageView yellow;

    // ImageView that represents the button that the user can press to select GREEN as their color
    public ImageView green;

    // AnchorPane where the chat can be put
    public AnchorPane chatContainer;

    // Label that contains the name of the first player in the connected players list
    @FXML
    private Label player1;

    // Label that contains the name of the second player in the connected players list (if there's one)
    @FXML
    private Label player2;

    // Label that contains the name of the third player in the connected players list (if there's one)
    @FXML
    private Label player3;

    // Label that contains the name of the forth player in the connected players list (if there's one)
    @FXML
    private Label player4;

    // array that contains all the labels regarding connected players
    private Label[] playerLabels;

    /**
     * When the user interface is loaded, it sets up the controller helper objects.
     * It launches the first update that updates the available colors and the connected players.
     */
    @FXML
    public void initialize() {
        playerLabels = new Label[]{player1, player2, player3, player4};

        this.update();
    }

    /**
     * Triggered when the user clicks on the arrows that allows to go back to the main scene.
     * It also sends a message to the server to communicate that the player wants to leave the game lobby
     */
    @FXML
    void goBackToLobby() {
        Client.getInstance().getServerHandler().sendMessage(new LeaveGameMessage());
        Client.getInstance().getView().getUserInterface().setMainScene();
    }

    /**
     * Allows the GUI manager to retrieve the AnchorPane where
     * the chat can be put.
     * Since this interface supports chat, an AnchorPane is provided.
     *
     * @return AnchorPane where the chat can be put
     */
    @Override
    protected AnchorPane getChatContainer() {
        return this.chatContainer;
    }

    /**
     * Allows the view to request the interface to update the displayed content.
     */
    @Override
    public void update() {
        // retrieving the list of connected players from the model
        List<Player> players;
        players = Client.getInstance().getView().getPlayersList();

        // setting up the labels
        int labelIndex = 0;
        for (Player player : players) {
            if (Client.getInstance().getView().getLocalPlayer() != null && Client.getInstance().getView().getLocalPlayer().nickname.equals(player.nickname)) {
                // this player's the local player
                playerLabels[labelIndex].setText(player.nickname + " (you)");
            }
            else {
                // this player isn't the local player
                playerLabels[labelIndex].setText(player.nickname);
            }

            if(player.getColor() != null) {
                // the player has selected a color
                playerLabels[labelIndex].setTextFill(PlayerColor.playerColorToColor(player.getColor()));
            }

            labelIndex++;
        }

        // clearing the unnecessary labels
        for(int i = players.size(); i < playerLabels.length; i++) {
            playerLabels[i].setText("");
            playerLabels[i].setTextFill(Color.color(0,0,0));
        }

        // updating the RED color button
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.RED)) {
            red.setOpacity(0.1);
        }
        else red.setOpacity(1);

        // updating the BLUE color button
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.BLUE)) {
            blue.setOpacity(0.1);
        }
        else blue.setOpacity(1);

        // updating the GREEN color button
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.GREEN)) {
            green.setOpacity(0.1);

        }
        else green.setOpacity(1);

        // updating the YELLOW colors button
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.YELLOW)) {
            yellow.setOpacity(0.1);
        }
        else yellow.setOpacity(1);
    }

    /**
     * Triggered when the user clicks on the RED button in order to select their color.
     * If the colors is available it sends a message to the server.
     */
    public void selectRed() {
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.RED)) {
            return;
        }
        red.setOpacity(0.5);

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.RED));
    }

    /**
     * Triggered when the user clicks on the BLUE button in order to select their color.
     * If the colors is available it sends a message to the server.
     */
    public void selectBlue() {
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.BLUE)) {
            return;
        }
        blue.setOpacity(0.5);

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.BLUE));
    }

    /**
     * Triggered when the user clicks on the YELLOW button in order to select their color.
     * If the colors is available it sends a message to the server.
     */
    public void selectYellow() {
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.YELLOW)) {
            return;
        }
        yellow.setOpacity(0.5);

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.YELLOW));
    }

    /**
     * Triggered when the user clicks on the GREEN button in order to select their color.
     * If the colors is available it sends a message to the server.
     */
    public void selectGreen() {
        if (!Client.getInstance().getView().getAvailableColors().contains(PlayerColor.GREEN)) {
            return;
        }
        green.setOpacity(0.5);

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(PlayerColor.GREEN));
    }
}
