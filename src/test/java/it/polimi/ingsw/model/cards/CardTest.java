package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.CoveredCornersScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.FreeScoreScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ItemCountScoringStrategy;
import it.polimi.ingsw.model.decks.VirtualDeck;
import it.polimi.ingsw.model.goals.ItemGoal;
import it.polimi.ingsw.utils.ItemCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private Card c1, c2, c3, c4, c5, c6, c7, c8, c9, c10;
    private Card[] cards;

    @BeforeEach
    void setupCards() {
        this.c1 = PlayCard.generateResourceCard("c1", "front_1", "back_1",
                Corner.EMPTY, Corner.ANIMAL,
                Corner.INSECT, null,
                Resource.FUNGUS, 1);

        this.c2 = PlayCard.generateResourceCard("c2", "front_2", "back_2",
                Corner.EMPTY, Corner.EMPTY,
                Corner.EMPTY, Corner.EMPTY,
                Resource.PLANT, 2);

        this.c3 = PlayCard.generateResourceCard("c3", "front_3", "back_3",
                Corner.INSECT, null,
                Corner.INSECT, null,
                Resource.ANIMAL, 3);

        this.c4 = PlayCard.generateResourceCard("c4", "front_4", "back_4",
                Corner.EMPTY, Corner.ANIMAL,
                Corner.EMPTY, Corner.EMPTY,
                Resource.INSECT, 4);

        this.c5 = PlayCard.generateGoldCard("c5", "front_5", "back_5",
                null, Corner.ANIMAL,
                null, null,
                Resource.FUNGUS,
                new ItemCollection().add(Corner.INSECT, 3),
                new CoveredCornersScoringStrategy(2));

        this.c6 = PlayCard.generateGoldCard("c6", "front_6", "back_6",
                Corner.PLANT, Corner.EMPTY,
                Corner.PLANT, null,
                Resource.PLANT,
                new ItemCollection().add(Corner.INSECT, 3).add(Corner.ANIMAL, 2),
                new FreeScoreScoringStrategy(1000));

        this.c7 = PlayCard.generateGoldCard("c7", "front_7", "back_7",
                Corner.EMPTY, null,
                Corner.INSECT, Corner.EMPTY,
                Resource.ANIMAL,
                new ItemCollection().add(Corner.ANIMAL, 3).add(Corner.PLANT, 2),
                new ItemCountScoringStrategy(Corner.FEATHER, 10));

        this.c8 = PlayCard.generateGoldCard("c8", "front_8", "back_8",
                Corner.FUNGUS, null,
                Corner.FUNGUS, Corner.FUNGUS,
                Resource.INSECT,
                new ItemCollection().add(Corner.FUNGUS, 3).add(Corner.PLANT, 2).add(Corner.ANIMAL),
                new CoveredCornersScoringStrategy(3));

        this.c9 = new StartCard("c9", "front_9", "back_9",
                Corner.ANIMAL, Corner.EMPTY,
                Corner.EMPTY, Corner.PLANT,

                Corner.ANIMAL, Corner.PLANT,
                Corner.FUNGUS, Corner.INSECT,
                new ItemCollection().add(Corner.PLANT).add(Corner.FUNGUS, 2));

        this.c10 = new StartCard("c10", "front_10", "back_10",
                Corner.PLANT, null,
                Corner.INSECT, Corner.PLANT,

                null, Corner.INSECT,
                Corner.EMPTY, null,
                new ItemCollection().add(Corner.INSECT, 1).add(Corner.ANIMAL, 2));

        cards = new Card[]{c1,c2,c3,c4,c5,c6,c7,c8,c9,c10};
    }

    @Test
    void getBackCorner() {
        assertEquals(Corner.EMPTY,c1.getBackCorner(0));
        assertEquals(Corner.EMPTY,c1.getBackCorner(1));
        assertEquals(Corner.EMPTY,c1.getBackCorner(2));
        assertEquals(Corner.EMPTY,c1.getBackCorner(3));

        assertEquals(Corner.EMPTY,c2.getBackCorner(0));
        assertEquals(Corner.EMPTY,c2.getBackCorner(1));
        assertEquals(Corner.EMPTY,c2.getBackCorner(2));
        assertEquals(Corner.EMPTY,c2.getBackCorner(3));

        assertEquals(Corner.EMPTY,c3.getBackCorner(0));
        assertEquals(Corner.EMPTY,c3.getBackCorner(1));
        assertEquals(Corner.EMPTY,c3.getBackCorner(2));
        assertEquals(Corner.EMPTY,c3.getBackCorner(3));

        assertEquals(Corner.EMPTY,c4.getBackCorner(0));
        assertEquals(Corner.EMPTY,c4.getBackCorner(1));
        assertEquals(Corner.EMPTY,c4.getBackCorner(2));
        assertEquals(Corner.EMPTY,c4.getBackCorner(3));

        assertEquals(Corner.EMPTY,c5.getBackCorner(0));
        assertEquals(Corner.EMPTY,c5.getBackCorner(1));
        assertEquals(Corner.EMPTY,c5.getBackCorner(2));
        assertEquals(Corner.EMPTY,c5.getBackCorner(3));

        assertEquals(Corner.EMPTY,c6.getBackCorner(0));
        assertEquals(Corner.EMPTY,c6.getBackCorner(1));
        assertEquals(Corner.EMPTY,c6.getBackCorner(2));
        assertEquals(Corner.EMPTY,c6.getBackCorner(3));

        assertEquals(Corner.EMPTY,c7.getBackCorner(0));
        assertEquals(Corner.EMPTY,c7.getBackCorner(1));
        assertEquals(Corner.EMPTY,c7.getBackCorner(2));
        assertEquals(Corner.EMPTY,c7.getBackCorner(3));

        assertEquals(Corner.EMPTY,c8.getBackCorner(0));
        assertEquals(Corner.EMPTY,c8.getBackCorner(1));
        assertEquals(Corner.EMPTY,c8.getBackCorner(2));
        assertEquals(Corner.EMPTY,c8.getBackCorner(3));

        assertEquals(Corner.ANIMAL,c9.getBackCorner(0));
        assertEquals(Corner.PLANT,c9.getBackCorner(1));
        assertEquals(Corner.FUNGUS,c9.getBackCorner(2));
        assertEquals(Corner.INSECT,c9.getBackCorner(3));

        assertNull(c10.getBackCorner(0));
        assertEquals(Corner.INSECT,c10.getBackCorner(1));
        assertEquals(Corner.EMPTY,c10.getBackCorner(2));
        assertNull(c10.getBackCorner(3));
    }

    @Test
    void getTopLeftCorner() {
        assertEquals(Corner.EMPTY, c1.getTopLeftCorner(true));
        assertEquals(Corner.EMPTY, c1.getTopLeftCorner(false));

        assertEquals(Corner.EMPTY, c2.getTopLeftCorner(true));
        assertEquals(Corner.EMPTY, c2.getTopLeftCorner(false));

        assertEquals(Corner.EMPTY, c3.getTopLeftCorner(true));
        assertEquals(Corner.INSECT, c3.getTopLeftCorner(false));

        assertEquals(Corner.EMPTY, c4.getTopLeftCorner(true));
        assertEquals(Corner.EMPTY, c4.getTopLeftCorner(false));

        assertEquals(Corner.EMPTY, c5.getTopLeftCorner(true));
        assertNull(c5.getTopLeftCorner(false));

        assertEquals(Corner.EMPTY, c6.getTopLeftCorner(true));
        assertEquals(Corner.PLANT, c6.getTopLeftCorner(false));

        assertEquals(Corner.EMPTY, c7.getTopLeftCorner(true));
        assertEquals(Corner.EMPTY, c7.getTopLeftCorner(false));

        assertEquals(Corner.EMPTY, c8.getTopLeftCorner(true));
        assertEquals(Corner.FUNGUS, c8.getTopLeftCorner(false));

        assertEquals(Corner.ANIMAL, c9.getTopLeftCorner(true));
        assertEquals(Corner.ANIMAL, c9.getTopLeftCorner(false));

        assertNull(c10.getTopLeftCorner(true));
        assertEquals(Corner.PLANT, c10.getTopLeftCorner(false));
    }

    @Test
    void getTopRightCorner() {
        assertEquals(Corner.EMPTY, c1.getTopRightCorner(true));
        assertEquals(Corner.ANIMAL, c1.getTopRightCorner(false));

        assertEquals(Corner.EMPTY, c2.getTopRightCorner(true));
        assertEquals(Corner.EMPTY, c2.getTopRightCorner(false));

        assertEquals(Corner.EMPTY, c3.getTopRightCorner(true));
        assertNull(c3.getTopRightCorner(false));

        assertEquals(Corner.EMPTY, c4.getTopRightCorner(true));
        assertEquals(Corner.ANIMAL, c4.getTopRightCorner(false));

        assertEquals(Corner.EMPTY, c5.getTopRightCorner(true));
        assertEquals(Corner.ANIMAL, c5.getTopRightCorner(false));

        assertEquals(Corner.EMPTY, c6.getTopRightCorner(true));
        assertEquals(Corner.EMPTY, c6.getTopRightCorner(false));

        assertEquals(Corner.EMPTY, c7.getTopRightCorner(true));
        assertNull(c7.getTopRightCorner(false));

        assertEquals(Corner.EMPTY, c8.getTopRightCorner(true));
        assertNull(c8.getTopRightCorner(false));

        assertEquals(Corner.PLANT, c9.getTopRightCorner(true));
        assertEquals(Corner.EMPTY, c9.getTopRightCorner(false));

        assertEquals(Corner.INSECT, c10.getTopRightCorner(true));
        assertNull(c10.getTopRightCorner(false));
    }

    @Test
    void getBottomLeftCorner() {
        assertEquals(Corner.EMPTY, c1.getBottomLeftCorner(true));
        assertEquals(Corner.INSECT, c1.getBottomLeftCorner(false));

        assertEquals(Corner.EMPTY, c2.getBottomLeftCorner(true));
        assertEquals(Corner.EMPTY, c2.getBottomLeftCorner(false));

        assertEquals(Corner.EMPTY, c3.getBottomLeftCorner(true));
        assertEquals(Corner.INSECT, c3.getBottomLeftCorner(false));

        assertEquals(Corner.EMPTY, c4.getBottomLeftCorner(true));
        assertEquals(Corner.EMPTY, c4.getBottomLeftCorner(false));

        assertEquals(Corner.EMPTY, c5.getBottomLeftCorner(true));
        assertNull(c5.getBottomLeftCorner(false));

        assertEquals(Corner.EMPTY, c6.getBottomLeftCorner(true));
        assertEquals(Corner.PLANT, c6.getBottomLeftCorner(false));

        assertEquals(Corner.EMPTY, c7.getBottomLeftCorner(true));
        assertEquals(Corner.INSECT, c7.getBottomLeftCorner(false));

        assertEquals(Corner.EMPTY, c8.getBottomLeftCorner(true));
        assertEquals(Corner.FUNGUS, c8.getBottomLeftCorner(false));

        assertEquals(Corner.FUNGUS, c9.getBottomLeftCorner(true));
        assertEquals(Corner.EMPTY, c9.getBottomLeftCorner(false));

        assertEquals(Corner.EMPTY,c10.getBottomLeftCorner(true));
        assertEquals(Corner.INSECT, c10.getBottomLeftCorner(false));
    }

    @Test
    void getBottomRightCorner() {
        assertEquals(Corner.EMPTY, c1.getBottomRightCorner(true));
        assertNull(c1.getBottomRightCorner(false));

        assertEquals(Corner.EMPTY, c2.getBottomRightCorner(true));
        assertEquals(Corner.EMPTY, c2.getBottomRightCorner(false));

        assertEquals(Corner.EMPTY, c3.getBottomRightCorner(true));
        assertNull(c3.getBottomRightCorner(false));

        assertEquals(Corner.EMPTY, c4.getBottomRightCorner(true));
        assertEquals(Corner.EMPTY, c4.getBottomRightCorner(false));

        assertEquals(Corner.EMPTY, c5.getBottomRightCorner(true));
        assertNull(c5.getBottomRightCorner(false));

        assertEquals(Corner.EMPTY, c6.getBottomRightCorner(true));
        assertNull(c6.getBottomRightCorner(false));

        assertEquals(Corner.EMPTY, c7.getBottomRightCorner(true));
        assertEquals(Corner.EMPTY, c7.getBottomRightCorner(false));

        assertEquals(Corner.EMPTY, c8.getBottomRightCorner(true));
        assertEquals(Corner.FUNGUS, c8.getBottomRightCorner(false));

        assertEquals(Corner.INSECT, c9.getBottomRightCorner(true));
        assertEquals(Corner.PLANT, c9.getBottomRightCorner(false));

        assertNull(c10.getBottomRightCorner(true));
        assertEquals(Corner.PLANT, c10.getBottomRightCorner(false));
    }

    @Test
    void collectItems() {
        ItemCollection itemCollection;

        itemCollection = c1.collectItems(true);
        assertEquals(1, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
        itemCollection = c1.collectItems(false);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(1, itemCollection.count(Corner.ANIMAL));
        assertEquals(1, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c2.collectItems(true);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(1, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
        itemCollection = c2.collectItems(false);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c3.collectItems(true);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(1, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
        itemCollection = c3.collectItems(false);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(2, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c4.collectItems(true);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(1, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
        itemCollection = c4.collectItems(false);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(1, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c5.collectItems(true);
        assertEquals(1, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
        itemCollection = c5.collectItems(false);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(1, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c6.collectItems(true);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(1, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
        itemCollection = c6.collectItems(false);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(2, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c7.collectItems(true);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(1, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
        itemCollection = c7.collectItems(false);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(1, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c8.collectItems(true);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(1, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
        itemCollection = c8.collectItems(false);
        assertEquals(3, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c9.collectItems(true);
        assertEquals(3, itemCollection.count(Corner.FUNGUS));
        assertEquals(2, itemCollection.count(Corner.PLANT));
        assertEquals(1, itemCollection.count(Corner.ANIMAL));
        assertEquals(1, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
        itemCollection = c9.collectItems(false);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(1, itemCollection.count(Corner.PLANT));
        assertEquals(1, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c10.collectItems(true);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(2, itemCollection.count(Corner.ANIMAL));
        assertEquals(2, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
        itemCollection = c10.collectItems(false);
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(2, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(1, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
    }

    @Test
    void collectBackItems() {
        ItemCollection itemCollection;

        itemCollection = c1.collectBackItems();
        assertEquals(1, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c2.collectBackItems();
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(1, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c3.collectBackItems();
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(1, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c4.collectBackItems();
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(1, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c5.collectBackItems();
        assertEquals(1, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c6.collectBackItems();
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(1, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c7.collectBackItems();
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(1, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c8.collectBackItems();
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(1, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c9.collectBackItems();
        assertEquals(3, itemCollection.count(Corner.FUNGUS));
        assertEquals(2, itemCollection.count(Corner.PLANT));
        assertEquals(1, itemCollection.count(Corner.ANIMAL));
        assertEquals(1, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));


        itemCollection = c10.collectBackItems();
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(2, itemCollection.count(Corner.ANIMAL));
        assertEquals(2, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
    }

    @Test
    void testEquals() {
        for(int i = 0; i < cards.length; i++) {
            for(int j = 0; j < cards.length; j++) {
                if (i == j) assertEquals(cards[i], cards[j]);
                else assertNotEquals(cards[i], cards[j]);
            }
            assertNotEquals(cards[i], new FreeScoreScoringStrategy(0));
            assertNotEquals(cards[i], new ItemGoal(new ItemCollection().add(Corner.PLANT), 2, ""));
            assertNotEquals(cards[i], new VirtualDeck());
        }
    }

    @Test
    void getFrontPath() {
        for(int i = 0; i < cards.length; i++) {
            assertEquals(cards[i].getFrontPath(), "front_" + (i + 1));
        }
    }

    @Test
    void getBackPath() {
        for(int i = 0; i < cards.length; i++) {
            assertEquals(cards[i].getBackPath(), "back_" + (i + 1));
        }
    }

    @Test
    void testToString() {
        String c1String = c1.toString();
        String c2String = c2.toString();
        String c3String = c3.toString();
        String c4String = c4.toString();
        String c5String = c5.toString();
        String c6String = c6.toString();
        String c7String = c7.toString();
        String c8String = c8.toString();
        String c9String = c9.toString();
        String c10String = c10.toString();

        assertTrue(c1String.contains("frontCorners = [EMPTY, ANIMAL, INSECT, null]"));
        assertTrue(c1String.contains("isGold = false"));
        assertTrue(c1String.contains("type = FUNGUS"));
        assertTrue(c1String.contains("constraint = {FUNGUS=0, PLANT=0, ANIMAL=0, INSECT=0, INK=0, SCROLL=0, FEATHER=0}"));
        assertTrue(c1String.contains("scoringStrategy = 1 pt"));

        assertTrue(c2String.contains("frontCorners = [EMPTY, EMPTY, EMPTY, EMPTY]"));
        assertTrue(c2String.contains("isGold = false"));
        assertTrue(c2String.contains("type = PLANT"));
        assertTrue(c2String.contains("constraint = {FUNGUS=0, PLANT=0, ANIMAL=0, INSECT=0, INK=0, SCROLL=0, FEATHER=0}"));
        assertTrue(c2String.contains("scoringStrategy = 2 pt"));

        assertTrue(c3String.contains("frontCorners = [INSECT, null, INSECT, null]"));
        assertTrue(c3String.contains("isGold = false"));
        assertTrue(c3String.contains("type = ANIMAL"));
        assertTrue(c3String.contains("constraint = {FUNGUS=0, PLANT=0, ANIMAL=0, INSECT=0, INK=0, SCROLL=0, FEATHER=0}"));
        assertTrue(c3String.contains("scoringStrategy = 3 pt"));

        assertTrue(c4String.contains("frontCorners = [EMPTY, ANIMAL, EMPTY, EMPTY]"));
        assertTrue(c4String.contains("isGold = false"));
        assertTrue(c4String.contains("type = INSECT"));
        assertTrue(c4String.contains("constraint = {FUNGUS=0, PLANT=0, ANIMAL=0, INSECT=0, INK=0, SCROLL=0, FEATHER=0}"));
        assertTrue(c4String.contains("scoringStrategy = 4 pt"));

        assertTrue(c5String.contains("frontCorners = [null, ANIMAL, null, null]"));
        assertTrue(c5String.contains("isGold = true"));
        assertTrue(c5String.contains("type = FUNGUS"));
        assertTrue(c5String.contains("constraint = {FUNGUS=0, PLANT=0, ANIMAL=0, INSECT=3, INK=0, SCROLL=0, FEATHER=0}"));
        assertTrue(c5String.contains("scoringStrategy = 2 x corner"));

        assertTrue(c6String.contains("frontCorners = [PLANT, EMPTY, PLANT, null]"));
        assertTrue(c6String.contains("isGold = true"));
        assertTrue(c6String.contains("type = PLANT"));
        assertTrue(c6String.contains("constraint = {FUNGUS=0, PLANT=0, ANIMAL=2, INSECT=3, INK=0, SCROLL=0, FEATHER=0}"));
        assertTrue(c6String.contains("scoringStrategy = 1000 pt"));

        assertTrue(c7String.contains("frontCorners = [EMPTY, null, INSECT, EMPTY]"));
        assertTrue(c7String.contains("isGold = true"));
        assertTrue(c7String.contains("type = ANIMAL"));
        assertTrue(c7String.contains("constraint = {FUNGUS=0, PLANT=2, ANIMAL=3, INSECT=0, INK=0, SCROLL=0, FEATHER=0}"));
        assertTrue(c7String.contains("scoringStrategy = 10 x FEATHER"));

        assertTrue(c8String.contains("frontCorners = [FUNGUS, null, FUNGUS, FUNGUS]"));
        assertTrue(c8String.contains("isGold = true"));
        assertTrue(c8String.contains("type = INSECT"));
        assertTrue(c8String.contains("constraint = {FUNGUS=3, PLANT=2, ANIMAL=1, INSECT=0, INK=0, SCROLL=0, FEATHER=0}"));
        assertTrue(c8String.contains("scoringStrategy = 3 x corner"));

        assertTrue(c9String.contains("frontCorners = [ANIMAL, EMPTY, EMPTY, PLANT]"));
        assertTrue(c9String.contains("backCorners = [ANIMAL, PLANT, FUNGUS, INSECT]"));
        assertTrue(c9String.contains("permanentResources = {FUNGUS=2, PLANT=1, ANIMAL=0, INSECT=0, INK=0, SCROLL=0, FEATHER=0}"));

        assertTrue(c10String.contains("frontCorners = [PLANT, null, INSECT, PLANT]"));
        assertTrue(c10String.contains("backCorners = [null, INSECT, EMPTY, null]"));
        assertTrue(c10String.contains("permanentResources = {FUNGUS=0, PLANT=0, ANIMAL=2, INSECT=1, INK=0, SCROLL=0, FEATHER=0}"));
    }
}