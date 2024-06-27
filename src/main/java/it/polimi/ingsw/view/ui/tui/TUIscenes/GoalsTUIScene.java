package it.polimi.ingsw.view.ui.tui.TUIscenes;


import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.tui.Printer;

public class GoalsTUIScene extends TUIScene{
    /**
     * Display the game scoreboard/results
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    @Override
    public void render(String statusMessage) {
        System.out.println("Private Goal:");
        Printer.printGoal(Client.getInstance().getView().getLocalPlayer().getPrivateGoal());
        System.out.println("\nCommon Goal #1: ");
        Printer.printGoal(Client.getInstance().getView().getGameModel().getCommonGoals()[0]);
        System.out.println("\nCommon Goal #2: ");
        Printer.printGoal(Client.getInstance().getView().getGameModel().getCommonGoals()[1]);

        System.out.println("\n\n" + statusMessage + "\n\n");
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
