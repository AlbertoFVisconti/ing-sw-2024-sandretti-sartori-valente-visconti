package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.FreeScoreScoringStrategy;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.utils.ItemCollection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PlayerTest {
    /**
     * placeCardTest check if after method placeCard is called, the card is correctly removed from the
     * player's hand and correctly placed in the board
     **/
    @Test
    void placeCardTest() throws Exception {
        Player player = new Player("Pippo123", null);
        PlayCard card1 = PlayCard.generateResourceCard("", Corner.FUNGUS, Corner.EMPTY, Corner.FUNGUS, null, Resource.FUNGUS, 0);
        PlayCard card2 = PlayCard.generateResourceCard("", Corner.FUNGUS, Corner.FUNGUS, null, Corner.EMPTY, Resource.FUNGUS, 0);
        PlayCard gcard1 = PlayCard.generateGoldCard("", Corner.EMPTY, null, Corner.EMPTY, null, Resource.FUNGUS, new ItemCollection().add(Corner.FUNGUS, 5), new FreeScoreScoringStrategy(5));
        player.addPlayerCard(card1);
        player.addPlayerCard(card2);
        player.addPlayerCard(gcard1);
        CardLocation x = new CardLocation(1, 1);
        player.placeCard(0, false, x);
        assertNull(player.getPlayerCards()[0]);
        assertEquals(player.getPlacedCardSlot(x).card(), card1);
    }
}



