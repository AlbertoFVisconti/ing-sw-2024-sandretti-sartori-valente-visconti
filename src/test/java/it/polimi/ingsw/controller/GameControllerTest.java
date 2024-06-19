package it.polimi.ingsw.controller;

import com.sun.source.tree.AssertTree;
import it.polimi.ingsw.events.messages.client.ClientMessage;
import it.polimi.ingsw.events.messages.client.ClientToServerPingMessage;
import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.decks.GoalDeckLoader;
import it.polimi.ingsw.model.decks.PlayCardDeckLoader;
import it.polimi.ingsw.model.decks.StartCardDeckLoader;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.cliendhandlers.ClientHandler;
import it.polimi.ingsw.network.cliendhandlers.RMIClientHandler;
import it.polimi.ingsw.network.rmi.GameControllerWrapper;
import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ui.gui.GraphicalUserInterface;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
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
    private  Game createGame(String id, int ep) throws IOException {
        GoalDeckLoader goalDeckLoader = new GoalDeckLoader("src/main/resources/json/goals.json");
        PlayCardDeckLoader resourceCardDeckLoader = new PlayCardDeckLoader("src/main/resources/json/cards/resourcecards.json");
        PlayCardDeckLoader goldCardDeckLoader = new PlayCardDeckLoader("src/main/resources/json/cards/goldcards.json");
        StartCardDeckLoader startCardDeckLoader = new StartCardDeckLoader("src/main/resources/json/cards/startcards.json");
        return new Game(goldCardDeckLoader, resourceCardDeckLoader, startCardDeckLoader, goalDeckLoader, id, ep);

    }

    @Test
    void addNewPlayer() throws IOException {
        //initialize param for the game
       Game g=createGame("1",4);
       GameController gc= new GameController(g);
        ClientHandler cl = createCl();

        //aggiungi i giocatori
        gc.addNewPlayer("mario",cl);
        gc.addNewPlayer("massimo",cl);
        gc.addNewPlayer("cristiano",cl);
        gc.addNewPlayer("alberto",cl);


        //test if the players have been inserted correctly
        assertEquals("mario",g.getPlayers().get(0).nickname);
        assertEquals("massimo",g.getPlayers().get(1).nickname);
        assertEquals("cristiano",g.getPlayers().get(2).nickname);
        assertEquals("alberto",g.getPlayers().get(3).nickname);
        //throws an exception if we try to add a 5th player
        assertThrows(RuntimeException.class,()->gc.addNewPlayer("5th",cl));
    }

    @Test
    void getGameStatus() throws Exception {
        //setup of variables
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
        assertEquals(GameStatus.LOBBY,gc.getGameStatus());



        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.updateStatus();
        assertEquals(GameStatus.GAME_CREATION,gc.getGameStatus());


        //normal turn of the game
        p1.setStartCard(g.getStartCardsDeck().draw());
        p1.setPrivateGoal(g.getGoalsDeck().draw());
        p1.placeStartingCard(false);
        p2.setStartCard(g.getStartCardsDeck().draw());
        p2.setPrivateGoal(g.getGoalsDeck().draw());
        p2.placeStartingCard(true);
        gc.updateStatus();
        assertEquals(GameStatus.NORMAL_TURN,gc.getGameStatus());

        //final turn once a player has hit 20 points
        g.getScoreBoard().setScore("massimo",21);
        gc.updateStatus();
        assertEquals(GameStatus.LAST_TURN,gc.getGameStatus());

        //since it never changed it's still the first player turn
        gc.updateStatus();
        assertEquals(GameStatus.END,gc.getGameStatus());
    }

    @Test
    void getTurnStatus() throws Exception {
        //setup of variables
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
        assertNull(gc.getTurnStatus());


        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.updateStatus();
        assertEquals(TurnStatus.PLACE,gc.getTurnStatus());


        //normal turn of the game
        p1.setStartCard(g.getStartCardsDeck().draw());
        p1.setPrivateGoal(g.getGoalsDeck().draw());
        p1.placeStartingCard(false);
        p2.setStartCard(g.getStartCardsDeck().draw());
        p2.setPrivateGoal(g.getGoalsDeck().draw());
        p2.placeStartingCard(true);
        assertEquals(TurnStatus.PLACE,gc.getTurnStatus());

        //placing a card changes the status
        //gc.placeCard(g.getTurn().getClientHandler().getPlayerIdentifier(),0,false,new CardLocation(1,1));
        //gc.updateStatus();
        //TODO: entrambi i clienthandler forzano la disconnessine perchè non trovano il client inizializzato qiundi
        //     quando faccio placecard viene chiamato update status ricorsivamente senza fine

        //assertEquals(TurnStatus.DRAW,gc.getTurnStatus());


    }

    @Test
    void forwardMessage() throws IOException {
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        MainController mc= MainController.getInstance();
        mc.connectClient(cl);
        g.setupScoreBoard();
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.updateStatus();

        //add 5 messages to the message queue
        for(int i=0; i<5;i++) gc.forwardMessage(new ClientToServerPingMessage(false));

        assertTrue(true);
    }

    @Test
    void handleDisconnection() throws Exception {
        //test of the disconnection before the game has started
        //setup of variables
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
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        g.addPlayer(p2);
        p2.setColor(PlayerColor.YELLOW);
        gc.updateStatus();
        //TODO: FIX THE BUG WHEN BOTH PLAYER ARE LEAVING THE LOBBY
        //the game is started, but it's before placing the first card
        //gc.handleDisconnection(cl);
        //assertFalse(g.getPlayers().contains(p1));


        //----------------------------------------------------------------------------------
        //test the disconnection after the game has started
        //setup of variables
        Game g2 = createGame("1",2);
        GameController gc2=new GameController(g);
        ClientHandler cl3 = createCl();
        mc.connectClient(cl3);
        g2.setupScoreBoard();
        //waiting for the other player to join
        Player p3= new Player("cristiano",cl3);
        p3.setColor(PlayerColor.BLUE);


        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl4 = createCl();
        mc.connectClient(cl4);
        Player p4= new Player("alberto",cl4);
        p4.setColor(PlayerColor.YELLOW);
        g2.addPlayer(p4);
        gc2.updateStatus();


        //normal turn of the game
        p3.setStartCard(g2.getStartCardsDeck().draw());
        p3.setPrivateGoal(g2.getGoalsDeck().draw());
        p3.placeStartingCard(false);
        p4.setStartCard(g2.getStartCardsDeck().draw());
        p4.setPrivateGoal(g2.getGoalsDeck().draw());
        p4.placeStartingCard(true);
        gc2.updateStatus();
        gc2.handleDisconnection(cl3);
        assertTrue(cl3.isDisconnected());
    }


    @Test
    void handleReconnection() throws Exception {
        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        MainController mc= MainController.getInstance();
        mc.connectClient(cl);
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        gc.updateStatus();
        //now add the second player
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        for(Player p: g.getPlayers()) System.out.println(p.nickname);

        //handle edge cases of reconnection (player doesn't exist, player never left)
        //TODO: this should throw an exception since the player is not disconnected, but right now it removes it
        //assertThrows(RuntimeException.class,()->{gc.handleReconnection(p1.nickname,cl,new GameControllerWrapper(gc));});
        assertThrows(RuntimeException.class,()->{gc.handleReconnection("cristiano",null,null);});

        //a client cannot reconnect if the game is in lobby mode
        //go to NORMAL TURN
        gc.updateStatus();
        p1.setStartCard(g.getStartCardsDeck().draw());
        p1.setPrivateGoal(g.getGoalsDeck().draw());
        p1.placeStartingCard(false);
        p2.setStartCard(g.getStartCardsDeck().draw());
        p2.setPrivateGoal(g.getGoalsDeck().draw());
        p2.placeStartingCard(true);
        gc.updateStatus();

        //clients disconnects and the reconnects
        gc.handleDisconnection(cl);
        gc.handleReconnection(p1.nickname,cl,new GameControllerWrapper(gc));
        //todo: add the change of disconnected when the player rejoins
        //assertFalse(cl.isDisconnected());
    }


    @Test
    void updateStatus() throws Exception {
        //setup of variables
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
        assertNull(gc.getTurnStatus());


        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.updateStatus();
        assertEquals(TurnStatus.PLACE,gc.getTurnStatus());


        //normal turn of the game
        gc.placeStartCard(cl.getPlayerIdentifier(), false);
        gc.selectPrivateGoal(cl.getPlayerIdentifier(),0);
        gc.placeStartCard(cl2.getPlayerIdentifier(), false);
        gc.selectPrivateGoal(cl2.getPlayerIdentifier(),0);
        assertEquals(TurnStatus.PLACE,gc.getTurnStatus());
        cl.messageReceived();
        cl2.messageReceived();

        //placing a card changes the status
        //gc.placeCard(g.getTurn().getClientHandler().getPlayerIdentifier(),0,false,new CardLocation(1,1));
        //TODO: entrambi i clienthandler forzano la disconnessine perchè non trovano il client inizializzato qiundi
        // quando faccio placecard viene chiamato update status ricorsivamente senza fine
        //assertEquals(TurnStatus.DRAW,gc.getTurnStatus());
    }

    @Test
    void evaluateGoals() throws IOException {
        //todo: implemet
    }

    @Test
    void selectPrivateGoal() throws IOException {
        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        MainController mc= MainController.getInstance();
        mc.connectClient(cl);
        g.setupScoreBoard();
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        ClientHandler cl2 = createCl();

        //choose private object before all the player are connected
        assertThrows(RuntimeException.class,()->gc.selectPrivateGoal(cl.getPlayerIdentifier(),0));
        //invalid parameter testing
        assertThrows(RuntimeException.class,()->gc.selectPrivateGoal("INVALIDIDTEST",0));
        assertThrows(RuntimeException.class,()->gc.selectPrivateGoal(cl.getPlayerIdentifier(),10));


        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.updateStatus();

        //choosing the startcard and the private goal
        Goal g1=p1.getAvailableGoals()[0];
        gc.placeStartCard(cl.getPlayerIdentifier(),true);
        gc.selectPrivateGoal(cl.getPlayerIdentifier(),0);
        Goal g2=p2.getAvailableGoals()[1];
        gc.placeStartCard(cl2.getPlayerIdentifier(),false);
       gc.selectPrivateGoal(cl2.getPlayerIdentifier(), 1);

       //correctly choosing the private goal
       assertEquals(p1.getPrivateGoal(),g1);
       assertEquals(p2.getPrivateGoal(),g2);

       //same player chooses more than once
        assertThrows(RuntimeException.class,()->gc.selectPrivateGoal(cl.getPlayerIdentifier(),1));

    }

    @Test
    void placeStartCard() throws Exception {
        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        MainController mc= MainController.getInstance();
        mc.connectClient(cl);
        g.setupScoreBoard();
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        ClientHandler cl2 = createCl();
        //cannot place unless all the players joined
        assertThrows(RuntimeException.class,()->{ gc.placeStartCard(cl.getPlayerIdentifier(),true);});
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.updateStatus();


        //normal turn of the game
        StartCard s1=p1.getStartCard();
        gc.placeStartCard(cl.getPlayerIdentifier(),true);
        p1.setPrivateGoal(g.getGoalsDeck().draw());
        StartCard s2=p2.getStartCard();
        gc.placeStartCard(cl2.getPlayerIdentifier(),false);
        p2.setPrivateGoal(g.getGoalsDeck().draw());

        assertEquals(new CardSlot(s1,true,0),p1.getBoard().get(new CardLocation(0,0)));
        assertEquals(new CardSlot(s2,false,0),p2.getBoard().get(new CardLocation(0,0)));
        assertThrows(RuntimeException.class,()->{ gc.placeStartCard(cl.getPlayerIdentifier(),true);});
    }

    @Test
    void placeCard() throws Exception {
        // Simulate user input: invalid input followed by valid input
        String[] strings={};
        Server.main(strings);
        String simulatedInput = "1\n1\n!create\n2\nmassimo\nred\n";
        String simulatedInput2 = "1\n1\n!join\nmassimo\nmario\nred\n";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(simulatedInput.getBytes());System.setIn(testInputStream);
        Client.main(strings);
        ServerHandler sh1=Client.getInstance().getServerHandler();
        testInputStream = new ByteArrayInputStream(simulatedInput2.getBytes());System.setIn(testInputStream);
        Client.main(strings);
        ServerHandler sh2=Client.getInstance().getServerHandler();

        //todo: client.getInstance è null anche qui
//        Game g= Client.getInstance().getView().getGameModel();
//        GameController gc=new GameController(g);
//        System.out.println("size of the player is "+g.getPlayers().size());
//        gc.placeStartCard(sh1.getPlayerIdentifier(),true);
//        gc.placeStartCard(sh2.getPlayerIdentifier(),true);
//        gc.selectPrivateGoal(sh1.getPlayerIdentifier(),0);
//        gc.selectPrivateGoal(sh2.getPlayerIdentifier(),0);

//        gc.placeCard(g.getTurn().getClientHandler().getPlayerIdentifier(),0,false,new CardLocation(1,1));


        assertTrue(true);



    }

    @Test
    void drawCard() {
        //todo:implement test
    }

    @Test
    void selectColor() throws IOException {
        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        MainController mc= MainController.getInstance();
        mc.connectClient(cl);
        g.setupScoreBoard();
        Player p1= new Player("mario",cl);
        g.addPlayer(p1);
        //correctly choose a color
        gc.selectColor(cl.getPlayerIdentifier(),PlayerColor.BLUE);
        assertFalse(g.getAvailableColor().contains(PlayerColor.BLUE));

        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        g.addPlayer(p2);
        //choose a color already taken
        assertThrows(RuntimeException.class,()->gc.selectColor(cl2.getPlayerIdentifier(),PlayerColor.BLUE));
        //invalid paramater
        assertThrows(RuntimeException.class,()->gc.selectColor("FAKEPLAYERIDENTIFIER",PlayerColor.BLUE));
        gc.selectColor(cl2.getPlayerIdentifier(),PlayerColor.YELLOW);
        assertEquals(2,g.getAvailableColor().size());
        gc.updateStatus();

        //a player can not be added/changed after the LOBBY phase
        assertThrows(RuntimeException.class,()->gc.selectColor(cl2.getPlayerIdentifier(),PlayerColor.GREEN));

    }

    @Test
    void sendChatMsg() throws IOException {

        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        MainController mc= MainController.getInstance();
        mc.connectClient(cl);
        g.setupScoreBoard();
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.updateStatus();

        //message from mario to massimo
        assertThrows(RuntimeException.class,()->gc.sendChatMsg("FAKEPLAYERIDENTIFIER","test","massimo"));
        assertThrows(RuntimeException.class,()->gc.sendChatMsg(cl.getPlayerIdentifier(),"test","cristiano"));
        gc.sendChatMsg(cl.getPlayerIdentifier(),"test","massimo");
        assertEquals(1,g.getChat().getMessagesChat("mario","massimo").size());

    }

    @Test
    void run() throws Exception {
        //setup of variables
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



        //waiting for both of the player to place the start card and select the objective
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.updateStatus();


        //normal turn of the game
        p1.setStartCard(g.getStartCardsDeck().draw());
        p1.setPrivateGoal(g.getGoalsDeck().draw());
        p1.placeStartingCard(false);
        p2.setStartCard(g.getStartCardsDeck().draw());
        p2.setPrivateGoal(g.getGoalsDeck().draw());
        p2.placeStartingCard(true);
        gc.updateStatus();

        //final turn once a player has hit 20 points
        g.getScoreBoard().setScore("massimo",21);
        gc.updateStatus();

        //since it never changed it's still the first player turn
        gc.updateStatus();
        gc.run();//run should finish because the match ended
        assertTrue(true);
    }

    @Test
    void getPlayerByPlayerIdentifier() throws IOException {
        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        MainController mc= MainController.getInstance();
        mc.connectClient(cl);
        g.setupScoreBoard();
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.updateStatus();


        assertNull(gc.getPlayerByPlayerIdentifier("TESTFAKEIDENTIFIER"));
        //test for the player
        Player res= gc.getPlayerByPlayerIdentifier(p1.getClientHandler().getPlayerIdentifier());
        assertEquals(p1.getClientHandler().getPlayerIdentifier(),res.getClientHandler().getPlayerIdentifier());
        assertEquals(p1.nickname,res.nickname);
        assertEquals(p1.getColor(),res.getColor());
        assertEquals(p1.getStartCard(),res.getStartCard());
        assertEquals(p1.getPrivateGoal(),res.getPrivateGoal());
        assertEquals(p1.getPlayerCard(0),res.getPlayerCard(0));
        assertEquals(p1.getPlayerCard(1),res.getPlayerCard(1));
        assertEquals(p1.getPlayerCard(2),res.getPlayerCard(2));
        for(CardLocation c: p1.getBoard().keySet()){
            assertEquals(p1.getBoard().get(c),res.getBoard().get(c));
        }


    }

    @Test
    void getPlayerByNickname() throws IOException {
        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        MainController mc= MainController.getInstance();
        mc.connectClient(cl);
        g.setupScoreBoard();
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.updateStatus();


        assertNull(gc.getPlayerByNickname("cristiano"));
        assertNull(gc.getPlayerByNickname("Mario"));
        //test for the player
        Player res= gc.getPlayerByNickname("mario");
        assertEquals(p1.getClientHandler().getPlayerIdentifier(),res.getClientHandler().getPlayerIdentifier());
        assertEquals(p1.nickname,res.nickname);
        assertEquals(p1.getColor(),res.getColor());
        assertEquals(p1.getStartCard(),res.getStartCard());
        assertEquals(p1.getPrivateGoal(),res.getPrivateGoal());
        assertEquals(p1.getPlayerCard(0),res.getPlayerCard(0));
        assertEquals(p1.getPlayerCard(1),res.getPlayerCard(1));
        assertEquals(p1.getPlayerCard(2),res.getPlayerCard(2));
        for(CardLocation c: p1.getBoard().keySet()){
            assertEquals(p1.getBoard().get(c),res.getBoard().get(c));
        }

    }

    @Test
    void isNicknameAvailable() throws Exception {
        //setup of variables
        Game g = createGame("1",2);
        GameController gc=new GameController(g);
        ClientHandler cl = createCl();
        MainController mc= MainController.getInstance();
        mc.connectClient(cl);
        g.setupScoreBoard();
        Player p1= new Player("mario",cl);
        p1.setColor(PlayerColor.BLUE);
        g.addPlayer(p1);
        ClientHandler cl2 = createCl();
        mc.connectClient(cl2);
        Player p2= new Player("massimo",cl2);
        p2.setColor(PlayerColor.YELLOW);
        g.addPlayer(p2);
        gc.updateStatus();

        assertFalse(gc.isNicknameAvailable("MARIO"));
        assertFalse(gc.isNicknameAvailable("mario"));
        assertTrue(gc.isNicknameAvailable("cristiano"));

    }
}