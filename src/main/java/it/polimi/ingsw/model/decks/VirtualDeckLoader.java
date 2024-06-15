package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.cards.PlayCard;

import java.util.List;

/**
 * Allows the client to force the local Game object to create VirtualDeck object.
 */
public class VirtualDeckLoader extends DeckLoader<PlayCard> {
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
    public Deck<PlayCard> getDeck() {
        return new VirtualDeck();
    }

    /**
     * VirtualDecks have no content. There's nothing to load.
     *
     * @return null
     */
    protected List<PlayCard> load() {
        return null;
    }
}
