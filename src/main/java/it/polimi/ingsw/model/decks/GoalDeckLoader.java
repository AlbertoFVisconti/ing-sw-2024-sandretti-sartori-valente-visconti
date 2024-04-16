package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.utils.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.goals.ItemGoal;
import it.polimi.ingsw.model.goals.PatternGoal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The GoalDeckLoader class allows to create objects to load standard game's goals from files.
 */
public class GoalDeckLoader extends DeckLoader<Goal>{
    /**
     * Build a new GoalDeckLoader that operates on the file whose name is provided.
     *
     * @param filename the name of the file which contains the deck's content
     */
    public GoalDeckLoader(String filename) {
        super(filename);
    }

    /**
     * Loads the goals deck's contents from the provided file. This method must be implemented
     * by subclasses to specify how the deck's content is loaded
     *
     * @return a list of Goals representing the deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    @Override
    public List<Goal> load() throws IOException {
        List<Goal> goals = new ArrayList<>();

        JSONArray goalsJson = buildJSONArrayFromFile(filename);

        for(int i = 0; i < goalsJson.length(); i++) {
            JSONObject json = goalsJson.getJSONObject(i);

            String goalType = json.getString("goal_type");
            int scorePerMatch = json.getInt("score");

            Resource[][] pattern;
            ItemCollection items;

            switch(goalType) {
                case "pattern":
                    JSONArray patternJson = json.getJSONArray("pattern");
                    int rows = patternJson.length();
                    int cols = patternJson.getJSONArray(0).length();

                    pattern = new Resource[rows][cols];

                    for(int y = 0; y < rows; y++) {
                        JSONArray currentRow = patternJson.getJSONArray(y);
                        for(int x = 0; x < cols; x++) {
                            String resourceName = currentRow.get(x).toString();

                            if(resourceName.isEmpty()){
                                pattern[y][x] = null;
                            }
                            else {
                                pattern[y][x] = Resource.valueOf(resourceName);
                            }
                        }
                    }

                    goals.add(
                            new PatternGoal(pattern, scorePerMatch)
                    );
                    break;
                case "items":
                    JSONArray constraintArray = json.getJSONArray("items");
                    items = new ItemCollection();

                    for(int j = 0; j < constraintArray.length(); j++) {
                        JSONObject itemsJson = constraintArray.getJSONObject(j);
                        items.add(
                                Corner.valueOf(itemsJson.getString("item")),
                                itemsJson.getInt("amount")
                        );
                    }

                    goals.add(
                            new ItemGoal(items, scorePerMatch)
                    );
                    break;
            }
        }

        return goals;
    }
}
