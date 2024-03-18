package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.goals.Goal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * A deck of goals that helps to load, store and distribute the goals
 */
public class GoalDeck extends Deck<Goal>{
    private boolean isLocked=false;

    /**
     * Constructs a GoalDeck by loading its content from a file
     *
     * @param goalFile the name of the files containing the goals
     * @throws IOException if there's a problem when trying to read the file
     */
    public GoalDeck(String goalFile) throws IOException {
        super(goalFile);
    }

    /**
     * Loads the goals' data from a file and returns them
     * as a list of Goals objects
     *
     * @param goalFile the name of the file containing the deck's data
     * @return a list of goals representing the deck's content
     */
    protected List<Goal> loadFromFile(String goalFile) {
        // TODO
        return new ArrayList<>();
    }

    /**
     * Locks the deck, preventing further drawing of goals
     */
    public void lock() {
        isLocked=true;
    }

    /**
     * If the deck is not locked, it draws a random goal from the deck and returns it.
     * It also assures that the drawn goal is actually valid before returning it.
     *
     * @return a random goal among the remaining ones
     * @throws UnsupportedOperationException if the goal is locked
     * @throws EmptyStackException if the deck is empty
     */
    @Override
    public Goal draw() {
        if(isLocked) throw new UnsupportedOperationException("cannot get Goal from deck when locked");
        return super.draw();
    }
}