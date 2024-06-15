package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.cards.corners.Resource;

/**
 * Interface that object that can be put in decks needs to implement.
 */
public interface Drawable {
    Resource getResourceType();
}
