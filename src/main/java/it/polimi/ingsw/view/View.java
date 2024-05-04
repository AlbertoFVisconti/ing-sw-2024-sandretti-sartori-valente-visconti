package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;

public class View implements VirtualView{
    private String playerIdentifier = null;
    private VirtualController controller = null;

    public View() throws RemoteException {}

    public String getPlayerIdentifier() {
        return playerIdentifier;
    }
    public VirtualController getController() {
        return controller;
    }

    public void setController(VirtualController controller) throws RemoteException {
        System.err.println("Remote controller received");
        this.controller = controller;
    }


    public void setPlayerIdentifier(String playerIdentifier) throws RemoteException {
        System.err.println("playerIdentifier received: " + playerIdentifier);
        this.playerIdentifier = playerIdentifier;
    }

    @Override
    public void setStartingCard(StartCard card) throws RemoteException {
        System.err.println("You received the starting card");
        System.err.println(card);
        System.err.println();
    }

    @Override
    public void setPublicGoal(Goal[] goals) throws RemoteException {
        System.err.println("You received the public goals");
        System.err.println(Arrays.toString(goals));
        System.err.println();
    }

    @Override
    public void setAvailablePrivateGoals(Goal[] goals) throws RemoteException {
        System.err.println("You received the available private goals");
        System.err.println(Arrays.toString(goals));
        System.err.println();
    }

    @Override
    public void setDefinitivePrivateGoal(Goal goal) throws RemoteException {
        System.err.println("You received your definitive private goal");
        System.err.println(goal);
        System.err.println();
    }

    @Override
    public void setPlayersCard(String playerNickname, PlayCard card, int index) throws RemoteException {
        index++;
        System.err.println(playerNickname+" received a card n°"+index);
        System.err.println(card);
        System.err.println();
    }

    @Override
    public void setVisibleCard(PlayCard card, int index) throws RemoteException {
        System.err.println("New visible card n°"+index);
        System.err.println(card);
        System.err.println();
    }

    @Override
    public void setDeckTopResource(Resource resource, int index) throws RemoteException {
        String deck = (index == 0 ? "resource" : "gold");
        System.err.println("New card on top of the "+deck+" card deck");
        System.err.println(resource.toString());
        System.err.println();
    }

    @Override
    public void placeCardOnPlayersBoard(String playerNickname,Card card, CardLocation location) throws RemoteException {
        System.err.println(playerNickname+" placed a card in " + location);
        System.err.println(card);
        System.err.println();
    }

    @Override
    public void updateGameList(Set<Integer> availableGames) throws RemoteException {
        System.err.println("Available games: " + availableGames);
    }
}
