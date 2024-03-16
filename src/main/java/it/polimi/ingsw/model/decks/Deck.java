package it.polimi.ingsw.model.decks;

import java.io.IOException;
import java.util.List;

abstract class Deck<T>{
    private final List<T> remaining;

    public Deck(String fileName) throws IOException {
        this.remaining = loadFromFile(fileName);
    }

    protected abstract List<T> loadFromFile(String fileName) throws IOException;

    protected T drawRandom() {
        if(remaining.isEmpty()) return null;

        int selectedIndex=(int) (Math.random()*remaining.size());
        return remaining.remove(selectedIndex); // remove returns the removed element
    }

    public abstract T draw();

}
