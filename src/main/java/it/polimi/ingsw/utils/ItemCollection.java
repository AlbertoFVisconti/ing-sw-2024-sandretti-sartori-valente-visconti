package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.cards.corners.Corner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * ItemCollection is class that allows to store information about amounts of items.
 * With item we intend every object of the Corner enum class, except {@code Corner.EMPTY}, which represents
 * the concept of "missing item", counting this kind of Corner is not useful nor reasonable for this application.
 * The class allows the user to perform set operations with ItemCollection and Corner types.
 * Storing capabilities become particularly useful when dealing with players' inventories or
 * cards' resource constraint and when moving amounts of items around (collecting resources from placed cards for instance).
 * Set operations allow goals and scoring evaluation methods to become simple to write, read and understand.
 * <p>
 * Example:
 * <pre>{@code
 *  // Creating a new empty ItemCollection
 *  ItemCollection collection = new ItemCollection();
 *
 *  // Adding 3 PLANTs, 1 ANIMAL and 2 FEATHERS to the ItemCollection
 *  collection.add(Corner.PLANT, 3)
 *            .add(Corner.ANIMAL)
 *            .add(Corner.FEATHER, 2);
 *
 *  // Asking the ItemCollection how many PLANTs were stored (3)
 *  int plantCount = collection.count(Corner.PLANT);
 *
 *  // Removing 2 PLANTs and 1 ANIMAL from the ItemCollection
 *  collection.sub(Corner.PLANT, 2)
 *            .sub(Corner.ANIMAL);
 *  }</pre>
 */
public class ItemCollection implements Serializable {
    private final EnumMap<Corner, Integer> content;

    /**
     * Constructs a new empty ItemColletion.
     * The new object will have every amount (re)set to zero.
     */
    public ItemCollection() {
        content = new EnumMap<>(Corner.class);
        content.put(Corner.PLANT, 0);
        content.put(Corner.ANIMAL, 0);
        content.put(Corner.FUNGUS, 0);
        content.put(Corner.INSECT, 0);

        content.put(Corner.FEATHER, 0);
        content.put(Corner.INK, 0);
        content.put(Corner.SCROLL, 0);
    }

    /**
     * Constructs a new ItemColletion starting from an existing one.
     * The new object will have the same initial amounts but will evolve independently
     * from the passed ItemCollection.
     *
     * @param itemCollection the ItemCollection upon which the new object will be created
     */
    public ItemCollection(ItemCollection itemCollection) {
        content = new EnumMap<>(itemCollection.content);
    }

    /**
     * Adds the amounts of items from the given ItemCollection to this one
     * and then returns the current ItemCollection to allow method chaining.
     *
     * @param itemCollection the ItemCollection whose amounts are added
     * @return this ItemCollection after the operation
     */
    public ItemCollection add(ItemCollection itemCollection) {
        for (Corner cornerType : content.keySet()) {
            this.content.put(cornerType, content.get(cornerType) + itemCollection.content.get(cornerType));
        }
        return this;
    }

    /**
     * Increases the amount of the given corner's item  by one
     * and then returns the current ItemCollection to allow method chaining.
     *
     * @param corner the Corner whose item's count needs to be increased by 1
     * @return this ItemCollection after the operation
     */
    public ItemCollection add(Corner corner) {
        return this.add(corner, 1);
    }

    /**
     * Increases the stored amount of the specified corner by the selected value's item
     * and then returns the current ItemCollection to allow method chaining.
     *
     * @param corner the Corner whose item's count needs to be increased
     * @param amount the value to add to the amount of the given corner's item
     * @return this ItemCollection after the operation
     */
    public ItemCollection add(Corner corner, int amount) {
        if (corner == Corner.EMPTY || corner == null) return this;
        if (this.count(corner) + amount < 0)
            throw new ArithmeticException("sub(Corner): itemCollection cannot have negative amounts");

        this.content.put(corner, content.get(corner) + amount);

        return this;
    }

    /**
     * Subtracts the amounts of items in the given ItemCollection from this one
     * and then returns the current ItemCollection to allow method chaining.
     *
     * @param itemCollection the ItemCollection whose amounts are subtracted
     * @return this ItemCollection after the operation
     */
    public ItemCollection sub(ItemCollection itemCollection) {
        if (!itemCollection.isSubSetOf(this))
            throw new ArithmeticException("sub(ItemCollection): itemCollection cannot have negative amounts");
        for (Corner cornerType : content.keySet()) {
            this.content.put(cornerType, content.get(cornerType) - itemCollection.content.get(cornerType));
        }
        return this;
    }

