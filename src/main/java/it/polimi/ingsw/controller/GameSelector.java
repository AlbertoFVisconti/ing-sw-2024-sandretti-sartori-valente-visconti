package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.decks.GoalDeckLoader;
import it.polimi.ingsw.model.decks.PlayCardDeckLoader;
import it.polimi.ingsw.model.decks.StartCardDeckLoader;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.events.messages.client.ClientMessage;
import it.polimi.ingsw.model.events.messages.server.JoinConfirmationMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientHandler;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class GameSelector extends Thread{
    private Map<Integer, GameController> Games;
    private Map<String, GameController> players;
    private int curr=0;

    private BlockingQueue<ClientMessage> messageQueue;

    GoalDeckLoader goalDeckLoader= new GoalDeckLoader("src/main/resources/json/goals.json");
    PlayCardDeckLoader resourceCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/resourcecards.json");
    PlayCardDeckLoader goldCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/goldcards.json");
    StartCardDeckLoader startCardDeckLoader= new StartCardDeckLoader("src/main/resources/json/cards/startcards.json");


    private GameSelector(){
        this.Games= new HashMap<>();
        this.players = new HashMap<>();
        this.messageQueue = new ArrayBlockingQueue<>(100);
        this.start();
    }

    private static GameSelector instance = null;
    public static GameSelector getInstance() {
        if(instance == null) {
            instance = new GameSelector();
        }
        return instance;
    }

    public void forwardMessage(ClientMessage message)  {
        try {
            if (message.messageType == MessageType.CONNECT_JOIN_MESSAGE) {
                this.messageQueue.put(message);
            } else {
                GameController controller = this.players.get(message.playerIdentifier);
                controller.forwardMessage(message);
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * returns true if there are no player with the same nickname in the selected game
     * @param game: the Game you want to check
     * @param Nickname: the nick you want to check
     * **/
    public static boolean isAvailable(Game game, String Nickname){
        List<Player> temp=game.getPlayers();
        for(Player p: temp){
            if(p.nickName.equals(Nickname))  return false;
        }
        return true;
    }
    public void RemoveGame(int idGame){
        Games.remove(idGame);
    }
    /**
     * @param expectedPlayers: the number of player expected for that game
     * **/
    public int CreateGame(int expectedPlayers) throws IOException {
        Game g=new Game(goldCardDeckLoader,resourceCardDeckLoader,startCardDeckLoader,goalDeckLoader, curr,expectedPlayers );

        GameController controller = new GameController(g);

        Games.put(curr,controller);

        return curr++;
    }

    public GameController getGameController(int gameId) {
        return this.Games.get(gameId);
    }

    public void addPlayer(int IDGame, String nickname, PlayerColor playerColor, ClientHandler clientHandler) throws Exception {
        GameController controller = this.Games.get(IDGame);

        Random rand = new Random();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String t = IDGame + nickname + timestamp + rand.nextInt(0, 1000);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        md.update(t.getBytes());
        byte[] digest = md.digest();

        String playerIdentifier = String.format("%032X", new BigInteger(1, digest));

        this.players.put(playerIdentifier, this.Games.get(IDGame));

        Game game = controller.getGame();
        if (!game.getAvailableColor().contains(playerColor)) throw new Exception("Color not available");
        game.getAvailableColor().remove(playerColor);
        game.addPlayer(new Player(playerIdentifier,nickname,playerColor, clientHandler));

        clientHandler.onUpdate(new JoinConfirmationMessage(playerIdentifier));


        System.err.println(nickname + " joined the game");

        controller.updateStatus();
    }

    public GameController getPlayersGame(String playerIdentifier) {
        return players.get(playerIdentifier);
    }

    public Player getPlayer(String playerIdentifier) {
        GameController controller = players.get(playerIdentifier);
        if(controller == null) return null;

        Game game = controller.getGame();

        for(Player p : game.getPlayers()) {
            if(Objects.equals(p.identifier, playerIdentifier)) {
                return p;
            }
        }
        return null;
    }

//    public void JoinGame(int idGame, int color, String Nickname) throws Exception{
//        Game game=Games.get(idGame).getGame();
//        if(game.getPlayers().size()==game.getExpectedPlayers()) throw new Exception("Game already full");
//        if (!isAvailable(game,Nickname)) throw new Exception("Nick already taken");
//        PlayerColor playerColor;
//        switch (color) {
//            case 1:
//                playerColor = PlayerColor.RED;
//                break;
//            case 2:
//                playerColor = PlayerColor.YELLOW;
//                break;
//            case 3:
//                playerColor = PlayerColor.GREEN;
//                break;
//            default:
//                playerColor = PlayerColor.BLUE;
//        }
//        if (!game.getAvailableColor().contains(playerColor)) throw new Exception("Color not available, choose another one");
//        game.getAvailableColor().remove(playerColor);
//        game.addPlayer(new Player(Nickname,playerColor));
//    }
    public Set<Integer> getAvailableGames(){
        return Games.keySet();
    }

    @Override
    public void run() {
        GameSelector selector = GameSelector.getInstance();
        while(true) {
            System.out.println("waiting...");
            try {
                this.messageQueue.take().execute(this, null);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
