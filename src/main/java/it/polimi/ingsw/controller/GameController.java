package it.polimi.ingsw.controller;
import it.polimi.ingsw.events.Observable;
import it.polimi.ingsw.events.messages.server.GameStatusUpdateMessage;
import it.polimi.ingsw.events.messages.server.ServerErrorMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.decks.Deck;
import it.polimi.ingsw.events.messages.client.ClientMessage;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.cliendhandlers.ClientHandler;
import it.polimi.ingsw.utils.CardLocation;

import java.security.InvalidParameterException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * GameController allows players to play a game.
 * It handles all the game logic and checks that the player is performing legal operations.
 **/
public class GameController extends Observable implements Runnable {
    private final Game game;
    private GameStatus gameStatus;
    private TurnStatus turnStatus;

    private final BlockingQueue<ClientMessage> messageQueue;

    /**
     * Constructs a GameController that handles the provided game.
     * <p>
     * By default, the game will be in LOBBY status.
     *
     * @param game  the game you want to be controlled
     **/
    public GameController(Game game){
        System.err.println("GameController created");
        this.game=game;
        turnStatus = null;
        gameStatus=GameStatus.LOBBY;
        this.messageQueue = new ArrayBlockingQueue<>(100);
        new Thread(this).start();
    }

    /**
     * Retrieves the current game status.
     *
     * @return the current game status.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Retrieves the current turn status.
     *
     * @return the current turn status.
     */
    public TurnStatus getTurnStatus() {
        return turnStatus;
    }

