package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.view.SocketClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private static final int SERVER_PORT = 1235;

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket clientSocket;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(true) {
            System.err.println("Waiting for a client to connect...");
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            new SocketClientHandler(clientSocket).start();
        }

    }
}
