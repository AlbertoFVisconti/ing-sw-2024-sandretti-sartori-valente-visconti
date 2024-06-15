package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.events.messages.client.DrawCardMessage;
import it.polimi.ingsw.events.messages.client.PlaceCardMessage;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ui.gui.MediaManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
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
    public ImageView tableBackground;


    private String currentlyDisplayedPlayer;

    @FXML
    public Label fungusLabel;
    @FXML
    public Label plantLabel;
    @FXML
    public Label animalLabel;
    @FXML
    public Label insectLabel;
    @FXML
    public Label featherLabel;
    @FXML
    public Label inkLabel;
    @FXML
    public Label scrollLabel;

    private EnumMap<Corner, Label> cornerToLabel;

    @FXML
    public ImageView resourceDeck;
    @FXML
    public ImageView goldDeck;
    @FXML
    public ImageView visibleCard1;
    @FXML
    public ImageView visibleCard2;
    @FXML
    public ImageView visibleCard3;
    @FXML
    public ImageView visibleCard4;

    @FXML
    public ImageView commonGoal1;
    @FXML
    public ImageView commonGoal2;

    private ImageView[] drawableCards;

    @FXML
    public Label turnWarningLabel;
    @FXML
    public Button showScoreBoard;
    @FXML
    public ImageView playerColor1;
    @FXML
    public ImageView playerColor2;
    @FXML
    public ImageView playerColor3;
    @FXML
    public ImageView playerColor4;

    private ImageView[] playerColors;


    @FXML
    public AnchorPane chatContainer;
    @FXML
    public AnchorPane TablePane;

    @FXML
    public ImageView handCard1;

    @FXML
    public ImageView handCard2;

    @FXML
    public ImageView handCard3;

    private ImageView[] hand;


    @FXML
    public ImageView privateGoal;

    @FXML
    public ImageView startingCard;

    @FXML
    public Button playerNickname1;

    @FXML
    public Button playerNickname2;

    @FXML
    public Button playerNickname3;

    @FXML
    public Button playerNickname4;

    private Button[] playerNicknameLabels;

    @FXML
    public Label scorePlayer1;

    @FXML
    public Label scorePlayer2;

    @FXML
    public Label scorePlayer3;

    @FXML
    public Label scorePlayer4;

    private Label[] scoreLabels;

    private int sel;

    //hashmap to store the shapes that are placed on the board
    private final HashMap<CardLocation, ImageView> placeCardButtons = new HashMap<>();
    //hashmap to store the image of the cards that are placed on the board
    private final HashMap<CardLocation, ImageView> boardCardImages = new HashMap<>();
    long lastClickTime;
    long DOUBLE_CLICK_THRESHOLD=300;
    boolean[] isflipped={false,false,false};

    @FXML
    public void initialize() {
        this.currentlyDisplayedPlayer = Client.getInstance().getView().getLocalPlayerName();

        drawableCards = new ImageView[]{resourceDeck, goldDeck, visibleCard1, visibleCard2, visibleCard3, visibleCard4};
        scoreLabels = new Label[]{scorePlayer1, scorePlayer2, scorePlayer3, scorePlayer4};
        playerNicknameLabels = new Button[]{playerNickname1, playerNickname2, playerNickname3, playerNickname4};
        playerColors = new ImageView[]{playerColor1, playerColor2, playerColor3, playerColor4};
        hand = new ImageView[]{handCard1, handCard2, handCard3};

        cornerToLabel = new EnumMap<>(Corner.class);
        cornerToLabel.put(Corner.FUNGUS, fungusLabel);
        cornerToLabel.put(Corner.PLANT, plantLabel);
        cornerToLabel.put(Corner.ANIMAL, animalLabel);
        cornerToLabel.put(Corner.INSECT, insectLabel);
        cornerToLabel.put(Corner.FEATHER, featherLabel);
        cornerToLabel.put(Corner.INK, inkLabel);
        cornerToLabel.put(Corner.SCROLL, scrollLabel);


        TablePane.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/fxml/Style.css")).toExternalForm()));

        View view = Client.getInstance().getView();
        MediaManager mediaManager = MediaManager.getInstance();

        privateGoal.setImage(mediaManager.getImage(view.getLocalPlayer().getPrivateGoal()));
        commonGoal1.setImage(mediaManager.getImage(view.getGameModel().getCommonGoals()[0]));
        commonGoal2.setImage(mediaManager.getImage(view.getGameModel().getCommonGoals()[1]));

        for(int i = 0; i < hand.length; i++) {
            hand[i].setImage(mediaManager.getImage(view.getLocalPlayer().getPlayerCard(i), false));
            hand[i].getStyleClass().add("clickable");
        }

        for(ImageView imageView : drawableCards) {
            imageView.getStyleClass().add("clickable");
        }

        List<Player> players = Client.getInstance().getView().getGameModel().getPlayers();
        for(int i = 0; i < players.size(); i++) {
            playerColors[i].setImage(MediaManager.getInstance().getImage(
                    PlayerColor.playerColorToImagePath(players.get(i).getColor())
            ));
        }

        for(int i = 0; i < players.size(); i++) {
            playerNicknameLabels[i].setText(players.get(i).nickname);
            scoreLabels[i].setVisible(true);
        }

        for(int i = players.size(); i < playerNicknameLabels.length; i++) {
            playerNicknameLabels[i].setVisible(false);
            scoreLabels[i].setVisible(false);
        }

        update();
    }

    public void SelectCard(int id){
        if (!Client.getInstance().getView().getGameModel().getTurn().equals(Client.getInstance().getView().getLocalPlayer())|| !Client.getInstance().getView().getTurnStatus().equals(TurnStatus.PLACE)){
        }else{
            sel=id;
            for(int i=0; i<hand.length;i++){
                hand[i].setOpacity(1);
                if (i==id)
                    hand[i].setOpacity(0.5);
            }
        }
    }

    @FXML
    public void SelectCard1(MouseEvent mouseEvent) {
        if (!Client.getInstance().getView().getGameModel().getTurn().equals(Client.getInstance().getView().getLocalPlayer())|| !Client.getInstance().getView().getTurnStatus().equals(TurnStatus.PLACE)){
            //TODO: send a message to the client 'you can't place a card now'
        }else{
            sel=0;
            handCard1.setOpacity(0.5);
            handCard2.setOpacity(1);
            handCard3.setOpacity(1);
        }
    }
    @FXML
    public void SelectCard2(MouseEvent mouseEvent) {
        if (!Client.getInstance().getView().getGameModel().getTurn().equals(Client.getInstance().getView().getLocalPlayer()) || !Client.getInstance().getView().getTurnStatus().equals(TurnStatus.PLACE)) {
            //TODO: send a message to the client 'you can't place a card now'
        }else{
            sel = 1;
            handCard2.setOpacity(0.5);
            handCard1.setOpacity(1);
            handCard3.setOpacity(1);
        }

    }
    @FXML
    public void SelectCard3(MouseEvent mouseEvent) {
        if (!Client.getInstance().getView().getGameModel().getTurn().equals(Client.getInstance().getView().getLocalPlayer()) || !Client.getInstance().getView().getTurnStatus().equals(TurnStatus.PLACE)) {
            //TODO: send a message to the client 'you can't place a card now'
        }else{
            sel = 2;
            handCard3.setOpacity(0.5);
            handCard2.setOpacity(1);
            handCard1.setOpacity(1);
        }
    }

    public void refreshScoreboard() {
        List<Player> players;
        players = Client.getInstance().getView().getPlayersList();

        for(int i = 0; i < players.size(); i++) {
            scoreLabels[i].setText(Integer.toString(Client.getInstance().getView().getGameModel().getScoreBoard().getScore(players.get(i).nickname)));
        }
    }

    public void disableDecks(){
        for(ImageView imageView : drawableCards) {
            imageView.setOnMouseClicked(null);
            imageView.setOpacity(0.5);
        }
    }

    public void disableCards() {
        for(ImageView imageView : hand) {
            imageView.setOnMouseClicked(null);
            imageView.setOpacity(0.5);
        }
    }

    private void addCardSlot(CardSlot cardSlot,CardLocation cl){
        ImageView cardImage = new ImageView(MediaManager.getInstance().getImage(cardSlot));
        cardImage.setFitHeight(CARD_HEIGHT);
        cardImage.setFitWidth(CARD_WIDTH);

        cardImage.setLayoutX(startingCard.getLayoutX() + (cl.x() * (CARD_WIDTH - CARD_CORNER_WIDTH)));
        cardImage.setLayoutY(startingCard.getLayoutY() + (-cl.y() * (CARD_HEIGHT - CARD_CORNER_HEIGHT)));

        boardCardImages.put(cl, cardImage);

        cardImage.getStyleClass().add("placed_card");

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
        if(boardCardImages.containsKey(cl)){
            for(int i: POSS){
                for(int j: POSS){
                    CardLocation p = new CardLocation(cl.x() + i, cl.y() + j);
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
            Client.getInstance().getServerHandler().sendMessage(new PlaceCardMessage(sel, isflipped[sel], new CardLocation(cardLocation.x() , cardLocation.y() )));
            //isflipped= new boolean[]{false, false, false};//after placing all the card a now front-facing
            for (ImageView s : placeCardButtons.values()) {
                s.setVisible(false);
                s.setOnMouseClicked(null);
            }
            handCard1.setOpacity(1);
            handCard2.setOpacity(1);
            handCard3.setOpacity(1);
        } catch (RuntimeException e) {
            reportError(e);
        }
    }

    public void addShape(CardLocation cl){
        if(placeCardButtons.containsKey(cl)){
            placeCardButtons.get(cl).setVisible(true);
            placeCardButtons.get(cl).setOnMouseClicked((MouseEvent mouseEvent) -> placeMessage(cl));
            return;
        }


        ImageView shape = new ImageView();
        placeCardButtons.put(cl, shape);
        shape.setImage(MediaManager.getInstance().getImage("/image/cardshape.png"));
        shape.setFitHeight(CARD_HEIGHT);
        shape.setFitWidth(CARD_WIDTH);
        shape.getStyleClass().add("cardshape");

        shape.setLayoutX(startingCard.getLayoutX() + (cl.x() * (CARD_WIDTH-CARD_CORNER_WIDTH)));
        shape.setLayoutY(startingCard.getLayoutY() + (-cl.y() * (CARD_HEIGHT-CARD_CORNER_HEIGHT)));

        shape.setVisible(true);
        TablePane.getChildren().add(shape);
        shape.setOnMouseClicked((MouseEvent mouseEvent) -> placeMessage(cl));
    }
    public void flipCard(int i) {
        isflipped[i]=!isflipped[i];
        if (isflipped[i]) {
            hand[i].setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getPlayerCards()[i].getBackPath())).toString()
            ));
        } else {
            hand[i].setImage(new Image(
                    Objects.requireNonNull(getClass().getResource(Client.getInstance().getView().getLocalPlayer().getPlayerCards()[i].getFrontPath())).toString()
            ));
        }
    }


    @Override
    public void update() {
        refreshScoreboard();

        ArrayList<CardLocation> newCardsOnBoard = new ArrayList<>();

        Player displayedPlayer = null;

        MediaManager mediaManager = MediaManager.getInstance();
        View view = Client.getInstance().getView();

        for(Player p : view.getGameModel().getPlayers()) {
            if(p.nickname.equals(currentlyDisplayedPlayer)) {
                displayedPlayer = p;
                break;
            }
        }
        if(displayedPlayer == null) displayedPlayer = Client.getInstance().getView().getLocalPlayer();

        boolean localPlayerBoard = view.getLocalPlayer().nickname.equals(displayedPlayer.nickname);


        Map<CardLocation,CardSlot> playerBoard = displayedPlayer.getBoard();
        for(CardLocation cardLocation : playerBoard.keySet()) {
            if(!boardCardImages.containsKey(cardLocation)) {
                newCardsOnBoard.add(cardLocation);
            }
        }

        // update inventory
        for(Corner item : cornerToLabel.keySet()) {
            cornerToLabel.get(item).setText(String.valueOf(displayedPlayer.getInventory().count(item)));
        }

        newCardsOnBoard.sort(Comparator.comparingInt((CardLocation cl) -> playerBoard.get(cl).placementTurn()));
        for(CardLocation cardLocation: newCardsOnBoard) {
            this.addCardSlot(playerBoard.get(cardLocation),cardLocation);
        }



        PlayCard[] playerCards =  displayedPlayer.getPlayerCards();
        for(int i = 0; i < hand.length; i++) {
            hand[i].setImage(mediaManager.getImage(playerCards[i], isflipped[i]));
        }

        resourceDeck.setImage(mediaManager.getImage(view.getGameModel().getResourceCardsDeck().getTopOfTheStack().getPath()));
        goldDeck.setImage(mediaManager.getImage(view.getGameModel().getGoldCardsDeck().getTopOfTheStack().getGoldenPath()));

        for(int i = 2; i < drawableCards.length; i++) {
            drawableCards[i].setImage(mediaManager.getImage(view.getGameModel().getVisibleCards()[i-2], false));
        }

        if((view.getPlayersTurn().equals(view.getLocalPlayer().nickname))) {
            if (view.getTurnStatus().equals(TurnStatus.PLACE)) {
                turnWarningLabel.setText(view.getLocalPlayer().nickname + " it's your turn to place a card!");
                //cards again clickable

                for (int i = 0; i < hand.length; i++) {
                    int finalI = i;
                    if(localPlayerBoard) {
                        hand[finalI].setOnMouseClicked((MouseEvent mouseEvent) -> {
                            sel = finalI;
                            for (int j = 0; j < hand.length; j++) {
                                if (finalI == j){
                                    hand[finalI].setOpacity(0.5);

                                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                                        long clickTime = System.currentTimeMillis();
                                        if (clickTime - lastClickTime < DOUBLE_CLICK_THRESHOLD) {
                                            flipCard(finalI);
                                        } else {
                                            SelectCard(finalI);
                                        }
                                        System.out.println(isflipped[0]+" "+isflipped[1]+" "+ isflipped[2]);
                                        lastClickTime = clickTime;
                                    }

                                }
                                else hand[j].setOpacity(1);
                            }
                        });
                    }
                    else {
                        hand[finalI].setOnMouseClicked(null);
                    }

                    hand[i].setOpacity(1);
                }

                disableDecks();

                if(localPlayerBoard) addPlaceHolder(new CardLocation(0,0), new HashSet<>());

            } else if (view.getTurnStatus().equals(TurnStatus.DRAW)) {
                for(int i = 0; i < drawableCards.length; i++) {
                    drawableCards[i].setOpacity(1);
                    int finalI = i;
                    if(localPlayerBoard) {
                        drawableCards[i].setOnMouseClicked((MouseEvent mouseEvent) -> Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(finalI)));
                    }
                    else {
                        drawableCards[i].setOnMouseClicked(null);
                    }
                }

                turnWarningLabel.setText(view.getLocalPlayer().nickname + " it's your turn to pick up a card!");
                for(ImageView s : placeCardButtons.values()){
                    s.setVisible(false);
                    s.setOnMouseClicked(null);
                }
            }
        }
        else {
            disableDecks();
            turnWarningLabel.setText(view.getPlayersTurn() + "'s turn!");
        }
    }

    public void showScoreboard(ActionEvent event) {
        Client.getInstance().getView().getUserInterface().setScoreScene();
    }

    @Override
    protected AnchorPane getChatContainer() {
        return chatContainer;
    }

    public void viewPlayer1(ActionEvent actionEvent) {
        viewPlayer(1);
    }
    public void viewPlayer2(ActionEvent actionEvent) {
        viewPlayer(2);
    }
    public void viewPlayer3(ActionEvent actionEvent) {
        viewPlayer(3);
    }
    public void viewPlayer4(ActionEvent actionEvent) {
        viewPlayer(4);
    }

    private void viewPlayer(int playerID) {
        String nickname;
        try {
            nickname = playerNicknameLabels[playerID - 1].getText();
        } catch (Exception e) {
            return;
        }

        if(!nickname.equals(currentlyDisplayedPlayer)) {
            this.currentlyDisplayedPlayer = nickname;

            this.boardCardImages.clear();
            this.TablePane.getChildren().clear();
            this.TablePane.getChildren().add(tableBackground);
            this.placeCardButtons.clear();

            this.update();
        }
    }
}
