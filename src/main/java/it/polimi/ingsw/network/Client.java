package it.polimi.ingsw.network;

import it.polimi.ingsw.network.serverhandlers.RMIServerHandler;
import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.network.serverhandlers.SocketServerHandler;
import it.polimi.ingsw.view.ViewWrapper;
import it.polimi.ingsw.view.ui.gui.FXGraphicalUserInterface;
import it.polimi.ingsw.view.ui.tui.TextualUserInterface;
import it.polimi.ingsw.view.ui.UserInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class Client {
    private final UserInterface userInterface;
    public static Client instance;
    private final ServerHandler serverHandler;


    private Client(UserInterface userInterface, ServerHandler serverHandler) {
        this.userInterface = userInterface;
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



    private void run() {
        userInterface.start();
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        Scanner scanner = new Scanner(System.in);

        int selectedProtocol, selectedInterface;
        do {
            System.out.println("Select protocol (1 -> socket, 2 -> RMI): ");
            selectedProtocol = scanner.nextInt();
        } while(selectedProtocol != 1 && selectedProtocol != 2);

        do {
            System.out.println("Select User Interface (1 -> TUI, 2 -> GUI): ");
            selectedInterface = scanner.nextInt();
        } while(selectedInterface != 1 && selectedInterface != 2);


        UserInterface userInterface;
        if(selectedInterface == 2) userInterface = new FXGraphicalUserInterface();
        else userInterface = new TextualUserInterface(scanner);

        ServerHandler handler;
        if(selectedProtocol == 1) handler = new SocketServerHandler("127.0.0.1", 1235);
        else handler = new RMIServerHandler( new ViewWrapper(userInterface), "127.0.0.1", 1234, "MainController");


        Client client = new Client(userInterface, handler);
        client.run();

    }

    public UserInterface getUserInterface() {
        return userInterface;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }
    public static Client getInstance() {
        return instance;
    }


}
