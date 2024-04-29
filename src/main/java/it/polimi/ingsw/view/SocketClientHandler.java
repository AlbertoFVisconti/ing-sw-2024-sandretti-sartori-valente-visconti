package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.events.messages.client.ClientMessage;
import it.polimi.ingsw.model.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.model.events.messages.server.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler extends Thread implements ClientHandler {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public SocketClientHandler(Socket socket) {
        this.socket = socket;
        this.setSocket(socket);
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdate(ServerMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        ClientMessage message;
        GameSelector gameSelector = GameSelector.getInstance();

        while(true) {
            try {
                message = (ClientMessage) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(message.messageType == MessageType.CONNECT_JOIN_MESSAGE) {
                ((JoinGameMessage)message).setClientHandler(this);
            }
            gameSelector.forwardMessage(message);
        }

    }
}
