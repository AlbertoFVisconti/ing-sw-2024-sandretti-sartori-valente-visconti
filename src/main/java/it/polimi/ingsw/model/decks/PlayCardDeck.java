package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.decks.loaders.DeckLoader;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class represents the Decks of Resource and Gold cards.
 * It allows to load, store and distribute those kinds of cards.
 * It also handles two unveiled cards the user can choose to pick
 * and the upside down card on top of the deck, that can be picked as well.
 */
public class PlayCardDeck extends Deck<PlayCard> {

    private final PlayCard[] visibleCards;

    /**
     * Constructs a PlayCardDeck by loading its content from a file.
     * It also draws the first three cards (if available) and sets them as
     * visible cars, one of those being the cards on top of the deck, thus upside down.
     *
     * @param deckLoader the DeckLoader that provides this deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    public PlayCardDeck(DeckLoader<PlayCard> deckLoader) throws IOException {
        super(deckLoader);

        visibleCards = new PlayCard[]{
                null,
                this.drawRandom(),
                this.drawRandom()
        };

        /*
        * since all the visible cards are at some point on the top of the deck
        * the card that is left there should be the last one to be generated
        * although this actually matters only if the deck holds less than 3 cards
        */
        visibleCards[0] = this.drawRandom();
        if(visibleCards[0] != null) visibleCards[0].flip();
    }

    /**
     * This method allows to use the PlayCardDeck as if it was a standard deck (without the visible cards basically)
     * In order to do that, this method returns one of the visible cards.
     * If there's no visible cards, that means that the deck is empty.
     *
     * @return the card on top of the deck
     * @throws EmptyStackException if the deck contains no card
     */
    @Override
    public PlayCard draw() {
        List<Integer> availableCardsIndexes = new ArrayList<>();

        for(int i = 0; i < visibleCards.length; i++) {
            if(visibleCards[i] != null) availableCardsIndexes.add(i);
        }

        if(availableCardsIndexes.isEmpty()) throw new EmptyStackException();

        int selectedIndex=(int) (Math.random()*availableCardsIndexes.size());
        return draw(availableCardsIndexes.get(selectedIndex));
    }

    /**
     * Draws the selected cards among the available visible ones.
     * <p>
     * After a card is drawn, the card on top of the deck is moved to replace the missing card and flipped
     * If the card on top of the deck is the one being drawn, then this operation is unnecessary.
     * Then, a new cards is extracted from the remaining cards (in this case a null value is accepted, since the user
     * could still have visible cards to draw) to take the place on top of the deck (if it's not null, it is also flipped
     * in order to make it shows the back side).
     *
     * @param index the index of the card to draw 0 being the top of the deck
     * @return the drawn PlayCard
     * @throws InvalidParameterException if the provided index is out of range
     * @throws NoSuchElementException if there's no card available in the specified slot
     */
    public PlayCard draw(int index)  {
        if(index < 0 || index > 2) throw new InvalidParameterException("check the param of the getCard function");
        if(visibleCards[index] == null) throw new NoSuchElementException("parameter is in the approved range, but there's no card here");

        // if not null, the card on top of the deck always needs to be flipped
        if(visibleCards[0] != null) visibleCards[0].flip();

        PlayCard pickedCard= visibleCards[index];

        // redundant if i == 0 otherwise replace the picked card with the card on top of the deck (already flipped)
        visibleCards[index]=visibleCards[0];

        // get the next card to place on top of the deck upside down
        visibleCards[0]= this.drawRandom();
        if(visibleCards[0] != null) visibleCards[0].flip();

        return pickedCard;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && Arrays.stream(this.visibleCards).allMatch(Objects::isNull);
    }
}
