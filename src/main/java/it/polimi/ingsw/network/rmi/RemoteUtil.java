package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.decks.Deck;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface RemoteUtil extends Remote{
    public void joinGame ( int idgame, int color, String nick) throws RemoteException;
    public Card drawTopCard(Deck deck) throws RemoteException;
    public Card drawVisibleCard(int i) throws RemoteException;
    public void place(Point point) throws RemoteException;

}
