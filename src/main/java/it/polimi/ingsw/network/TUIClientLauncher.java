package it.polimi.ingsw.network;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.view.ui.tui.TextualUserInterface;

import java.util.Scanner;

public class TUIClientLauncher {
    /**
     * The main method to run the tui client application.
     *
     * @param args commend line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserInterface userInterface= new TextualUserInterface(scanner);
        View view = new View(userInterface);

//        ServerHandler handler;
//        if (selectedProtocol == 1) handler = new SocketServerHandler("127.0.0.1", 1235);
//        else handler = new RMIServerHandler(new ViewWrapper(view), "127.0.0.1", 1234, "MainController");

        Client.getInstance().setup(view);
    }
}
