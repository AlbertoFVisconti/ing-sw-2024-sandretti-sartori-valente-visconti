package it.polimi.ingsw.network;

import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.view.View;

/**
 * Allows to run the client application that allows to play the game as a client.
 */
public class Client {
    // Singleton
    public static Client instance;

    // local view
    private View view = null;

    // Server Handler
    private ServerHandler serverHandler = null;

    private Boolean uiInitialized = false;

    /**
     * Builds a Client
     */
    private Client() {}

    /**
     * Allows report disconnection from the server to the user.
     * This method bring the user to the connection interface so that
     * they can retry connection.
     */
    public void disconnect() {
        this.serverHandler = null;
        this.view.getUserInterface().setConnectionScene();
        this.view.getUserInterface().reportError(new RuntimeException(
                "Failed to reach server!"
        ));
    }

    /**
     * Sets up the client's View.
     * Blocks the client until the user interface is ready
     *
     * @param view the local player's view
     */
    void setup(View view) {
        this.view = view;

        boolean done = false;
        while (!done) {
            synchronized (this) {
                if (this.uiInitialized) {
                    view.getUserInterface().setConnectionScene();
                    done = true;
                }
            }
        }
    }

    /**
     * Returns a reference to the Client object
     *
     * @return reference to Client object
     */
    public static Client getInstance() {
        if (instance == null) instance = new Client();
        return instance;
    }

    /**
     * Retrieves the local view
     *
     * @return the local View
     */
    public View getView() {
        return view;
    }

    /**
     * Retrieves the local server handler
     *
     * @return the local ServerHandler
     */
    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    /**
     * Allows to set the Client's ServerHandler.
     * Used when the player select the protocol and the server's data.
     * If the connection fails, this method will report the error to the user.
     *
     * @param serverHandler ServerHandler that will manage communication with the server (mostly outbound)
     */
    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;

        try {
            serverHandler.connect();
        } catch (Exception e) {
            // connection to the server failed
            this.serverHandler = null;
        }

        if(this.serverHandler != null)
            view.getUserInterface().setMainScene();
        else {
            this.view.getUserInterface().reportError(new RuntimeException("Connection Failed"));
        }
    }

    /**
     * Allows to let the Client know that the user interface has been successfully initialized
     */
    public synchronized void confirmUIInitialized() {
        this.uiInitialized = true;
    }
}
