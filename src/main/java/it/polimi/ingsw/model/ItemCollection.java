package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.CornerStatus;
import it.polimi.ingsw.model.cards.corners.Item;
import it.polimi.ingsw.model.cards.corners.Resource;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class ItemCollection {
    private final HashMap<Corner, Integer> content;

    public ItemCollection() {
        content = new HashMap<>();
        content.put(Resource.PLANT,0);
        content.put(Resource.ANIMAL,0);
        content.put(Resource.FUNGUS,0);
        content.put(Resource.INSECT,0);

        content.put(Item.FEATHER,0);
        content.put(Item.INK,0);
        content.put(Item.SCROLL,0);
    }

    public ItemCollection(ItemCollection itemCollection) {
        content = new HashMap<>(itemCollection.content);
    }

    public ItemCollection add(ItemCollection itemCollection) {
        for(Corner item : content.keySet()) {
            this.content.put(item, content.get(item)+itemCollection.content.get(item));
        }
        return this;
    }

    public ItemCollection add(Corner item) {
        return this.add(item, 1);
    }

    public ItemCollection add(Corner item, int amount) {
        if(!(item instanceof CornerStatus)) {
            this.content.put(item, content.get(item) + amount);
        }
        return this;
    }

    public ItemCollection sub(ItemCollection itemCollection) {
        for(Corner item : content.keySet()) {
            this.content.put(item, content.get(item)-itemCollection.content.get(item));
        }
        return this;
    }

    public ItemCollection sub(Corner item) {
        return this.add(item, -1);
    }

    public ItemCollection sub(Corner item, int amount) {
        return this.add(item, -amount);
    }


    public boolean isSubSetOf(ItemCollection itemCollection) {
        for(Corner item : content.keySet()) {
            if(itemCollection.content.get(item) < this.content.get(item)) {
                return false;
            }
        }
        return true;
    }

    public int divide(ItemCollection itemCollection) {
        // TODO
        return 0;
    }

    public int count(Corner item) {
        if (item instanceof CornerStatus) throw new NoSuchElementException();
        return this.content.get(item);
    }
}