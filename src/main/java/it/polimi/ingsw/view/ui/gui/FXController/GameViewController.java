package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.events.messages.client.DrawCardMessage;
import it.polimi.ingsw.events.messages.client.PlaceCardMessage;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.utils.CardLocation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GameViewController implements GUIScene {

    public ImageView resourceDeckf1;
    public ImageView goldDeckf1;
    public ImageView resourceDeckf2;
    public ImageView goldDeckf2;
    public ImageView commongoal1;
    public ImageView commongoal2;
    public ImageView resourceDeckb;
    public ImageView goldDeckb;
    public Label turnwarning;
    public Button showScoreBoard;
    public ImageView p1color;
    public ImageView p2color;
    public ImageView p3color;
    public ImageView p4color;
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
    //hashmap to store the shapes that are placed on the board
    private final HashMap<CardLocation, ImageView> boardshapes = new HashMap<>();
    //hashmap to store the image of the cards that are placed on the board
    private final HashMap<CardLocation, ImageView> cardsimage = new HashMap<>();
    String previousplayer;

    @FXML
    public void initialize() {
        try {
            if (Client.getInstance().getView().getSelectedside() == 0) {
                startingcard.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getBoard().get(new CardLocation(0, 0)).card().getFrontpath())).toString()
                ));
            } else {
                startingcard.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getBoard().get(new CardLocation(0, 0)).card().getFrontpath())).toString()
                ));
            }
            privategoal.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getPrivateGoal().getPath())).toString()
            ));
            handcard1.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getPlayerCards()[0].getFrontpath())).toString()
            ));
            handcard2.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getPlayerCards()[1].getFrontpath())).toString()
            ));
            handcard3.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getPlayerCards()[2].getFrontpath())).toString()
            ));
            resourceDeckb.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getResourceCardsDeck().getTopOfTheStack().getPath())).toString()
            ));
            resourceDeckf1.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getVisibleCards()[0].getFrontpath())).toString()
            ));
            resourceDeckf2.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getVisibleCards()[1].getFrontpath())).toString()
            ));
            goldDeckf1.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getVisibleCards()[2].getFrontpath())).toString()
            ));
            goldDeckf2.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getVisibleCards()[3].getFrontpath())).toString()
            ));
            goldDeckb.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getGoldCardsDeck().getTopOfTheStack().getGoldenPath())).toString()
            ));
            commongoal1.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getCommonGoals()[0].getPath())).toString()
            ));
            commongoal2.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getCommonGoals()[1].getPath())).toString()
            ));
            setcolors();

        }
        catch (NullPointerException ignored) {}

        List<Player> players;
        players = Client.getInstance().getView().getPlayersList();
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
        cardsimage.put(new CardLocation(0, 0), startingcard);
        update();


    }

    private  void setcolors(){
        switch(Client.getInstance().getView().getGameModel().getPlayers().get(0).getColor()){
            case RED:
                p1color.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource("/image/RedCircle.png")).toString()
                ));
                break;
            case GREEN:
                p1color.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource("/image/GreenCircle.png")).toString()
                ));
                break;
            case YELLOW:
                p1color.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource("/image/YellowCircle.png")).toString()
                ));
                break;
            case BLUE:
                p1color.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource("/image/BlueCircle.png")).toString()
                ));
                break;
        }
        switch (Client.getInstance().getView().getGameModel().getPlayers().get(1).getColor()){
            case RED:
                p2color.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource("/image/RedCircle.png")).toString()
                ));
                break;
            case GREEN:
                p2color.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource("/image/GreenCircle.png")).toString()
                ));
                break;
            case YELLOW:
                p2color.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource("/image/YellowCircle.png")).toString()
                ));
                break;
            case BLUE:
                p2color.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource("/image/BlueCircle.png")).toString()
                ));
                break;
        }
        if (Client.getInstance().getView().getGameModel().getPlayers().size() > 2) {
            switch (Client.getInstance().getView().getGameModel().getPlayers().get(2).getColor()) {
                case RED:
                    p3color.setImage(new Image(
                            Objects.requireNonNull(getClass().getResource("/image/RedCircle.png")).toString()
                    ));
                    break;
                case GREEN:
                    p3color.setImage(new Image(
                            Objects.requireNonNull(getClass().getResource("/image/GreenCircle.png")).toString()
                    ));
                    break;
                case YELLOW:
                    p3color.setImage(new Image(
                            Objects.requireNonNull(getClass().getResource("/image/YellowCircle.png")).toString()
                    ));
                    break;
                case BLUE:
                    p3color.setImage(new Image(
                            Objects.requireNonNull(getClass().getResource("/image/BlueCircle.png")).toString()
                    ));
                    break;
            }
        }
        if (Client.getInstance().getView().getGameModel().getPlayers().size() > 3) {
            switch (Client.getInstance().getView().getGameModel().getPlayers().get(3).getColor()) {
                case RED:
                    p4color.setImage(new Image(
                            Objects.requireNonNull(getClass().getResource("/image/RedCircle.png")).toString()
                    ));
                    break;
                case GREEN:
                    p4color.setImage(new Image(
                            Objects.requireNonNull(getClass().getResource("/image/GreenCircle.png")).toString()
                    ));
                    break;
                case YELLOW:
                    p4color.setImage(new Image(
                            Objects.requireNonNull(getClass().getResource("/image/YellowCircle.png")).toString()
                    ));
                    break;
                case BLUE:
                    p4color.setImage(new Image(
                            Objects.requireNonNull(getClass().getResource("/image/BlueCircle.png")).toString()
                    ));
                    break;
            }
        }
    }
    @FXML
    public void SelectCard1(MouseEvent mouseEvent) {
        if (!Client.getInstance().getView().getGameModel().getTurn().equals(Client.getInstance().getView().getLocalPlayer())|| !Client.getInstance().getView().getTurnStatus().equals(TurnStatus.PLACE)){
            //TODO: send a message to the client 'you can't place a card now'
        }else{
            sel=0;
            selected=Client.getInstance().getView().getLocalPlayer().getPlayerCards()[0];
            handcard1.setOpacity(0.5);
            handcard2.setOpacity(1);
            handcard3.setOpacity(1);
        }
    }
    @FXML
    public void SelectCard2(MouseEvent mouseEvent) {
        if (!Client.getInstance().getView().getGameModel().getTurn().equals(Client.getInstance().getView().getLocalPlayer()) || !Client.getInstance().getView().getTurnStatus().equals(TurnStatus.PLACE)) {
            //TODO: send a message to the client 'you can't place a card now'
        }else{
            sel = 1;
            selected = Client.getInstance().getView().getLocalPlayer().getPlayerCards()[1];
            handcard2.setOpacity(0.5);
            handcard1.setOpacity(1);
            handcard3.setOpacity(1);
        }

    }
    @FXML
    public void SelectCard3(MouseEvent mouseEvent) {
        if (!Client.getInstance().getView().getGameModel().getTurn().equals(Client.getInstance().getView().getLocalPlayer()) || !Client.getInstance().getView().getTurnStatus().equals(TurnStatus.PLACE)) {
            //TODO: send a message to the client 'you can't place a card now'
        }else{
            sel = 2;
            selected = Client.getInstance().getView().getLocalPlayer().getPlayerCards()[2];
            handcard3.setOpacity(0.5);
            handcard2.setOpacity(1);
            handcard1.setOpacity(1);
        }
    }

    public void refreshScoreboard() {
        List<Player> players;
        players = Client.getInstance().getView().getPlayersList();
        s1.setText(Integer.toString(Client.getInstance().getView().getGameModel().getScoreBoard().getScore(players.get(0).nickName)));
        s2.setText(Integer.toString(Client.getInstance().getView().getGameModel().getScoreBoard().getScore(players.get(1).nickName)));
        if (players.size() > 2) {
            s3.setText(Integer.toString(Client.getInstance().getView().getGameModel().getScoreBoard().getScore(players.get(2).nickName)));
        }
        if (players.size() > 3) {
            s4.setText(Integer.toString(Client.getInstance().getView().getGameModel().getScoreBoard().getScore(players.get(3).nickName)));
        }
    }

    public void deactiveDeck(){
        resourceDeckb.setOnMouseClicked(null);
        goldDeckb.setOnMouseClicked(null);
        resourceDeckf1.setOnMouseClicked(null);
        resourceDeckf2.setOnMouseClicked(null);
        goldDeckf1.setOnMouseClicked(null);
        goldDeckf2.setOnMouseClicked(null);
        resourceDeckb.setOpacity(0.5);
        goldDeckb.setOpacity(0.5);
        resourceDeckf1.setOpacity(0.5);
        resourceDeckf2.setOpacity(0.5);
        goldDeckf1.setOpacity(0.5);
        goldDeckf2.setOpacity(0.5);
    }

    public void deactiveCard() {
        handcard1.setOnMouseClicked(null);
        handcard2.setOnMouseClicked(null);
        handcard3.setOnMouseClicked(null);
        handcard1.setOpacity(0.5);
        handcard2.setOpacity(0.5);
        handcard3.setOpacity(0.5);
    }

    public void addShape(CardLocation cl, int i, int j){
            ImageView shape = new ImageView();
            boardshapes.put(new CardLocation(cl.getX() + i, cl.getY() + j), shape);
            shape.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource("/image/cardshape.png")).toString()));
            shape.setFitHeight(124.0);
            shape.setFitWidth(171.0);
            double lx=0;
            double ly=0;
            if (cardsimage.get(cl)!=null) {
                lx = cardsimage.get(cl).getLayoutX();
                ly = cardsimage.get(cl).getLayoutY();
                shape.setLayoutX(lx + (i * 128));
                shape.setLayoutY(ly + (-j * 63));
                shape.setOpacity(0.5);
                shape.setVisible(true);
                TablePane.getChildren().add(shape);
                shape.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    try {
                        Client.getInstance().getServerHandler().sendMessage(new PlaceCardMessage(sel, false, new CardLocation(cl.getX() + i, cl.getY() + j)));
                        for (ImageView s : boardshapes.values()) {
                            s.setVisible(false);
                            s.setOnMouseClicked(null);
                        }
                        ImageView card = new ImageView();
                        cardsimage.put(new CardLocation(cl.getX() + i, cl.getY() + j), card);
                        card.setImage(new Image(
                                Objects.requireNonNull(getClass().getResource(selected.getFrontpath())).toString()));
                        card.setFitHeight(124.0);
                        card.setFitWidth(171.0);
                        card.setLayoutX(cardsimage.get(cl).getLayoutX() + (i * 128));
                        card.setLayoutY(cardsimage.get(cl).getLayoutY() + (-j * 63));
                        TablePane.getChildren().add(card);
                        card.setVisible(true);
                        handcard1.setOpacity(1);
                        handcard2.setOpacity(1);
                        handcard3.setOpacity(1);
                    } catch (RuntimeException e) {
                        reportError(e);
                    }
                });
            }else{
                System.out.println("non ho trovato la cardsimage in "+cl.getX()+" "+cl.getY());
            }
    }

    @Override
    public void update() {

        refreshScoreboard();

        resourceDeckb.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getResourceCardsDeck().getTopOfTheStack().getPath())).toString()
        ));
        resourceDeckf1.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getVisibleCards()[0].getFrontpath())).toString()
        ));
        resourceDeckf2.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getVisibleCards()[1].getFrontpath())).toString()
        ));
        goldDeckf1.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getVisibleCards()[2].getFrontpath())).toString()
        ));
        goldDeckf2.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getVisibleCards()[3].getFrontpath())).toString()
        ));
        goldDeckb.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getGameModel().getGoldCardsDeck().getTopOfTheStack().getGoldenPath())).toString()
        ));

        if((Client.getInstance().getView().getPlayersTurn().equals(Client.getInstance().getView().getLocalPlayer().getNickname()))) {
            if (Client.getInstance().getView().getTurnStatus().equals(TurnStatus.PLACE)) {
                turnwarning.setText(Client.getInstance().getView().getLocalPlayer().getNickname() + " it's your turn to place a card!");
                //cards again clickable
                this.handcard1.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    sel = 0;
                    selected = Client.getInstance().getView().getLocalPlayer().getPlayerCards()[0];
                    handcard1.setOpacity(0.5);
                    handcard2.setOpacity(1);
                    handcard3.setOpacity(1);
                });
                handcard2.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    sel = 1;
                    selected = Client.getInstance().getView().getLocalPlayer().getPlayerCards()[1];
                    handcard2.setOpacity(0.5);
                    handcard1.setOpacity(1);
                    handcard3.setOpacity(1);
                });
                handcard3.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    sel = 2;
                    selected = Client.getInstance().getView().getLocalPlayer().getPlayerCards()[2];
                    handcard3.setOpacity(0.5);
                    handcard2.setOpacity(1);
                    handcard1.setOpacity(1);
                });
                handcard1.setOpacity(1);
                handcard2.setOpacity(1);
                handcard3.setOpacity(1);

                deactiveDeck();

                //adds the shapes(slot where you can place cards) to the board
                for (CardLocation cardLocation : Client.getInstance().getView().getLocalPlayer().getBoard().keySet()) {
                    if (!Client.getInstance().getView().getLocalPlayer().getBoard().containsKey(new CardLocation(cardLocation.getX() + 1, cardLocation.getY() + 1))) {
                        if ((!boardshapes.containsKey(new CardLocation(cardLocation.getX() + 1, cardLocation.getY() + 1))) || (boardshapes.containsKey(new CardLocation(cardLocation.getX() + 1, cardLocation.getY() + 1))) && !boardshapes.get(new CardLocation(cardLocation.getX() + 1, cardLocation.getY() + 1)).isVisible()) {
                            addShape(cardLocation, 1, 1);
                        }
                    }
                    if (!Client.getInstance().getView().getLocalPlayer().getBoard().containsKey(new CardLocation(cardLocation.getX() + 1, cardLocation.getY() - 1))) {
                        if ((!boardshapes.containsKey(new CardLocation(cardLocation.getX() + 1, cardLocation.getY() - 1))) || (boardshapes.containsKey(new CardLocation(cardLocation.getX() + 1, cardLocation.getY() - 1))) && !boardshapes.get(new CardLocation(cardLocation.getX() + 1, cardLocation.getY() - 1)).isVisible()){
                            addShape(cardLocation, 1, -1);
                        }
                    }
                    if (!Client.getInstance().getView().getLocalPlayer().getBoard().containsKey(new CardLocation(cardLocation.getX() - 1, cardLocation.getY() + 1))) {
                        if ((!boardshapes.containsKey(new CardLocation(cardLocation.getX() - 1, cardLocation.getY() + 1))) || (boardshapes.containsKey(new CardLocation(cardLocation.getX() - 1, cardLocation.getY() + 1))) && !boardshapes.get(new CardLocation(cardLocation.getX() - 1, cardLocation.getY() + 1)).isVisible()) {
                            addShape(cardLocation, -1, 1);
                        }
                    }
                    if (!Client.getInstance().getView().getLocalPlayer().getBoard().containsKey(new CardLocation(cardLocation.getX() - 1, cardLocation.getY() - 1))) {
                        if ((!boardshapes.containsKey(new CardLocation(cardLocation.getX() - 1, cardLocation.getY() - 1))) || (boardshapes.containsKey(new CardLocation(cardLocation.getX() - 1, cardLocation.getY() - 1))) && !boardshapes.get(new CardLocation(cardLocation.getX() - 1, cardLocation.getY() - 1)).isVisible()){
                            addShape(cardLocation, -1, -1);
                        }
                    }
                }

            } else if (Client.getInstance().getView().getTurnStatus().equals(TurnStatus.DRAW)) {
                resourceDeckb.setOpacity(1);
                goldDeckb.setOpacity(1);
                resourceDeckf1.setOpacity(1);
                resourceDeckf2.setOpacity(1);
                goldDeckf1.setOpacity(1);
                goldDeckf2.setOpacity(1);
                previousplayer = Client.getInstance().getView().getPlayersTurn();
                turnwarning.setText(Client.getInstance().getView().getLocalPlayer().getNickname() + " it's your turn to pick up a card!");
                for(ImageView s : boardshapes.values()){
                    s.setVisible(false);
                    s.setOnMouseClicked(null);
                }
                resourceDeckb.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(0));
                });
                goldDeckb.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(1));
                });
                resourceDeckf1.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(2));
                });
                resourceDeckf2.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(3));
                });
                goldDeckf1.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(4));
                });
                goldDeckf2.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(5));
                });
            }
        }else {
            deactiveDeck();

            turnwarning.setText(Client.getInstance().getView().getPlayersTurn() + "'s turn!");
            if(Client.getInstance().getView().getLocalPlayerName().equals(previousplayer)){
                switch (sel){
                    case 0:
                        handcard1.setImage(new Image(
                                Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getPlayerCards()[sel].getFrontpath())).toString())
                        );
                        break;
                    case 1:
                        handcard2.setImage(new Image(
                                Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getPlayerCards()[sel].getFrontpath())).toString())
                        );
                        break;
                    case 2:
                        handcard3.setImage(new Image(
                                Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getPlayerCards()[sel].getFrontpath())).toString())
                        );
                        break;
                }

            }
        }

    }


    @Override
    public void reportError(RuntimeException exception)  {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
        });

    }


    public void showScoreboard(ActionEvent event) {
        Client.getInstance().getView().getUserInterface().setScoreScene();
    }
}
