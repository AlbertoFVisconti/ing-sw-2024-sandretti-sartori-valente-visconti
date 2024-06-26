package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.events.messages.client.ClientToServerPingMessage;
import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.chat.ChatMessage;
import it.polimi.ingsw.model.decks.VirtualDeck;
import it.polimi.ingsw.model.decks.VirtualDeckLoader;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.saving.GameData;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.ui.UserInterface;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class View implements VirtualView {
    // local player's user interface
    private final UserInterface userInterface;

    // local model used to store data from the server (only contains data that the player is allowed to access)
    private Game gameModel;

    // reference to local player's object in the local model, used to simplify methods
    private Player localPlayer;

    // set of available games (when received from the server)
    protected HashSet<String> availableGames;

    // game status data
    private GameStatus gameStatus;
    private TurnStatus turnStatus;
    private String playersTurn;

    // message processing queues
    // two queues are used in order to avoid processing messages in illegal order (for example, a model update message processed before a join confirmation message)
    private final BlockingQueue<ServerMessage> messageQueue;
    private final BlockingQueue<ServerMessage> gameMessages;

    /**
     * Constructs a local View object that handles the incoming message from the server
     * and display the game content through the provided user interface.
     *
     * @param userInterface the user interface that needs to be used to present game data to the user
     */
    public View(UserInterface userInterface) {
        this.userInterface = userInterface;
        messageQueue = new ArrayBlockingQueue<>(100);
        gameMessages = new ArrayBlockingQueue<>(100);

        // setting up the local model (might be overridden by a join confirmation message)
        try {
            gameModel = new Game(
                    new VirtualDeckLoader(),
                    new VirtualDeckLoader(),
                    null,
                    null,
                    null,
                    -1
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // first queue's thread
        new Thread(
                () -> {
                    ServerMessage message;

                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            message = messageQueue.take();

                            // handing game related messages to the gameMessages queue
                            if (message.messageType == MessageType.PRIVATE_MODEL_UPDATE_MESSAGE || message.messageType == MessageType.MODEL_UPDATE_MESSAGE) {
                                this.gameMessages.put(message);
                                continue;
                            }

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        // processing non-game related messages
                        synchronized (this) {
                            message.updateView(this);
                        }

                        // sending update request to the user interface
                        if (message.messageType == MessageType.CONNECT_JOIN_MESSAGE ||
                                message.messageType == MessageType.SERVER_ERROR_MESSAGE ||
                                message.messageType == MessageType.CHAT_MESSAGE ||
                                message.messageType == MessageType.GAME_LIST_MESSAGE) {
                            this.userInterface.update();
                        }
                    }
                }
        ).start();

        // second queue's thread
        new Thread(
                () -> {
                    ServerMessage message;

                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            // game message won't be processed until a local player object is built
                            // (a game is joined)
                            if (this.localPlayer != null) {
                                message = gameMessages.take();
                            } else {
                                continue;
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        // processing game related messages
                        synchronized (this) {
                            message.updateView(this);
                        }

                        // sending update request to the user interface
                        this.userInterface.update();
                    }
                }
        ).start();
    }

    /**
     * Retrieves the user interface used by the View to present game data to the player
     *
     * @return a reference to the user interface
     */
    public UserInterface getUserInterface() {
        return userInterface;
    }

    /**
     * Retrieves a reference to the local player object in the local model.
     *
     * @return a reference to the local player model object
     */
    public Player getLocalPlayer() {
        return localPlayer;
    }

    /**
     * Retrieves the game status
     *
     * @return the game status
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Retrieves the turn status
     *
     * @return the turn status
     */
    public TurnStatus getTurnStatus() {
        return turnStatus;
    }

    /**
     * Allows to deliver message to the View to be processed asynchronously.
     *
     * @param message ServerMessage that the View needs to process
     */
    public void forwardMessage(ServerMessage message) {
        try {
            this.messageQueue.put(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // setController is only used by RMI, and it is actually the only non-asynchronous RMI method
    // thus, this "message" from the server is actually processed directly in the ViewWrapper
    // and the View doesn't need to implement this method.
    @Override
    public void setController(VirtualController controller) {}


    /**
     * Allows to set the client's playerIdentifier.
     * The view will deliver the playerIdentifier to the ServerHandler
     * that will put it in every outgoing message.
     *
     * @param playerIdentifier the playerIdentifier generated by the server for this client
     */
    @Override
    public void setPlayerIdentifier(String playerIdentifier) {
        Client.getInstance().getServerHandler().setPlayerIdentifier(playerIdentifier);
    }

    /**
     * Allows to set the client's starting card.
     *
     * @param card the StartCard object representing the player starting card.
     */
    @Override
    public void setStartingCard(StartCard card) {
        localPlayer.setStartCard(card);
    }

    /**
     * Allows to set the common goals for the game the player's playing
     *
     * @param goals a list of Goal objects that represents the public goals for the current game.
     */
    @Override
    public void setCommonGoals(Goal[] goals) {
        gameModel.setCommonGoals(goals);
    }

    /**
     * Allows to set the available private goal for the local player
     *
     * @param goals a list of Goal object that represents the available private goals.
     */
    @Override
    public void setAvailablePrivateGoals(Goal[] goals) {
        localPlayer.setAvailableGoals(goals);
    }

    /**
     * Allows to set the definitive private goal for the local player
     *
     * @param goal the Goal object representing the player definitive private Goal.
     */
    @Override
    public void setDefinitivePrivateGoal(Goal goal) {
        localPlayer.setPrivateGoal(goal);

        if(this.localPlayer.getPlacedCardSlot(new CardLocation(0,0)) == null) {
            this.userInterface.setPlaceStartScene();
        }
        else {
            this.userInterface.setPlayerBoardScene(localPlayer);
        }
    }

    /**
     * Allows to update one of the player's cards
     *
     * @param playerNickname the nickname of the player whose board needs to be updated.
     * @param card           the PlayCard object representing the new card to put in the player's hand.
     * @param index          the index of the player's hand array that needs to be updated
     */
    @Override
    public void setPlayersCard(String playerNickname, PlayCard card, int index) {
        for (Player p : gameModel.getPlayers()) {
            if (p.nickname.equals(playerNickname)) {
                p.setPlayerCard(card, index);
                break;
            }
        }
    }

    /**
     * Allows to set one of the visible cards for the game that the local player's playing.
     *
     * @param card  the PlayCard object representing the new visible card.
     * @param index the index of the visible card to change.
     */
    @Override
    public void setVisibleCard(PlayCard card, int index) {
        PlayCard[] visibleCards = gameModel.getVisibleCards();
        if (visibleCards == null) {
            gameModel.resetVisibleCards(index + 1);
            PlayCard[] newVisibleCards = gameModel.getVisibleCards();
            newVisibleCards[index] = card;
        } else if (index >= visibleCards.length) {
            visibleCards = visibleCards.clone();
            gameModel.resetVisibleCards(index + 1);
            PlayCard[] newVisibleCards = gameModel.getVisibleCards();

            System.arraycopy(visibleCards, 0, newVisibleCards, 0, visibleCards.length);
            newVisibleCards[index] = card;
        } else {
            visibleCards[index] = card;
        }
    }

    /**
     * Allows to update one of the decks in the game that the local player's playing.
     *
     * @param resource the Resource object representing the resource on the back of the card on top of the deck.
     * @param index    the deckID that identify the deck that needs to be updated.
     */
    @Override
    public void setDeckTopResource(Resource resource, int index) {
        VirtualDeck deck;
        if (index == 0) {
            deck = (VirtualDeck) gameModel.getResourceCardsDeck();
        } else {
            deck = (VirtualDeck) gameModel.getGoldCardsDeck();
        }

        deck.setTopOfTheStack(resource);
    }

    /**
     * Allows to place a card on a player's board.
     *
     * @param playerNickname the nickname of the player whose board needs to be updated.
     * @param cardSlot       the CardSlot that contains the card that was placed.
     * @param location       the location where the CardSlot needs to be placed.
     */
    @Override
    public void placeCardOnPlayersBoard(String playerNickname, CardSlot cardSlot, CardLocation location) {
        boolean found = false;

        for (Player p : gameModel.getPlayers()) {
            if (p.nickname.equals(playerNickname)) {
                // player was found
                if (location.equals(new CardLocation(0, 0))) {
                    // the player has placed the starting card
                    // placing the start card
                    p.setStartCard((StartCard) cardSlot.card());
                    p.placeStartingCard(cardSlot.onBackSide());
                } else {
                    // the player has place a Play card (golden/resource)
                    // placing the card
                    p.placeCard(cardSlot.card(), cardSlot.onBackSide(), location);
                }
                found = true;
                break;
            }
        }

        // the player wasn't found
        if(!found) throw new RuntimeException("unknown player");

        // if the player that placed the card is the local player and if the local player still
        // needs to select the private goal, the private goal selection interface is displayed
        if(playerNickname.equals(this.localPlayer.nickname) && this.gameStatus == GameStatus.GAME_CREATION & this.localPlayer.getPrivateGoal() == null) {
            this.userInterface.setSelectGoalScene();
        }
    }

    /**
     * Allows to provide the set of available games that the local player can join
     *
     * @param availableGames a set of integer, each integer represents the ID of an available game
     */
    @Override
    public void updateGameList(Set<String> availableGames) {
        this.availableGames = new HashSet<>(availableGames);
    }

    /**
     * Allows to confirm that the local player has successfully joined a game.
     * It also allows to provide game data that to rebuild the local model.
     *
     * @param nickname the nickname that the clients chose when they joined the game.
     * @param savings  if the player is rejoining a game, this ClientGameData object contains all data that the player need
     */
    @Override
    public void confirmJoin(String nickname, GameData savings) {
        this.localPlayer = new Player(nickname, null);

        // load data if provided
        if (savings != null) {
            this.gameModel = new Game(savings);
        }

        // "extracting" a reference to the local player
        List<Player> currPlayer = gameModel.getPlayers();
        if (currPlayer != null) {
            for (Player player : currPlayer) {
                if (player.nickname.equals(localPlayer.nickname)) {
                    this.localPlayer = player;
                    break;
                }
            }
        }

        // if game data was provided, the user interface needs to display the appropriate scene, depending on the current game phase
        if(savings != null) {
            if(localPlayer.getBoard().get(new CardLocation(0,0)) == null) this.userInterface.setPlaceStartScene();
            else if (localPlayer.getPrivateGoal() == null) this.userInterface.setSelectGoalScene();
            else this.userInterface.setPlayerBoardScene(localPlayer);
        }
        else this.userInterface.setWaitPlayersScene();
    }

    /**
     * Allows to update the player list in the local model.
     *
     * @param nicknames the array of nicknames of the players in the game.
     * @param colors    the array of colors of the players in the game.
     */
    @Override
    public void updatePlayersList(String[] nicknames, PlayerColor[] colors) {
        gameModel.resetPlayers();

        // creating new players to match the updated list
        for (int i = 0; i < nicknames.length; i++) {
            gameModel.addPlayer(new Player(nicknames[i], null));
            gameModel.getPlayers().get(i).setColor(colors[i]);

            if (localPlayer != null && nicknames[i].equals(localPlayer.nickname)) {

                this.localPlayer = gameModel.getPlayers().get(i);
            }
        }

        if(this.localPlayer != null && this.localPlayer.getColor() != null) {
            this.userInterface.setWaitPlayersScene();
        }

        this.gameModel.updateAvailableColors();
    }

    /**
     * Allows to update the game status data.
     *
     * @param gameStatus  the current game phase
     * @param turnStatus  the current turn status (either DRAW or PLACE)
     * @param playersTurn the nickname of the player that needs to play.
     */
    @Override
    public void updateGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playersTurn) {
        if(this.gameStatus != gameStatus) {
            // the game status has changed
            switch (gameStatus) {
                default: break;
                case GAME_CREATION: // game started
                    this.gameModel.setupScoreBoard();
                    if(localPlayer.getPlacedCardSlot(new CardLocation(0,0)) == null) this.userInterface.setPlaceStartScene();
                    break;
                case NORMAL_TURN: // first turn started
                    this.userInterface.setPlayerBoardScene(localPlayer);
                    break;
                case END:
                    this.userInterface.setScoreScene();
                    break;
            }
        }

        // updating local variables
        this.gameStatus = gameStatus;
        this.turnStatus = turnStatus;
        this.playersTurn = playersTurn;

        // forwarding the status update to the user interface
        this.userInterface.setGameStatus(gameStatus, turnStatus, playersTurn);
    }


    /**
     * Allows to report and error to the local player
     *
     * @param exception a RuntimeException containing the error that needs to be reported.
     */
    @Override
    public void reportError(RuntimeException exception) {
        this.userInterface.reportError(exception);
    }

    /**
     * Allows to update the local score board
     *
     * @param scoreBoard the updated scoreboard
     */
    @Override
    public void updateScore(ScoreBoard scoreBoard) {
        this.gameModel.getScoreBoard().copyScore(scoreBoard);
    }

    /**
     * Allows to process a ping message.
     *
     * @param isAnswer {@code true} if the server is answering to a previous ping message, {@code false} if the server is checking on the client.
     */
    @Override
    public void ping(boolean isAnswer) {
        if (!isAnswer) Client.getInstance().getServerHandler().sendMessage(new ClientToServerPingMessage(true));

        // TODO handle server "disconnection"
    }

    /**
     * Allows to forward a chat message to the local player.
     * The message can be either public or private.
     * The message can also be sent from the local player: this serves
     * as an ack message that confirms that the message has been successfully delivered.
     *
     * @param chatMessage the ChatMessage object that contains info about the message
     * @param isPrivate {@code true} if the message is private, {@code false} otherwise
     */
    @Override
    public void sendChatMsg(ChatMessage chatMessage, boolean isPrivate) {
        if (isPrivate) {
            String remoteNickname;
            if (chatMessage.getSenderNick().equals(this.localPlayer.nickname)) {
                remoteNickname = chatMessage.getReceiverNick();
            } else {
                remoteNickname = chatMessage.getSenderNick();
            }

            this.gameModel.getChat().appendMessage(chatMessage, remoteNickname, this.localPlayer.nickname);
        } else this.gameModel.getChat().appendMessage(chatMessage, null, null);
    }

    /**
     * Retrieves the local representation of the available games
     *
     * @return the set of available games (might be outdated)
     */
    public HashSet<String> getAvailableGames() {
        return availableGames;
    }


    /**
     * Retrieves the local representation of the available colors to choose from for
     * the game that the local player's playing.
     *
     * @return the set of available colors (might be outdated)
     */
    public Set<PlayerColor> getAvailableColors() {
        return gameModel.getAvailableColor();
    }

    /**
     * Retrieves the list of players for the game the local player's playing
     *
     * @return the list of players playing the game the local player's playing (might be outdated)
     */
    public List<Player> getPlayersList() {
        return gameModel.getPlayers();
    }

    /**
     * Retrieves the nickname of the local player
     *
     * @return the nickname of the local player, {@code null} if the local player isn't set yet
     */
    public String getLocalPlayerName() {
        if(localPlayer == null) return null;
        return localPlayer.nickname;
    }

    /**
     * Retrieves the local game model
     *
     * @return the game model (might be outdated)
     */
    public Game getGameModel() {
        return gameModel;
    }

    /**
     * Retrieves the nickname of the player that needs to play.
     *
     * @return the nickname of the player that needs to play
     */
    public String getPlayersTurn() {
        return this.playersTurn;
    }
}
