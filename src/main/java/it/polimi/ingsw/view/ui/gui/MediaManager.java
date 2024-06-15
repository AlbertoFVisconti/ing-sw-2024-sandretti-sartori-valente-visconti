package it.polimi.ingsw.view.ui.gui;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.goals.Goal;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Objects;

public class MediaManager {
    private static MediaManager instance = null;

    public static MediaManager getInstance() {
        if (instance == null) instance = new MediaManager();
        return instance;
    }

    private final HashMap<String, Image> loadedContent;
    private final Image altImage;

    private MediaManager() {
        try {
            altImage = new Image(Objects.requireNonNull(getClass().getResource("/image/cardshape.png")).toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load resources!");
        }

        loadedContent = new HashMap<>();
    }

    public Image getImage(CardSlot cardSlot) {
        if (cardSlot == null) return altImage;
        return this.getImage(cardSlot.card(), cardSlot.onBackSide());
    }

    public Image getImage(Card card, boolean backSide) {
        if(card == null) return altImage;

        if(backSide) {
            return getImage(card.getBackPath());
        }
        else {
            return getImage(card.getFrontPath());
        }
    }

    public Image getImage(String path) {
        if(path == null) return altImage;

        if(!loadedContent.containsKey(path)) {
            try {
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

        return loadedContent.get(path);
    }

    public Image getImage(Goal privateGoal) {
        if (privateGoal == null) return altImage;
        return getImage(privateGoal.getPath());
    }
}
