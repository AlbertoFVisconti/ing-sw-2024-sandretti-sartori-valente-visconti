package it.polimi.ingsw.model.decks;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.cards.PlayCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;


public class DeckTest<T extends Drawable> {
    /**
     * drawTest1 test if an empty Deck throws correctly the EmptyStackException**/
    @Test
    void drawTest1(){
        List<T> param= new ArrayList<>();
        Deck<T> Deck = new Deck<>(param);
        Exception exception=assertThrows(EmptyStackException.class, ()->Deck.draw());


    }
}
