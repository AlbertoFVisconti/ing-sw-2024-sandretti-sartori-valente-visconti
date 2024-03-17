package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.cards.StartCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 *  A deck of StartCards.
 *  It allows to load, store and distribute StartCards.
 */
public class StartCardDeck extends Deck<StartCard>{
    /**
     * Constructs a StartCardDeck by loading its contents from a file.
     *
     * @param fileName the name of the file containing the deck's data
     * @throws IOException if there's a problem when trying to read the file
     */
    public StartCardDeck(String fileName) throws IOException {
        super(fileName);
    }

    /**
     * Loads the StartCards' data from the file and returns them as a list
     * of PlayCard objects.
     *
     * @param fileName the name of the file containing the deck's data
     * @return a list of StartCard objects representing the deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    @Override
    protected List<StartCard> loadFromFile(String fileName) throws IOException {
        // TODO
        return new ArrayList<>();
    }

    /**
     * Draws a random StartCard from the deck and returns it.
     * It also assures that the drawn card is actually valid before returning it.
     *
     * @return a random StartCard among the remaining ones
     * @throws EmptyStackException if the deck is empty
     */
    public StartCard draw() {
        StartCard c = this.drawRandom();
        if(c == null) throw new EmptyStackException();

        return c;
    }
}
