package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.utils.ItemCollection;
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
import java.util.ArrayList;
import java.util.List;

/**
 * The PlayCardDeckLoader class allows to create objects to load standard game's PlayCards from files.
 */
public class PlayCardDeckLoader extends DeckLoader<PlayCard>{
    /**
     * Build a new PlayCardDeckLoader that operates on the file whose name is provided.
     *
     * @param filename the name of the file which contains the deck's content
     */
    public PlayCardDeckLoader(String filename) {
        super(filename);
    }

    /**
     * Loads the PlayCard deck's contents from the provided file. This method must be implemented
     * by subclasses to specify how the deck's content is loaded
     *
     * @return a list of PlayCards representing the deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    @Override
    public List<PlayCard> load() throws IOException {
        List<PlayCard> cards = new ArrayList<>();

        JSONArray cardsJson = buildJSONArrayFromFile(filename);

        for (int i = 0; i < cardsJson.length(); i++) {
            JSONObject json = cardsJson.getJSONObject(i);

            boolean isGoldCard = json.getBoolean("is_gold");

            Resource type = Resource.valueOf(json.getString("type"));

            Corner topLeft = null,
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

                cards.add(PlayCard.generateGoldCard(topLeft, topRight, bottomLeft, bottomRight, type, constraint, scoringStrategy));

            }
            else {
                int score = json.getInt("free_score");

                cards.add(PlayCard.generateResourceCard(topLeft, topRight, bottomLeft, bottomRight, type, score));
            }
        }

        return cards;
    }
}
