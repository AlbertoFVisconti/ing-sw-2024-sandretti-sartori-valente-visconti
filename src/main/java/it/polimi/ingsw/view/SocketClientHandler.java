package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.events.messages.client.ClientMessage;
import it.polimi.ingsw.model.events.messages.server.ServerMessage;
import it.polimi.ingsw.network.rmi.GameControllerWrapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Implementation of the ClientHandler interface that allows to handle clients that communicate with
 * the server through TCP sockets. It also receives and dispatches messages from the client.
 */
public class SocketClientHandler extends Thread implements ClientHandler {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private GameController gameController;

    /**
     * Constructs a SocketClientHandler object that handles communication on the provided socket.
     *
     * @param socket the client's socket used to receive and send messages.
     */
    public SocketClientHandler(Socket socket) {
        this.socket = socket;
        this.setSocket(socket);
    }

    /**
     * Allows to change the client's socket.
     *
     * @param socket the new client's socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Triggered when an observed object notify its subscribers.
     * Can also be used to send messages to the client.
     *
     * @param message the message that needs to be delivered to the client.
     */
    @Override
    public void onUpdate(ServerMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method run with Thread.start().
     * Reads incoming messages and dispatches them.
     */
    @Override
    public void run() {
        ClientMessage message;

        while(true) {
            try {
                message = (ClientMessage) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            message.setSender(this);

            if(message.messageType == MessageType.CONNECT_JOIN_MESSAGE) {
                GameSelector.getInstance().forwardMessage(message);
            }
            else {
                gameController.forwardMessage(message);
            }

        }

    }

    /**
     * Allows to link the ClientHandler to the specific game that the client is playing.
     * In this case the GameControllerWrapper isn't actually sent to the client.
     * A reference to the actual GameController is keep within the SocketClientHandler and is used to
     * deliver incoming messages directly to the right controller.
     *
     * @param gameControllerWrapper the GameControllerWrapper object that contains a reference to the game the client is playing.
     */
    @Override
    public void setController(GameControllerWrapper gameControllerWrapper) {
        this.gameController = gameControllerWrapper.getGameController();
    }
}
