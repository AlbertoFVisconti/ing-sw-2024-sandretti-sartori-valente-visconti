package it.polimi.ingsw.network.serverhandlers;


import it.polimi.ingsw.events.messages.client.*;
import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.utils.CardLocation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketServerHandler extends ServerHandler implements Runnable   {
    private Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    public SocketServerHandler(UserInterface userInterface, String ip, int port) throws IOException {
        super(userInterface);

        socket = new Socket(ip, port);
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        new Thread(this).start();
    }

    @Override
    public void joinGame(int IDGame, String nick) {
        try {
            outputStream.writeObject(new JoinGameMessage(this.getUserInterface().getPlayerIdentifier(), IDGame, false, -1, nick));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(int expectedPlayers, String nick) {
        try {
            outputStream.writeObject(new JoinGameMessage(this.getUserInterface().getPlayerIdentifier(), -1, true, expectedPlayers, nick));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getAvailableGames() {
        try {
            outputStream.writeObject(new GameListRequestMessage(this.getUserInterface().getPlayerIdentifier()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeCard(int index, boolean onBackSide, CardLocation location) {
        try {
            outputStream.writeObject(new PlaceCardMessage(this.getUserInterface().getPlayerIdentifier(), index, onBackSide, location));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawCard(int index) {
        try {
            outputStream.writeObject(new DrawCardMessage(this.getUserInterface().getPlayerIdentifier(), index));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeStartCard(boolean onBackSide) {
        try {
            outputStream.writeObject(new PlaceStartCardMessage(this.getUserInterface().getPlayerIdentifier(), onBackSide));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectPrivateGoal(int index) {
        try {
            outputStream.writeObject(new SelectGoalMessage(this.getUserInterface().getPlayerIdentifier(), index));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectColor(PlayerColor color) {
        try {
            outputStream.writeObject(new SelectColorMessage(this.getUserInterface().getPlayerIdentifier(),color));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        ServerMessage message;

        while(true) {
            try {
                message = (ServerMessage) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            this.userInterface.forwardMessage(message);
        }
    }
}
