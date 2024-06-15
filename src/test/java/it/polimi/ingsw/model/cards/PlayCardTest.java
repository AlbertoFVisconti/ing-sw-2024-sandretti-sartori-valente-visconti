package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.CoveredCornersScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.FreeScoreScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ItemCountScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ScoringStrategy;
import it.polimi.ingsw.utils.ItemCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayCardTest {
    private PlayCard c1, c2, c3, c4, c5, c6, c7, c8;
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


        cards = new Card[]{c1, c2, c3, c4, c5, c6, c7, c8};
    }

    @Test
    void getConstraint() {
        assertTrue(c1.getConstraint().toList().isEmpty());
        assertTrue(c2.getConstraint().toList().isEmpty());
        assertTrue(c3.getConstraint().toList().isEmpty());
        assertTrue(c4.getConstraint().toList().isEmpty());


        ItemCollection itemCollection;

        itemCollection = c5.getConstraint();
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(3, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));

        itemCollection = c6.getConstraint();
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(2, itemCollection.count(Corner.ANIMAL));
        assertEquals(3, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));

        itemCollection = c7.getConstraint();
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(2, itemCollection.count(Corner.PLANT));
        assertEquals(3, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));

        itemCollection = c8.getConstraint();
        assertEquals(3, itemCollection.count(Corner.FUNGUS));
        assertEquals(2, itemCollection.count(Corner.PLANT));
        assertEquals(1, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
    }

    @Test
    void getScoringStrategy() {
        ScoringStrategy scoringStrategy;

        scoringStrategy = c1.getScoringStrategy();
        assertInstanceOf(FreeScoreScoringStrategy.class, scoringStrategy);

        scoringStrategy = c2.getScoringStrategy();
        assertInstanceOf(FreeScoreScoringStrategy.class, scoringStrategy);

        scoringStrategy = c3.getScoringStrategy();
        assertInstanceOf(FreeScoreScoringStrategy.class, scoringStrategy);

        scoringStrategy = c4.getScoringStrategy();
        assertInstanceOf(FreeScoreScoringStrategy.class, scoringStrategy);

        scoringStrategy = c5.getScoringStrategy();
        assertInstanceOf(CoveredCornersScoringStrategy.class, scoringStrategy);

        scoringStrategy = c6.getScoringStrategy();
        assertInstanceOf(FreeScoreScoringStrategy.class, scoringStrategy);

        scoringStrategy = c7.getScoringStrategy();
        assertInstanceOf(ItemCountScoringStrategy.class, scoringStrategy);

        scoringStrategy = c8.getScoringStrategy();
        assertInstanceOf(CoveredCornersScoringStrategy.class, scoringStrategy);
    }

    @Test
    void getType() {
        assertEquals(Resource.FUNGUS, c1.getType());
        assertEquals(Resource.PLANT, c2.getType());
        assertEquals(Resource.ANIMAL, c3.getType());
        assertEquals(Resource.INSECT, c4.getType());
        assertEquals(Resource.FUNGUS, c5.getType());
        assertEquals(Resource.PLANT, c6.getType());
        assertEquals(Resource.ANIMAL, c7.getType());
        assertEquals(Resource.INSECT, c8.getType());
    }

    @Test
    void isGold() {
        assertFalse(c1.isGold());
        assertFalse(c2.isGold());
        assertFalse(c3.isGold());
        assertFalse(c4.isGold());
        assertTrue(c5.isGold());
        assertTrue(c6.isGold());
        assertTrue(c7.isGold());
        assertTrue(c8.isGold());
    }

    @Test
    void getResourceType() {
        assertEquals(Resource.FUNGUS, c1.getResourceType());
        assertEquals(Resource.PLANT, c2.getResourceType());
        assertEquals(Resource.ANIMAL, c3.getResourceType());
        assertEquals(Resource.INSECT, c4.getResourceType());
        assertEquals(Resource.FUNGUS, c5.getResourceType());
        assertEquals(Resource.PLANT, c6.getResourceType());
        assertEquals(Resource.ANIMAL, c7.getResourceType());
        assertEquals(Resource.INSECT, c8.getResourceType());
    }
}