package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.network.cliendhandlers.SocketClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {

    private final int port;

    public SocketServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        Socket clientSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(true) {
            System.err.println("Waiting for a socket client to connect...");
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            GameSelector.getInstance().connectClient(new SocketClientHandler(clientSocket));
        }

    }
}
