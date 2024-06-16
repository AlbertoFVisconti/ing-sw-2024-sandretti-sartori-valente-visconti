package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.CoveredCornersScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.FreeScoreScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ItemCountScoringStrategy;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.utils.ItemCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PlayerTest {

    private Card c1, c2, c3, c4, c5, c6, c7, c8, c9, c10;
    private Card[] cards;

    /**
     * placeCardTest check if after method placeCard is called, the card is correctly placed in the board
     **/

    @Test
    void placeCardTest() throws Exception {
        Player player = new Player("Pippo123", null);
        PlayCard card1 = PlayCard.generateResourceCard("", "", "", Corner.FUNGUS, Corner.EMPTY, Corner.FUNGUS, null, Resource.FUNGUS, 0);
        PlayCard card2 = PlayCard.generateResourceCard("", "", "",Corner.FUNGUS, Corner.FUNGUS, null, Corner.EMPTY, Resource.FUNGUS, 0);
        PlayCard gcard1 = PlayCard.generateGoldCard("", "", "", Corner.EMPTY, null, Corner.EMPTY, null, Resource.FUNGUS, new ItemCollection().add(Corner.FUNGUS, 5), new FreeScoreScoringStrategy(5));
        player.addPlayerCard(card1);
        player.addPlayerCard(card2);
        player.addPlayerCard(gcard1);
        CardLocation x = new CardLocation(1, 1);
        player.placeCard(player.getPlayerCard(0), false, x);

        assertEquals(player.getPlacedCardSlot(x).card(), card1);
    }

    Player p1,p2,p3,p4,p5;

    @BeforeEach
    void setup() {
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

        p1 = new Player("p1", null);
        p2 = new Player("p2", null);
        p3 = new Player("p3", null);
        p4 = new Player("p4", null);
        p5 = new Player("p5", null);

        p1.setColor(PlayerColor.RED);
        p2.setColor(PlayerColor.GREEN);
        p3.setColor(PlayerColor.BLUE);
        p4.setColor(PlayerColor.YELLOW);

        p1.setStartCard((StartCard)c9);
        p2.setStartCard((StartCard)c9);
        p3.setStartCard((StartCard)c10);

        p1.setPlayerCard((PlayCard) c1, 0);
        p1.setPlayerCard((PlayCard)c2, 1);
        p1.setPlayerCard((PlayCard)c3, 2);

        p2.setPlayerCard((PlayCard)c4, 0);
        p2.setPlayerCard((PlayCard)c5, 1);
        p2.setPlayerCard((PlayCard)c6, 2);

        p3.setPlayerCard((PlayCard)c1, 0);
        p3.setPlayerCard((PlayCard)c4, 1);
        p3.setPlayerCard((PlayCard)c7, 2);

        p4.setPlayerCard((PlayCard)c6, 0);
        p4.setPlayerCard((PlayCard)c8, 1);
    }


    @Test
    void getSaving() {
    }

    @Test
    void getClientSaving() {
    }

    @Test
    void getSetColor() {
        assertEquals(PlayerColor.RED, p1.getColor());
        assertEquals(PlayerColor.GREEN, p2.getColor());
        assertEquals(PlayerColor.BLUE, p3.getColor());
        assertEquals(PlayerColor.YELLOW, p4.getColor());
        assertNull(p5.getColor());

        p1.setColor(null);
        p2.setColor(PlayerColor.RED);
        p3.setColor(PlayerColor.GREEN);
        p4.setColor(PlayerColor.BLUE);
        p5.setColor(PlayerColor.YELLOW);

        assertNull(p1.getColor());
        assertEquals(PlayerColor.RED, p2.getColor());
        assertEquals(PlayerColor.GREEN, p3.getColor());
        assertEquals(PlayerColor.BLUE, p4.getColor());
        assertEquals(PlayerColor.YELLOW, p5.getColor());

        p1.setColor(null);
        p2.setColor(null);
        p3.setColor(null);
        p4.setColor(null);
        p5.setColor(null);

        assertNull(p1.getColor());
        assertNull(p2.getColor());
        assertNull(p3.getColor());
        assertNull(p4.getColor());
        assertNull(p5.getColor());

        p1.setColor(PlayerColor.RED);
        p2.setColor(PlayerColor.RED);
        p3.setColor(PlayerColor.RED);
        p4.setColor(PlayerColor.RED);
        p5.setColor(PlayerColor.RED);

        assertEquals(PlayerColor.RED, p1.getColor());
        assertEquals(PlayerColor.RED, p2.getColor());
        assertEquals(PlayerColor.RED, p3.getColor());
        assertEquals(PlayerColor.RED, p4.getColor());
        assertEquals(PlayerColor.RED, p5.getColor());

        p1.setColor(PlayerColor.BLUE);
        p2.setColor(PlayerColor.BLUE);
        p3.setColor(PlayerColor.BLUE);
        p4.setColor(PlayerColor.BLUE);
        p5.setColor(PlayerColor.BLUE);

        assertEquals(PlayerColor.BLUE, p1.getColor());
        assertEquals(PlayerColor.BLUE, p2.getColor());
        assertEquals(PlayerColor.BLUE, p3.getColor());
        assertEquals(PlayerColor.BLUE, p4.getColor());
        assertEquals(PlayerColor.BLUE, p5.getColor());

        p1.setColor(PlayerColor.GREEN);
        p2.setColor(PlayerColor.YELLOW);
        p3.setColor(PlayerColor.GREEN);
        p4.setColor(PlayerColor.YELLOW);
        p5.setColor(PlayerColor.GREEN);

        assertEquals(PlayerColor.GREEN, p1.getColor());
        assertEquals(PlayerColor.YELLOW, p2.getColor());
        assertEquals(PlayerColor.GREEN, p3.getColor());
        assertEquals(PlayerColor.YELLOW, p4.getColor());
        assertEquals(PlayerColor.GREEN, p5.getColor());
    }

    @Test
    void getSetStartCard() {
        assertEquals(c9, p1.getStartCard());
        assertEquals(c9, p2.getStartCard());
        assertEquals(c10, p3.getStartCard());

        p1.setStartCard(null);
        p2.setStartCard(null);
        p3.setStartCard((StartCard)c9);

        assertNull(p1.getStartCard());
        assertNull(p2.getStartCard());
        assertEquals(c9, p3.getStartCard());

        StartCard newStartCard = new StartCard(
                "newCard",
                "fn1",
                "bn1",
                Corner.ANIMAL, Corner.EMPTY,
                Corner.EMPTY, Corner.INSECT,
                Corner.EMPTY, Corner.EMPTY,
                null, null,
                new ItemCollection().add(Corner.PLANT, 1000)
        );

        p1.setStartCard(newStartCard);
        p2.setStartCard((StartCard)c9);
        p3.setStartCard((StartCard)c10);

        assertEquals(newStartCard, p1.getStartCard());
        assertEquals(c9, p2.getStartCard());
        assertEquals(c10, p3.getStartCard());

        p1.setStartCard(newStartCard);
        p2.setStartCard(newStartCard);
        p3.setStartCard(newStartCard);

        assertEquals(newStartCard, p1.getStartCard());
        assertEquals(newStartCard, p2.getStartCard());
        assertEquals(newStartCard, p3.getStartCard());
    }

    @Test
    void setPlayerCard() {
    }

    @Test
    void setAvailableGoals() {
    }

    @Test
    void placeCard() {
    }

    @Test
    void placeStartingCard() {
    }

    @Test
    void getPrivateGoal() {
    }

    @Test
    void setPrivateGoal() {
    }

    @Test
    void getPlacedCardSlot() {
    }

    @Test
    void getBoard() {
    }

    @Test
    void getInventory() {
    }

    @Test
    void addItems() {
    }

    @Test
    void addItem() {
    }

    @Test
    void removeItems() {
    }

    @Test
    void removeItem() {
    }

    @Test
    void hasDisconnected() {
    }

    @Test
    void addPlayerCard() {
    }

    @Test
    void getAvailableGoals() {
    }
    @Test
    void getPlayerCards() {
    }

    @Test
    void getPlayerCard() {
    }
}



