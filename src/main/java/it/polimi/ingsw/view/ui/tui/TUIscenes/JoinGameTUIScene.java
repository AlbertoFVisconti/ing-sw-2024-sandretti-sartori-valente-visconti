package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.network.Client;

import java.security.InvalidParameterException;

public class JoinGameTUIScene implements TUIScene{
    private int providedFields = 0;
    private int gameID;
    private String nickname;

    @Override
    public void render(String statusMessage) {
        System.out.println("Available Games:");

        if(Client.getInstance().getView().getAvailableGames() != null) {
            for(Integer g : Client.getInstance().getView().getAvailableGames()) {
                System.out.println("\t" + g);
            }
        }


        System.out.print("\nSelect game to join: \n");
        if(providedFields == 0) return;

        System.out.print(gameID + "\n");
        System.out.print("\tNickname: ");
        if(providedFields == 1) return;

        System.out.print(this.nickname + "\n");
    }

    @Override
    public void processInput(String[] tokens) {
        if(tokens.length == 0) return;

        if(this.providedFields == 0) {
            try {
                gameID = Integer.parseInt(tokens[0]);
            } catch (NumberFormatException e) {
                throw new InvalidParameterException(e);
            }

            if (!Client.getInstance().getView().getAvailableGames().contains(gameID)) {
                throw new InvalidParameterException("a game cannot contain less then 2 players and more than 4");
            }

            this.providedFields++;
        }
        else if(this.providedFields == 1) {
            this.nickname = tokens[0];
            if(nickname.isEmpty()) throw new InvalidParameterException("Insert a valid nickname");
            this.providedFields++;
        }

        if(this.providedFields == 2) {
            Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(gameID, false, -1, nickname));
        }
    }
}
