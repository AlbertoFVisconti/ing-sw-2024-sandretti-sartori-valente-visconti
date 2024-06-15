package it.polimi.ingsw.model.saving;

import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.decks.VirtualDeck;
import it.polimi.ingsw.model.goals.Goal;

import java.util.ArrayList;

/**
 * ClientGameSaving class allows to create objects that contain
 * all the information that is needed to recreate a game representation object
 * on the client's View. It also makes sure that the data delivered
 * to the client only contains information that the player is allowed
 * to receive.
 */
public class ClientGameSaving extends GameSaving {

    /**
     * Creates an object that contains all the basic information that is needed to recreate a game object on the client's View.
     *
     * @param players an ArrayList of ClientPlayerSavings that allows to recover players' data
     * @param gameId the game identifier string
     * @param goldCardsDeckTopResource the resource visible on top of the gold card deck
     * @param resourceCardsDeckTopResource the resource visible on top of the resource card deck
     * @param visibleCards the visible cards of the saved game
     * @param scoreBoard the scoreboard of the saved game
     * @param publicGoal the public goal of the saved game
     */
    public ClientGameSaving(ArrayList<ClientPlayerSaving> players, String gameId, Resource goldCardsDeckTopResource, Resource resourceCardsDeckTopResource, PlayCard[] visibleCards, ScoreBoard scoreBoard, Goal[] publicGoal) {
        super(-1, new ArrayList<>(players), gameId, new VirtualDeck(goldCardsDeckTopResource), new VirtualDeck(resourceCardsDeckTopResource), visibleCards, scoreBoard, publicGoal, null, null);
    }
}
