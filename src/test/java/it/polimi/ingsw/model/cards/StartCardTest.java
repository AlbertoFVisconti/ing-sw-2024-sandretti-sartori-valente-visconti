package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.utils.ItemCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartCardTest {
    private StartCard c1, c2;
    @BeforeEach
    void setupCards() {
        this.c1 = new StartCard("c1", "front_1", "back_1",
                Corner.ANIMAL, Corner.EMPTY,
                Corner.EMPTY, Corner.PLANT,

                Corner.ANIMAL, Corner.PLANT,
                Corner.FUNGUS, Corner.INSECT,
                new ItemCollection().add(Corner.PLANT).add(Corner.FUNGUS, 2));

        this.c2 = new StartCard("c2", "front_2", "back_2",
                Corner.PLANT, null,
                Corner.INSECT, Corner.PLANT,

                null, Corner.INSECT,
                Corner.EMPTY, null,
                new ItemCollection().add(Corner.INSECT, 1).add(Corner.ANIMAL, 2));

    }

    @Test
    void getPermanentResources() {
        ItemCollection itemCollection;

        itemCollection = c1.getPermanentResources();
        assertEquals(2, itemCollection.count(Corner.FUNGUS));
        assertEquals(1, itemCollection.count(Corner.PLANT));
        assertEquals(0, itemCollection.count(Corner.ANIMAL));
        assertEquals(0, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));

        itemCollection = c2.getPermanentResources();
        assertEquals(0, itemCollection.count(Corner.FUNGUS));
        assertEquals(0, itemCollection.count(Corner.PLANT));
        assertEquals(2, itemCollection.count(Corner.ANIMAL));
        assertEquals(1, itemCollection.count(Corner.INSECT));
        assertEquals(0, itemCollection.count(Corner.FEATHER));
        assertEquals(0, itemCollection.count(Corner.INK));
        assertEquals(0, itemCollection.count(Corner.SCROLL));
    }

    @Test
    void getResourceType() {
        assertNull(c1.getResourceType());
        assertNull(c2.getResourceType());
    }
}