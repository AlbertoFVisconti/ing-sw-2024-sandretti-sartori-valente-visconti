package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.network.cliendhandlers.SocketClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread object that needs to be run in order to allow connection from socket clients.
 */
public class SocketServer extends Thread {
    // the port number on which the server listens for incoming connection
    private final int port;

    /**
     * Builds a SocketServer with the specified port number
     *
     * @param port the port number
     */
    public SocketServer(int port) {
        this.port = port;
    }

    /**
     * Thread method that listen for incoming socket client connection and handles them
     */
    @Override
    public void run() {
        ServerSocket serverSocket;
        Socket clientSocket;
        try {
            // creating a server socket with the specified port
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.err.println("Socket Server ready");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                // accept next incoming connection
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // forward the connection request to the MainController that will handle it
            MainController.getInstance().connectClient(new SocketClientHandler(clientSocket));
        }

    }
}
