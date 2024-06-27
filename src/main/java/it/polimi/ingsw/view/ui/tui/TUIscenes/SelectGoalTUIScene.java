package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.SelectGoalMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.tui.Printer;

import java.security.InvalidParameterException;

/**
 * SelectGoalTUIScene handles the interface that allows the TUI user to select the
 * private goal.
 */
public class SelectGoalTUIScene extends TUIScene{
    /**
     * Displays the private goals and asks the user to select one of them
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    @Override
    public void render(String statusMessage) {
        System.out.println("\n\n" + statusMessage + "\n\n");

        Player p = Client.getInstance().getView().getLocalPlayer();

        if(p == null || p.getAvailableGoals() == null) return;

        System.out.println("Goal #0:");
        Printer.printGoal(p.getAvailableGoals()[0]);

        System.out.println("Goal #1");
        Printer.printGoal(p.getAvailableGoals()[1]);


        System.out.println("\n\nSelect goal: ");
    }

    /**
     * Allows to provide the user selection
     *
     * @param tokens user inputted tokens
     */
    @Override
    public void processInput(String[] tokens) {
        int selection;
        try {
            selection = Integer.parseInt(tokens[0]);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(e);
        }

        if (selection <  0 || selection >= Client.getInstance().getView().getLocalPlayer().getAvailableGoals().length) {
            throw new InvalidParameterException("invalid goal");
        }

        Client.getInstance().getServerHandler().sendMessage(new SelectGoalMessage(selection));
    }
}
