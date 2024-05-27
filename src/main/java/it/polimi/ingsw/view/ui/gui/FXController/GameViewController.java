package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.PlaceCardMessage;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.view.ui.gui.FXGraphicalUserInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

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
    private int sel;
    private Card selected;

    @FXML
    public void initialize() throws FileNotFoundException {
        FXGraphicalUserInterface.currentInterface = this;
        System.out.println(Client.getInstance().getUserInterface().getLocalPlayer().getStartCard());
        if (Client.getInstance().getUserInterface().getSelectedside() == 0) {
            startingcard.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getBoard().get(new CardLocation(0, 0)).getCard().getFrontpath())).toString()
            ));
        } else {
            startingcard.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getBoard().get(new CardLocation(0, 0)).getCard().getFrontpath())).toString()
            ));
        }
        privategoal.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getPrivateGoal().getPath())).toString()
        ));
        handcard1.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[0].getFrontpath())).toString()
        ));
        handcard2.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[1].getFrontpath())).toString()
        ));
        handcard3.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[2].getFrontpath())).toString()
        ));
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

        ImageView shape1 = new ImageView(new Image(new FileInputStream("src/main/resources/image/cardshape.png")));
        shape1.setFitHeight(124.0);
        shape1.setFitWidth(171.0);
        shape1.setLayoutX(startingcard.getLayoutX()+128);
        shape1.setLayoutY(startingcard.getLayoutY()-63);
        shape1.setOpacity(0.5);
        shape1.setVisible(true);
        TablePane.getChildren().add(shape1);
        shape1.setOnMouseClicked((MouseEvent mouseEvent) -> {
            shape1.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(selected.getFrontpath())).toString()));
            Client.getInstance().getServerHandler().sendMessage(new PlaceCardMessage(0, false, new CardLocation(1, 1)));
            shape1.setOpacity(1);
        });
        ImageView shape2 = new ImageView(new Image(new FileInputStream("src/main/resources/image/cardshape.png")));
        shape2.setFitHeight(124.0);
        shape2.setFitWidth(171.0);
        shape2.setLayoutX(startingcard.getLayoutX()+128);
        shape2.setLayoutY(startingcard.getLayoutY()+63);
        shape2.setOpacity(0.5);
        shape2.setVisible(true);
        TablePane.getChildren().add(shape2);
        shape2.setOnMouseClicked((MouseEvent mouseEvent) -> {
            shape2.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(selected.getFrontpath())).toString()));
            Client.getInstance().getServerHandler().sendMessage(new PlaceCardMessage(0, false, new CardLocation(1, -1)));
            shape2.setOpacity(1);
        });
        ImageView shape3 = new ImageView(new Image(new FileInputStream("src/main/resources/image/cardshape.png")));
        shape3.setFitHeight(124.0);
        shape3.setFitWidth(171.0);
        shape3.setLayoutX(startingcard.getLayoutX()-128);
        shape3.setLayoutY(startingcard.getLayoutY()-63);
        shape3.setOpacity(0.5);
        shape3.setVisible(true);
        TablePane.getChildren().add(shape3);
        shape3.setOnMouseClicked((MouseEvent mouseEvent) -> {
            shape3.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(selected.getFrontpath())).toString()));
            Client.getInstance().getServerHandler().sendMessage(new PlaceCardMessage(0, false, new CardLocation(-1, 1)));
            shape3.setOpacity(1);
        });
        ImageView shape4 = new ImageView(new Image(new FileInputStream("src/main/resources/image/cardshape.png")));
        shape4.setFitHeight(124.0);
        shape4.setFitWidth(171.0);
        shape4.setLayoutX(startingcard.getLayoutX()-128);
        shape4.setLayoutY(startingcard.getLayoutY()+63);
        shape4.setOpacity(0.5);
        shape4.setVisible(true);
        TablePane.getChildren().add(shape4);
        shape4.setOnMouseClicked((MouseEvent mouseEvent) -> {
            shape4.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(selected.getFrontpath())).toString()));
            Client.getInstance().getServerHandler().sendMessage(new PlaceCardMessage(0, false, new CardLocation(-1, -1)));
            shape4.setOpacity(1);
        });
    }
    @FXML
    public void SelectCard1(MouseEvent mouseEvent) {
        sel=1;
        selected=Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[0];
        handcard1.setOpacity(0.5);
        handcard2.setOpacity(1);
        handcard3.setOpacity(1);


    }
    @FXML
    public void SelectCard2(MouseEvent mouseEvent) {
        sel=2;
        selected=Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[1];
        handcard2.setOpacity(0.5);
        handcard1.setOpacity(1);
        handcard3.setOpacity(1);
        handcard2.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[1].getFrontpath())).toString()
        ));

    }
    @FXML
    public void SelectCard3(MouseEvent mouseEvent) {
        sel=3;
        selected=Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[2];
        handcard3.setOpacity(0.5);
        handcard2.setOpacity(1);
        handcard1.setOpacity(1);
        handcard3.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[2].getFrontpath())).toString()
        ));

    }
    @Override
    public void update() {
        Platform.runLater(() -> {
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
            if(selected==Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[0]){
                handcard1.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[0].getFrontpath())).toString()
                ));
            }
//            switch (sel){
//                case 1:
//                    handcard1.setImage(new Image(
//                            Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[0].getFrontpath())).toString()
//                    ));
//                    break;
//                case 2:
//                    handcard2.setImage(new Image(
//                            Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[1].getFrontpath())).toString()
//                    ));
//                    break;
//                case 3:
//                    handcard3.setImage(new Image(
//                            Objects.requireNonNull(getClass().getResource(Client.getInstance().getUserInterface().getLocalPlayer().getPlayerCards()[2].getFrontpath())).toString()
//                    ));
//                    break;
//            }
            //TODO: update the cards slot on the table
        });

    }


    @Override
    public void reportError(RuntimeException exception) throws RemoteException {

    }


}

