package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.messages.client.ClientMessage;
import it.polimi.ingsw.events.messages.client.ClientToServerPingMessage;
import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.CoveredCornersScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.FreeScoreScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ItemCountScoringStrategy;
import it.polimi.ingsw.model.decks.GoalDeckLoader;
import it.polimi.ingsw.model.decks.PlayCardDeckLoader;
import it.polimi.ingsw.model.decks.StartCardDeckLoader;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.goals.ItemGoal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.saving.GameBackup;
import it.polimi.ingsw.model.saving.GameData;
import it.polimi.ingsw.model.saving.PlayerData;
import it.polimi.ingsw.network.cliendhandlers.ClientHandler;
import it.polimi.ingsw.network.cliendhandlers.RMIClientHandler;
import it.polimi.ingsw.network.rmi.GameControllerWrapper;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.utils.ItemCollection;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ui.gui.GraphicalUserInterface;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

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
        GoalDeckLoader goalDeckLoader = new GoalDeckLoader("/json/goals.json");
        PlayCardDeckLoader resourceCardDeckLoader = new PlayCardDeckLoader("/json/cards/resourcecards.json");
        PlayCardDeckLoader goldCardDeckLoader = new PlayCardDeckLoader("/json/cards/goldcards.json");
        StartCardDeckLoader startCardDeckLoader = new StartCardDeckLoader("/json/cards/startcards.json");
        return new Game(goldCardDeckLoader, resourceCardDeckLoader, startCardDeckLoader, goalDeckLoader, id, ep);

    }

    @Test
    void addNewPlayer() throws IOException {
        //initialize param for the game
       Game g=createGame("1",4);
       GameController gc= new GameController(g);
        ClientHandler cl = createCl();

        //aggiungi i giocatori
        gc.addNewPlayer("mario",cl, null);
        gc.addNewPlayer("massimo",cl, null);
        gc.addNewPlayer("cristiano",cl, null);
        gc.addNewPlayer("alberto",cl, null);


        //test if the players have been inserted correctly
        assertEquals("mario",g.getPlayers().get(0).nickname);
        assertEquals("massimo",g.getPlayers().get(1).nickname);
        assertEquals("cristiano",g.getPlayers().get(2).nickname);
        assertEquals("alberto",g.getPlayers().get(3).nickname);
        //throws an exception if we try to add a 5th player
        assertThrows(RuntimeException.class,()->gc.addNewPlayer("5th",cl, null));
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
        assertEquals(GameStatus.NORMAL_TURN,gc.getGameStatus());

        //final turn once a player has hit 20 points
        g.getScoreBoard().setScore("massimo",21);
        gc.forceUpdateStatus();
        assertEquals(GameStatus.LAST_TURN,gc.getGameStatus());

        //since it never changed it's still the first player turn
        gc.forceUpdateStatus();
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
        gc.forceUpdateStatus();
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
        //gc.forceUpdateStatus();
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
        gc.forceUpdateStatus();

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
        gc.forceUpdateStatus();
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
        gc2.forceUpdateStatus();


        //normal turn of the game
        p3.setStartCard(g2.getStartCardsDeck().draw());
        p3.setPrivateGoal(g2.getGoalsDeck().draw());
        p3.placeStartingCard(false);
        p4.setStartCard(g2.getStartCardsDeck().draw());
        p4.setPrivateGoal(g2.getGoalsDeck().draw());
        p4.placeStartingCard(true);
        gc2.forceUpdateStatus();
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
        gc.forceUpdateStatus();
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
        gc.forceUpdateStatus();
        p1.setStartCard(g.getStartCardsDeck().draw());
        p1.setPrivateGoal(g.getGoalsDeck().draw());
        p1.placeStartingCard(false);
        p2.setStartCard(g.getStartCardsDeck().draw());
        p2.setPrivateGoal(g.getGoalsDeck().draw());
        p2.placeStartingCard(true);
        gc.forceUpdateStatus();

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
        gc.forceUpdateStatus();
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
        gc.forceUpdateStatus();

        //choosing the startcard and the private goal
        Goal g1=p1.getAvailableGoals()[0];
        gc.placeStartCard(cl.getPlayerIdentifier(),true);
        //index out of bound
        assertThrows(RuntimeException.class,()->gc.selectPrivateGoal(cl.getPlayerIdentifier(),-1));
        gc.selectPrivateGoal(cl.getPlayerIdentifier(),0);
        //can't select private goal twice
        assertThrows(RuntimeException.class,()->gc.selectPrivateGoal(cl.getPlayerIdentifier(),0));
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
        gc.forceUpdateStatus();


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
    void gameLogicTest() {
        Card c1, c2, c3, c4, c5, c6, c7, c8, c9, c10;
        Card[] cards;

        c1 = PlayCard.generateResourceCard("c1", "front_1", "back_1",
           Corner.EMPTY, Corner.ANIMAL,
           Corner.INSECT, null,
           Resource.FUNGUS, 1);

        c2 = PlayCard.generateResourceCard("c2", "front_2", "back_2",
           Corner.EMPTY, Corner.EMPTY,
           Corner.EMPTY, Corner.EMPTY,
           Resource.PLANT, 2);

        c3 = PlayCard.generateResourceCard("c3", "front_3", "back_3",
           Corner.INSECT, null,
           Corner.INSECT, null,
           Resource.ANIMAL, 3);

        c4 = PlayCard.generateResourceCard("c4", "front_4", "back_4",
           Corner.EMPTY, Corner.ANIMAL,
           Corner.EMPTY, Corner.EMPTY,
           Resource.INSECT, 4);

        c5 = PlayCard.generateGoldCard("c5", "front_5", "back_5",
           null, Corner.ANIMAL,
           null, null,
           Resource.FUNGUS,
           new ItemCollection().add(Corner.INSECT, 3),
           new CoveredCornersScoringStrategy(2));

        c6 = PlayCard.generateGoldCard("c6", "front_6", "back_6",
           Corner.PLANT, Corner.EMPTY,
           Corner.PLANT, null,
           Resource.PLANT,
           new ItemCollection().add(Corner.INSECT, 3).add(Corner.ANIMAL, 2),
           new FreeScoreScoringStrategy(1000));

        c7 = PlayCard.generateGoldCard("c7", "front_7", "back_7",
           Corner.EMPTY, null,
           Corner.INSECT, Corner.EMPTY,
           Resource.ANIMAL,
           new ItemCollection().add(Corner.ANIMAL, 3).add(Corner.PLANT, 2),
           new ItemCountScoringStrategy(Corner.FEATHER, 10));

        c8 = PlayCard.generateGoldCard("c8", "front_8", "back_8",
           Corner.FUNGUS, null,
           Corner.FUNGUS, Corner.FUNGUS,
           Resource.INSECT,
           new ItemCollection().add(Corner.FUNGUS, 3).add(Corner.PLANT, 2).add(Corner.ANIMAL),
           new CoveredCornersScoringStrategy(3));

        c9 = new StartCard("c9", "front_9", "back_9",
           Corner.ANIMAL, Corner.EMPTY,
           Corner.EMPTY, Corner.PLANT,

           Corner.ANIMAL, Corner.PLANT,
           Corner.FUNGUS, Corner.INSECT,
           new ItemCollection().add(Corner.PLANT).add(Corner.FUNGUS, 2));

        c10 = new StartCard("c10", "front_10", "back_10",
           Corner.PLANT, null,
           Corner.INSECT, Corner.PLANT,

           null, Corner.INSECT,
           Corner.EMPTY, null,
           new ItemCollection().add(Corner.INSECT, 1).add(Corner.ANIMAL, 2));

        cards = new Card[]{c1,c2,c3,c4,c5,c6,c7,c8,c9,c10};

        Player p1, p2 ,p3 ,p4;
        p1 = new Player("p1", new HelperClientHandler("p1"));
        p2 = new Player("p2", new HelperClientHandler("p2"));
        p3 = new Player("p3", new HelperClientHandler("p3"));
        p4 = new Player("p4", new HelperClientHandler("p4"));

        p1.setStartCard((StartCard) c9);
        p2.setStartCard((StartCard) c10);
        p3.setStartCard((StartCard) c9);
        p4.setStartCard((StartCard) c10);

        p1.placeStartingCard(true);
        p2.placeStartingCard(true);
        p3.placeStartingCard(false);
        p4.placeStartingCard(false);

        p1.setPlayerCard((PlayCard) c1, 0);
        p1.setPlayerCard((PlayCard) c2, 1);
        p1.setPlayerCard((PlayCard) c3, 2);

        p2.setPlayerCard((PlayCard) c4, 0);
        p2.setPlayerCard((PlayCard) c5, 1);
        p2.setPlayerCard((PlayCard) c6, 2);

        p3.setPlayerCard((PlayCard) c7, 0);
        p3.setPlayerCard((PlayCard) c8, 1);
        p3.setPlayerCard((PlayCard) c1, 2);

        p4.setPlayerCard((PlayCard) c2, 0);
        p4.setPlayerCard((PlayCard) c4, 1);
        p4.setPlayerCard((PlayCard) c6, 2);

        p1.setPrivateGoal(new ItemGoal(new ItemCollection().add(Corner.PLANT), 2, ""));
        p2.setPrivateGoal(new ItemGoal(new ItemCollection().add(Corner.FEATHER), 2, ""));
        p3.setPrivateGoal(new ItemGoal(new ItemCollection().add(Corner.FUNGUS), 2, ""));
        p4.setPrivateGoal(new ItemGoal(new ItemCollection().add(Corner.INK), 2, ""));

        p1.setColor(PlayerColor.RED);
        p2.setColor(PlayerColor.BLUE);
        p3.setColor(PlayerColor.GREEN);
        p4.setColor(PlayerColor.YELLOW);

        GoalDeckLoader goalDeckLoader = new GoalDeckLoader("/json/goals.json");
        PlayCardDeckLoader resourceCardDeckLoader = new PlayCardDeckLoader("/json/cards/resourcecards.json");
        PlayCardDeckLoader goldCardDeckLoader = new PlayCardDeckLoader("/json/cards/goldcards.json");
        StartCardDeckLoader startCardDeckLoader = new StartCardDeckLoader("/json/cards/startcards.json");

        Game game;
        try {
            game = new Game(
                    goldCardDeckLoader,
                    resourceCardDeckLoader,
                    startCardDeckLoader,
                    goalDeckLoader,
                    "game",
                    4

            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GameController gameController = new GameController(game);

        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(p4);

        ArrayList<PlayerData> playerData = new ArrayList<>();
        playerData.add(p1.getSaving());
        playerData.add(p2.getSaving());
        playerData.add(p3.getSaving());
        playerData.add(p4.getSaving());

        game.getVisibleCards()[0] = (PlayCard) c1;
        game.getVisibleCards()[1] = (PlayCard)c2;
        game.getVisibleCards()[2] = (PlayCard)c5;
        game.getVisibleCards()[3] = (PlayCard)c6;

        GameData gameData = game.getSaving();
        GameBackup gameBackup = new GameBackup(gameData, GameStatus.NORMAL_TURN, TurnStatus.PLACE);

        gameController.loadBackup(gameBackup);

        // it is not this player's turn
        assertThrows(RuntimeException.class,()-> {
            gameController.placeCard("p2", 0, true, new CardLocation(-1,1));
        });

        // it is not this player's turn
        assertThrows(RuntimeException.class,()-> {
            gameController.placeCard("p3", 0, true, new CardLocation(-1,1));
        });

        // it is not this player's turn
        assertThrows(RuntimeException.class,()-> {
            gameController.placeCard("p4", 0, true, new CardLocation(-1,1));
        });

        // it this player turn, but they need to place first
        assertThrows(RuntimeException.class, () -> {
            gameController.drawCard("p1", 2);
        });

        // it this player turn, but location is invalid
        assertThrows(RuntimeException.class, () -> {
            gameController.placeCard("p1", 0, true, new CardLocation(2,2));
        });

        // it this player turn, but index is out of bound
        assertThrows(RuntimeException.class, () -> {
            gameController.placeCard("p1", -1, true, new CardLocation(-1,1));
        });

        gameController.placeCard("p1", 0, true, new CardLocation(-1,1));
        assertEquals(c1, p1.getPlacedCardSlot(new CardLocation(-1,1)).card());

        // it is this player turn, but they already placed
        assertThrows(RuntimeException.class, () -> {
            gameController.placeCard("p1", 0, true, new CardLocation(1,1));
        });

        gameController.drawCard("p1", 2);
        assertEquals(c1, p1.getPlayerCard(0));

        // it is this player turn, but card constratins are not met
        assertThrows(RuntimeException.class, () -> {
            gameController.placeCard("p2", 1, false, new CardLocation(1,1));
        });

        gameController.placeCard("p2", 0, false, new CardLocation(1,1));
        assertEquals(c4, p2.getPlacedCardSlot(new CardLocation(1,1)).card());

        //index is out of bound
        assertThrows(RuntimeException.class, () -> {
            gameController.drawCard("p2", -1);
        });

        Resource r= game.getResourceCardsDeck().getTopOfTheStack();
        gameController.drawCard("p2", 0);
        assertEquals(r, p2.getPlayerCard(0).getResourceType());

        gameController.placeCard("p3", 0, true, new CardLocation(1,1));
        assertEquals(c7, p3.getPlacedCardSlot(new CardLocation(1,1)).card());

        r= game.getGoldCardsDeck().getTopOfTheStack();
        gameController.drawCard("p3", 1);
        assertEquals(r, p3.getPlayerCard(0).getResourceType());

        //top right corner of the start card is null
        assertThrows(RuntimeException.class, () -> {
            gameController.placeCard("p4", 0, false, new CardLocation(1,1));
        });

        gameController.placeCard("p4", 0, false, new CardLocation(1,-1));
        assertEquals(c2, p4.getPlacedCardSlot(new CardLocation(1,-1)).card());

        gameController.drawCard("p4", 4);
        assertEquals(c5, p4.getPlayerCard(0));

        gameController.placeCard("p1", 0, true, new CardLocation(1,1));
        assertEquals(c1, p1.getPlacedCardSlot(new CardLocation(1,1)).card());

        gameController.drawCard("p1", 3);
        assertEquals(c2, p1.getPlayerCard(0));

        gameController.placeCard("p2", 0, false, new CardLocation(-1,-1));
        assertEquals(c4, p2.getPlacedCardSlot(new CardLocation(1,1)).card());

        gameController.drawCard("p2", 5);
        assertEquals(c6, p2.getPlayerCard(0));

        gameController.placeCard("p3", 0, true, new CardLocation(-1,1));

        r= game.getResourceCardsDeck().getTopOfTheStack();
        gameController.drawCard("p3", 0);
        assertEquals(r, p3.getPlayerCard(0).getResourceType());

        gameController.placeCard("p4", 1, false, new CardLocation(-1,-1));
        assertEquals(c4, p4.getPlacedCardSlot(new CardLocation(-1,-1)).card());

        //game's not finished
        assertThrows(RuntimeException.class, () -> {
            gameController.evaluateGoals();
        });

        //TODO end game simulation (last turn, evaluate goals ecc)













        // TODO more simulation (drawing from both decks

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
        gc.forceUpdateStatus();

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
        gc.forceUpdateStatus();

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
        gc.forceUpdateStatus();


        //normal turn of the game
        p1.setStartCard(g.getStartCardsDeck().draw());
        p1.setPrivateGoal(g.getGoalsDeck().draw());
        p1.placeStartingCard(false);
        p2.setStartCard(g.getStartCardsDeck().draw());
        p2.setPrivateGoal(g.getGoalsDeck().draw());
        p2.placeStartingCard(true);
        gc.forceUpdateStatus();

        //get the message queue to see if the ping msg arrived
        Field privateValueField2 = GameController.class.getDeclaredField("messageQueue");
        privateValueField2.setAccessible(true);
        BlockingQueue<ClientMessage> messageQueue=(BlockingQueue<ClientMessage>)privateValueField2.get(gc);
        for(int i=0;i<5;i++) messageQueue.put(new ClientToServerPingMessage(false));

        Thread thread= new Thread(()->gc.run());
        thread.start();
        Thread.sleep(1000);

        assertEquals(0,messageQueue.size());

        //final turn once a player has hit 20 points
        g.getScoreBoard().setScore("massimo",21);
        gc.forceUpdateStatus();

        //since it never changed it's still the first player turn
        gc.forceUpdateStatus();

        gc.handleDisconnection(cl);
        gc.handleDisconnection(cl2);

        gc.run();//run should finish because all the player have left
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
        gc.forceUpdateStatus();


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
        gc.forceUpdateStatus();


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
        gc.forceUpdateStatus();

        assertFalse(gc.isNicknameAvailable("MARIO"));
        assertFalse(gc.isNicknameAvailable("mario"));
        assertTrue(gc.isNicknameAvailable("cristiano"));

    }

    private static class HelperClientHandler extends ClientHandler {

        public HelperClientHandler(String identifier) {
           this.setPlayerIdentifier(identifier);
        }

        @Override
        public void sendMessage(ServerMessage message) {}

        @Override
        protected void linkController(GameControllerWrapper gameControllerWrapper) {}
    }
}