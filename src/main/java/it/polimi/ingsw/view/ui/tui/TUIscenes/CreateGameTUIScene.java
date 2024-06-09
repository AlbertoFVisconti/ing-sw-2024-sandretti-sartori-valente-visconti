package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.network.Client;

import java.security.InvalidParameterException;

public class CreateGameTUIScene implements TUIScene{
    int expectedPlayers;
    String nickname;

    int providedFields = 0;

    @Override
    public void render(String statusMessage) {
        System.out.print("Create a game:\n");
        System.out.print("\tExpected Players: ");
        if(providedFields == 0) return;

        System.out.print(this.expectedPlayers + "\n");
        System.out.print("\tNickname: ");
        if(providedFields == 1) return;

        System.out.print(this.nickname + "\n");
    }

    @Override
    public void processInput(String[] tokens) {
        if(tokens.length == 0) return;

        if(this.providedFields == 0) {
            try {
                expectedPlayers = Integer.parseInt(tokens[0]);
            } catch (NumberFormatException e) {
                throw new InvalidParameterException("Invalid expected players value");
            }

            if (expectedPlayers < 2 || expectedPlayers > 4) {
                throw new InvalidParameterException("a game cannot contain less then 2 players or more than 4");
            }

            this.providedFields++;
        }
        else if(this.providedFields == 1) {
            this.nickname = tokens[0];
            if(nickname.isEmpty()) throw new InvalidParameterException("Insert a valid nickname");
            this.providedFields++;
        }

        if(this.providedFields == 2) {
            Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(-1, true, expectedPlayers, nickname));
        }
    }
}
