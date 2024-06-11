package it.polimi.ingsw.network;

import it.polimi.ingsw.network.serverhandlers.RMIServerHandler;
import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.network.serverhandlers.SocketServerHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewWrapper;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.view.ui.gui.GraphicalUserInterface;
import it.polimi.ingsw.view.ui.tui.TextualUserInterface;

import java.io.IOException;
import java.util.Scanner;

/**
 * Allows to run the client application that allows to play the game as a client.
 */
public class Client {
    // Singleton
    public static Client instance;

    // local view
    private final View view;

    // Server Handler
    private final ServerHandler serverHandler;

    /**
     * Builds a Client with the specified view and server handler
     *
     * @param view the local view
     * @param serverHandler the local server handler
     */
    private Client(View view, ServerHandler serverHandler) {
        this.view = view;
        this.serverHandler = serverHandler;

        instance = this;

        try {
            serverHandler.connect();
        } catch (Exception e) {
            // connection to the server failed
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Returns a reference to the Client object
     *
     * @return reference to Client object
     */
    public static Client getInstance() {
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
     * Starts the client application by running the view
     */
    private void run() {
        view.start();
    }

    /**
     * The main method to run the client application.
     *
     * @param args commend line arguments
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        int selectedProtocol, selectedInterface;

        // asking to select the connection protocol (socket or RMI)
        do {
            System.out.println("Select protocol (1 -> socket, 2 -> RMI): ");
            selectedProtocol = scanner.nextInt();
        } while (selectedProtocol != 1 && selectedProtocol != 2);

        // asking to select between TUI/CLI and GUI
        do {
            System.out.println("Select User Interface (1 -> TUI, 2 -> GUI): ");
            selectedInterface = scanner.nextInt();
        } while (selectedInterface != 1 && selectedInterface != 2);


        UserInterface userInterface;
        if (selectedInterface == 2) {
            userInterface = new GraphicalUserInterface();
            new Thread((GraphicalUserInterface)userInterface).start();
        }
        else userInterface= new TextualUserInterface(scanner);


        View view = new View(userInterface);

        ServerHandler handler;
        if (selectedProtocol == 1) handler = new SocketServerHandler("127.0.0.1", 1235);
        else handler = new RMIServerHandler(new ViewWrapper(view), "127.0.0.1", 1234, "MainController");


        Client client = new Client(view, handler);
        client.run();

    }
}
