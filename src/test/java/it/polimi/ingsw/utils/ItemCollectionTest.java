package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.cards.corners.Corner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemCollectionTest {
    @Test
    void testAdd() {
        ItemCollection c1 = new ItemCollection();

        c1.add(Corner.ANIMAL)                           // 1 ANIMAL
                .add(Corner.ANIMAL)                     // 2 ANIMALs
                .add(Corner.ANIMAL, 3);          // 5 ANIMALs


        assertEquals(0, c1.count(Corner.FUNGUS));
        assertEquals(0, c1.count(Corner.PLANT));
        assertEquals(5, c1.count(Corner.ANIMAL));
        assertEquals(0, c1.count(Corner.INSECT));

        ItemCollection c2 = new ItemCollection(c1);     // 5 ANIMALs

        c2.add(Corner.INSECT, 10)                // 5 ANIMALs 10 INSECTs
                .add(c1)                                // 10 ANIMALs 10 INSECTs
                .add(c2)                                // 20 ANIMALs 20 INSECTs
                .add(Corner.PLANT, 2);           // 2 PLANTs 20 ANIMALs 20 INSECTs

        assertEquals(0, c2.count(Corner.FUNGUS));
        assertEquals(2, c2.count(Corner.PLANT));
        assertEquals(20, c2.count(Corner.ANIMAL));
        assertEquals(20, c2.count(Corner.INSECT));
    }

    @Test
    void testSub() {
        ItemCollection c1 = new ItemCollection();

        c1.add(Corner.ANIMAL)
                .add(Corner.ANIMAL)
                .add(Corner.ANIMAL, 3)
                .add(Corner.FUNGUS, 3);          // 5 ANIMALs 3 FUNGI


        ItemCollection c2 = new ItemCollection(c1);

        c2.add(Corner.INSECT, 10)
                .add(c1)
                .add(c2)
                .add(Corner.PLANT, 2)
                .add(Corner.FUNGUS, 100);           // 2 PLANTs 20 ANIMALs 20 INSECTs 112 FUNGI


        c1.sub(Corner.ANIMAL)
                .sub(Corner.ANIMAL, 2)
                .sub(Corner.FUNGUS, 1); // 2 ANIMALs 2 FUNGI

        assertEquals(2, c1.count(Corner.FUNGUS));
        assertEquals(0, c1.count(Corner.PLANT));
        assertEquals(2, c1.count(Corner.ANIMAL));
        assertEquals(0, c1.count(Corner.INSECT));


        c2.sub(c1) // 2 PLANTs 18 ANIMALs 20 INSECTs 110 FUNGI
                .sub(c1) // 2 PLANTs 16 ANIMALs 20 INSECTs 108 FUNGI
                .sub(Corner.FUNGUS, 50) // 2 PLANTs 16 ANIMALs 20 INSECTs 58 FUNGI
                .sub(Corner.PLANT).sub(Corner.PLANT) // 0 PLANTs 16 ANIMALs 20 INSECTs 58 FUNGI
                .sub(c1) // 0 PLANTs 14 ANIMALs 20 INSECTs 56 FUNGI
                .sub(Corner.INSECT, 17); // 0 PLANTs 14 ANIMALs 3 INSECTs 56 FUNGI


        assertEquals(56, c2.count(Corner.FUNGUS));
        assertEquals(0, c2.count(Corner.PLANT));
        assertEquals(14, c2.count(Corner.ANIMAL));
        assertEquals(3, c2.count(Corner.INSECT));
    }


    @Test
    void testIsSubSetOf() {
        ItemCollection c1 = new ItemCollection()
                .add(Corner.FUNGUS, 10)
                .add(Corner.PLANT, 10)
                .add(Corner.ANIMAL, 10)
                .add(Corner.INSECT, 10);

        ItemCollection c2 = new ItemCollection()
                .add(Corner.FUNGUS, 5)
                .add(Corner.PLANT, 5)
                .add(Corner.ANIMAL, 1)
                .add(Corner.INSECT, 0);

        assertTrue(c2.isSubSetOf(c1));
        assertFalse(c1.isSubSetOf(c2));

        c2.add(Corner.INSECT, 10);
        // c1: 10 FUNGI, 10 PLANTs, 10 ANIMALs, 10 INSECTs
        // c2: 5 FUNGI, 5 PLANTs, 1 ANIMALs, 10 INSECTs

        assertTrue(c2.isSubSetOf(c1));
        assertFalse(c1.isSubSetOf(c2));

        c1.sub(Corner.ANIMAL, 5);
        // c1: 10 FUNGI, 10 PLANTs, 5 ANIMALs, 10 INSECTs
        // c2: 5 FUNGI, 5 PLANTs, 1 ANIMALs, 10 INSECTs

        assertTrue(c2.isSubSetOf(c1));
        assertFalse(c1.isSubSetOf(c2));

        c2.add(Corner.ANIMAL, 5);
        // c1: 10 FUNGI, 10 PLANTs, 5 ANIMALs, 10 INSECTs
        // c2: 5 FUNGI, 5 PLANTs, 6 ANIMALs, 10 INSECTs

        assertFalse(c2.isSubSetOf(c1));
        assertFalse(c1.isSubSetOf(c2));

        c2.add(Corner.FUNGUS, 5).add(Corner.PLANT, 5);
        // c1: 10 FUNGI, 10 PLANTs, 5 ANIMALs, 10 INSECTs
        // c2: 10 FUNGI, 10 PLANTs, 6 ANIMALs, 10 INSECTs

        assertFalse(c2.isSubSetOf(c1));
        assertTrue(c1.isSubSetOf(c2));

        c1.add(Corner.ANIMAL);

        // c1: 10 FUNGI, 10 PLANTs, 6 ANIMALs, 10 INSECTs
        // c2: 10 FUNGI, 10 PLANTs, 6 ANIMALs, 10 INSECTs

        assertTrue(c2.isSubSetOf(c1));
        assertTrue(c1.isSubSetOf(c2));
    }

    @Test
    void testDivide() {
        ItemCollection c1 = new ItemCollection();

        c1.add(Corner.ANIMAL)
                .add(Corner.ANIMAL)
                .add(Corner.ANIMAL, 3);          // 5 ANIMALs

        ItemCollection c2 = new ItemCollection(c1);

        c2.add(Corner.INSECT, 10)
                .add(c1)
                .add(c2)
                .add(Corner.PLANT, 2);           // 2 PLANTs 20 ANIMALs 20 INSECTs

        ItemCollection c3 = new ItemCollection();

        c3.add(Corner.ANIMAL)
                .add(Corner.ANIMAL)
                .add(Corner.ANIMAL, 3)
                .add(Corner.FUNGUS, 3);          // 5 ANIMALs 3 FUNGI


        ItemCollection c4 = new ItemCollection(c3);

        c4.add(Corner.INSECT, 10)
                .add(c3)
                .add(c4)
                .add(Corner.PLANT, 2)
                .add(Corner.FUNGUS, 100);         // 2 PLANTs 20 ANIMALs 20 INSECTs 112 FUNGI


        assertEquals(1, c1.divide(c1));
        assertEquals(1, c2.divide(c2));
        assertEquals(1, c3.divide(c3));
        assertEquals(1, c4.divide(c4));

        assertEquals(4, c2.divide(c1));
        assertEquals(1, c3.divide(c1));
        assertEquals(4, c4.divide(c1));

        assertEquals(0, c1.divide(c2));
        assertEquals(0, c3.divide(c2));
        assertEquals(1, c4.divide(c2));

        assertEquals(0, c1.divide(c3));
        assertEquals(0, c2.divide(c3));
        assertEquals(4, c4.divide(c3));

        assertEquals(0, c1.divide(c4));
        assertEquals(0, c2.divide(c4));
        assertEquals(0, c3.divide(c4));
    }

    @Test
    void testCount() {
        ItemCollection c1 = new ItemCollection();

        c1.add(Corner.ANIMAL)
                .add(Corner.ANIMAL)
                .add(Corner.ANIMAL, 3);          // 5 ANIMALs

        ItemCollection c2 = new ItemCollection(c1);

        c2.add(Corner.INSECT, 10)
                .add(c1)
                .add(c2)
                .add(Corner.PLANT, 2);           // 2 PLANTs 20 ANIMALs 20 INSECTs

        ItemCollection c3 = new ItemCollection();

        c3.add(Corner.ANIMAL)
                .add(Corner.ANIMAL)
                .add(Corner.ANIMAL, 3)
                .add(Corner.FUNGUS, 3);          // 5 ANIMALs 3 FUNGI


        ItemCollection c4 = new ItemCollection(c3);

        c4.add(Corner.INSECT, 10)
                .add(c3)
                .add(c4)
                .add(Corner.PLANT, 2)
                .add(Corner.FUNGUS, 100);         // 2 PLANTs 20 ANIMALs 20 INSECTs 112 FUNGI

        assertEquals(0, c1.count(Corner.FUNGUS));
        assertEquals(0, c1.count(Corner.PLANT));
        assertEquals(5, c1.count(Corner.ANIMAL));
        assertEquals(0, c1.count(Corner.INSECT));

        assertEquals(0, c2.count(Corner.FUNGUS));
        assertEquals(2, c2.count(Corner.PLANT));
        assertEquals(20, c2.count(Corner.ANIMAL));
        assertEquals(20, c2.count(Corner.INSECT));

        assertEquals(3, c3.count(Corner.FUNGUS));
        assertEquals(0, c3.count(Corner.PLANT));
        assertEquals(5, c3.count(Corner.ANIMAL));
        assertEquals(0, c3.count(Corner.INSECT));

        assertEquals(112, c4.count(Corner.FUNGUS));
        assertEquals(2, c4.count(Corner.PLANT));
        assertEquals(20, c4.count(Corner.ANIMAL));
        assertEquals(20, c4.count(Corner.INSECT));
    }
}
