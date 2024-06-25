package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.gui.MediaManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ScoreBoardController extends GUIScene{

    public AnchorPane scorePane;
    public ImageView redPawn;
    public ImageView greenPawn;
    public ImageView yellowPawn;
    public ImageView bluePawn;
    @FXML
    public ImageView playerColor1;
    @FXML
    public ImageView playerColor2;
    @FXML
    public ImageView playerColor3;
    @FXML
    public ImageView playerColor4;

    private ImageView[] playerColors;
    public Label scorePlayer1;
    public Label scorePlayer2;
    public Label scorePlayer3;
    public Label scorePlayer4;
    public Label playerNickname1;
    public Label playerNickname2;
    public Label playerNickname3;
    public Label playerNickname4;
    public Label announcement;
    public Label winner;

    private Label[] scoreLabels;
    private Label[] playerNicknameLabels;
    private final Map<Integer, Point> scoreMap = new HashMap<>();
    private final EnumMap<PlayerColor, ImageView> colorToPawn = new EnumMap<>(PlayerColor.class);

    @FXML
    public void initialize() {
        winner.setVisible(false);
        announcement.setVisible(false);
        scoreLabels = new Label[]{scorePlayer1, scorePlayer2, scorePlayer3, scorePlayer4};
        playerNicknameLabels = new Label[]{playerNickname1, playerNickname2, playerNickname3, playerNickname4};
        playerColors = new ImageView[]{playerColor1, playerColor2, playerColor3, playerColor4};

        colorToPawn.put(PlayerColor.RED, redPawn);
        colorToPawn.put(PlayerColor.GREEN, greenPawn);
        colorToPawn.put(PlayerColor.YELLOW, yellowPawn);
        colorToPawn.put(PlayerColor.BLUE, bluePawn);

        scoreMap.put(0, new Point(93, 731));
        scoreMap.put(1, new Point(190, 731));
        scoreMap.put(2, new Point(286, 731));
        scoreMap.put(3, new Point(334, 645));
        scoreMap.put(4, new Point(238, 645));//problema
        scoreMap.put(5, new Point(142, 645));
        scoreMap.put(6, new Point(45, 645));
        scoreMap.put(7, new Point(45, 559));
        scoreMap.put(8, new Point(142, 559));
        scoreMap.put(9, new Point(238, 559));
        scoreMap.put(10, new Point(334, 559));
        scoreMap.put(11, new Point(334, 473));
        scoreMap.put(12, new Point(238, 473));
        scoreMap.put(13, new Point(142, 473));
        scoreMap.put(14, new Point(45, 473));
        scoreMap.put(15, new Point(45, 386));
        scoreMap.put(16, new Point(142, 386));
        scoreMap.put(17, new Point(238, 386));
        scoreMap.put(18, new Point(334, 386));
        scoreMap.put(19, new Point(334, 300));
        scoreMap.put(20, new Point(190, 258));
        scoreMap.put(21, new Point(45, 300));
        scoreMap.put(22, new Point(45, 214));
        scoreMap.put(23, new Point(45, 128));
        scoreMap.put(24, new Point(100, 56));
        scoreMap.put(25, new Point(190, 40));
        scoreMap.put(26, new Point(278, 56));
        scoreMap.put(27, new Point(334, 127));
        scoreMap.put(28, new Point(334, 128));
        scoreMap.put(29, new Point(190, 146));

        yellowPawn.setVisible(false);
        greenPawn.setVisible(false);
        redPawn.setVisible(false);
        bluePawn.setVisible(false);

        List<Player> players = Client.getInstance().getView().getGameModel().getPlayers();

        for(int i = 0; i < players.size(); i++) {
            playerNicknameLabels[i].setText(players.get(i).nickname);
            scoreLabels[i].setVisible(true);
        }

        for(int i = players.size(); i < playerNicknameLabels.length; i++) {
            playerNicknameLabels[i].setVisible(false);
            scoreLabels[i].setVisible(false);
        }
        for(int i = 0; i < players.size(); i++) {
            playerColors[i].setImage(MediaManager.getInstance().getImage(
                    PlayerColor.playerColorToImagePath(players.get(i).getColor())
            ));
        }

        update();
    }

    @Override
    public void update() {
        for (Player player: Client.getInstance().getView().getGameModel().getPlayers()) {
            int score = Client.getInstance().getView().getGameModel().getScoreBoard().getScore(player.nickname);
            score=score%30;

            ImageView pawn = colorToPawn.get(player.getColor());

            pawn.setVisible(true);
            pawn.setLayoutX(scoreMap.get(score).getX());
            pawn.setLayoutY(scoreMap.get(score).getY());

            refreshScoreboard();

            if(Client.getInstance().getView().getGameStatus().equals(GameStatus.END)){
                announcement.setVisible(true);
                int winnerScore = 0;
                for(Player p : Client.getInstance().getView().getGameModel().getPlayers()){
                    if(Client.getInstance().getView().getGameModel().getScoreBoard().getScore(p.nickname) > winnerScore){
                        winnerScore = Client.getInstance().getView().getGameModel().getScoreBoard().getScore(p.nickname);
                        winner.setText(p.nickname);
                    }
                }
                winner.setVisible(true);


            }
        }
    }

    public void refreshScoreboard() {
        List<Player> players;
        players = Client.getInstance().getView().getPlayersList();

        for(int i = 0; i < players.size(); i++) {
            scoreLabels[i].setText(Integer.toString(Client.getInstance().getView().getGameModel().getScoreBoard().getScore(players.get(i).nickname)));
        }
    }
    @Override
    protected AnchorPane getChatContainer() {
        return null;
    }
}
