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
     *
     */
    private Client() {}

    void setup(View view) {
        this.view = view;

        boolean done = false;
        while (!done) {
            synchronized (this) {
                if (this.uiInitialized) {
                    view.getUserInterface().setProtocolScene();
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

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;

        try {
            serverHandler.connect();
        } catch (Exception e) {
            // connection to the server failed
            this.serverHandler = null;
        }

        if(this.serverHandler != null)
            view.getUserInterface().setStartingScene();
        else {
            this.view.getUserInterface().reportError(new RuntimeException("Connection Failed"));
        }
    }

    public synchronized void confirmUIInitialized() {
        this.uiInitialized = true;
    }
}
