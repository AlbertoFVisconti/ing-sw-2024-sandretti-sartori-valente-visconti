package it.polimi.ingsw.model.decks;

import java.util.List;

/**
 * Allows the client to force the local Game object to create VirtualDeck object.
 *
 * @param <T> the type of "Card" that the generated VirtualDecks "held"
 */
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

    /**
     * VirtualDecks have no content. There's nothing to load.
     *
     * @return null
     */
    protected List<T> load() {
        return null;
    }
}
