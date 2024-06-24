package it.polimi.ingsw.network;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ui.gui.GraphicalUserInterface;

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
