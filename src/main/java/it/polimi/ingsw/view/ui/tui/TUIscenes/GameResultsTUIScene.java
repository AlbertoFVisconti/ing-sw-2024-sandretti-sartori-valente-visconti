package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.tui.Printer;

/**
 * GameResultsTUIScene handles the interface that allows the TUI user to
 * view the game scoreboard/results
 */
public class GameResultsTUIScene extends TUIScene{
    /**
     * Display the game scoreboard/results
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    @Override
    public void render(String statusMessage) {
        Printer.printScoreBoard(Client.getInstance().getView().getGameModel().getScoreBoard(), Client.getInstance().getView().getPlayersList());
        System.out.println("\n\n");
    }

    /**
     * GameResultsTUIScene doesn't require any input.
     * This method does nothing.
     *
     * @param tokens user inputted tokens
     */
    @Override
    public void processInput(String[] tokens) {}
}
