package it.polimi.ingsw.model.decks;


import it.polimi.ingsw.utils.ItemCollection;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The GoalDeckLoader class allows to create objects to load standard game's StartCards from files.
 */
public class StartCardDeckLoader extends DeckLoader<StartCard> {
    /**
     * Build a new StartCardDeckLoader that operates on the file whose name is provided.
     *
     * @param filename the name of the file which contains the deck's content
     */
    public StartCardDeckLoader(String filename) {
        super(filename);
    }

    /**
     * Loads the StartCard deck's contents from the provided file. This method must be implemented
     * by subclasses to specify how the deck's content is loaded
     *
     * @return a list of StartCards representing the deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    @Override
    public List<StartCard> load() throws IOException {
        List<StartCard> cards = new ArrayList<>();

        JSONArray cardsJson = buildJSONArrayFromFile(filename);

        for(int i = 0; i < cardsJson.length(); i++) {
            JSONObject json = cardsJson.getJSONObject(i);

            Corner frontTopLeft = null,
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
                    new StartCard(
                            "start_card" + i,
                            frontTopLeft,frontTopRight, frontBottomLeft, frontBottomRight,
                            backTopLeft, backTopRight, backBottomLeft, backBottomRight,
                            permanentResources
                    )
            );
        }

        return cards;
    }
}
