package it.polimi.ingsw.network.cliendhandlers;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.events.messages.client.ClientMessage;
import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.network.rmi.GameControllerWrapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Implementation of the ClientHandler interface that allows to handle clients that communicate with
 * the server through TCP sockets. It also receives and dispatches messages from the client.
 */
public class SocketClientHandler extends ClientHandler {
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private GameController gameController;

    /**
     * Constructs a SocketClientHandler object that handles communication on the provided socket.
     *
     * @param socket the client's socket used to receive and send messages.
     */
    public SocketClientHandler(Socket socket) {
        super();

        this.setSocket(socket);

        new Thread(
                () -> {
                    ClientMessage message;

                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            // reading the next incoming message
                            message = (ClientMessage) inputStream.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            // client's disconnected
                            this.forceDisconnection();
                            break;
                        }

                        if (message.messageType == MessageType.CONNECT_JOIN_MESSAGE ||
                                message.messageType == MessageType.PING_MESSAGE) {
                            // forwarding the message to the MainController
                            MainController.getInstance().forwardMessage(message);
                        } else {
                            // forwarding the message to the GameController
                            gameController.forwardMessage(message);
                        }

                    }
                }
        ).start();
    }

    /**
     * Allows to change the client's socket.
     *
     * @param socket the new client's socket
     */
    public void setSocket(Socket socket) {
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a message to the client through socket.
     *
     * @param message ServerMessage object containing the data that needs to be sent to the client
     */
    @Override
    public synchronized void sendMessage(ServerMessage message) {
        try {
            outputStream.writeObject(message);
            outputStream.reset();
        } catch (IOException e) {

            this.forceDisconnection();
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
    public void linkController(GameControllerWrapper gameControllerWrapper) {
        this.gameController = gameControllerWrapper.getGameController();
    }
}
