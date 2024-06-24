package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.messages.client.ClientMessage;
import it.polimi.ingsw.events.messages.client.ClientToServerPingMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.decks.GoalDeckLoader;
import it.polimi.ingsw.model.decks.PlayCardDeckLoader;
import it.polimi.ingsw.model.decks.StartCardDeckLoader;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.cliendhandlers.ClientHandler;
import it.polimi.ingsw.network.cliendhandlers.RMIClientHandler;
import it.polimi.ingsw.network.rmi.GameControllerWrapper;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ui.gui.GraphicalUserInterface;
import it.polimi.ingsw.view.ui.tui.TextualUserInterface;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {
    private String createId(){
        //create a new player identifier
        Random rand = new Random();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String t = timestamp.toString() + rand.nextInt(0, 1000000);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        md.update(t.getBytes());
        byte[] digest = md.digest();

        String playerIdentifier = String.format("%032X", new BigInteger(1, digest));
        return playerIdentifier;
    }

    private ClientHandler createCl(){
        return new RMIClientHandler(new View(new GraphicalUserInterface()));
    }
    private Game createGame(String id, int ep) throws IOException {
        GoalDeckLoader goalDeckLoader = new GoalDeckLoader("src/main/resources/json/goals.json");
        PlayCardDeckLoader resourceCardDeckLoader = new PlayCardDeckLoader("src/main/resources/json/cards/resourcecards.json");
        PlayCardDeckLoader goldCardDeckLoader = new PlayCardDeckLoader("src/main/resources/json/cards/goldcards.json");
        StartCardDeckLoader startCardDeckLoader = new StartCardDeckLoader("src/main/resources/json/cards/startcards.json");
        return new Game(goldCardDeckLoader, resourceCardDeckLoader, startCardDeckLoader, goalDeckLoader, id, ep);

    }

    @Test
    void getInstance() {
        //the method returns always the same object
        MainController mc=MainController.getInstance();
        MainController mc2=MainController.getInstance();
        assertEquals(mc,mc2);

    }

    @Test
    void forwardMessage() {
        MainController mc=MainController.getInstance();

        //add 5 messages to the message queue
        for(int i=0; i<5;i++) mc.forwardMessage(new ClientToServerPingMessage(false));

        assertTrue(true);
    }

    @Test
    void removeGame() throws IOException, NoSuchFieldException, IllegalAccessException {
        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        MainController mc= MainController.getInstance();

        //not best practice but i had no way to check if the game was actually added and then deleted
        Field privateValueField = MainController.class.getDeclaredField("gameControllerWrappers");
        privateValueField.setAccessible(true);

        Map<String, GameControllerWrapper> gameControllerWrappers=(Map<String, GameControllerWrapper>)privateValueField.get(mc);

        gameControllerWrappers.put(g.getIdGame(),new GameControllerWrapper(gc));

        assertTrue(gameControllerWrappers.containsKey(g.getIdGame()));
        mc.removeGame(g.getIdGame());

        assertFalse(gameControllerWrappers.containsKey(g.getIdGame()));


    }

    @Test
    void getGameController() throws Exception {

        MainController mc =MainController.getInstance();

        //not best practice but i had no way to check if the game-gamewrapper was actually added
        Field privateValueField = MainController.class.getDeclaredField("gameControllerWrappers");
        privateValueField.setAccessible(true);
        Map<String, GameControllerWrapper> gameControllerWrappers=(Map<String, GameControllerWrapper>)privateValueField.get(mc);

        //create and save the games and gamecontrollers
        Game[] games= new Game[2];
        GameController[] gameControllers= new GameController[2];

        //creating the first game
        //--------------------------------------------------------------------------------
        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        mc.connectClient(cl);
        g.setupScoreBoard();
        //waiting for the other player to join
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        assertEquals(GameStatus.LOBBY,gc.getGameStatus());
        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.forceUpdateStatus();
        assertEquals(GameStatus.GAME_CREATION,gc.getGameStatus());
        //normal turn of the game
        p1.setStartCard(g.getStartCardsDeck().draw());
        p1.setPrivateGoal(g.getGoalsDeck().draw());
        p1.placeStartingCard(false);
        p2.setStartCard(g.getStartCardsDeck().draw());
        p2.setPrivateGoal(g.getGoalsDeck().draw());
        p2.placeStartingCard(true);
        gc.forceUpdateStatus();
        games[0]=g;
        gameControllers[0]=gc;
        gameControllerWrappers.put(g.getIdGame(), new GameControllerWrapper(gc));
        //crea il secondo gioco
        //-----------------------------------------------
        Game g2 = createGame("2",2);
        GameController gc2=new GameController(g);
        ClientHandler cl3 = createCl();
        mc.connectClient(cl3);
        g2.setupScoreBoard();
        //waiting for the other player to join
        Player p3= new Player("mario",cl3);
        p3.setColor(PlayerColor.BLUE);
        g2.addPlayer(p3);
        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl4 = createCl();
        mc.connectClient(cl4);
        Player p4= new Player("massimo",cl4);
        p4.setColor(PlayerColor.YELLOW);
        g2.addPlayer(p4);
        games[1]=g2;
        gameControllers[1]=gc2;
        gameControllerWrappers.put(g2.getIdGame(), new GameControllerWrapper(gc2));

        assertEquals(gameControllers[0].getGameStatus(),mc.getGameController(games[0].getIdGame()).getGameStatus());
        assertEquals(gameControllers[0].getTurnStatus(),mc.getGameController(games[0].getIdGame()).getTurnStatus());
        assertEquals(gameControllers[1].getGameStatus(),mc.getGameController(games[1].getIdGame()).getGameStatus());
        assertEquals(gameControllers[1].getTurnStatus(),mc.getGameController(games[1].getIdGame()).getTurnStatus());

        assertThrows(NullPointerException.class,()->mc.getGameController("FAKEIDTEST"));


    }

    @Test
    void getPlayersGameController() throws Exception {
        MainController mc =MainController.getInstance();

        //not best practice but i had no way to check if the player-gamecontoller was actually added
        Field privateValueField = MainController.class.getDeclaredField("playerIdentifierToGameController");
        privateValueField.setAccessible(true);
        Map<String, GameController> playerIdentifierToGameController=(Map<String, GameController>)privateValueField.get(mc);

        //create and save the games and gamecontrollers
        Game[] games= new Game[2];
        GameController[] gameControllers= new GameController[2];

        //creating the first game
        //--------------------------------------------------------------------------------
        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        mc.connectClient(cl);
        g.setupScoreBoard();
        //waiting for the other player to join
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        assertEquals(GameStatus.LOBBY,gc.getGameStatus());
        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.forceUpdateStatus();
        assertEquals(GameStatus.GAME_CREATION,gc.getGameStatus());
        //normal turn of the game
        p1.setStartCard(g.getStartCardsDeck().draw());
        p1.setPrivateGoal(g.getGoalsDeck().draw());
        p1.placeStartingCard(false);
        p2.setStartCard(g.getStartCardsDeck().draw());
        p2.setPrivateGoal(g.getGoalsDeck().draw());
        p2.placeStartingCard(true);
        gc.forceUpdateStatus();
        playerIdentifierToGameController.put(cl.getPlayerIdentifier(),gc );
        playerIdentifierToGameController.put(cl2.getPlayerIdentifier(),gc );
        //crea il secondo gioco
        //-----------------------------------------------
        Game g2 = createGame("2",2);
        GameController gc2=new GameController(g);
        ClientHandler cl3 = createCl();
        mc.connectClient(cl3);
        g2.setupScoreBoard();
        //waiting for the other player to join
        Player p3= new Player("mario",cl3);
        p3.setColor(PlayerColor.BLUE);
        g2.addPlayer(p3);
        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl4 = createCl();
        mc.connectClient(cl4);
        Player p4= new Player("massimo",cl4);
        p4.setColor(PlayerColor.YELLOW);
        g2.addPlayer(p4);
        playerIdentifierToGameController.put(cl3.getPlayerIdentifier(),gc2);
        playerIdentifierToGameController.put(cl4.getPlayerIdentifier(),gc2);

        assertEquals(gc,mc.getPlayersGameController(cl.getPlayerIdentifier()));
        assertEquals(gc2,mc.getPlayersGameController(cl3.getPlayerIdentifier()));
        assertNotEquals(gc,mc.getPlayersGameController(cl3.getPlayerIdentifier()));
        assertNull(mc.getPlayersGameController("FAKEIDTEST"));



    }

    @Test
    void getPlayersClientHandler() throws IOException {
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        MainController mc= MainController.getInstance();
        mc.connectClient(cl);
        g.setupScoreBoard();
        //waiting for the other player to join
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        // the game is started, but it's before placing the first card


        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.forceUpdateStatus();

        assertEquals(mc.getPlayersClientHandler(cl.getPlayerIdentifier()),cl);
        assertNull(mc.getPlayersClientHandler("FAKEIDTEST"));

    }

    @Test
    void getPlayer() throws Exception {
        MainController mc =MainController.getInstance();

        //not best practice but i had no way to check if the player-gamecontoller was actually added
        Field privateValueField = MainController.class.getDeclaredField("playerIdentifierToGameController");
        privateValueField.setAccessible(true);
        Map<String, GameController> playerIdentifierToGameController=(Map<String, GameController>)privateValueField.get(mc);


        //creating the first game
        //--------------------------------------------------------------------------------
        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        mc.connectClient(cl);
        g.setupScoreBoard();
        //waiting for the other player to join
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        assertEquals(GameStatus.LOBBY,gc.getGameStatus());
        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.forceUpdateStatus();
        assertEquals(GameStatus.GAME_CREATION,gc.getGameStatus());
        //normal turn of the game
        p1.setStartCard(g.getStartCardsDeck().draw());
        p1.setPrivateGoal(g.getGoalsDeck().draw());
        p1.placeStartingCard(false);
        p2.setStartCard(g.getStartCardsDeck().draw());
        p2.setPrivateGoal(g.getGoalsDeck().draw());
        p2.placeStartingCard(true);
        gc.forceUpdateStatus();
        playerIdentifierToGameController.put(cl.getPlayerIdentifier(),gc );
        playerIdentifierToGameController.put(cl2.getPlayerIdentifier(),gc );
        //crea il secondo gioco
        //-----------------------------------------------
        Game g2 = createGame("2",2);
        GameController gc2=new GameController(g);
        ClientHandler cl3 = createCl();
        mc.connectClient(cl3);
        g2.setupScoreBoard();
        //waiting for the other player to join
        Player p3= new Player("mario",cl3);
        p3.setColor(PlayerColor.BLUE);
        g2.addPlayer(p3);
        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl4 = createCl();
        mc.connectClient(cl4);
        Player p4= new Player("massimo",cl4);
        p4.setColor(PlayerColor.YELLOW);
        g2.addPlayer(p4);
        playerIdentifierToGameController.put(cl3.getPlayerIdentifier(),gc2);
        playerIdentifierToGameController.put(cl4.getPlayerIdentifier(),gc2);


        assertEquals(p1,mc.getPlayer(cl.getPlayerIdentifier()));
        assertEquals(p2,mc.getPlayer(cl2.getPlayerIdentifier()));
        assertNotEquals(p3,mc.getPlayer(cl.getPlayerIdentifier()));
        assertNull(mc.getPlayer("FAKEIDTEST"));

    }

    @Test
    void run() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        MainController mc=MainController.getInstance();
        //get the message queue to see if the ping msg arrived
        Field privateValueField2 = MainController.class.getDeclaredField("messageQueue");
        privateValueField2.setAccessible(true);
        BlockingQueue<ClientMessage> messageQueue=(BlockingQueue<ClientMessage>)privateValueField2.get(mc);

        for(int i=0;i<5;i++) messageQueue.put(new ClientToServerPingMessage(false));

        Thread thread= new Thread(()->mc.run());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
        thread.join();

        assertEquals(0,messageQueue.size());




    }

    @Test
    void joinGame() {
        //todo:impossible to create without setting up the network
    }

    @Test
    void getAvailableGames() throws IOException, NoSuchFieldException, IllegalAccessException {
        MainController mc=MainController.getInstance();
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        mc.connectClient(cl);

        mc.getAvailableGames(cl.getPlayerIdentifier());

        //get the message queue to see if the ping msg arrived
        Field privateValueField2 = GameController.class.getDeclaredField("messageQueue");
        privateValueField2.setAccessible(true);
        BlockingQueue<ClientMessage> messageQueue=(BlockingQueue<ClientMessage>)privateValueField2.get(gc);

    }

    @Test
    void connect() throws RemoteException, NoSuchFieldException, IllegalAccessException {
        MainController mc =MainController.getInstance();
        //not best practice but i had no way to check if the player-gamecontoller was actually added
        Field privateValueField = MainController.class.getDeclaredField("playerIdentifierToClientHandler");
        privateValueField.setAccessible(true);
        Map<String, ClientHandler> playerIdentifierToClientHandler=(Map<String, ClientHandler>)privateValueField.get(mc);
        playerIdentifierToClientHandler.clear();

        mc.connect(new View(new TextualUserInterface(new Scanner(System.in))));


        assertEquals(1,playerIdentifierToClientHandler.size());
    }

    @Test
    void ping() throws IOException, NoSuchFieldException, IllegalAccessException {
        MainController mc=MainController.getInstance();
        ClientHandler cl = createCl();
        mc.connectClient(cl);
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        Field privateValueField = MainController.class.getDeclaredField("playerIdentifierToGameController");
        privateValueField.setAccessible(true);
        Map<String, GameController> playerIdentifierToGameController=(Map<String, GameController>)privateValueField.get(mc);
        playerIdentifierToGameController.put(cl.getPlayerIdentifier(),gc);
        mc.ping(cl.getPlayerIdentifier(),true);




    }

    @Test
    void leaveGame() throws NoSuchFieldException, IllegalAccessException, IOException {
        MainController mc=MainController.getInstance();
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        mc.connectClient(cl);
        ClientHandler cl2=createCl();
        mc.connectClient(cl2);

        //not best practice but i had no way to check if the player-gamecontoller was actually added
        Field privateValueField = MainController.class.getDeclaredField("playerIdentifierToGameController");
        privateValueField.setAccessible(true);
        Map<String, GameController> playerIdentifierToGameController=(Map<String, GameController>)privateValueField.get(mc);
        playerIdentifierToGameController.put(cl.getPlayerIdentifier(),gc);
        playerIdentifierToGameController.put(cl2.getPlayerIdentifier(),gc);
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.forceUpdateStatus();

        mc.leaveGame("FAKEPLAYERIDTEST");
        assertEquals(2,g.getPlayers().size());

        mc.leaveGame(cl.getPlayerIdentifier());
        Field privateValueField2 = GameController.class.getDeclaredField("connectedPlayers");
        privateValueField2.setAccessible(true);
        int connectedPlayers=(Integer) privateValueField2.get(gc);
        assertEquals(1,connectedPlayers);

    }

    @Test
    void createGame()  {
        //todo:impossible to create without setting up the network since it uses join game.
    }

    @Test
    void connectClient() throws IOException {
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        MainController mc= MainController.getInstance();
        mc.connectClient(cl);
        g.setupScoreBoard();
        //waiting for the other player to join
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        // the game is started, but it's before placing the first card


        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.forceUpdateStatus();

        assertEquals(mc.getPlayersClientHandler(cl.getPlayerIdentifier()),cl);

    }
}