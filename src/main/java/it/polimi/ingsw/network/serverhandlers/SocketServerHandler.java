package it.polimi.ingsw.network.serverhandlers;


import it.polimi.ingsw.events.messages.Message;
import it.polimi.ingsw.events.messages.client.ClientMessage;
import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.network.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Handle the client-side socket connection to the server.
 */
public class SocketServerHandler extends ServerHandler implements Runnable {
    // the socket that connects to the server and output/input streams
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    // Socket server's IP address
    private final String ip;
    // Socket server's port
    private final int port;

    /**
     * Builds a SocketServerHandler with the specified parameters
     *
     * @param ip socket server's IP address
     * @param port socket server's port
     */
    public SocketServerHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Sends a message to the server through socket connection
     *
     * @param message ClientMessage that needs to be sent
     */
    @Override
    protected void forwardMessage(ClientMessage message) {
        try {
            outputStream.writeObject(message);
            outputStream.reset();
        } catch (IOException e) {
            Client.getInstance().disconnect();
        }
    }

    /**
     *
     *
     * @throws Exception if an error occurs during the connection
     */
    @Override
    public void connect() throws Exception {
        socket = new Socket(ip, port);
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        new Thread(this).start();
    }

    /**
     * Thread method that process inbound message and forwards them to the View
     */
    @Override
    public void run() {
        Message message;

        while (!Thread.currentThread().isInterrupted()) {
            try {
                message = (Message) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                // cannot reach the server
                Client.getInstance().disconnect();
                return;
            }

            Client.getInstance().getView().forwardMessage((ServerMessage) message);
        }
    }
}
