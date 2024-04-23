package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.ItemCollection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemGoalTest {
    @Test
    void testEvaluate1() {
        Player player = new Player("","test1Player", PlayerColor.BLUE, null);

        // sets up the inventory of the player
        // so that they've the exact number of items described by the instructions below
        // we assume that Player.addItems() works as intended (since they will be tested in the PlayerTest's tests)
        // we also assume that ItemCollection works fine
        // ItemCollectionTest's tests passing prove that much
        // These instructions don't look very "handy", but this kind of operation
        // is never actually needed in the game logic, thus this is just for testing purpose
        player.addItems(new ItemCollection().add(Corner.FUNGUS,  100));
        player.addItems(new ItemCollection().add(Corner.PLANT,   200));
        player.addItems(new ItemCollection().add(Corner.ANIMAL,  300));
        player.addItems(new ItemCollection().add(Corner.INSECT,  10));
        player.addItems(new ItemCollection().add(Corner.SCROLL,  100));
        player.addItems(new ItemCollection().add(Corner.INK,     123));
        player.addItems(new ItemCollection().add(Corner.FEATHER, 10));


        ItemCollection goalConstraint = new ItemCollection();
        goalConstraint.add(Corner.FUNGUS);

        // the player obtains 1 point for each FUNGUS they have
        //      ==> the player has 100 FUNGI, thus they should obtain
        //          1*100 = 100 points
        ItemGoal goal1 = new ItemGoal(goalConstraint, 1);

        goalConstraint = new ItemCollection();
        goalConstraint.add(Corner.ANIMAL, 6);

        // the player obtains 2 points for each set of 6 ANIMALs they have
        //      ==> the player has 300 ANIMALs, thus they should obtain
        //          300 / 6 = 50 sets
        //          50 * 2 = 100 points
        ItemGoal goal2 = new ItemGoal(goalConstraint, 2);

        goalConstraint = new ItemCollection();
        goalConstraint.add(Corner.FUNGUS, 2)
                .add(Corner.PLANT, 2);

        // the player obtains 10 points for each set of 2 FUNGI and 2 PLANTs they have
        //      ==> the player has 100 FUNGI and 200 PLANTs, thus they should obtain
        //          100 / 2 = 50 sets of 2 FUNGI
        //          200 / 2 = 100 sets of 2 PLANTS
        //          we are constraint by the 50 sets of FUNGI, thus
        //          50 * 10 = 500 points
        ItemGoal goal3 = new ItemGoal(goalConstraint, 10);

        goalConstraint = new ItemCollection();
        goalConstraint.add(Corner.FEATHER, 5)
                .add(Corner.INSECT, 5)
                .add(Corner.ANIMAL, 1);

        // the player obtains 1 point for each set of 5 FEATHERs, 5 INSECTs and 1 ANIMAL they have
        //      ==> the player has 10 FEATHERs, 10 INSECTs and 300 ANIMALs, thus they should obtain
        //          10 / 5 = 2 sets of 5 FEATHERs
        //          10 / 5 = 2 sets of 5 INSECTs
        //          300 / 1 = 300 sets of 1 ANIMAL
        //          we are constraint by the 2 sets of FEATHERs (or INSECTs), thus
        //          2 * 1 = 2 points
        ItemGoal goal4 = new ItemGoal(goalConstraint, 1);

        assertEquals(100, goal1.evaluate(player));
        assertEquals(100, goal2.evaluate(player));
        assertEquals(500, goal3.evaluate(player));
        assertEquals(2, goal4.evaluate(player));
    }

    @Test
    void testEvaluate2() {
        Player player = new Player("","test2Player", PlayerColor.RED, null);

        // sets up the inventory of the player
        // so that they've the exact number of items described by the instructions below
        // we assume that Player.addItems() works as intended (since they will be tested in the PlayerTest's tests)
        // we also assume that ItemCollection works fine
        // ItemCollectionTest's tests passing prove that much
        // These instructions don't look very "handy", but this kind of operation
        // is never actually needed in the game logic, thus this is just for testing purpose

        // player.addItems(new ItemCollection().add(Corner.FUNGUS,  0));        redundant: the player has 0 FUNGI
        player.addItems(new ItemCollection().add(Corner.PLANT,   3));
        player.addItems(new ItemCollection().add(Corner.ANIMAL,  5));
        player.addItems(new ItemCollection().add(Corner.INSECT,  2));
        player.addItems(new ItemCollection().add(Corner.SCROLL,  3));
        player.addItems(new ItemCollection().add(Corner.INK,     13));
        player.addItems(new ItemCollection().add(Corner.FEATHER, 1000));


        ItemCollection goalConstraint = new ItemCollection();
        goalConstraint.add(Corner.FUNGUS);

        // the player obtains 1 point for each FUNGUS they have
        //      ==> the player has 0 FUNGI, thus they should obtain
        //          0 points
        ItemGoal goal1 = new ItemGoal(goalConstraint, 1);

        goalConstraint = new ItemCollection();
        goalConstraint.add(Corner.INK, 4);

        // the player obtains 33 points for each set of 4 INKs they have
        //      ==> the player has 13 INKs, thus they should obtain
        //          13 / 4 = 3 sets
        //          3 * 33 = 99 points
        ItemGoal goal2 = new ItemGoal(goalConstraint, 33);

        goalConstraint = new ItemCollection();
        goalConstraint.add(Corner.INK, 2)
                .add(Corner.FEATHER, 250);

        // the player obtains 1000 points for each set of 2 INKs and 250 FEATHERs they have
        //      ==> the player has 13 INKs and 1000 FEATHERs, thus they should obtain
        //          13 / 2 = 6 sets of 2 INKs
        //          1000 / 250 = 4 sets of 2 FEATHERs
        //          we are constraint by the 4 sets of FEATHERs, thus
        //          4 * 1000 = 4000 points
        ItemGoal goal3 = new ItemGoal(goalConstraint, 1000);

        goalConstraint = new ItemCollection();
        goalConstraint.add(Corner.PLANT, 1)
                .add(Corner.ANIMAL, 1)
                .add(Corner.INSECT, 1)
                .add(Corner.FEATHER, 1)
                .add(Corner.INK, 1)
                .add(Corner.SCROLL, 1);

        // the player obtains 4 point for each set that contains every single item (except FUNGI) they have
        //      ==> among the required items, the ones the player lacks the most, are INSECTS:
        //          2 * 4 = 8 points
        ItemGoal goal4 = new ItemGoal(goalConstraint, 4);

        assertEquals(0, goal1.evaluate(player));
        assertEquals(99, goal2.evaluate(player));
        assertEquals(4000, goal3.evaluate(player));
        assertEquals(8, goal4.evaluate(player));
    }
}