    /**
     * Decreases the stored amount of the specified corner's item by one
     * and then returns the current ItemCollection to allow method chaining.
     *
     * @param corner the Corner whose item's count needs to be decreased by 1
     * @return this ItemCollection after the operation
     */
    public ItemCollection sub(Corner corner) {
        return this.add(corner, -1);
    }

    /**
     * Decreases the stored amount of the specified corner's item by the selected value
     * and then returns the current ItemCollection to allow method chaining.
     *
     * @param corner the Corner whose item's count needs to be decreased
     * @param amount the value to subtract from the amount of the given corner's item
     * @return this ItemCollection after the operation
     */
    public ItemCollection sub(Corner corner, int amount) {
        return this.add(corner, -amount);
    }


    /**
     * Checks whether the current ItemCollection is a subset of the given ItemCollection.
     * Basically, it checks if, for every item, the amount in the current ItemCollection are always
     * less than or equal to the amount in the given ItemCollection.
     *
     * @param itemCollection the ItemCollection to compare the current one against
     * @return {@code true} if this ItemCollection is a subset of given one, {@code false} otherwise
     */
    public boolean isSubSetOf(ItemCollection itemCollection) {
        for (Corner cornerType : content.keySet()) {
            if (itemCollection.content.get(cornerType) < this.content.get(cornerType)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Computes how many times the given ItemCollection could be subtracted from the
     * current one before any amount becomes negative.
     * In other words, it computes the maximum number of partition of the current ItemCollection
     * that are equal to the given ItemCollection.
     * <p>
     * Both definitions suggest that trying to perform this operation when given an empty
     * ItemCollection (amount = 0 for every item) has an undefined result.
     * <p>
     * This result is also represented by the smallest integer division between amounts (this amount / given amount)
     * of item whose amounts in the given (divisor) ItemCollection isn't zero.
     *
     * @param itemCollection the divisor ItemCollection
     * @return times given ItemCollection is found in this ItemCollection
     * @throws ArithmeticException if the given divisor ItemCollection is empty
     */
    public int divide(ItemCollection itemCollection) {
        int worstRatio = -1,
                amountInDivisor,
                amountInDividend,
                currentItemRatio;

        for (Corner cornerType : content.keySet()) {
            amountInDivisor = itemCollection.content.get(cornerType);

            // if amountInDivisor == 0, this cornerType does not affect the final result: any check is unnecessary
            if (amountInDivisor != 0) {
                // the divisor contains at least an item of this cornerType: I need to compute the ratio
                amountInDividend = this.content.get(cornerType);
                currentItemRatio = amountInDividend / amountInDivisor;

                if (currentItemRatio < worstRatio || worstRatio == -1) {
                    // currentItemRatio is the worst so far (could also mean that it's the first valid currentItemRatio)
                    worstRatio = currentItemRatio;
                }
            }
        }

        // worst == -1 means that the condition amountInDivisor != 0 was always false: the divisor (itemCollection) is empty
        if (worstRatio == -1)
            throw new ArithmeticException("cannot perform division with an empty ItemCollection as divisor");

        // the worst ratio, if it exists, is the final result: the divisor (itemCollection) is contained worstRatio times in the dividend (this)
        return worstRatio;
    }

    /**
     * Returns the amount of a specific Corner's item in this ItemCollection.
     *
     * @param corner Corner whose item we want to know the amount stored
     * @return the amount of the specified Corner's Item in this ItemCollection
     * @throws NullPointerException   if the specified Corner is null
     * @throws NoSuchElementException if the specified Corner is {@code Corner.EMPTY} thus a "missing item"
     */
    public int count(Corner corner) {
        if (corner == null) throw new NullPointerException();
        if (corner == Corner.EMPTY) throw new NoSuchElementException();
        return this.content.get(corner);
    }

    /**
     * Retrieves the ItemCollection content as a list of Corners.
     * Each Corner amounts in the list matches the amounts in the ItemCollection
     *
     * @return a List containing the ItemCollection's content
     */
    public List<Corner> toList() {
        List<Corner> result = new ArrayList<>();

        for (Corner corner : this.content.keySet()) {
            for (int i = 0; i < this.content.get(corner); i++) {
                result.add(corner);
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return content.toString();
    }
}