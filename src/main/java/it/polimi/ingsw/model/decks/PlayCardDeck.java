package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.CoveredCornersScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.FreeScoreScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ItemCountScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ScoringStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.NoSuchElementException;

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
     * @param cardFile the name of the file containing the deck's data
     * @throws IOException if there's a problem when trying to read the file
     */
    public PlayCardDeck(String cardFile) throws IOException {
        super(cardFile);

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
     * Loads the PlayCards' data from a file and returns them
     * as a list of PlayCard objects
     *
     * @param fileName the name of the file containing the deck's data
     * @return a list of PlayCards representing the deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    protected List<PlayCard> loadFromFile(String fileName) throws IOException {
        List<PlayCard> cards = new ArrayList<>();

        JSONArray cardsJson = buildJSONArrayFromFile(fileName);

        for (int i = 0; i < cardsJson.length(); i++) {
            JSONObject json = cardsJson.getJSONObject(i);

            boolean isGoldCard = json.getBoolean("is_gold");

            Resource type = Resource.valueOf(json.getString("type"));

            Corner  topLeft = null,
                    topRight = null,
                    bottomLeft = null,
                    bottomRight = null;

            String cornerString = json.getString("tl_corner");
            if(!cornerString.equals("HIDDEN")) topLeft = Corner.valueOf(cornerString);

            cornerString = json.getString("tr_corner");
            if(!cornerString.equals("HIDDEN"))topRight = Corner.valueOf(cornerString);

            cornerString = json.getString("bl_corner");
            if(!cornerString.equals("HIDDEN"))bottomLeft = Corner.valueOf(cornerString);

            cornerString = json.getString("br_corner");
            if(!cornerString.equals("HIDDEN"))bottomRight = Corner.valueOf(cornerString);

            if(isGoldCard) {
                ScoringStrategy scoringStrategy = null;

                switch(json.getString("scoring_strategy")) {
                    case "item_count":
                        try {
                            scoringStrategy = new ItemCountScoringStrategy(
                                    Corner.valueOf(json.getString("item_to_count")),
                                    json.getInt("score_per_item")
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "free_score":
                        scoringStrategy = new FreeScoreScoringStrategy(
                                json.getInt("free_score")
                        );
                        break;
                    case "corner_count":
                        scoringStrategy = new CoveredCornersScoringStrategy(
                                json.getInt("score_per_corner")
                        );
                        break;
                }

                JSONArray constraintArray = json.getJSONArray("constraint");
                ItemCollection constraint = new ItemCollection();

                for(int j = 0; j < constraintArray.length(); j++) {
                    JSONObject constraintObject = constraintArray.getJSONObject(j);
                    constraint.add(
                            Corner.valueOf(constraintObject.getString("item")),
                            constraintObject.getInt("amount")
                    );
                }

                cards.add(Card.generateGoldCard(topLeft, topRight, bottomLeft, bottomRight, type, constraint, scoringStrategy));

            }
            else {
                int score = json.getInt("free_score");

                cards.add(Card.generateResourceCard(topLeft, topRight, bottomLeft, bottomRight, type, score));
            }
        }

        return cards;
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


}
