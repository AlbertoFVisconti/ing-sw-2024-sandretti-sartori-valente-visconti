package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.cards.StartCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class StartCardDeck extends Deck<StartCard>{
    public StartCardDeck(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    protected List<StartCard> loadFromFile(String fileName) throws IOException {
        // TODO
        return new ArrayList<>();
    }

    public StartCard draw() {
        StartCard c = this.drawRandom();
        if(c == null) throw new EmptyStackException();

        return c;
    }
}
