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

import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.NoSuchElementException;

public class PlayCardDeck extends Deck<PlayCard> {

    private final PlayCard[] visibleCards;


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

    protected List<PlayCard> loadFromFile(String cardFile) throws IOException {
        List<PlayCard> cards = new ArrayList<>();

        FileReader reader = new FileReader(cardFile);
        StringBuilder jsonString = new StringBuilder();

        int c;
        while((c = reader.read()) != -1) {
            jsonString.append((char) c);
        }
        reader.close();

        JSONArray cardsJson = new JSONArray(jsonString.toString());

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
     * This method allows to use the PlayCardDeck as it was a standard deck (without the visible cards basically)
     * In order to do that, this method returns one of the visible card.
     * If there's no visible card, that means that the deck is empty.
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

    public PlayCard draw(int i)  {
        if(i < 0 || i > 2) throw new InvalidParameterException("check the param of the getCard function");
        if(visibleCards[i] == null) throw new NoSuchElementException("parameter is in the approved range, but there's no card here");

        // if not null, the card on top of the deck always needs to be flipped
        if(visibleCards[0] != null) visibleCards[0].flip();

        PlayCard pickedCard= visibleCards[i];

        // redundant if i == 0 otherwise replace the picked card with the card on top of the deck (already flipped)
        visibleCards[i]=visibleCards[0];

        // get the next card to place on top of the deck upside down
        visibleCards[0]= this.drawRandom();
        if(visibleCards[0] != null) visibleCards[0].flip();

        return pickedCard;
    }


}
