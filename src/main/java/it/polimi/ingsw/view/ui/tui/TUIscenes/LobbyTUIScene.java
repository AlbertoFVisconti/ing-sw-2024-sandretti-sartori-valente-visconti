package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.SelectColorMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;

import java.security.InvalidParameterException;
import java.util.Set;

/**
 * LobbyTUIScene handles the interface that allows the TUI user to select its color
 * and view the list of connected players
 */
public class LobbyTUIScene extends TUIScene{
    /**
     * Display the list of connected players and the list of available colors
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    @Override
    public void render(String statusMessage) {
        System.out.println("Connected Players:");
        for(Player p : Client.getInstance().getView().getPlayersList()) {
            System.out.print("\t" + p.nickname);

            if(p.getColor() != null) {
                System.out.print("\t\t\t" + p.getColor().toString());
            }

            System.out.print("\n");
        }

        System.out.println("\n\n" + statusMessage + "\n\n");

        Set<PlayerColor> availableColors = Client.getInstance().getView().getAvailableColors();
        if(availableColors != null) {
            for(PlayerColor color : availableColors) {
                System.out.println("\t - " + color.toString());
            }
        }
        System.out.println("Insert your color: ");
    }

    /**
     * Allows to provide the user input in order to select color
     *
     * @param tokens user inputted tokens
     */
    @Override
    public void processInput(String[] tokens) {
        if(tokens.length == 0) return;

        PlayerColor color;
        try {
            color = PlayerColor.valueOf(tokens[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("invalid color");
        }

        Client.getInstance().getServerHandler().sendMessage(new SelectColorMessage(color));
    }
}
