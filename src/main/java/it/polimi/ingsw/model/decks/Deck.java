package it.polimi.ingsw.model.decks;

import java.io.IOException;
import java.util.List;

/**
 * An abstract representation of the concept of deck of a specified type of element.
 * Expanding classes must provide methods to load the deck's content from a file
 * and to draw the content of the resulting deck
 *
 * @param <T> the type of elements contained by the deck
 */
abstract class Deck<T>{
    private final List<T> remaining;

    /**
     * Constructs e sets up a new deck from a specified file.
     *
     * @param fileName the name of the file to load the deck content from
     * @throws IOException if there's a problem when trying to read the file
     */
    public Deck(String fileName) throws IOException {
        this.remaining = loadFromFile(fileName);
    }

    /**
     * Loads the deck's contents from a given file. This method must be implemented
     * by subclasses to specify how the deck is loaded
     *
     * @param fileName the name of the file containing the deck's data
     * @return a list of elements T representing the deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    protected abstract List<T> loadFromFile(String fileName) throws IOException;

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
    public abstract T draw();

}
