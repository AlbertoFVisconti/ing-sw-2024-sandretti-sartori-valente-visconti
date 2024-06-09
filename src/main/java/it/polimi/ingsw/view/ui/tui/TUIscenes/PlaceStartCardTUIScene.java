package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.PlaceStartCardMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.tui.Printer;

import java.security.InvalidParameterException;

public class PlaceStartCardTUIScene implements TUIScene{
    @Override
    public void render(String statusMessage) {
        Player localPlayer = Client.getInstance().getView().getLocalPlayer();

        System.out.println("\n\n" + statusMessage + "\n\n");
        System.out.println("Your starting card:\n");
        System.out.println(localPlayer.getStartCard());
        System.out.println("\tFront:");
        Printer.printCard(localPlayer.getStartCard(), false);
        System.out.println("\tBack");
        Printer.printCard(localPlayer.getStartCard(), true);

        System.out.println("\nSelect the starting card side (0 -> front, 1 -> back): ");
    }

    @Override
    public void processInput(String[] tokens) {
        int selection;
        try {
            selection = Integer.parseInt(tokens[0]);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(e);
        }

        if (selection != 0 && selection != 1) {
            throw new InvalidParameterException("invalid card side");
        }

        Client.getInstance().getServerHandler().sendMessage(new PlaceStartCardMessage(selection == 1));
    }
}
