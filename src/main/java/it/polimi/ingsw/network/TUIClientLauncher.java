package it.polimi.ingsw.network;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.view.ui.tui.TextualUserInterface;

import java.util.Scanner;

/**
 * Allows to launch a TUI client application to play the game through a textual user interface
 */
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

        Client.getInstance().setup(view);
    }
}
