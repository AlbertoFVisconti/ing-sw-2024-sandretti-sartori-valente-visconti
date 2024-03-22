package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.decks.loaders.DeckLoader;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.List;

/**
 * An abstract representation of the concept of deck of a specified type of element.
 * It allows the user to draw random elements from the deck.
 *
 * @param <T> the type of elements contained by the deck
 */
public class Deck<T>{
    private final List<T> remaining;

    /**
     * Constructs e sets up a new deck from a specified loader.
     *
     * @param deckLoader the DeckLoader that provides this deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    public Deck(DeckLoader<T> deckLoader) throws IOException {
        this.remaining = deckLoader.getContent();
    }

    /**
     * Takes a random element from the deck's content, removes it from the deck
     * and returns it. Intended for internal usage.
     *
     * @return a random element among the remaining ones
     */
    protected T drawRandom() {
        if(remaining.isEmpty()) return null;

        int selectedIndex=(int) (Math.random()*remaining.size());
        return remaining.remove(selectedIndex); // remove returns the removed element
    }

    /**
     * Allows to actually draw a card from the deck. Subclasses must implement this method
     * in order to specify how the elements are drawn from the deck
     *
     * @return the drawn element
     */
    public T draw() {
        T drawnElement = this.drawRandom();
        if(drawnElement == null) throw new EmptyStackException();
        return drawnElement;
    }
}
