package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  A deck of StartCards.
 *  It allows to load, store and distribute StartCards.
 */
public class StartCardDeck extends Deck<StartCard>{
    /**
     * Constructs a StartCardDeck by loading its contents from a file.
     *
     * @param fileName the name of the file containing the deck's data
     * @throws IOException if there's a problem when trying to read the file
     */
    public StartCardDeck(String fileName) throws IOException {
        super(fileName);
    }

    /**
     * Loads the StartCards' data from the file and returns them as a list
     * of PlayCard objects.
     *
     * @param fileName the name of the file containing the deck's data
     * @return a list of StartCard objects representing the deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    @Override
    protected List<StartCard> loadFromFile(String fileName) throws IOException {
        List<StartCard> cards = new ArrayList<>();

        JSONArray cardsJson = buildJSONArrayFromFile(fileName);

        for(int i = 0; i < cardsJson.length(); i++) {
            JSONObject json = cardsJson.getJSONObject(i);

            Corner  frontTopLeft = null,
                    frontTopRight = null,
                    frontBottomLeft = null,
                    frontBottomRight = null,
                    backTopLeft = null,
                    backTopRight = null,
                    backBottomLeft = null,
                    backBottomRight = null;

            String cornerString = json.getString("tl_front_corner");
            if(!cornerString.equals("HIDDEN")) frontTopLeft = Corner.valueOf(cornerString);

            cornerString = json.getString("tr_front_corner");
            if(!cornerString.equals("HIDDEN")) frontTopRight = Corner.valueOf(cornerString);

            cornerString = json.getString("bl_front_corner");
            if(!cornerString.equals("HIDDEN")) frontBottomLeft = Corner.valueOf(cornerString);

            cornerString = json.getString("br_front_corner");
            if(!cornerString.equals("HIDDEN")) frontBottomRight = Corner.valueOf(cornerString);

            cornerString = json.getString("tl_back_corner");
            if(!cornerString.equals("HIDDEN")) backTopLeft = Corner.valueOf(cornerString);

            cornerString = json.getString("tr_back_corner");
            if(!cornerString.equals("HIDDEN")) backTopRight = Corner.valueOf(cornerString);

            cornerString = json.getString("bl_back_corner");
            if(!cornerString.equals("HIDDEN")) backBottomLeft = Corner.valueOf(cornerString);

            cornerString = json.getString("br_back_corner");
            if(!cornerString.equals("HIDDEN")) backBottomRight = Corner.valueOf(cornerString);

            JSONArray permanentResourcesArray = json.getJSONArray("permanent");
            ItemCollection permanentResources = new ItemCollection();

            for(int j = 0; j < permanentResourcesArray.length(); j++) {
                permanentResources.add(
                        Corner.valueOf(permanentResourcesArray.get(j).toString())
                );
            }

            cards.add(
                    StartCard.generateStartCard(frontTopLeft,frontTopRight, frontBottomLeft, frontBottomRight,
                            backTopLeft, backTopRight, backBottomLeft, backBottomRight,
                            permanentResources
                    )
            );
        }

        return cards;
    }
}
