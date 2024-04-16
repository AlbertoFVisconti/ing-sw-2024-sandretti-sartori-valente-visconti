package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.model.decks.GoalDeckLoader;
import it.polimi.ingsw.model.decks.PlayCardDeckLoader;
import it.polimi.ingsw.model.decks.StartCardDeckLoader;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    /**
     * StartedGameTest checks if when number of expected players is reached,
     * the GameStatus is not LOBBY**/
    @Test
    void StartedGameTest() throws Exception {
        //create players
        Player p1=new Player("pippo1", PlayerColor.BLUE);
        Player p2= new Player("pippo2", PlayerColor.GREEN);
        //initialize parameters for game
        int id=1;
        int expectedPlayers=2;
        GoalDeckLoader goalDeckLoader= new GoalDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/goals.json");
        PlayCardDeckLoader resourceCardDeckLoader= new PlayCardDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/cards/resourcecards.json");
        PlayCardDeckLoader goldCardDeckLoader= new PlayCardDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/cards/goldcards.json");
        StartCardDeckLoader startCardDeckLoader= new StartCardDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/cards/startcards.json");
        //create game
        Game g=new Game(goldCardDeckLoader,resourceCardDeckLoader,startCardDeckLoader,goalDeckLoader, id,expectedPlayers );
        //add players
        g.addPlayer(p1);
        g.addPlayer(p2);
        //create controller
        GameController gc = new GameController(g);
        assertNotEquals(gc.getGameStatus(), GameStatus.LOBBY);

    }

    /**
     * nextTurnTest checks if nextTurn() method works without issues**/
    @Test
    void nextTurnTest() throws Exception {
        //create players
        Player p1=new Player("pippo1", PlayerColor.BLUE);
        Player p2= new Player("pippo2", PlayerColor.GREEN);
        //initialize parameters for game
        int id=1;
        int expectedPlayers=2;
        GoalDeckLoader goalDeckLoader= new GoalDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/goals.json");
        PlayCardDeckLoader resourceCardDeckLoader= new PlayCardDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/cards/resourcecards.json");
        PlayCardDeckLoader goldCardDeckLoader= new PlayCardDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/cards/goldcards.json");
        StartCardDeckLoader startCardDeckLoader= new StartCardDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/cards/startcards.json");
        //create game
        Game g=new Game(goldCardDeckLoader,resourceCardDeckLoader,startCardDeckLoader,goalDeckLoader, id,expectedPlayers );
        //add players
        g.addPlayer(p1);
        g.addPlayer(p2);

        g.startGame();
        Player t1=g.getTurn();
        g.nextTurn();
        Player t2=g.getTurn();
        assertNotEquals(t1, t2);
        g.nextTurn();
        Player t3=g.getTurn();
        assertEquals(t1, t3);
    }
    /**
     * RefillVisibleCardTest checks if the Visible Cards are correctly replaced after a player draws one of them**/
    @Test
    void RefillVisibleCardTest() throws Exception {
        //create players
        Player p1=new Player("pippo1", PlayerColor.BLUE);
        Player p2= new Player("pippo2", PlayerColor.GREEN);
        //initialize parameters for game
        int id=1;
        int expectedPlayers=2;
        GoalDeckLoader goalDeckLoader= new GoalDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/goals.json");
        PlayCardDeckLoader resourceCardDeckLoader= new PlayCardDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/cards/resourcecards.json");
        PlayCardDeckLoader goldCardDeckLoader= new PlayCardDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/cards/goldcards.json");
        StartCardDeckLoader startCardDeckLoader= new StartCardDeckLoader("/Users/mavio/IdeaProjects/ing-sw-2024-sandretti-sartori-valente-visconti/src/main/resources/json/cards/startcards.json");
        //create game
        Game g=new Game(goldCardDeckLoader,resourceCardDeckLoader,startCardDeckLoader,goalDeckLoader, id,expectedPlayers );
        //add players
        g.addPlayer(p1);
        g.addPlayer(p2);
        g.startGame();
        g.getVisibleCards()[3]=null;
        g.refillVisibleCards();
        assertNotEquals(null, g.getVisibleCards()[3]);
        assertTrue(g.getVisibleCards()[3].isGold() && g.getVisibleCards()[2].isGold());
        assertFalse(g.getVisibleCards()[0].isGold() && g.getVisibleCards()[1].isGold());
    }

}
