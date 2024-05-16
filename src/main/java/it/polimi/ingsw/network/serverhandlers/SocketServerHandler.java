package it.polimi.ingsw.network.serverhandlers;


import it.polimi.ingsw.events.messages.client.*;
import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.network.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketServerHandler extends ServerHandler implements Runnable   {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private final String ip;
    private final int port;

    public SocketServerHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected void forwardMessage(ClientMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void connect() throws Exception {
        socket = new Socket(ip, port);
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        new Thread(this).start();
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

            Client.getInstance().getUserInterface().forwardMessage(message);
        }
    }
}
