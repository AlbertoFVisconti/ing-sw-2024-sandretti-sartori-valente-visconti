package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.CoveredCornersScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.FreeScoreScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ItemCountScoringStrategy;
import it.polimi.ingsw.utils.ItemCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardSlotTest {
    private Card c1, c2, c3, c4, c5, c6, c7, c8, c9, c10;
    private Card[] cards;

    private CardSlot cs_f1, cs_f2, cs_f3, cs_f4, cs_f5, cs_f6, cs_f7, cs_f8, cs_f9, cs_f10;
    private CardSlot cs_b1, cs_b2, cs_b3, cs_b4, cs_b5, cs_b6, cs_b7, cs_b8, cs_b9, cs_b10;
    private CardSlot[] cardSlotsFront;
    private CardSlot[] cardSlotsBack;

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

        cs_f1 = new CardSlot(c1, false, 0);
        cs_f2 = new CardSlot(c2, false, 1);
        cs_f3 = new CardSlot(c3, false, 2);
        cs_f4 = new CardSlot(c4, false, 3);
        cs_f5 = new CardSlot(c5, false, 4);
        cs_f6 = new CardSlot(c6, false, 5);
        cs_f7 = new CardSlot(c7, false, 6);
        cs_f8 = new CardSlot(c8, false, 7);
        cs_f9 = new CardSlot(c9, false, 8);
        cs_f10 =new CardSlot(c10, false, 9);

        cs_b1 = new CardSlot(c1, true, 10);
        cs_b2 = new CardSlot(c2, true, 11);
        cs_b3 = new CardSlot(c3, true, 12);
        cs_b4 = new CardSlot(c4, true, 13);
        cs_b5 = new CardSlot(c5, true, 14);
        cs_b6 = new CardSlot(c6, true, 15);
        cs_b7 = new CardSlot(c7, true, 16);
        cs_b8 = new CardSlot(c8, true, 17);
        cs_b9 = new CardSlot(c9, true, 18);
        cs_b10 =new CardSlot(c10, true, 19);

        cardSlotsFront = new CardSlot[]{cs_f1, cs_f2, cs_f3, cs_f4, cs_f5, cs_f6, cs_f7, cs_f8, cs_f9, cs_f10};
        cardSlotsBack  = new CardSlot[]{cs_b1, cs_b2, cs_b3, cs_b4, cs_b5, cs_b6, cs_b7, cs_b8, cs_b9, cs_b10};
    }
    @Test
    void getTopRightCorner() {
        assertEquals(Corner.EMPTY, cs_b1.getTopRightCorner());
        assertEquals(Corner.ANIMAL, cs_f1.getTopRightCorner());

        assertEquals(Corner.EMPTY, cs_b2.getTopRightCorner());
        assertEquals(Corner.EMPTY, cs_f2.getTopRightCorner());

        assertEquals(Corner.EMPTY, cs_b3.getTopRightCorner());
        assertNull(cs_f3.getTopRightCorner());

        assertEquals(Corner.EMPTY, cs_b4.getTopRightCorner());
        assertEquals(Corner.ANIMAL, cs_f4.getTopRightCorner());

        assertEquals(Corner.EMPTY, cs_b5.getTopRightCorner());
        assertEquals(Corner.ANIMAL, cs_f5.getTopRightCorner());

        assertEquals(Corner.EMPTY, cs_b6.getTopRightCorner());
        assertEquals(Corner.EMPTY, cs_f6.getTopRightCorner());

        assertEquals(Corner.EMPTY, cs_b7.getTopRightCorner());
        assertNull(cs_f7.getTopRightCorner());

        assertEquals(Corner.EMPTY, cs_b8.getTopRightCorner());
        assertNull(cs_f8.getTopRightCorner());

        assertEquals(Corner.PLANT, cs_b9.getTopRightCorner());
        assertEquals(Corner.EMPTY, cs_f9.getTopRightCorner());

        assertEquals(Corner.INSECT, cs_b10.getTopRightCorner());
        assertNull(cs_f10.getTopRightCorner());
    }

    @Test
    void getBottomLeftCorner() {
        assertEquals(Corner.EMPTY, cs_b1.getBottomLeftCorner());
        assertEquals(Corner.INSECT, cs_f1.getBottomLeftCorner());

        assertEquals(Corner.EMPTY, cs_b2.getBottomLeftCorner());
        assertEquals(Corner.EMPTY, cs_f2.getBottomLeftCorner());

        assertEquals(Corner.EMPTY, cs_b3.getBottomLeftCorner());
        assertEquals(Corner.INSECT, cs_f3.getBottomLeftCorner());

        assertEquals(Corner.EMPTY, cs_b4.getBottomLeftCorner());
        assertEquals(Corner.EMPTY, cs_f4.getBottomLeftCorner());

        assertEquals(Corner.EMPTY, cs_b5.getBottomLeftCorner());
        assertNull(cs_f5.getBottomLeftCorner());

        assertEquals(Corner.EMPTY, cs_b6.getBottomLeftCorner());
        assertEquals(Corner.PLANT, cs_f6.getBottomLeftCorner());

        assertEquals(Corner.EMPTY, cs_b7.getBottomLeftCorner());
        assertEquals(Corner.INSECT, cs_f7.getBottomLeftCorner());

        assertEquals(Corner.EMPTY, cs_b8.getBottomLeftCorner());
        assertEquals(Corner.FUNGUS, cs_f8.getBottomLeftCorner());

        assertEquals(Corner.FUNGUS, cs_b9.getBottomLeftCorner());
        assertEquals(Corner.EMPTY, cs_f9.getBottomLeftCorner());

        assertEquals(Corner.EMPTY,cs_b10.getBottomLeftCorner());
        assertEquals(Corner.INSECT, cs_f10.getBottomLeftCorner());
    }

    @Test
    void getBottomRightCorner() {
        assertEquals(Corner.EMPTY, cs_b1.getBottomRightCorner());
        assertNull(cs_f1.getBottomRightCorner());

        assertEquals(Corner.EMPTY, cs_b2.getBottomRightCorner());
        assertEquals(Corner.EMPTY, cs_f2.getBottomRightCorner());

        assertEquals(Corner.EMPTY, cs_b3.getBottomRightCorner());
        assertNull(cs_f3.getBottomRightCorner());

        assertEquals(Corner.EMPTY, cs_b4.getBottomRightCorner());
        assertEquals(Corner.EMPTY, cs_f4.getBottomRightCorner());

        assertEquals(Corner.EMPTY, cs_b5.getBottomRightCorner());
        assertNull(cs_f5.getBottomRightCorner());

        assertEquals(Corner.EMPTY, cs_b6.getBottomRightCorner());
        assertNull(cs_f6.getBottomRightCorner());

        assertEquals(Corner.EMPTY, cs_b7.getBottomRightCorner());
        assertEquals(Corner.EMPTY, cs_f7.getBottomRightCorner());

        assertEquals(Corner.EMPTY, cs_b8.getBottomRightCorner());
        assertEquals(Corner.FUNGUS, cs_f8.getBottomRightCorner());

        assertEquals(Corner.INSECT, cs_b9.getBottomRightCorner());
        assertEquals(Corner.PLANT, cs_f9.getBottomRightCorner());

        assertNull(cs_b10.getBottomRightCorner());
        assertEquals(Corner.PLANT, cs_f10.getBottomRightCorner());
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
    void card() {
        int i = 0;
        for(CardSlot cardSlot : cardSlotsFront) {
            assertEquals(cards[i], cardSlot.card());
            i++;
        }

        i = 0;
        for(CardSlot cardSlot : cardSlotsBack) {
            assertEquals(cards[i], cardSlot.card());
            i++;
        }
    }

    @Test
    void onBackSide() {
        for(CardSlot cardSlot : cardSlotsFront) {
            assertFalse(cardSlot.onBackSide());
        }
        for(CardSlot cardSlot : cardSlotsBack) {
            assertTrue(cardSlot.onBackSide());
        }
    }

    @Test
    void placementTurn() {
        for(int i = 0; i < cardSlotsFront.length; i++) {
            assertEquals(i, cardSlotsFront[i].placementTurn());
            assertEquals(i+10,cardSlotsBack[i].placementTurn());
        }
    }
}