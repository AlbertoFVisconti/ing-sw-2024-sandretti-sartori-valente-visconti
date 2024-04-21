package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualController extends Remote {
    public void placeCard(String playerIdentifier, int index, CardLocation location) throws RemoteException;

    public void drawCard(String playerIdentifier, int index) throws RemoteException;

    public void placeStartCard(String playerIdentifier, boolean onBackSide) throws RemoteException;

    public void selectPrivateGoal(String playerIdentifier, int index) throws RemoteException;
}
