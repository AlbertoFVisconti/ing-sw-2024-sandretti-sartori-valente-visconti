package it.polimi.ingsw.network;

import it.polimi.ingsw.network.serverhandlers.RMIServerHandler;
import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.network.serverhandlers.SocketServerHandler;
import it.polimi.ingsw.view.ui.GraphicalUserInterface;
import it.polimi.ingsw.view.ui.TextualUserInterface;
import it.polimi.ingsw.view.ui.UserInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class Client {
    private final UserInterface userInterface;

    private Client(UserInterface userInterface) {
        this.userInterface = userInterface;
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
        if(selectedInterface == 2) userInterface = new GraphicalUserInterface();
        else userInterface = new TextualUserInterface(scanner);

        ServerHandler handler;
        if(selectedProtocol == 1) handler = new SocketServerHandler(userInterface, "127.0.0.1", 1235);
        else handler = new RMIServerHandler(userInterface, "127.0.0.1", 1234);


        userInterface.setServerHandler(handler);

        Client client = new Client(userInterface);
        client.run();

    }


}
