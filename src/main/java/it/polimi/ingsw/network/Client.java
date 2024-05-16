package it.polimi.ingsw.network;

import it.polimi.ingsw.network.serverhandlers.RMIServerHandler;
import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.network.serverhandlers.SocketServerHandler;
import it.polimi.ingsw.view.ui.FXGraphicalUserInterface;
import it.polimi.ingsw.view.ui.TextualUserInterface;
import it.polimi.ingsw.view.ui.UserInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    private final UserInterface userInterface;
    public static Client instance;
    private ServerHandler serverHandler;


    private Client(int selectedInterface, int selectedProtocol, Scanner scanner, ServerHandler handler) throws NotBoundException, IOException {
        instance = this;
        if(selectedInterface == 2) userInterface = new FXGraphicalUserInterface();
        else userInterface = new TextualUserInterface(scanner);

        if(selectedProtocol == 1) handler = new SocketServerHandler(userInterface, "127.0.0.1", 1235);
        else handler = new RMIServerHandler(userInterface, "127.0.0.1", 1234);

        userInterface.setServerHandler(handler);


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
//        if(selectedInterface == 2) userInterface = new FXGraphicalUserInterface(); //userInterface = new GraphicalUserInterface();
//        else userInterface = new TextualUserInterface(scanner);

        ServerHandler handler = null;
//        if(selectedProtocol == 1) handler = new SocketServerHandler(userInterface, "127.0.0.1", 1235);
//        else handler = new RMIServerHandler(userInterface, "127.0.0.1", 1234);


//        userInterface.setServerHandler(handler);

        Client client = new Client(selectedInterface, selectedProtocol, scanner, handler);
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
