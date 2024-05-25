package it.polimi.ingsw.view.ui;

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
import it.polimi.ingsw.model.saving.ClientGameSaving;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class UserInterface extends Thread implements VirtualView {
    protected Game gameModel;
    private Player localPlayer;

    protected HashSet<Integer> availableGames;

    private GameStatus gameStatus;
    private TurnStatus turnStatus;
    private String playersTurn;
    private int selectedside;



    private final BlockingQueue<ServerMessage> messageQueue;
    private final BlockingQueue<ServerMessage> gameMessages;

    public UserInterface() {
        messageQueue = new ArrayBlockingQueue<>(100);
        gameMessages = new ArrayBlockingQueue<>(100);

        try {
            gameModel = new Game(
                    new VirtualDeckLoader<>(),
                    new VirtualDeckLoader<>(),
                    null,
                    null,
                    -1,
                    -1
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Thread(
                () -> {
                    ServerMessage message;

                    while (true) {
                        try {
                            message = messageQueue.take();

                            if (message.messageType == MessageType.PRIVATE_MODEL_UPDATE_MESSAGE || message.messageType == MessageType.MODEL_UPDATE_MESSAGE) {
                                this.gameMessages.put(message);
                                continue;
                            }

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        synchronized (this) {
                            message.updateView(this);
                        }

                        if (message.messageType == MessageType.CONNECT_JOIN_MESSAGE ||
                                message.messageType == MessageType.SERVER_ERROR_MESSAGE ||
                                message.messageType == MessageType.CHAT_MESSAGE ||
                                message.messageType == MessageType.GAME_LIST_MESSAGE) {
                            this.update();
                        }
                    }
                }
        ).start();

        new Thread(
                () -> {
                    ServerMessage message;

                    while (true) {
                        try {
                            if (this.localPlayer != null) {
                                message = gameMessages.take();
                            } else {
                                Thread.sleep(100);
                                continue;
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        synchronized (this) {
                            message.updateView(this);
                        }
                        this.update();
                    }
                }
        ).start();
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public TurnStatus getTurnStatus() {
        return turnStatus;
    }

    public String getPlayersTurn() {
        return playersTurn;
    }

    public void forwardMessage(ServerMessage message) {
        try {
            this.messageQueue.put(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public abstract void update();


    @Override
    public void setController(VirtualController controller) throws RemoteException {
    }


    @Override
    public void setPlayerIdentifier(String playerIdentifier) throws RemoteException {
        //System.err.println("playerIdentifier received: " + playerIdentifier);
        Client.getInstance().getServerHandler().setPlayerIdentifier(playerIdentifier);
    }

    @Override
    public void setStartingCard(StartCard card) throws RemoteException {
        //System.err.println("You received the starting card");
        //System.err.println(card);
        //System.err.println();

        localPlayer.setStartCard(card);
    }

    @Override
    public void setPublicGoal(Goal[] goals) throws RemoteException {
        //System.err.println("You received the public goals");
        //System.err.println(Arrays.toString(goals));
        //System.err.println();

        gameModel.setCommonGoals(goals);
    }

    @Override
    public void setAvailablePrivateGoals(Goal[] goals) throws RemoteException {
        //System.err.println("You received the available private goals");
        //System.err.println(Arrays.toString(goals));
        //System.err.println();

        localPlayer.setAvailableGoals(goals);
    }

    @Override
    public void setDefinitivePrivateGoal(Goal goal) throws RemoteException {
        //System.err.println("You received your definitive private goal");
        //System.err.println(goal);
        //System.err.println();

        localPlayer.setPrivateGoal(goal);
    }

    @Override
    public void setPlayersCard(String playerNickname, PlayCard card, int index) throws RemoteException {
        //System.err.println(playerNickname+" received a card n°"+index);
        //System.err.println(card);
        //System.err.println();

        for (Player p : gameModel.getPlayers()) {
            if (p.nickName.equals(playerNickname)) {
                p.setPlayerCard(card, index);
                break;
            }
        }
    }

    @Override
    public void setVisibleCard(PlayCard card, int index) throws RemoteException {
        //System.err.println("New visible card n°"+index);
        //System.err.println(card);
        //System.err.println();

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

    @Override
    public void setDeckTopResource(Resource resource, int index) throws RemoteException {
        //String deckName = (index == 0 ? "resource" : "gold");
        //System.err.println("New card on top of the "+deckName+" card deck");
        //System.err.println(resource.toString());
        //System.err.println();

        VirtualDeck<PlayCard> deck;
        if (index == 0) {
            deck = (VirtualDeck<PlayCard>) gameModel.getResourceCardsDeck();
        } else {
            deck = (VirtualDeck<PlayCard>) gameModel.getGoldCardsDeck();
        }

        deck.setTopOfTheStack(resource);
    }

    @Override
    public void placeCardOnPlayersBoard(String playerNickname, CardSlot cardSlot, CardLocation location) throws RemoteException {
        //System.err.println(playerNickname+" placed a cardSlot in " + location);
        //System.err.println(cardSlot);
        //System.err.println();

        for (Player p : gameModel.getPlayers()) {
            if (p.nickName.equals(playerNickname)) {
                if (location.equals(new CardLocation(0, 0))) {
                    try {
                        p.setStartCard((StartCard) cardSlot.card());
                        p.placeStartingCard(cardSlot.onBackSide());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    int i = 0;
                    for (PlayCard c : p.getPlayerCards()) {
                        if (c != null && c.equals(cardSlot.card())) {
                            break;
                        }
                        i++;
                    }

                    if (i == p.getPlayerCards().length)
                        throw new RuntimeException("Card not found in player's hand in local model");

                    p.placeCard(i, cardSlot.onBackSide(), location);

                }
                break;
            }
        }
    }

    @Override
    public void updateGameList(Set<Integer> availableGames) throws RemoteException {
        //System.err.println("Available games: " + availableGames);

        this.availableGames = new HashSet<>(availableGames);
    }

    @Override
    public void confirmJoin(String nickname, ClientGameSaving savings) throws RemoteException {
        //System.err.println("Successfully joined the game as " + nickname);

        this.localPlayer = new Player(nickname, null);

        if (savings != null) {
            this.gameModel = new Game(savings);
        }

        List<Player> currPlayer = gameModel.getPlayers();
        if (currPlayer != null) {
            for (Player player : currPlayer) {
                if (player.nickName.equals(localPlayer.nickName)) {
                    this.localPlayer = player;
                    break;
                }
            }
        }
    }

    @Override
    public void updatePlayersList(String[] nicknames, PlayerColor[] colors) throws RemoteException {
        //System.err.println("Players list: " + Arrays.toString(nicknames));

        gameModel.resetPlayers();

        for (int i = 0; i < nicknames.length; i++) {
            gameModel.addPlayer(new Player(nicknames[i], null));
            gameModel.getPlayers().get(i).setColor(colors[i]);

            if (localPlayer != null && nicknames[i].equals(localPlayer.nickName)) {

                this.localPlayer = gameModel.getPlayers().get(i);
            }
        }


        this.gameModel.updateAvailableColors();
    }

    @Override
    public void updateGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playersTurn) throws RemoteException {
        if (this.gameStatus != GameStatus.GAME_CREATION && gameStatus == GameStatus.GAME_CREATION) {
            // NOTE: more or less same to assume that the GAME_CREATION update message won't
            // be received before joining messages
            this.gameModel.setupScoreBoard();
        }

        this.gameStatus = gameStatus;
        this.turnStatus = turnStatus;
        this.playersTurn = playersTurn;
    }

    @Override
    public void updateScore(ScoreBoard scoreBoard) throws RemoteException {
        this.gameModel.getScoreBoard().copyScore(scoreBoard);
    }

    @Override
    public void ping(boolean isAnswer) throws RemoteException {
        if (!isAnswer) Client.getInstance().getServerHandler().sendMessage(new ClientToServerPingMessage(true));

        // TODO handle server "disconnection"
    }

    @Override
    public void receiveMessage(ChatMessage chatMessage, boolean isPrivate) throws RemoteException {
        if (isPrivate) {
            String remoteNickname;
            if (chatMessage.getSenderNick().equals(this.localPlayer.nickName)) {
                remoteNickname = chatMessage.getReceiverNick();
            } else {
                remoteNickname = chatMessage.getSenderNick();
            }

            this.gameModel.getChat().appendMessage(chatMessage, remoteNickname, this.localPlayer.nickName);
        } else this.gameModel.getChat().appendMessage(chatMessage, null, null);
    }

    public HashSet<Integer> getAvailableGames() {
        return availableGames;
    }

    public Set<PlayerColor> getAvailableColors() {
        return gameModel.getAvailableColor();
    }
    public List<Player> getPlayersList() {
        return gameModel.getPlayers();
    }
    public String getLocalPlayerName() {
        return localPlayer.nickName;
    }
    public int getSelectedside() {
        return selectedside;
    }
    public void setSelectedside(int selectedside) {
        this.selectedside = selectedside;
    }
    public Game getGameModel() {
        return gameModel;
    }
}
