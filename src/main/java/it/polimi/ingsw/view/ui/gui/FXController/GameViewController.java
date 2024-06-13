package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.events.messages.client.DrawCardMessage;
import it.polimi.ingsw.events.messages.client.PlaceCardMessage;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.ui.gui.MediaManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

import java.util.*;

public class GameViewController extends GUIScene {
    public static final int[] POSS = {-1, 1};
    private final static double CARD_HEIGHT = 114;
    private final static double CARD_WIDTH = 171;

    private final static double CARD_CORNER_HEIGHT = (265.0/662.0) * CARD_HEIGHT;
    private final static double CARD_CORNER_WIDTH = (219.0/993.0) * CARD_WIDTH;


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
    public AnchorPane chatContainer;
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

    @FXML
    public void initialize() {
        TablePane.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/fxml/Style.css")).toExternalForm()));

        try {
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

    private void addCardSlot(CardSlot cardSlot,CardLocation cl){
        ImageView cardImage = new ImageView(MediaManager.getInstance().getImage(cardSlot));
        cardImage.setFitHeight(CARD_HEIGHT);
        cardImage.setFitWidth(CARD_WIDTH);

        cardImage.setLayoutX(startingcard.getLayoutX() + (cl.getX() * (CARD_WIDTH - CARD_CORNER_WIDTH)));
        cardImage.setLayoutY(startingcard.getLayoutY() + (-cl.getY() * (CARD_HEIGHT - CARD_CORNER_HEIGHT)));

        cardsimage.put(cl, cardImage);

        TablePane.getChildren().add(cardImage);
        cardImage.setVisible(true);
    }

    private boolean placeable(CardLocation cl ){
        Map<CardLocation, CardSlot> board = Client.getInstance().getView().getLocalPlayer().getBoard();

        if(board.get(cl) != null) return false;

        if(board.containsKey(cl.bottomLeftNeighbour())
                && board.get(cl.bottomLeftNeighbour()).getTopRightCorner()==null)
            return false;
        if(board.containsKey(cl.topLeftNeighbour())
                && board.get(cl.topLeftNeighbour()).getBottomRightCorner()==null)
            return false;
        if(board.containsKey(cl.bottomRightNeighbour())
                && board.get(cl.bottomRightNeighbour()).getTopLeftCorner()==null )
            return false;
        return !board.containsKey(cl.topRightNeighbour())
                || board.get(cl.topRightNeighbour()).getBottomLeftCorner() != null;
    }
    public void addPlaceHolder(CardLocation cl, Set<CardLocation> seen){
        if(cardsimage.containsKey(cl)){
            for(int i: POSS){
                for(int j: POSS){
                    CardLocation p = new CardLocation(cl.getX() + i, cl.getY() + j);
                    if (!seen.contains(p)) {
                        seen.add(p);
                        if(placeable(p)) addShape(p);
                        addPlaceHolder(p, seen);
                    }
                }
            }
        }
    }

    private void placeMessage(CardLocation cardLocation) {
        try {
            Client.getInstance().getServerHandler().sendMessage(new PlaceCardMessage(sel, false, new CardLocation(cardLocation.getX() , cardLocation.getY() )));
            for (ImageView s : boardshapes.values()) {
                s.setVisible(false);
                s.setOnMouseClicked(null);
            }
            handcard1.setOpacity(1);
            handcard2.setOpacity(1);
            handcard3.setOpacity(1);
        } catch (RuntimeException e) {
            reportError(e);
        }
    }

    public void addShape(CardLocation cl){
        if(boardshapes.containsKey(cl)){
            boardshapes.get(cl).setVisible(true);
            boardshapes.get(cl).setOnMouseClicked((MouseEvent mouseEvent) -> placeMessage(cl));
            return;
        }


        ImageView shape = new ImageView();
        boardshapes.put(cl, shape);
        shape.setImage(MediaManager.getInstance().getImage("/image/cardshape.png"));
        shape.setFitHeight(CARD_HEIGHT);
        shape.setFitWidth(CARD_WIDTH);
        shape.getStyleClass().add("cardshape");

        shape.setLayoutX(startingcard.getLayoutX() + (cl.getX() * (CARD_WIDTH-CARD_CORNER_WIDTH)));
        shape.setLayoutY(startingcard.getLayoutY() + (-cl.getY() * (CARD_HEIGHT-CARD_CORNER_HEIGHT)));

        shape.setVisible(true);
        TablePane.getChildren().add(shape);
        shape.setOnMouseClicked((MouseEvent mouseEvent) -> placeMessage(cl));
    }

    @Override
    public void update() {
        refreshScoreboard();

        ArrayList<CardLocation> newCardsOnBoard = new ArrayList<>();

        Map<CardLocation,CardSlot> playerBoard = Client.getInstance().getView().getLocalPlayer().getBoard();
        for(CardLocation cardLocation : playerBoard.keySet()) {
            if(!cardsimage.containsKey(cardLocation)) {
                newCardsOnBoard.add(cardLocation);
            }
        }

        newCardsOnBoard.sort(Comparator.comparingInt((CardLocation cl) -> playerBoard.get(cl).placementTurn()));

        for(CardLocation cardLocation: newCardsOnBoard) {
            this.addCardSlot(playerBoard.get(cardLocation),cardLocation);
        }


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


                addPlaceHolder(new CardLocation(0,0), new HashSet<>());

            } else if (Client.getInstance().getView().getTurnStatus().equals(TurnStatus.DRAW)) {
                resourceDeckb.setOpacity(1);
                goldDeckb.setOpacity(1);
                resourceDeckf1.setOpacity(1);
                resourceDeckf2.setOpacity(1);
                goldDeckf1.setOpacity(1);
                goldDeckf2.setOpacity(1);

                turnwarning.setText(Client.getInstance().getView().getLocalPlayer().getNickname() + " it's your turn to pick up a card!");
                for(ImageView s : boardshapes.values()){
                    s.setVisible(false);
                    s.setOnMouseClicked(null);
                }
                resourceDeckb.setOnMouseClicked((MouseEvent mouseEvent) -> Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(0)));
                goldDeckb.setOnMouseClicked((MouseEvent mouseEvent) -> Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(1)));
                resourceDeckf1.setOnMouseClicked((MouseEvent mouseEvent) -> Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(2)));
                resourceDeckf2.setOnMouseClicked((MouseEvent mouseEvent) -> Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(3)));
                goldDeckf1.setOnMouseClicked((MouseEvent mouseEvent) -> Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(4)));
                goldDeckf2.setOnMouseClicked((MouseEvent mouseEvent) -> Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(5)));
            }
        }
        else {
            deactiveDeck();

            turnwarning.setText(Client.getInstance().getView().getPlayersTurn() + "'s turn!");
        }

        PlayCard[] playerCards =  Client.getInstance().getView().getLocalPlayer().getPlayerCards();

        if(playerCards[0] != null) {
            handcard1.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(playerCards[0].getFrontpath())).toString())
            );
        }
        else {
            handcard1.setImage(null);
        }

        if(playerCards[1] != null) {
            handcard2.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(playerCards[1].getFrontpath())).toString())
            );
        }
        else {
            handcard2.setImage(null);
        }

        if(playerCards[2] != null) {
            handcard3.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(playerCards[2].getFrontpath())).toString())
            );
        }
        else {
            handcard3.setImage(null);
        }

    }

    public void showScoreboard(ActionEvent event) {
        Client.getInstance().getView().getUserInterface().setScoreScene();
    }

    @Override
    protected AnchorPane getChatContainer() {
        return chatContainer;
    }
}
