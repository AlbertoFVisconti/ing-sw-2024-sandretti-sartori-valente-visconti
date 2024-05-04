package it.polimi.ingsw.network.serverhandlers;


import it.polimi.ingsw.events.messages.client.*;
import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketServerHandler extends ServerHandler {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final String ip;
    private final int port;
    public SocketServerHandler(View clientView, String ip, int port) throws IOException {
        super(clientView);

        this.ip = ip;
        this.port = port;

        socket = new Socket(ip, port);
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        new Thread(
                () -> {
                    ServerMessage message;

                    while(true) {
                        try {
                            message = (ServerMessage) inputStream.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        this.forwardMessage(message);
                    }
                }
        ).start();
    }

    @Override
    public void joinGame(int IDGame, PlayerColor color, String nick) {
        try {
            outputStream.writeObject(new JoinGameMessage(this.getClientView().getPlayerIdentifier(), IDGame, false, -1, nick, color));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(int expectedPlayers, PlayerColor color, String nick) {
        try {
            outputStream.writeObject(new JoinGameMessage(this.getClientView().getPlayerIdentifier(), -1, true, expectedPlayers, nick, color));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getAvailableGames() {
        try {
            outputStream.writeObject(new GameListRequestMessage(this.getClientView().getPlayerIdentifier()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeCard(int index, boolean onBackSide, CardLocation location) {
        try {
            outputStream.writeObject(new PlaceCardMessage(this.getClientView().getPlayerIdentifier(), index, onBackSide, location));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawCard(int index) {
        try {
            outputStream.writeObject(new DrawCardMessage(this.getClientView().getPlayerIdentifier(), index));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeStartCard(boolean onBackSide) {
        try {
            outputStream.writeObject(new PlaceStartCardMessage(this.getClientView().getPlayerIdentifier(), onBackSide));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectPrivateGoal(int index) {
        try {
            outputStream.writeObject(new SelectGoalMessage(this.getClientView().getPlayerIdentifier(), index));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
