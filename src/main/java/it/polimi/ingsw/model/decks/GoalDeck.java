package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.goals.Goal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class GoalDeck extends Deck<Goal>{
    private boolean isLocked=false;

    public GoalDeck(String goalFile) throws IOException {
        super(goalFile);
    }

    protected List<Goal> loadFromFile(String goalFile) {
        // TODO
        return new ArrayList<>();
    }

    public void lock() {
        isLocked=true;
    }

    @Override
    public Goal draw() {
        if(isLocked) throw new UnsupportedOperationException("cannot get Goal from deck when locked");
        Goal g = this.drawRandom();
        if(g == null) throw new EmptyStackException();

        return g;
    }
}