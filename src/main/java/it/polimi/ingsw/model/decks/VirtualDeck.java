package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.cards.corners.Resource;

/**
 * VirtualDeck
 */
public class VirtualDeck<T extends Drawable> extends Deck<T> {
    private Resource shownResource;

    public VirtualDeck() {
    }

    public VirtualDeck(Resource topOfTheStack) {
        this.shownResource = topOfTheStack;
    }

    /**
     * VirtualDecks don't allow to draw a card.
     *
     * @throws UnsupportedOperationException since this kind of deck doesn't allow drawing.
     */
    @Override
    public T draw() {
        throw new UnsupportedOperationException("This is a virtual deck, it doesn't allow drawing");
    }

    /**
     * Allows to change the resource shown on top of the deck.
     *
     * @param shownResource the resource on the back of the card on top.
     */
    public void setTopOfTheStack(Resource shownResource) {
        this.shownResource = shownResource;
    }

    /**
     * Retrieves the Resource type of the card that's currently on top of the deck (which can be empty).
     *
     * @return the element that's on top of the stack {@code null} if the deck's empty.
     */
    @Override
    public Resource getTopOfTheStack() {
        return this.shownResource;
    }

    /**
     * Checks whether the deck is empty or not.
     * <p>
     * The deck's empty if there's no resource to show
     *
     * @return {@code true} if the deck's empty, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return this.shownResource == null;
    }
}
