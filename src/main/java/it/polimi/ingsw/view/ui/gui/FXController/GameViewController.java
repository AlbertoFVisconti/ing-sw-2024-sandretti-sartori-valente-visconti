package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.view.ui.gui.FXGraphicalUserInterface;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class GameViewController extends UserInterface {

    @FXML
    private AnchorPane TablePane;

    @FXML
    private ImageView handcard1;

    @FXML
    private ImageView handcard2;

    @FXML
    private ImageView handcard3;

    @FXML
    private ImageView privategoal;

    @FXML
    private ImageView startingcard;
    @FXML
    private Label p1;

    @FXML
    private Label p2;

    @FXML
    private Label p3;

    @FXML
    private Label p4;
    @FXML
    private Label s1;

    @FXML
    private Label s2;

    @FXML
    private Label s3;

    @FXML
    private Label s4;

    @FXML
    public void initialize() {
        FXGraphicalUserInterface.currentInterface = this;
        if (Client.getInstance().getUserInterface().getSelectedside() == 0) {
            startingcard = new ImageView(new Image(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getFrontpath()));
        } else {
            startingcard = new ImageView(new Image(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard().getBackpath()));
        }
        privategoal = new ImageView(new Image(Client.getInstance().getUserInterface().getLocalPlayer().getPrivateGoal().getPath()));
        handcard1 = new ImageView(new Image(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[0].getFrontpath()));
        handcard2 = new ImageView(new Image(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[1].getFrontpath()));
        handcard3 = new ImageView(new Image(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[2].getFrontpath()));
        List<Player> players;
        players = Client.getInstance().getUserInterface().getPlayersList();
        if (players.size() == 2) {
            p3.setVisible(false);
            p4.setVisible(false);
            s3.setVisible(false);
            s4.setVisible(false);
        } else if (players.size() == 3) {
            p4.setVisible(false);
            s4.setVisible(false);
        }
        p1.setText(players.get(0).getNickname());
        p2.setText(players.get(1).getNickname());
        if (players.size() > 2) {
            p3.setText(players.get(2).getNickname());
        }
        if (players.size() > 3) {
            p4.setText(players.get(3).getNickname());
        }
    }
    @Override
    public void update() {
        List<Player> players;
        players = Client.getInstance().getUserInterface().getPlayersList();
        s1.setText(Integer.toString(Client.getInstance().getUserInterface().getGameModel().getScoreBoard().getScore(players.get(0).nickName)));
        s2.setText(Integer.toString(Client.getInstance().getUserInterface().getGameModel().getScoreBoard().getScore(players.get(1).nickName)));
        if (players.size() > 2) {
            s3.setText(Integer.toString(Client.getInstance().getUserInterface().getGameModel().getScoreBoard().getScore(players.get(2).nickName)));
        }
        if (players.size() > 3) {
            s4.setText(Integer.toString(Client.getInstance().getUserInterface().getGameModel().getScoreBoard().getScore(players.get(3).nickName)));
        }
    }


    @Override
    public void reportError(RuntimeException exception) throws RemoteException {

    }
}

