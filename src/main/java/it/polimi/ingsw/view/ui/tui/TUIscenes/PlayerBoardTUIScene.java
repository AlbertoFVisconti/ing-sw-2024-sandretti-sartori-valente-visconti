package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.PlaceCardMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.ui.tui.Printer;

import java.security.InvalidParameterException;

/**
 * PlayerBoardTUIScene handles the interface that allows the user to view
 * a player's board and to place cards.
 */
public class PlayerBoardTUIScene extends TUIScene{
    // player whose board needs to be displayed
    private Player player;

    private boolean placing;

    // input fields
    int card;
    int side;
    int x;
    int y;

    private int xOffset=0, yOffset =0;

    /**
     * Builds a PlayerBoardTUIScene that displays the given player's board
     *
     * @param player Player whose board needs to be displayed
     */
    public PlayerBoardTUIScene(Player player) {
        this.player = player;
    }

    /**
     * Allow to change the Player whose board is displayed.
     *
     * @param player Player whose board needs to be displayed
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Displays the board and asks the data required to place a card (if placement is possible)
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    @Override
    public void render(String statusMessage) {
        Printer.printBoard(player.getBoard(),xOffset,yOffset);
        System.out.println("\n\nYour Cards:");
        Printer.printHand(player.getPlayerCards());
        System.out.println("\n\n" + statusMessage + "\n\n");


        if(placing) {
            System.out.print("Selected card: ");
            if (providedInput == 0) return;
            System.out.print(card + "\n");
            System.out.print("Select side (0 -> front, 1 -> back): ");
            if (providedInput == 1) return;
            System.out.print(side + "\n");
            System.out.print("Placing locatio!|n:\n");
            System.out.print("\tX: ");
            if (providedInput == 2) return;
            System.out.print(x + "\n");
            System.out.print("\tY: ");
            if (providedInput == 3) return;
            System.out.print(y + "\n\n");
        }
        else {
            System.out.println("Use: u (up), d (down), l (left), r (right) to go through the board");

            if(player.nickname.equals(Client.getInstance().getView().getLocalPlayerName())
                    && player.nickname.equals(Client.getInstance().getView().getPlayersTurn())) {
                System.out.println("input the card number to place it");
            }
            System.out.println("use !board [player name] to view another player's board");
        }
    }

    /**
     * Allows to provide the user inputted data that are required to place a card.
     *
     * @param tokens user inputted tokens
     */
    @Override
    public void processInput(String[] tokens) {
        if(tokens == null || tokens.length == 0) return;

        if(placing) {
            switch (providedInput) {
                default:
                    break;
                case 0:

                    break;
                case 1:
                    try {
                        side = Integer.parseInt(tokens[0]);
                    } catch (NumberFormatException e) {
                        throw new InvalidParameterException(e);
                    }

                    if (side != 0 && side != 1) {
                        throw new InvalidParameterException("invalid side");
                    }
                    providedInput++;
                    break;
                case 2:
                    try {
                        x = Integer.parseInt(tokens[0]);
                    } catch (NumberFormatException e) {
                        throw new InvalidParameterException("Invalid x");
                    }
                    providedInput++;
                    break;
                case 3:
                    try {
                        y = Integer.parseInt(tokens[0]);
                    } catch (NumberFormatException e) {
                        throw new InvalidParameterException("Invalid y");
                    }
                    providedInput++;
                    break;
            }
        }
        else {
            switch (tokens[0]) {
                case "u":
                    this.yOffset++;
                    break;
                case "d":
                    this.yOffset--;
                    break;
                case "l":
                    this.xOffset--;
                    break;
                case "r":
                    this.xOffset++;
                    break;
                default:
                    try {
                        card = Integer.parseInt(tokens[0]);
                    } catch (NumberFormatException e) {
                        throw new InvalidParameterException("invalid input");
                    }

                    if (player.getPlayerCards().length <= card || card < 0) {
                        throw new InvalidParameterException("invalid input");
                    }
                    providedInput = 1;
                    placing = true;
                    break;
            }
        }

        if(providedInput == 4) {
            Client.getInstance().getServerHandler().sendMessage(new PlaceCardMessage(card, side == 1, new CardLocation(x,y)));
            placing = false;
        }
    }
}
