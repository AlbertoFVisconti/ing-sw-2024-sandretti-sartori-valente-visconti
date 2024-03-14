package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.corners.Corner;

import java.util.EnumMap;
import java.util.NoSuchElementException;

public class ItemCollection {
    private final EnumMap<Corner, Integer> content;

    public ItemCollection() {
        content = new EnumMap<>(Corner.class);
        content.put(Corner.PLANT,0);
        content.put(Corner.ANIMAL,0);
        content.put(Corner.FUNGUS,0);
        content.put(Corner.INSECT,0);

        content.put(Corner.FEATHER,0);
        content.put(Corner.INK,0);
        content.put(Corner.SCROLL,0);
    }

    public ItemCollection(ItemCollection itemCollection) {
        content = new EnumMap<>(itemCollection.content);
    }

    public ItemCollection add(ItemCollection itemCollection) {
        for(Corner cornerType : content.keySet()) {
            this.content.put(cornerType, content.get(cornerType)+itemCollection.content.get(cornerType));
        }
        return this;
    }

    public ItemCollection add(Corner corner) {
        return this.add(corner, 1);
    }

    public ItemCollection add(Corner corner, int amount) {
        if(corner != null && corner != Corner.EMPTY) {
            this.content.put(corner, content.get(corner) + amount);
        }
        return this;
    }

    public ItemCollection sub(ItemCollection itemCollection) {
        for(Corner cornerType : content.keySet()) {
            this.content.put(cornerType, content.get(cornerType)-itemCollection.content.get(cornerType));
        }
        return this;
    }

    public ItemCollection sub(Corner corner) {
        return this.add(corner, -1);
    }

    public ItemCollection sub(Corner corner, int amount) {
        return this.add(corner, -amount);
    }


    public boolean isSubSetOf(ItemCollection itemCollection) {
        for(Corner cornerType : content.keySet()) {
            if(itemCollection.content.get(cornerType) < this.content.get(cornerType)) {
                return false;
            }
        }
        return true;
    }

    public int divide(ItemCollection itemCollection) {
        int worstRatio = -1,
            amountInDivisor,
            amountInDividend,
            currentItemRatio;

        for(Corner cornerType : content.keySet()) {
            amountInDivisor = itemCollection.content.get(cornerType);

            // if amountInDivisor == 0, this cornerType does not constraint the final result: any check is unnecessary
            if(amountInDivisor != 0) {
                // the divisor contains at least an item of this cornerType: I need to compute the ratio
                amountInDividend = this.content.get(cornerType);
                currentItemRatio = amountInDividend /amountInDivisor;

                if( currentItemRatio < worstRatio || worstRatio == -1) {
                    // currentItemRatio is the worst so far (could also mean that it's the first valid currentItemRatio)
                    worstRatio = currentItemRatio;
                }
            }
        }

        // worst == -1 means that the condition amountInDivisor != 0 was always false: the divisor (itemCollection) is empty
        if (worstRatio == -1) throw new ArithmeticException("cannot perform division with an empty ItemCollection as divisor");

        // the worst ratio, if it exits, is the final result: the divisor (itemCollection) is contained worstRatio times in the dividend (this)
        return worstRatio;
    }

    public int count(Corner corner) {
        if (corner == null) throw new NullPointerException();
        if (corner == Corner.EMPTY) throw new NoSuchElementException();
        return this.content.get(corner);
    }
}