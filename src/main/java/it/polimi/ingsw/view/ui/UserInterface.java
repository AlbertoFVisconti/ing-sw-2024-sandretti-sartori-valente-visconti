package it.polimi.ingsw.view.ui;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.decks.VirtualDeck;
import it.polimi.ingsw.model.decks.VirtualDeckLoader;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class UserInterface extends Thread implements VirtualView {
    private ServerHandler serverHandler;

    private String playerIdentifier = null;

    protected final Game gameModel;
    private Player localPlayer;

    protected HashSet<Integer> availableGames;

    private GameStatus gameStatus;
    private TurnStatus turnStatus;
    private String playersTurn;

    private final BlockingQueue<ServerMessage> messageQueue;
    public UserInterface() {
        messageQueue = new ArrayBlockingQueue<>(100);

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

                    while(true) {
                        try {
                            message = messageQueue.take();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        message.updateView(this);
                        this.update();

                    }
                }
        ).start();
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }
    public ServerHandler getServerHandler() {
        return serverHandler;
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


    abstract protected void update();

    public String getPlayerIdentifier() {
        return playerIdentifier;
    }


    /**
     * The setController call shouldn't reach this class under any circumstance.
     *
     * @param controller
     * @throws RemoteException
     */
    @Override
    public void setController(VirtualController controller) throws RemoteException {}


    @Override
    public void setPlayerIdentifier(String playerIdentifier) throws RemoteException {
        //System.err.println("playerIdentifier received: " + playerIdentifier);
        this.playerIdentifier = playerIdentifier;
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

        for(Player p : gameModel.getPlayers()) {
            if(p.nickName.equals(playerNickname)) {
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
        if(visibleCards == null) {
            gameModel.resetVisibleCards(index+1);
            PlayCard[] newVisibleCards = gameModel.getVisibleCards();
            newVisibleCards[index] = card;
        }
        else if(index >= visibleCards.length) {
            visibleCards = visibleCards.clone();
            gameModel.resetVisibleCards(index+1);
            PlayCard[] newVisibleCards = gameModel.getVisibleCards();

            System.arraycopy(visibleCards, 0, newVisibleCards, 0, visibleCards.length);
            newVisibleCards[index] = card;
        }
        else {
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
        if(index == 0) {
            deck = (VirtualDeck<PlayCard>)gameModel.getResourceCardsDeck();
        }
        else {
            deck = (VirtualDeck<PlayCard>)gameModel.getGoldCardsDeck();
        }

        deck.setTopOfTheStack(resource);
    }

    @Override
    public void placeCardOnPlayersBoard(String playerNickname, Card card, CardLocation location) throws RemoteException {
        //System.err.println(playerNickname+" placed a card in " + location);
        //System.err.println(card);
        //System.err.println();

        for(Player p : gameModel.getPlayers()) {
            if(p.nickName.equals(playerNickname)) {
                if(location.equals(new CardLocation(0,0))) {
                    try {
                        p.setStartCard((StartCard)card);
                        p.placeStartingCard(card.isOnBackSide());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    int i = 0;
                    for (PlayCard c : p.getPlayerCards()) {
                        if (c.equals(card)) {
                            break;
                        }
                        i++;
                    }

                    if (i == p.getPlayerCards().length)
                        throw new RuntimeException("Card not found in player's hand in local model");

                    p.placeCard(i, card.isOnBackSide(), location);

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
    public void confirmJoin(String nickname) throws RemoteException {
        //System.err.println("Successfully joined the game as " + nickname);

        this.localPlayer = new Player(nickname, null);

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

        for(int i = 0; i < nicknames.length; i++) {
            gameModel.addPlayer(new Player(nicknames[i], null));
            gameModel.getPlayers().get(i).setColor(colors[i]);

            if(localPlayer != null && nicknames[i].equals(localPlayer.nickName)) {

                this.localPlayer = gameModel.getPlayers().get(i);
            }
        }


        this.gameModel.updateAvailableColors();
    }

    @Override
    public void updateGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playersTurn) throws RemoteException {
        this.gameStatus = gameStatus;
        this.turnStatus = turnStatus;
        this.playersTurn = playersTurn;
    }

    @Override
    public void updateScore(ScoreBoard scoreBoard) throws RemoteException {
        this.gameModel.getScoreBoard().copyScore(scoreBoard);
    }

    public HashSet<Integer> getAvailableGames() {
        return availableGames;
    }
}
