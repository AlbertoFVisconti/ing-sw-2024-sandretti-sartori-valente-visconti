package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ItemCollection;
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
import java.util.List;
import java.util.NoSuchElementException;

public class Deck {
    private final List<Card> remaining;
    private final Card[] visibleCard;


    public Deck(String cardFile) throws IOException {
        this.remaining = loadFromFile(cardFile);

        visibleCard= new Card[]{
                null,
                this.getRandomCard(),
                this.getRandomCard()
        };

        /*
        * since all the visible cards are at some point on the top of the deck
        * the card that is left there should be the last one to be generated
        * although this actually matters only if the deck holds less than 3 cards
        */
        visibleCard[0] = this.getRandomCard();
        if(visibleCard[0] != null) visibleCard[0].flip();
    }

    private static List<Card> loadFromFile(String cardFile) throws IOException {
        List<Card> cards = new ArrayList<>();

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

    private Card getRandomCard(){
        if(remaining.isEmpty()) return null;

        int selectedCardIndex=(int) (Math.random()*remaining.size());
        return remaining.remove(selectedCardIndex); // remove returns the removed element
    }
    public Card getCard(int i)  {
        if(i < 0 || i > 2) throw new InvalidParameterException("check the param of the getCard function");
        if(visibleCard[i] == null) throw new NoSuchElementException("parameter is in the approved range, but there's no card here");

        // if not null, the card on top of the deck always needs to be flipped
        if(visibleCard[0] != null) visibleCard[0].flip();

        Card pickedCard= visibleCard[i];

        // redundant if i == 0 otherwise replace the picked card with the card on top of the deck (already flipped)
        visibleCard[i]=visibleCard[0];

        // get the next card to place on top of the deck upside down
        visibleCard[0]=getRandomCard();
        if(visibleCard[0] != null) visibleCard[0].flip();

        return pickedCard;
    }
}
