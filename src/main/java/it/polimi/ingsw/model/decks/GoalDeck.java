package it.polimi.ingsw.model.decks;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.goals.ItemGoal;
import it.polimi.ingsw.model.goals.PatternGoal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * A deck of goals that helps to load, store and distribute the goals
 */
public class GoalDeck extends Deck<Goal>{
    private boolean isLocked=false;

    /**
     * Constructs a GoalDeck by loading its content from a file
     *
     * @param goalFile the name of the files containing the goals
     * @throws IOException if there's a problem when trying to read the file
     */
    public GoalDeck(String goalFile) throws IOException {
        super(goalFile);
    }

    /**
     * Loads the goals' data from a file and returns them
     * as a list of Goals objects
     *
     * @param fileName the name of the file containing the deck's data
     * @return a list of goals representing the deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    protected List<Goal> loadFromFile(String fileName) throws IOException {
        List<Goal> goals = new ArrayList<>();

        JSONArray goalsJson = buildJSONArrayFromFile(fileName);

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

                    for(int y = 0; y < cols; y++) {
                        JSONArray currentRow = patternJson.getJSONArray(y);
                        for(int x = 0; x < rows; x++) {
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

    /**
     * Locks the deck, preventing further drawing of goals
     */
    public void lock() {
        isLocked=true;
    }

    /**
     * If the deck is not locked, it draws a random goal from the deck and returns it.
     * It also assures that the drawn goal is actually valid before returning it.
     *
     * @return a random goal among the remaining ones
     * @throws UnsupportedOperationException if the goal is locked
     * @throws EmptyStackException if the deck is empty
     */
    @Override
    public Goal draw() {
        if(isLocked) throw new UnsupportedOperationException("cannot get Goal from deck when locked");
        return super.draw();
    }
}