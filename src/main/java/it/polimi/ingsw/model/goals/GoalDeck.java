package it.polimi.ingsw.model.goals;

import java.util.List;

public class GoalDeck {
    private boolean isLocked;
    private List<Goal> remaining;

    public void lockDeck() {
        isLocked=true;
    }

    public Goal getCard() {
        return null;
    }
}
