package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.cards.corners.Resource;

/**
 * Interface that object that can be put in decks needs to implement.
 */
public interface Drawable {
    /**
     * Retrieves the Drawable element's resource card type (if it has one)
     *
     * @return the Drawable element's resource card type, {@code null} if the Drawable element doesn't have a card type
     */
    Resource getResourceType();
}
