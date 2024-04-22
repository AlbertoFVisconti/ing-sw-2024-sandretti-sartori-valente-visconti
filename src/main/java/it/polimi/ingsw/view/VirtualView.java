package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote {

    void setStartingCard(StartCard card) throws RemoteException;

    void setPublicGoal(Goal[] goals) throws RemoteException;

    void setAvailablePrivateGoals(Goal[] goals) throws RemoteException;

    void setDefinitivePrivateGoal(Goal goal) throws RemoteException;

    void setPlayersCard(PlayCard card, int index) throws RemoteException;

    void setVisibleCard(PlayCard card, int index) throws RemoteException;

    void setDeckTopResource(Resource resource, int index) throws RemoteException;

    void placeCardOnPlayersBoard(Card card, CardLocation location) throws RemoteException;
}