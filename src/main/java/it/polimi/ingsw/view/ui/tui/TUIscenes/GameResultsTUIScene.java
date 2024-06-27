package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.tui.Printer;

import java.util.List;

/**
 * GameResultsTUIScene handles the interface that allows the TUI user to
 * view the game scoreboard/results
 */
public class GameResultsTUIScene extends TUIScene{
    /**
     * Display the game scoreboard/results
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    @Override
    public void render(String statusMessage) {
        Printer.printScoreBoard(Client.getInstance().getView().getGameModel().getScoreBoard(), Client.getInstance().getView().getPlayersList());
        System.out.println("\n\n");

        if(Client.getInstance().getView().getGameStatus() == GameStatus.END) {
            List<Player> winningPlayers = Client.getInstance().getView().getGameModel().getScoreBoard().getWinners();

            if(winningPlayers != null && !winningPlayers.isEmpty()) {
                System.out.println("Winners: ");
                StringBuilder winnerLabelText = new StringBuilder(winningPlayers.getFirst().nickname);
                for(int i = 1; i < winningPlayers.size(); i++) {
                    winnerLabelText.append("& ").append(winningPlayers.get(i).nickname);
                }

                System.out.println(winnerLabelText);

            }

        }
    }

    /**
     * GameResultsTUIScene doesn't require any input.
     * This method does nothing.
     *
     * @param tokens user inputted tokens
     */
    @Override
    public void processInput(String[] tokens) {}
}
