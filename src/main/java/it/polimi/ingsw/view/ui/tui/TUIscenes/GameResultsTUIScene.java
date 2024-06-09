package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.tui.Printer;

public class GameResultsTUIScene implements TUIScene{
    @Override
    public void render(String statusMessage) {
        Printer.printScoreBoard(Client.getInstance().getView().getGameModel().getScoreBoard(), Client.getInstance().getView().getPlayersList());
        System.out.println("\n\n");
    }

    @Override
    public void processInput(String[] tokens) {

    }
}
