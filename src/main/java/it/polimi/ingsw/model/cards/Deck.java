package it.polimi.ingsw.model.cards;

import java.security.InvalidParameterException;
import java.util.EmptyStackException;
import java.util.List;
import java.util.NoSuchElementException;

public class Deck {
    private List<Card> remaining;
    private  Card[] visibleCard= new Card[3];

    public Deck(/*TODO*/) {
        // TODO
    }

    // TODO: setup method(s)

    private Card getRandomCard(){
        if(remaining.isEmpty()) return null;

        int selectedCardIndex=(int) (Math.random()*remaining.size());

        Card temp= remaining.get(selectedCardIndex);
        remaining.remove(selectedCardIndex);

        return temp;
    }
    public Card getCard(int i)  {
        if(i < 0 || i > 2) throw new InvalidParameterException("check the param of the getCard function");
        if(visibleCard[i] == null) throw new NoSuchElementException("parameter is in the approved range, but there's no card here");

        Card temp= visibleCard[i];

        // redundant if i == 0
        visibleCard[i]=visibleCard[0];

        // this line always flip the card on top of the deck (which is always with the back side up)
        // this mean that: if i == 0 it will flip the selected card before returning it
        // otherwise, it will flip the card so that every card but the first has the front side up
        if(visibleCard[i] != null) visibleCard[i].flip();

        // in any case a new card needs to be put in visibleCard[0] (top of the deck) and flipped so that it shows the back (if not null)
        visibleCard[0]=getRandomCard();
        if(visibleCard[0] != null) visibleCard[0].flip();

        return temp;
    }
}
