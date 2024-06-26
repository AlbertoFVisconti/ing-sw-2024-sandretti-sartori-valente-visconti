package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.DrawCardMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ui.tui.Printer;

import java.security.InvalidParameterException;

/**
 * DrawTUIScene handles the interface that allows the TUI user to pick
 * up one of the drawable cards.
 */
public class DrawTUIScene extends TUIScene{
    /**
     * Displays the drawable cards and asks the user to select the card to draw/pick up
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    @Override
    public void render(String statusMessage) {
        View view = Client.getInstance().getView();
        Printer.printDrawableCards(view.getGameModel().getResourceCardsDeck(), view.getGameModel().getGoldCardsDeck(), view.getGameModel().getVisibleCards());

        System.out.println("\n\n" + statusMessage + "\n\n");

        System.out.print("\n\nSelect Card to pick up: ");
    }

    /**
     * Allows to provide the user selection.
     *
     * @param tokens user inputted tokens
     */
    @Override
    public void processInput(String[] tokens) {
        if(tokens.length == 0) return;

        int selectedCard;
        try {
            selectedCard = Integer.parseInt(tokens[0]);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(e);
        }

        if (selectedCard < 0 || selectedCard >= 6) {
            throw new InvalidParameterException("invalid selection");
        }

        Client.getInstance().getServerHandler().sendMessage(new DrawCardMessage(selectedCard));
    }
}
