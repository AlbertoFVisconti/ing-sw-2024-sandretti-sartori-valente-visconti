package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.network.Client;

import java.security.InvalidParameterException;

/**
 * JoinGameTUIScene handles the interface that allows the TUI user to
 * join one of the available games
 */
public class JoinGameTUIScene extends TUIScene{
    // input fields
    private String gameID;
    private String nickname;

    /**
     * Displays the list of available games and asks the user to input data in order to join
     * one of them
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    @Override
    public void render(String statusMessage) {
        System.out.println("Available Games:");

        if(Client.getInstance().getView().getAvailableGames() != null) {
            for(String g : Client.getInstance().getView().getAvailableGames()) {
                System.out.println("\t" + g);
            }
        }


        System.out.print("\nSelect game to join: \n");
        if(providedInput == 0) return;

        System.out.print(gameID + "\n");
        System.out.print("\tNickname: ");
        if(providedInput == 1) return;

        System.out.print(this.nickname + "\n");
    }

    /**
     * Allows to provide the data inputted by the user to the interface in order to join game.
     *
     * @param tokens user inputted tokens
     */
    @Override
    public void processInput(String[] tokens) {
        if(tokens.length == 0) return;

        if(this.providedInput == 0) {
            gameID = tokens[0];

            if (!Client.getInstance().getView().getAvailableGames().contains(gameID)) {
                throw new InvalidParameterException("a game cannot contain less then 2 players and more than 4");
            }

            this.providedInput++;
        }
        else if(this.providedInput == 1) {
            this.nickname = tokens[0];
            if(nickname.isEmpty()) throw new InvalidParameterException("Insert a valid nickname");
            this.providedInput++;
        }

        if(this.providedInput == 2) {
            Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(gameID, false, -1, nickname));
        }
    }
}