    /**
     * Retrieves a reference to the Game that the controller is handling.
     *
     * @return the Game the controller is handling.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Put a message for this Game in a blocking queue.
     * Messages are elaborated asynchronously on another thread.
     *
     * @param message the message that needs to be processed by this controller.
     */
    public void forwardMessage(ClientMessage message)  {
        try {
            this.messageQueue.put(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates, if needed, the Game status.
     * Allows to start and end games.
     * A game enters the GAME_CREATION status once the number of players matches the
     * number of expected players. In this phase players can asynchronously select their
     * private goal and place their starting card.
     * When everyone has selected their goal and placed their starting card, the game
     * enters the NORMAL_TURN status.
     * When an even trigger the last turn of the game and the current turn ends,
     * the game enters its LAST_TURN status.
     * After the last player placed their last card, the game comes to an end.
     * Thus, the Game is put in END status.
     */
    public void updateStatus() {
        if(gameStatus == GameStatus.LOBBY) {
            if (game.getExpectedPlayers() == game.getPlayers().size() && game.getAvailableColor().size() == (4 - game.getExpectedPlayers())) {
                this.game.subscribeCommonObservers();

                System.err.println("ExpectedPlayers amount reached, game starts. Connected players:");
                for(Player p : game.getPlayers()) {
                    System.err.println("\t" + p.nickName);
                }

                turnStatus = TurnStatus.PLACE;
                gameStatus = GameStatus.GAME_CREATION;
                game.startGame();
            }
        }
        else if(gameStatus == GameStatus.GAME_CREATION) {
            boolean flag = true;
            for (Player p : game.getPlayers()) {
                if(p.getPlacedCard(new CardLocation(0,0)) == null
                    || p.getPrivateGoal() == null) {
                    flag = false;
                    break;
                }
            }

            if(flag) {
                System.err.println("Players placed starting cards and selected goal, first turn starts");
                gameStatus = GameStatus.NORMAL_TURN;
            }
        }
        else if(game.isFirstPlayersTurn()) {
            if (gameStatus == GameStatus.LAST_TURN) {
                System.err.println("GAME FINISHED");
                gameStatus = GameStatus.END;
            }
            else if (gameStatus == GameStatus.NORMAL_TURN) {
                boolean flag = true;
                for (Player p : game.getPlayers()) {
                    if (game.getScoreBoard().getScore(p) >= 20) {
                        System.err.println(p.nickName + "reached 20 points, last turn starts");
                        this.gameStatus = GameStatus.LAST_TURN;
                        flag = false;
                        break;
                    }
                }

                if(flag && game.emptyDecks()) {

                    System.err.println("Decks are empty, last turn starts");
                    this.gameStatus = GameStatus.LAST_TURN;
                }
            }

        }

        notifyObservers(new GameStatusUpdateMessage(gameStatus, turnStatus, game.getTurn().nickName));
    }

    /**
     * Helper method to check if a card can be placed in the player's board
     * in a specified location.
     *
     * @param location the location that needs to be checked.
     * @param player the player whose board needs to be checked.
     * @return {@code true} if the location is valid and a card can be placed, {@code false} otherwise.
     */
    private static boolean isLocationValid(CardLocation location, Player player){
        if (player.getPlacedCard(location) != null) return false;


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
                || player.getPlacedCard(location.topLeftNeighbour()) != null
                || player.getPlacedCard(location.bottomRightNeighbour()) != null
                || player.getPlacedCard(location.bottomLeftNeighbour()) != null;
    }

    /**
     * Runs evaluations for all goals (both public and private goals).
     * Updates each player's scoring with the result of the evaluations.
     */
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

    /**
     * Allows to select the private goal, among the available ones, for the
     * specified player.
     * <p>
     * This operation is only valid if the game is in GAME_CREATION status, if the
     * player exists and if they haven't already selected their private goal.
     *
     * @param player the player who is selecting the private goal.
     * @param index the index of the selected goal in the available ones.
     */
    public void selectPrivateGoal(Player player, int index) {
        if (gameStatus != GameStatus.GAME_CREATION) {
            throw new RuntimeException("Cannot select private goal in this game status");
        }

        if (player.getPrivateGoal() != null) {
            throw new RuntimeException("Cannot select private goal twice");
        }

        Goal[] availableGoals = player.getAvailableGoals();

        if (index < 0 || index >= availableGoals.length) {
            throw new RuntimeException("index out of bound");
        }

        player.setPrivateGoal(availableGoals[index]);


        System.err.println(this.game.getIdGame() + ": "+player.nickName + " selected their goal");
        updateStatus();
    }

    /**
     * Allows to place the starting card, on a specific side, for the player.
     * <p>
     * This operation is only valid if the game is in NORMAL_TURN status or in LAST_TURN status,
     * if the player exists and if they haven't already placed their starting card.
     *
     * @param player the player who is placing the starting card.
     * @param onBackSide {@code true} if the starting card needs to be placed with the back side up, {@code false} otherwise.
     */
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

        updateInventory(player,new CardLocation(0,0));


        System.err.println(this.game.getIdGame() + ": "+player.nickName + " placed their starting card");
        updateStatus();
    }

    /**
     * Helper method that updates a player inventory after a placement occurred.
     *
     * @param player the player whose inventory needs to be updated.
     * @param location the location where the placement occurred.
     */
    private static void updateInventory(Player player, CardLocation location) {
        Card placedCard = player.getPlacedCard(location);
        player.addItems(placedCard.collectItems());

        Card t = player.getPlacedCard(location.topLeftNeighbour());
        if(t != null) {
            player.removeItem(t.getBottomRightCorner());
        }

        t = player.getPlacedCard(location.topRightNeighbour());
        if(t != null) {
            player.removeItem(t.getBottomLeftCorner());
        }

        t = player.getPlacedCard(location.bottomLeftNeighbour());
        if(t != null) {
            player.removeItem(t.getTopRightCorner());
        }

        t = player.getPlacedCard(location.bottomRightNeighbour());
        if(t != null) {
            player.removeItem(t.getTopLeftCorner());
        }
    }

    /**
     * Allows to place a card, on a specific side, in a player's board.
     * <p>
     * This operation is only valid if the game is in NORMAL_TURN status or in LAST_TURN status,
     * if the player exists, if it's their turn and if they haven't already placed a card in the current turn.
     *
     * @param player the player who is placing the card.
     * @param index the index of the card in the player's hand that needs to be placed.
     * @param onBackSide {@code true} if the starting card needs to be placed with the back side up, {@code false} otherwise.
     * @param location the CardLocation where the card needs to be placed in the player's board.
     */
    public void placeCard(Player player, int index, boolean onBackSide, CardLocation location) {
         if (!player.getClientHandler().getPlayerIdentifier().equals(game.getTurn().getClientHandler().getPlayerIdentifier())) {
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
                player.placeCard(index, onBackSide, location);

                updateInventory(player, location);

                game.getScoreBoard().addScore(player,

                        // it is safe to assume that the card that was just placed is a PlayCard
                        // since it is certain to come from the player's hand
                        ((PlayCard)player.getPlacedCard(location)).getScoringStrategy().evaluate(player, location)
                );

                // Card successfully placed
                this.turnStatus = TurnStatus.DRAW;
            } catch (InvalidParameterException e) {
                // Invalid index
                throw new RuntimeException(e);
            }
         }
         else {
            throw new RuntimeException("cannot place card in the provided location");
         }

        System.err.println(this.game.getIdGame() + ": "+player.nickName + " placed a card");
        updateStatus();
    }

    /**
     * Allows to a player to pick up one of the visible cards or to draw from the decks.
     * <p>
     * This operation is only valid if the game is in NORMAL_TURN status,
     * if the player exists, if it's their turn and if they have already placed a card in the current turn.
     *
     * @param player the player who is drawing.
     * @param index the index of the cards that the player wants to pick up.
     */
    public void drawCard(Player player, int index) {
        if (!player.getClientHandler().getPlayerIdentifier().equals(game.getTurn().getClientHandler().getPlayerIdentifier())) {
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
                    visibleCards[index] = null;
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

        System.err.println(this.game.getIdGame() + ": "+player.nickName + " picked up a card");
        updateStatus();
    }

    public void selectColor(Player player, PlayerColor color) {
        if(game.getAvailableColor().contains(color)) {
            player.setColor(color);
            game.updateAvailableColors();
        }
        else {
            throw new RuntimeException("This color isn't available");
        }

        System.err.println(this.game.getIdGame() + ": "+player.nickName + " selected "+color+" as their color");
        updateStatus();
    }

    /**
     * Thread method that processes messages asynchronously.
     */
    @Override
    public void run() {
        GameSelector selector = GameSelector.getInstance();
        ClientMessage message;
        while(true) {
            try {
                message = this.messageQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                message.execute(selector, this);
            } catch (RuntimeException e) {
                ClientHandler sender = selector.getPlayersClientHandler(message.getPlayerIdentifier());
                if (sender != null) {
                    // sender was recognized, sending back a ServerErrorMessage
                    sender.sendMessage(new ServerErrorMessage(e));
                }
            }

        }
    }


}
