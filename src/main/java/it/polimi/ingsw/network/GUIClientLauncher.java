package it.polimi.ingsw.network;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ui.gui.GraphicalUserInterface;

/**
 * Allows to launch a GUI client application to play the game through a graphical user interface
 */
public class GUIClientLauncher {
    /**
     * The main method to run the GUI client application.
     *
     * @param args commend line arguments
     */
    public static void main(String[] args) {
        GraphicalUserInterface userInterface = new GraphicalUserInterface();
        new Thread(userInterface).start();
        View view = new View(userInterface);

        Client.getInstance().setup(view);
    }
}
