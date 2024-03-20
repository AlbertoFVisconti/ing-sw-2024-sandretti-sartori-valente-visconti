package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatternGoalTest {

    @Test
    void evaluate() {
        PatternGoal g = new PatternGoal(
                new Resource[][]{
                        {Resource.INSECT, Resource.ANIMAL},
                        {null, Resource.ANIMAL}
                },
                2
        );

        Player p = new Player("", PlayerColor.BLUE);

        for(int i = 0; i < 20; i++) {
            PlayCard c1 = Card.generateResourceCard(Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.ANIMAL, 2);
            PlayCard c2 = Card.generateResourceCard(Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Corner.EMPTY, Resource.INSECT, 2);

            p.setPlayerCard(c1,0);
            p.placeCard(0, new CardLocation(0,-i*2));
            p.setPlayerCard(c2,0);
            p.placeCard(0, new CardLocation(-1,1+-i*2));

        }

        assertEquals(20, g.evaluate(p));
    }
}