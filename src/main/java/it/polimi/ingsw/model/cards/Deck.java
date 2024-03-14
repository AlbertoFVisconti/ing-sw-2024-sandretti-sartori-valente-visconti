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
        if(i < 0 || i > 3) throw new InvalidParameterException("check the param of the getCard function");
        if(visibleCard[i] == null) throw new NoSuchElementException("parameter is in the approved range, but there's no card here");

        Card temp= visibleCard[i];
        visibleCard[i]=visibleCard[0]; // redundant if i == 0: this line simply does nothing

        // in any case a new card needs to be put in visibleCard[0] (top of the deck) and flipped
        visibleCard[0]=getRandomCard();
        visibleCard[0].flip();

        return temp;
    }
}
