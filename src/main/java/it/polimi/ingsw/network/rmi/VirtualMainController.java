package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VirtualMainController extends Remote {
    String joinGame (VirtualView view, int IDgame, PlayerColor color, String nick) throws RemoteException;
    String createGame(VirtualView view, int expectedPlayers, PlayerColor color, String nick) throws RemoteException;

    List<Integer> getAvailableGames() throws RemoteException;
}
