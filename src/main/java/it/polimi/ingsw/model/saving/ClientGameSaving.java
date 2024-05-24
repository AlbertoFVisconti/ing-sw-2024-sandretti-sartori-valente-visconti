package it.polimi.ingsw.model.saving;

import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.decks.VirtualDeck;
import it.polimi.ingsw.model.goals.Goal;

import java.util.ArrayList;

public class ClientGameSaving extends GameSaving {
    public ClientGameSaving(ArrayList<ClientPlayerSaving> players, int gameId, Resource goldCardsDeckTopResource, Resource resourceCardsDeckTopResource, PlayCard[] visibleCards, ScoreBoard scoreBoard, Goal[] publicGoal) {
        super(-1, new ArrayList<>(players), gameId, new VirtualDeck<>(goldCardsDeckTopResource), new VirtualDeck<>(resourceCardsDeckTopResource), visibleCards, scoreBoard, publicGoal, null, null);
    }
}
