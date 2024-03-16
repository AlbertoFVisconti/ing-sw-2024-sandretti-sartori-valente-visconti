package it.polimi.ingsw.model.goals;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class GoalDeck {
    private boolean isLocked=false;
    private final List<Goal> remaining;

    public GoalDeck(String goalFile) {
        this.remaining = loadFromFile(goalFile);
    }

    private static List<Goal> loadFromFile(String goalFile) {
        // TODO
        return new ArrayList<>();
    }


    public void lockDeck() {
        isLocked=true;
    }

    public Goal getCard() {
        if(isLocked) throw new UnsupportedOperationException("cannot get Goal from deck when locked");
        if(remaining.isEmpty()) throw new EmptyStackException();

        int drawnGoalIndex = (int) (Math.random()*remaining.size());
        return remaining.remove(drawnGoalIndex);
    }
}
