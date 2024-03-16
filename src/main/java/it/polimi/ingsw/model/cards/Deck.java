package it.polimi.ingsw.model.cards;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

public class Deck {
    private List<Card> remaining;
    private Card[] visibleCard= new Card[3];

    public Deck(/*TODO*/) {
        // TODO
    }

    // TODO: setup method(s)

    private Card getRandomCard(){
        if(remaining.isEmpty()) return null;

        int selectedCardIndex=(int) (Math.random()*remaining.size());
        return remaining.remove(selectedCardIndex); // remove returns the removed element
    }
    public Card getCard(int i)  {
        if(i < 0 || i > 2) throw new InvalidParameterException("check the param of the getCard function");
        if(visibleCard[i] == null) throw new NoSuchElementException("parameter is in the approved range, but there's no card here");

        // if not null, the card on top of the deck always needs to be flipped
        if(visibleCard[0] != null) visibleCard[0].flip();

        Card pickedCard= visibleCard[i];

        // redundant if i == 0 otherwise replace the picked card with the card on top of the deck (already flipped)
        visibleCard[i]=visibleCard[0];

        // get the next card to place on top of the deck upside down
        visibleCard[0]=getRandomCard();
        if(visibleCard[0] != null) visibleCard[0].flip();

        return pickedCard;
    }
}
