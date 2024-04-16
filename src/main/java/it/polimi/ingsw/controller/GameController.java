package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.decks.Deck;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CardLocation;

import java.security.InvalidParameterException;

/**
 * deals with all the logic after a game is selected and created
 * **/
public class GameController {
    private final Game game;
    private GameStatus gameStatus;
    private TurnStatus turnStatus;
    //TODO  create some lock to avoid a data race if we want ot give the same controller to diff games, otherwise keep it as is
    /**
     * @param game  the game you want to be controlled
     * **/public GameController(Game game){
        this.game=game;
        gameStatus=GameStatus.LOBBY;
        this.waitPlayers();
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    private void updateStatus() {
         if(gameStatus == GameStatus.GAME_CREATION) {
             for (Player p : game.getPlayers()) {
                 if(p.getPlacedCard(new CardLocation(0,0)) == null
                    || p.getPrivateGoal() == null) {
                     return;
                 }
             }

             gameStatus = GameStatus.NORMAL_TURN;
         }
         else if(game.isFirstPlayersTurn()) {
             if (gameStatus == GameStatus.LAST_TURN) {
                 gameStatus = GameStatus.END;
             }
             else if (gameStatus == GameStatus.NORMAL_TURN) {
                 for (Player p : game.getPlayers()) {
                     if (game.getScoreBoard().getScore(p) >= 20) {
                         this.gameStatus = GameStatus.LAST_TURN;
                         return;
                     }
                 }

                 if(game.getResourceCardsDeck().isEmpty() && game.getGoldCardsDeck().isEmpty()) {
                     this.gameStatus = GameStatus.LAST_TURN;
                 }
             }

         }

    }

    private boolean isLocationValid(CardLocation location, Player player){
         if (player.getPlacedCard(location.topRightNeighbour()) != null&&
                (player.getPlacedCard(location.topRightNeighbour()).getBottomLeftCorner()==null)) {
            return false;
         }
         if (player.getPlacedCard(location.topLeftNeighbour()) != null&&
                (player.getPlacedCard(location.topLeftNeighbour()).getBottomRightCorner()==null)) {
             return false;
         }
         if (player.getPlacedCard(location.bottomRightNeighbour())!=null&&
                (player.getPlacedCard(location.bottomRightNeighbour()).getTopLeftCorner()==null)) {
             return false;
         }
         if (player.getPlacedCard(location.bottomLeftNeighbour())!=null&&
                (player.getPlacedCard(location.bottomLeftNeighbour()).getTopRightCorner()==null)) {
             return false;
         }

        return player.getPlacedCard(location.topRightNeighbour()) != null
                || player.getPlacedCard(location.topRightNeighbour()) != null
                || player.getPlacedCard(location.topRightNeighbour()) != null
                || player.getPlacedCard(location.topRightNeighbour()) != null;

    }

    private void waitPlayers() {
        while (gameStatus.equals(GameStatus.LOBBY)) {
            if (game.getPlayers().size() == game.getExpectedPlayers()) {
                //TODO change if we consider the client connected to the server as players or not, now it's just to avoid errors
                gameStatus = GameStatus.GAME_CREATION;
            }
        }
        this.setUp();
    }
    private void setUp(){
         game.shufflePlayers();
         game.startGame();

//         for(Player p: game.getPlayers()) {
//             Goal t1= game.getGoal();
//             Goal t2= game.getGoal();
//             System.out.println("Select the starting card: \n");//TODO change this with a message from the client
//             Scanner scanner=new Scanner(System.in);
//             int i = scanner.nextInt();
//             p.setPrivateGoal(i==1? t1:t2);
//             try {
//                 p.placeStartingCard();
//             } catch (Exception e) {
//                 throw new RuntimeException(e);
//             }
//         }
         gameStatus=GameStatus.NORMAL_TURN;
         //this.normalTurn();


    }
//    private void normalTurn() {
//         while (currstatus.equals(GameStatus.NORMAL_TURN)){
//             for (Player player: game.getPlayers()){
//                 System.out.println("select the index of the card and the point where to place the card");
//                 Scanner scanner=new Scanner(System.in);
//                 while (true) { // TODO maybe change this with a max n of tries after when the player is sent a tutorial
//                     int i = scanner.nextInt();
//                     int x=  scanner.nextInt();
//                     int y= scanner.nextInt();// TODO change this to a message from the client
//                     if(checkBottomLeft(x,y,player)&&checkBottomRight(x,y,player)&&checkTopLeft(x,y,player)&&checkTopRight(x,y,player)) {
//                         try {
//                             player.placeCard(i, new CardLocation(x, y));
//                             break;
//                         } catch (InvalidParameterException e) {
//                             throw new RuntimeException(e);
//                         }
//                     }
//                 }
//             }
//             ScoreBoard scoreBoard=this.game.getScoreBoard();
//             for(Player player: game.getPlayers()){
//                 if(scoreBoard.getScore(player)>=20) {
//                     currstatus = GameStatus.LAST_TURN;
//                     lastTurn();
//                 }
//
//             }
//         }
//    }
//    private void lastTurn(){
//        for (Player player: game.getPlayers()){
//            System.out.println("select the index of the card and the point where to place the card");
//            Scanner scanner=new Scanner(System.in);
//            while (true) { // TODO maybe change this with a max n of tries after when the player is sent a tutorial
//                int i = scanner.nextInt();
//                int x=  scanner.nextInt();
//                int y= scanner.nextInt();// TODO change this to a message from the client
//                if(checkBottomLeft(x,y,player)&&checkBottomRight(x,y,player)&&checkTopLeft(x,y,player)&&checkTopRight(x,y,player)) {
//                    try {
//                        player.placeCard(i, new CardLocation(x, y));
//                        break;
//                    } catch (InvalidParameterException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        }
//        currstatus=GameStatus.END;
//
//    }
//    private void end(){
//         return;
//    }


    public void evaluateGoals() {
         if(gameStatus != GameStatus.END) {
             throw new RuntimeException("The game's still running");
         }

         ScoreBoard scoreBoard = game.getScoreBoard();

         for(Player p : game.getPlayers()) {
             for(Goal g: game.getCommonGoals()) {
                 scoreBoard.addScore(p, g.evaluate(p));
             }
             scoreBoard.addScore(p, p.getPrivateGoal().evaluate(p));
         }
    }

    public void selectPrivateGoal(Player player, int index) {
         if(gameStatus != GameStatus.GAME_CREATION) {
             throw new RuntimeException("Cannot select private goal in this game status");
         }

         if(player.getPrivateGoal() != null) {
             throw new RuntimeException("Cannot select private goal twice");
         }

         Goal[] availableGoals = player.getAvailableGoals();

         if(index < 0 || index >= availableGoals.length) {
             throw new RuntimeException("index out of bound");
         }

         player.setPrivateGoal(availableGoals[index]);

         updateStatus();
    }

    public void placeStartCard(Player player, boolean onBackSide) {
        if(gameStatus != GameStatus.GAME_CREATION) {
            throw new RuntimeException("Cannot place starting card in this game status");
        }

        if(player.getPlacedCard(new CardLocation(0,0)) != null) {
            throw new RuntimeException("starting card already placed");
        }

        try {
            player.placeStartingCard(onBackSide);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        updateStatus();
    }

    public void placeCard(Player player, int index, CardLocation location) {
         if (!player.equals(game.getPlayers())) {
             throw new RuntimeException("it is not this player's turn");
         }
         if(this.gameStatus != GameStatus.NORMAL_TURN && this.gameStatus != GameStatus.LAST_TURN) {
             throw new RuntimeException("Game's not running (finished or never started");
         }
         if(this.turnStatus != TurnStatus.PLACE) {
             throw new RuntimeException("Placement already occurred, player's expected to draw a card");
         }

         if(isLocationValid(location, player)) {
            try {
                player.placeCard(index, location);
                // Card successfully placed
                this.turnStatus = TurnStatus.DRAW;
                game.getScoreBoard().addScore(player,

                        // is safe to assume that the card that was just placed is a PlayCard
                        // since it is certain to come from the player's hand
                        ((PlayCard)player.getPlacedCard(location)).getScoringStrategy().evaluate(player, location)
                );
            } catch (InvalidParameterException e) {
                // Invalid index
                throw new RuntimeException(e);
            }
         }
         else {
            throw new RuntimeException("cannot place card in the provided location");
         }
    }

    public void drawCard(Player player, int index) {
        if (!player.equals(game.getPlayers())) {
            throw new RuntimeException("it is not this player's turn");
        }
        if(this.gameStatus != GameStatus.NORMAL_TURN && this.gameStatus != GameStatus.LAST_TURN) {
            throw new RuntimeException("Game's not running (finished or never started");
        }
        if (this.turnStatus != TurnStatus.DRAW) {
            throw new RuntimeException("the player must place a card before they can draw");
        }

        if (index < 0 || index > 5) {
            throw new RuntimeException("index out of range");
        }

        if (index == 0) {
            // drawing from resource card deck
            Deck<PlayCard> deck = game.getResourceCardsDeck();
            if (deck.isEmpty()) {
                throw new RuntimeException("Deck's empty");
            }

            try {
                player.addPlayerCard(deck.draw());
                // card successfully drawn
                game.nextTurn();
                updateStatus();
                this.turnStatus = TurnStatus.PLACE;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (index == 1) {
            // drawing from gold card deck
            Deck<PlayCard> deck = game.getGoldCardsDeck();
            if (deck.isEmpty()) {
                throw new RuntimeException("Deck's empty");
            }

            try {
                player.addPlayerCard(deck.draw());
                // card successfully drawn
                game.nextTurn();
                updateStatus();
                this.turnStatus = TurnStatus.PLACE;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // drawing one of the visible cards

            index = index - 2;

            PlayCard[] visibleCards = game.getVisibleCards();

            if (visibleCards[index] != null) {
                try {
                    player.addPlayerCard(visibleCards[index]);
                    // card drawn successfully
                    game.refillVisibleCards();
                    game.nextTurn();
                    updateStatus();
                    this.turnStatus = TurnStatus.PLACE;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("There's no card to draw in the provided location");
            }
        }
    }
}
