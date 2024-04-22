package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameSelector;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class MainController extends UnicastRemoteObject implements VirtualMainController {
    public MainController() throws RemoteException {
    }

    @Override
    public String joinGame(VirtualView view, int IDgame, PlayerColor color, String nick) throws RemoteException {
        GameSelector gameSelector = GameSelector.getInstance();

        GameController controller = gameSelector.getGameController(IDgame);

        if(controller == null) throw new NoSuchElementException("The selected game does not exist");

        // TODO: check if game is already started (also check if player is trying to rejoin)

        if(!GameSelector.isAvailable(controller.getGame(), nick)) throw new IllegalArgumentException("The selected nickname is not available");

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Random rand = new Random();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String t = IDgame + nick + timestamp + rand.nextInt(0, 1000);

        md.update(t.getBytes());
        byte[] digest = md.digest();

        String playerIdentifier = String.format("%032X", new BigInteger(1, digest));

        try {
            gameSelector.addPlayer(IDgame, playerIdentifier, nick, color, view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.err.println(nick + " joined the game");

        controller.updateStatus();

        return playerIdentifier;
    }

    @Override
    public String createGame(VirtualView view, int expectedPlayers, PlayerColor color, String nick) throws RemoteException {
        GameSelector gameSelector = GameSelector.getInstance();

        try {
            return this.joinGame(view, gameSelector.CreateGame(expectedPlayers), color, nick);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> getAvailableGames() throws RemoteException {
        return GameSelector.getInstance().getAvailableGames().stream().toList();
    }
}
