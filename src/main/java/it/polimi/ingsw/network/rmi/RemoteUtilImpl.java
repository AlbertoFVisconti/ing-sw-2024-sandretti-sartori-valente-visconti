package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameSelector;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.decks.Deck;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class RemoteUtilImpl extends UnicastRemoteObject implements RemoteUtil {
    public RemoteUtilImpl() throws RemoteException{};
    @Override
    public void joinGame( int idgame, int color, String nick) throws RemoteException {
    }

    @Override
    public Card drawTopCard(Deck deck) throws RemoteException {
        return null;
    }

    @Override
    public Card drawVisibleCard(int i) throws RemoteException {
        return null;
    }

    @Override
    public void place(Point point) throws RemoteException {

    }
}
