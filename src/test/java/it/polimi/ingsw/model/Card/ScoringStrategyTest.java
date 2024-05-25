package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.CoveredCornersScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.FreeScoreScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ItemCountScoringStrategy;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.utils.ItemCollection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ItemCountTest1 checks the correct evaluation of the ItemCountScoringStrategy
 **/
public class ScoringStrategyTest {
    @Test
    void ItemCountTest1() throws Exception {
        ItemCountScoringStrategy Strategy1 = new ItemCountScoringStrategy(Corner.FEATHER, 1);
        ItemCountScoringStrategy Strategy2 = new ItemCountScoringStrategy(Corner.INK, 2);
        ItemCountScoringStrategy Strategy3 = new ItemCountScoringStrategy(Corner.FUNGUS, 1);
        Player p1 = new Player("pippo123", null);
        p1.addItem(Corner.FEATHER);
        p1.addItem(Corner.FEATHER);
        p1.addItem(Corner.FEATHER);
        p1.addItem(Corner.ANIMAL);
        p1.addItem(Corner.PLANT);
        p1.addItem(Corner.FUNGUS);
        p1.addItem(Corner.INSECT);
        p1.addItem(Corner.INK);
        assertEquals(Strategy1.evaluate(p1, new CardLocation(0, 0)), 3);
        assertEquals(Strategy2.evaluate(p1, new CardLocation(0, 0)), 2);
        assertEquals(Strategy3.evaluate(p1, new CardLocation(0, 0)), 1);
    }

    /**
     * FreScoreTest1 checks the correct evaluation of the FreeScoreScoringStrategy
     **/
    @Test
    void FreeScoreTest1() {
        FreeScoreScoringStrategy Strategy1 = new FreeScoreScoringStrategy(3);
        FreeScoreScoringStrategy Strategy2 = new FreeScoreScoringStrategy(2);
        Player p1 = new Player("pippo123", null);
        assertEquals(Strategy1.evaluate(p1, new CardLocation(0, 0)), 3);
        assertEquals(Strategy2.evaluate(p1, new CardLocation(0, 0)), 2);

    }

    /**
     * CoveredCornerTest1 checks the correct evaluation of the CoveredCornerTest1
     **/
    @Test
    void CoveredCornerTest1() throws Exception {
        CoveredCornersScoringStrategy Strategy1 = new CoveredCornersScoringStrategy(2);
        Player p1 = new Player("pippo123", null);
        ItemCollection permanent = new ItemCollection();
        permanent.add(Corner.INSECT);
        p1.setStartCard(new StartCard("", Corner.FUNGUS, Corner.PLANT, Corner.INSECT, Corner.ANIMAL, Corner.EMPTY, Corner.PLANT, Corner.INSECT, Corner.EMPTY, permanent));
        p1.placeStartingCard(true);
        PlayCard card1 = PlayCard.generateResourceCard("", Corner.FUNGUS, Corner.EMPTY, Corner.FUNGUS, null, Resource.FUNGUS, 0);
        p1.addPlayerCard(card1);
        CardLocation cl = new CardLocation(-1, 1);
        p1.placeCard(0, false, cl);
        assertEquals(Strategy1.evaluate(p1, cl), 2);


    }
}

