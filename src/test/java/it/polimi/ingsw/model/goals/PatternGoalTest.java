package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CardLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatternGoalTest {

    @Test
    void testEvaluate1() {
        // goal1's pattern requires 2 ANIMAL cards one on top of the other
        // the card on top has an INSECT card as top-left neighbour
        PatternGoal goal1 = new PatternGoal(
                new Resource[][]{
                        {Resource.INSECT, Resource.ANIMAL},
                        {null, Resource.ANIMAL}
                },
                10,
                ""
        );

        // goal2's pattern requires just an INSECT card by itself
        PatternGoal goal2 = new PatternGoal(
                new Resource[][]{
                        {Resource.INSECT}
                },
                2,
                ""
        );

        // goal3's pattern requires just an ANIMAL card by itself
        PatternGoal goal3 = new PatternGoal(
                new Resource[][]{
                        {Resource.ANIMAL}
                },
                3,
                ""
        );

        // goal4's pattern requires 3 INSECT cards one on top of the other
        // the card on top has an ANIMAL card as top-right neighbour
        PatternGoal goal4 = new PatternGoal(
                new Resource[][]{
                        {null, Resource.ANIMAL},
                        {Resource.INSECT, null},
                        {Resource.INSECT, null},
                        {Resource.INSECT, null}
                },
                12,
                ""
        );

        Player p = new Player("", null);

        // creates a board that has 21 ANIMAL cards one on top of the other
        // each ANIMAL card also has an INSECT card as its top-left neighbour
        for (int i = 0; i < 21; i++) {
            PlayCard c1 = PlayCard.generateResourceCard("", "", "",Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.ANIMAL, 2);
            PlayCard c2 = PlayCard.generateResourceCard("", "","",Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.INSECT, 2);

            p.setPlayerCard(c1, 0);
            p.placeCard(p.getPlayerCard(0), false, new CardLocation(0, -i * 2));
            p.setPlayerCard(c2, 0);
            p.placeCard(p.getPlayerCard(0), false, new CardLocation(-1, 1 + -i * 2));

        }

        // goal1's pattern has a maximum of 10 non-overlapping matches
        // thus 10*10 = 100 points
        assertEquals(100, goal1.evaluate(p));
        // there are exactly 21 INSECT cards in the board
        // thus 21 * 2 = 42 points
        assertEquals(42, goal2.evaluate(p));
        // there are exactly 21 ANIMAL cards in the board
        // thus 21 * 3 = 63 points
        assertEquals(63, goal3.evaluate(p));
        // goal4's pattern has a maximum of 6 non-overlapping matches
        // thus 6*12 = 72 points
        assertEquals(72, goal4.evaluate(p));
    }


    @Test
    void testEvaluate2() {
        // similar to testEvaluate1
        // it uses the same goals and initializes the board in a similar manner,
        // but it creates a wider board with multiple groups of conflicting matches

        // goal1's pattern requires 2 ANIMAL cards one on top of the other
        // the card on top has an INSECT card as top-left neighbour
        PatternGoal goal1 = new PatternGoal(
                new Resource[][]{
                        {Resource.INSECT, Resource.ANIMAL},
                        {null, Resource.ANIMAL}
                },
                10,
                ""
        );

        // goal2's pattern requires just an INSECT card by itself
        PatternGoal goal2 = new PatternGoal(
                new Resource[][]{
                        {Resource.INSECT}
                },
                2,
                ""
        );

        // goal3's pattern requires just an ANIMAL card by itself
        PatternGoal goal3 = new PatternGoal(
                new Resource[][]{
                        {Resource.ANIMAL}
                },
                3,
                ""
        );

        // goal4's pattern requires 3 INSECT cards one on top of the other
        // the card on top has an ANIMAL card as top-right neighbour
        PatternGoal goal4 = new PatternGoal(
                new Resource[][]{
                        {null, Resource.ANIMAL},
                        {Resource.INSECT, null},
                        {Resource.INSECT, null},
                        {Resource.INSECT, null}
                },
                12,
                ""
        );

        Player p = new Player("", null);

        // creates a board 4 distant columns
        // Each column has a certain number of ANIMAL cards one on top of the other
        // each ANIMAL card also has an INSECT card as its top-left neighbour
        // this board can't actually appear in a regular game, but it serves as a test case for
        // the (private) method PatternGoal.splitInConnectedGroups(...)

        // creates the first column with 21 ANIMAL cards (+21 INSECTs)
        for (int i = 0; i < 21; i++) {
            PlayCard c1 = PlayCard.generateResourceCard("", "", "", Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.ANIMAL, 2);
            PlayCard c2 = PlayCard.generateResourceCard("", "", "",Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.INSECT, 2);

            p.setPlayerCard(c1, 0);
            p.placeCard(p.getPlayerCard(0), false, new CardLocation(0, -i * 2));
            p.setPlayerCard(c2, 0);
            p.placeCard(p.getPlayerCard(0), false, new CardLocation(-1, 1 + -i * 2));
        }

        // creates the second column with 24 ANIMAL cards (+24 INSECTs)
        for (int i = 0; i < 24; i++) {
            PlayCard c1 = PlayCard.generateResourceCard("", "", "", Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.ANIMAL, 2);
            PlayCard c2 = PlayCard.generateResourceCard("", "","",Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.INSECT, 2);

            p.setPlayerCard(c1, 0);
            p.placeCard(p.getPlayerCard(0), false, new CardLocation(10, -i * 2));
            p.setPlayerCard(c2, 0);
            p.placeCard(p.getPlayerCard(0), false, new CardLocation(9, 1 + -i * 2));

        }

        // creates the third column with 12 ANIMAL cards (+12 INSECTs)
        for (int i = 0; i < 12; i++) {
            PlayCard c1 = PlayCard.generateResourceCard("", "","",Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.ANIMAL, 2);
            PlayCard c2 = PlayCard.generateResourceCard("", "","",Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.INSECT, 2);

            p.setPlayerCard(c1, 0);
            p.placeCard(p.getPlayerCard(0), false, new CardLocation(20, -i * 2));
            p.setPlayerCard(c2, 0);
            p.placeCard(p.getPlayerCard(0), false, new CardLocation(19, 1 + -i * 2));

        }
        // creates the forth column with 29 ANIMAL cards (+29 INSECTs)
        for (int i = 0; i < 29; i++) {
            PlayCard c1 = PlayCard.generateResourceCard("", "","",Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.ANIMAL, 2);
            PlayCard c2 = PlayCard.generateResourceCard("", "","",Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.INSECT, 2);

            p.setPlayerCard(c1, 0);
            p.placeCard(p.getPlayerCard(0), false, new CardLocation(30, -i * 2));
            p.setPlayerCard(c2, 0);
            p.placeCard(p.getPlayerCard(0), false, new CardLocation(29, 1 + -i * 2));

        }

        // goal1's pattern has a maximum of 10+12+6+14 = 42 non-overlapping matches
        // thus 42 * 10 = 420 points
        assertEquals(420, goal1.evaluate(p));
        // there are exactly 21+24+12+29 = 86 INSECT cards in the board
        // thus 86 * 2 = 172 points
        assertEquals(172, goal2.evaluate(p));
        // there are exactly 21+24+12+29 = 86 ANIMAL cards in the board
        // thus 86 * 3 = 258 points
        assertEquals(258, goal3.evaluate(p));
        // goal4's pattern has a maximum of 6+7+3+9 = 25 non-overlapping matches
        // thus 25*12 = 300 points
        assertEquals(300, goal4.evaluate(p));
    }
}