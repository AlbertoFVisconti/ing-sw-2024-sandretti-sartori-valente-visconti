package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.network.Client;

import java.security.InvalidParameterException;

/**
 * CreateGameTUIScene handles the interface that allows the TUI user to create a new
 * game.
 */
public class CreateGameTUIScene extends TUIScene{
    // input fields
    int expectedPlayers;
    String nickname;

    /**
     * Display the input requests for the user to create a game
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    @Override
    public void render(String statusMessage) {
        System.out.print("Create a game:\n");
        System.out.print("\tExpected Players: ");
        if(providedInput == 0) return;

        System.out.print(this.expectedPlayers + "\n");
        System.out.print("\tNickname: ");
        if(providedInput == 1) return;

        System.out.print(this.nickname + "\n");
    }

    /**
     * Provide user inputted data that allows to provide data in order to create a game.
     *
     * @param tokens user inputted tokens
     */
    @Override
    public void processInput(String[] tokens) {
        if(tokens.length == 0) return;

        if(this.providedInput == 0) {
            try {
                expectedPlayers = Integer.parseInt(tokens[0]);
            } catch (NumberFormatException e) {
                throw new InvalidParameterException("Invalid expected players value");
            }

            if (expectedPlayers < 2 || expectedPlayers > 4) {
                throw new InvalidParameterException("a game cannot contain less then 2 players or more than 4");
            }

            this.providedInput++;
        }
        else if(this.providedInput == 1) {
            this.nickname = tokens[0];
            if(nickname.isEmpty()) throw new InvalidParameterException("Insert a valid nickname");
            this.providedInput++;
        }

        if(this.providedInput == 2) {
            Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(null, true, expectedPlayers, nickname));
        }
    }
}
