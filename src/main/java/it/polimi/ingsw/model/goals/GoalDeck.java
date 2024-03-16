package it.polimi.ingsw.model.goals;

import java.util.EmptyStackException;
import java.util.List;

public class GoalDeck {
    private boolean isLocked;
    private List<Goal> remaining;

    public GoalDeck(/*TODO*/) {
        // TODO
    }

    // TODO: setup method(s)

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
