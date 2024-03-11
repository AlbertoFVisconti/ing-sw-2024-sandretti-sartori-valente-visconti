package it.polimi.ingsw.model.cards;

import java.security.InvalidParameterException;
import java.util.EmptyStackException;
import java.util.List;

public class Deck {
    private List<Card> remaining;
    private  Card[] visibleCard= new Card[3];

    private Card getRandomCard(){
        return remaining.get((int) (Math.random()*remaining.size()));
    }
    public Card getCard(int i) throws Exception {
        if (remaining.isEmpty()) throw new EmptyStackException();
        Card temp=null;
        if(i==0){
            temp=visibleCard[0];
            visibleCard[0]=getRandomCard();
        }
        else if(i<3){
            temp=visibleCard[i];
            visibleCard[i]=visibleCard[0];
            visibleCard[i].flip();
            visibleCard[0]=getRandomCard();

        }
        else throw new InvalidParameterException("check the param of the getCard function");
        return temp;
    }
}
