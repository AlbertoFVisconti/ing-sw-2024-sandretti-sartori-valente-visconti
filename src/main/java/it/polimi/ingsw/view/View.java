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
import it.polimi.ingsw.model.saving.ClientGameSaving;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.ui.UserInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class View extends Thread implements VirtualView {
    private final UserInterface userInterface;

    private Game gameModel;
    private Player localPlayer;

    protected HashSet<Integer> availableGames;

    private GameStatus gameStatus;
    private TurnStatus turnStatus;
    private String playersTurn;
    private int selectedside=-1;



    private final BlockingQueue<ServerMessage> messageQueue;
    private final BlockingQueue<ServerMessage> gameMessages;

    public View(UserInterface userInterface) {
        this.userInterface = userInterface;
        messageQueue = new ArrayBlockingQueue<>(100);
        gameMessages = new ArrayBlockingQueue<>(100);

        userInterface.init();

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
                            this.userInterface.update();
                        }
                    }
                }
        ).start();

        new Thread(
                () -> {
                    ServerMessage message;

                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            if (this.localPlayer != null) {
                                message = gameMessages.take();
                            } else {
                                continue;
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        synchronized (this) {
                            message.updateView(this);
                        }
                        this.userInterface.update();
                    }
                }
        ).start();
    }

    public UserInterface getUserInterface() {
        return userInterface;
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

    public void forwardMessage(ServerMessage message) {
        try {
            this.messageQueue.put(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void setController(VirtualController controller) throws RemoteException {
    }


    @Override
    public void setPlayerIdentifier(String playerIdentifier) throws RemoteException {
        Client.getInstance().getServerHandler().setPlayerIdentifier(playerIdentifier);
    }

    @Override
    public void setStartingCard(StartCard card) throws RemoteException {
        localPlayer.setStartCard(card);
    }

    @Override
    public void setPublicGoal(Goal[] goals) throws RemoteException {
        gameModel.setCommonGoals(goals);
    }

    @Override
    public void setAvailablePrivateGoals(Goal[] goals) throws RemoteException {
        localPlayer.setAvailableGoals(goals);
    }

    @Override
    public void setDefinitivePrivateGoal(Goal goal) throws RemoteException {
        localPlayer.setPrivateGoal(goal);

        if(this.localPlayer.getPlacedCardSlot(new CardLocation(0,0)) == null) {
            this.userInterface.setPlaceStartScene();
        }
    }

    @Override
    public void setPlayersCard(String playerNickname, PlayCard card, int index) throws RemoteException {
        for (Player p : gameModel.getPlayers()) {
            if (p.nickName.equals(playerNickname)) {
                p.setPlayerCard(card, index);
                break;
            }
        }
    }

    @Override
    public void setVisibleCard(PlayCard card, int index) throws RemoteException {
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
        boolean found = false;

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

                    p.placeCard(cardSlot.card(), cardSlot.onBackSide(), location);
                }
                found = true;
                break;
            }
        }

        if(!found) throw new RuntimeException("unknown player");

        if(playerNickname.equals(this.localPlayer.nickName) && this.gameStatus == GameStatus.GAME_CREATION & this.localPlayer.getPrivateGoal() == null) {
            this.userInterface.setSelectGoalScene();
        }
    }

    @Override
    public void updateGameList(Set<Integer> availableGames) throws RemoteException {
        this.availableGames = new HashSet<>(availableGames);
    }

    @Override
    public void confirmJoin(String nickname, ClientGameSaving savings) throws RemoteException {
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

        if(savings != null) {
            if(localPlayer.getBoard().get(new CardLocation(0,0)) == null) this.userInterface.setPlaceStartScene();
            else if (localPlayer.getPrivateGoal() == null) this.userInterface.setSelectGoalScene();
            else this.userInterface.setPlayerBoardScene(localPlayer);
        }
        else this.userInterface.setWaitPlayersScene();
    }

    @Override
    public void updatePlayersList(String[] nicknames, PlayerColor[] colors) throws RemoteException {
        gameModel.resetPlayers();

        for (int i = 0; i < nicknames.length; i++) {
            gameModel.addPlayer(new Player(nicknames[i], null));
            gameModel.getPlayers().get(i).setColor(colors[i]);

            if (localPlayer != null && nicknames[i].equals(localPlayer.nickName)) {

                this.localPlayer = gameModel.getPlayers().get(i);
            }
        }

        if(this.localPlayer != null && this.localPlayer.getColor() != null) {
            this.userInterface.setWaitPlayersScene();
        }

        this.gameModel.updateAvailableColors();
    }

    @Override
    public void updateGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playersTurn) throws RemoteException {
        if(this.gameStatus != gameStatus) {
            switch (gameStatus) {
                default: break;
                case GAME_CREATION:
                    this.gameModel.setupScoreBoard();
                    if(localPlayer.getPlacedCardSlot(new CardLocation(0,0)) == null) this.userInterface.setPlaceStartScene();
                    break;
                case NORMAL_TURN:
                    this.userInterface.setPlayerBoardScene(localPlayer);
            }
        }


        this.gameStatus = gameStatus;
        this.turnStatus = turnStatus;
        this.playersTurn = playersTurn;

        this.userInterface.setGameStatus(gameStatus, turnStatus, playersTurn);
    }

    @Override
    public void reportError(RuntimeException exception) throws RemoteException {
        this.userInterface.reportError(exception);
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
    public void sendChatMsg(ChatMessage chatMessage, boolean isPrivate) throws RemoteException {
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

    public String getPlayersTurn() {
        return this.playersTurn;
    }
}
