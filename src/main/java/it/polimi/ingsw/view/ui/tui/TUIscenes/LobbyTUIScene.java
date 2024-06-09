package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.SelectColorMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;

import java.security.InvalidParameterException;
import java.util.Set;

public class LobbyTUIScene implements TUIScene{
    @Override
    public void render(String statusMessage) {
        System.out.println("Connected Players:");
        for(Player p : Client.getInstance().getView().getPlayersList()) {
            System.out.print("\t" + p.nickName);

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
