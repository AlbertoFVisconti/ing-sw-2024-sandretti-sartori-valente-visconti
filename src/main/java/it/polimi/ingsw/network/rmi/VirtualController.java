package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualController extends Remote {
    void placeCard(String playerIdentifier, int index, boolean onBackSide, CardLocation location) throws RemoteException;

    void drawCard(String playerIdentifier, int index) throws RemoteException;

    void placeStartCard(String playerIdentifier, boolean onBackSide) throws RemoteException;

    void selectPrivateGoal(String playerIdentifier, int index) throws RemoteException;
}
