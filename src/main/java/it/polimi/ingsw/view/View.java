package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class View extends UnicastRemoteObject implements VirtualView{
    public View() throws RemoteException {}

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
    public void setPlayersCard(PlayCard card, int index) throws RemoteException {
        index++;
        System.err.println("You received your card n°"+index);
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
    public void placeCardOnPlayersBoard(Card card, CardLocation location) throws RemoteException {
        System.err.println("Card Placed in " + location);
        System.err.println(card);
        System.err.println();
    }
}
