package it.polimi.ingsw.model.decks;

import java.util.List;

public class VirtualDeckLoader<T extends Drawable> extends DeckLoader<T> {
    /**
     * Builds a new VirtualDeckLoader.
     */
    public VirtualDeckLoader() {
        super("");
    }

    /**
     * Allows to create a VirtualDeck object.
     *
     * @return a new VirtualDeck object.
     */
    @Override
    public Deck<T> getDeck() {
        return new VirtualDeck<>();
    }

    @Override
    protected T duplicateElement(T element) {
        return null;
    }

    /**
     * VirtualDecks have no content. There's nothing to load.
     *
     * @return null

     */
    protected List<T> load()  {
        return null;
    }
}
