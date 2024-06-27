package it.polimi.ingsw.view.ui.gui;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.goals.Goal;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Objects;

/**
 * MediaManager class allows to load images from the resources.
 * Since most of the images will be loaded multiple times throughout the execution of
 * the application, MediaManager saves all the loaded images. That reduces
 * the need to read files on disk.
 * It also handles missing resources or broken URLs by providing an "alternative image".
 * That means that the executing thread won't raise any exception even if it tries to
 * display an images that isn't in the resources folder
 */
public class MediaManager {
    // MediaManager is a singleton
    private static MediaManager instance = null;

    /**
     * Allows to retrieve the MediaManager instance.
     *
     * @return MediaManager instance
     */
    public static MediaManager getInstance() {
        if (instance == null) instance = new MediaManager();
        return instance;
    }

    // hashmap that contains all the loaded content
    private final HashMap<String, Image> loadedContent;

    // alternative image that is returned when the requested image isn't found
    private final Image altImage;

    /**
     * Builds a MediaManager object.
     * It tries loads the alternative image and throws an exception if it fails.
     *
     * @throws RuntimeException if it cannot load the alternative images
     */
    private MediaManager() {
        try {
            altImage = new Image(Objects.requireNonNull(getClass().getResource("/image/cardshape.png")).toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load resources!");
        }

        loadedContent = new HashMap<>();
    }

    /**
     * Allows to retrieve the image that represent a CardSlot.
     * CardSlot define the card's orientation.
     *
     * @param cardSlot the CardSlot whose image is needed
     * @return the Image that represents the given CardSlot
     */
    public Image getImage(CardSlot cardSlot) {
        if (cardSlot == null) return altImage;
        return this.getImage(cardSlot.card(), cardSlot.onBackSide());
    }

    /**
     * Allows to retrieve the image that represents the specified
     * side of a given Card.
     *
     * @param card the card whose image is needed
     * @param backSide {@code true} if the front side of the card is needed, {@code false} otherwise
     * @return the Image that represents the given Card's specified side
     */
    public Image getImage(Card card, boolean backSide) {
        if(card == null) return altImage;

        if(backSide) {
            return getImage(card.getBackPath());
        }
        else {
            return getImage(card.getFrontPath());
        }
    }

    /**
     * Allows to retrieve an image given the path.
     *
     * @param path the path that needs to be used to load the image
     * @return the Image that was found at the provided path
     */
    public Image getImage(String path) {
        if(path == null) return altImage;

        // checking if the required image was already loaded
        if(!loadedContent.containsKey(path)) {
            // the image was never loaded
            try {
                // trying to load the resource
                loadedContent.put(path,
                        new Image(
                                Objects.requireNonNull(getClass().getResource(path)).toString()
                        )
                );
            }
            catch (Exception e) {
                // failed to load image
                return altImage;
            }
        }

        // returning the required image
        return loadedContent.get(path);
    }

    /**
     * Allows to retrieve the Image that represents
     * the given goal.
     *
     * @param goal Goal whose Image is needed
     * @return Image that represents the given Goal
     */
    public Image getImage(Goal goal) {
        if (goal == null) return altImage;
        return getImage(goal.getPath());
    }

    /**
     * Allows to retrieve the image that represents
     * a deck with a given resource on top of the stack.
     *
     * @param topOfTheStack Resource on top of the deck
     * @param gold {@code true} if the gold card deck is needed, {@code false} otherwise
     * @return the Image that represents the deck
     */
    public Image getImage(Resource topOfTheStack, boolean gold) {
        if(topOfTheStack == null) return altImage;
        if(gold) return getImage(topOfTheStack.getGoldenPath());
        else return getImage(topOfTheStack.getPath());
    }
}
