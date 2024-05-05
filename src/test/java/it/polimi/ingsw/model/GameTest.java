package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.decks.GoalDeckLoader;
import it.polimi.ingsw.model.decks.PlayCardDeckLoader;
import it.polimi.ingsw.model.decks.StartCardDeckLoader;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    /**
     * StartedGameTest checks if when number of expected players is reached,
     * the GameStatus is not LOBBY**/
    @Test
    void StartedGameTest() throws Exception {
        //create players
        Player p1=new Player("pippo1", null);
        Player p2= new Player("pippo2", null);
        //initialize parameters for game
        int id=1;
        int expectedPlayers=2;
        GoalDeckLoader goalDeckLoader= new GoalDeckLoader("src/main/resources/json/goals.json");
        PlayCardDeckLoader resourceCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/resourcecards.json");
        PlayCardDeckLoader goldCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/goldcards.json");
        StartCardDeckLoader startCardDeckLoader= new StartCardDeckLoader("src/main/resources/json/cards/startcards.json");
        //create game
        Game g=new Game(goldCardDeckLoader,resourceCardDeckLoader,startCardDeckLoader,goalDeckLoader, id,expectedPlayers );
        //add players
        g.addPlayer(p1);
        g.addPlayer(p2);

        g.startGame();

        // visible cards, common goals and decks shouldn't be null
        assertNotNull(g.getVisibleCards());
        assertNotNull(g.getCommonGoals());
        assertNotNull(g.getGoldCardsDeck());
        assertNotNull(g.getResourceCardsDeck());

        // it should be the first player's turn, and it shouldn't be the last round
        assertTrue(g.isFirstPlayersTurn());
        assertFalse(g.isFinalRound());

        assertEquals(id,g.getIdGame());

        // Decks shouldn't be empty
        assertFalse(g.getGoldCardsDeck().isEmpty());
        assertFalse(g.getResourceCardsDeck().isEmpty());

        for(Player p : g.getPlayers()) {
            if (!Objects.equals(p.nickName, p1.nickName) && !Objects.equals(p.nickName, p2.nickName)) {
                fail();
            }

            // the player's inventory should be empty
            assertEquals(0, p.getInventory().count(Corner.ANIMAL));
            assertEquals(0, p.getInventory().count(Corner.PLANT));
            assertEquals(0, p.getInventory().count(Corner.INSECT));
            assertEquals(0, p.getInventory().count(Corner.FUNGUS));
            assertEquals(0, p.getInventory().count(Corner.INK));
            assertEquals(0, p.getInventory().count(Corner.SCROLL));
            assertEquals(0, p.getInventory().count(Corner.FEATHER));

            assertEquals(0, p.getBoard().values().size());

            // the player shouldn't have chosen the private goal yet
            // regardless, he should have the available ones
            assertNull(p.getPrivateGoal());
            assertNotNull(p.getAvailableGoals());

            assertNotNull(p.getStartCard());

            assertNotNull(p.getPlayerCards());
        }
    }

    /**
     * nextTurnTest checks if nextTurn() method works without issues**/
    @Test
    void nextTurnTest() throws Exception {
        //create players
        Player p1=new Player("pippo1", null);
        Player p2= new Player("pippo2", null);
        //initialize parameters for game
        int id=1;
        int expectedPlayers=2;
        GoalDeckLoader goalDeckLoader= new GoalDeckLoader("src/main/resources/json/goals.json");
        PlayCardDeckLoader resourceCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/resourcecards.json");
        PlayCardDeckLoader goldCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/goldcards.json");
        StartCardDeckLoader startCardDeckLoader= new StartCardDeckLoader("src/main/resources/json/cards/startcards.json");
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
        Player p1=new Player( "pippo1", null);
        Player p2= new Player( "pippo2", null);
        //initialize parameters for game
        int id=1;
        int expectedPlayers=2;
        GoalDeckLoader goalDeckLoader= new GoalDeckLoader("src/main/resources/json/goals.json");
        PlayCardDeckLoader resourceCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/resourcecards.json");
        PlayCardDeckLoader goldCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/goldcards.json");
        StartCardDeckLoader startCardDeckLoader= new StartCardDeckLoader("src/main/resources/json/cards/startcards.json");
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
