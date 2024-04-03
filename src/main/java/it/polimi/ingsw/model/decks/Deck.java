package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.cards.corners.Resource;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * An abstract representation of the concept of deck of a specified type of element.
 * It allows the user to draw random elements from the deck.
 *
 * @param <T> the type of elements contained by the deck
 */
public class Deck<T extends Drawable>{
    private final List<T> remaining;
    private T topOfTheStack;

    /**
     * Constructs e sets up a new deck with the provided content.
     *
     * @param content a list of elements representing the deck's content
     */
    Deck(List<T> content)  {
        this.remaining = new ArrayList<>(content);
        this.topOfTheStack = next();
    }

    /**
     * Takes a random element from the deck's content, removes it from the deck
     * and returns it. Intended for internal usage.
     *
     * @return an element taken at random from the deck's content (top of the stack excluded)
     */
    private T next() {
        if(remaining.isEmpty()) return null;

        int selectedIndex = (int) (Math.random() * remaining.size());
        return remaining.remove(selectedIndex); // remove returns the removed element
    }

    /**
     * Allows to actually draw a card from the deck. Subclasses must implement this method
     * in order to specify how the elements are drawn from the deck
     *
     * @return the drawn element
     */
    public T draw() {
        T drawnElement = topOfTheStack;
        topOfTheStack = next();
        if(drawnElement == null) throw new EmptyStackException();
        return drawnElement;
    }

    /**
     * Retrieves the Resource type of the card that's currently on top of the deck (which can be empty).
     * <p>
     * This method should only be used when dealing with a Deck of PlayCards (or subclass).
     *
     * @return the element that's on top of the stack {@code null} if the deck's empty
     */
    public Resource getTopOfTheStack() {
        return this.topOfTheStack.getCardResourceType();
    }

    /**
     * Checks whether the deck is empty or not.
     * <p>
     * The deck's empty if there's no card on top of the stack (thus, no stack)
     *
     * @return {@code true} if the deck's empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return this.topOfTheStack == null;
    }
}
