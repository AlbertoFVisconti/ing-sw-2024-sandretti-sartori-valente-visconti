package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.PlaceCardMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.ui.tui.Printer;

import java.security.InvalidParameterException;

public class PlayerBoardTUIScene implements TUIScene{

    private Player player;

    int card;
    int side;
    int x;
    int y;
    int providedInput = 0;

    public PlayerBoardTUIScene(Player player) {
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void render(String statusMessage) {
        Printer.printBoard(player.getBoard());
        System.out.println("\n\nYour Cards:");
        Printer.printHand(player.getPlayerCards());
        System.out.println("\n\n" + statusMessage + "\n\n");


        System.out.print("Select a card to place: ");
        if(providedInput == 0) return;
        System.out.print(card+"\n");
        System.out.print("Select side (0 -> front, 1 -> back): ");
        if(providedInput == 1) return;
        System.out.print(side+"\n");
        System.out.print("Placing location:\n");
        System.out.print("\tX: ");
        if(providedInput == 2) return;
        System.out.print(x+"\n");
        System.out.print("\tY: ");
        if(providedInput == 3) return;
        System.out.print(y + "\n\n");
    }

    @Override
    public void processInput(String[] tokens) {
        switch (providedInput) {
            default:
                break;
            case 0:
                try {
                    card = Integer.parseInt(tokens[0]);
                } catch (NumberFormatException e) {
                    throw new InvalidParameterException(e);
                }

                if (player.getPlayerCards().length <= card || card < 0) {
                    throw new InvalidParameterException("invalid card");
                }
                providedInput++;
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

        if(providedInput == 4) {
            Client.getInstance().getServerHandler().sendMessage(new PlaceCardMessage(card, side == 1, new CardLocation(x,y)));
        }
    }
}
