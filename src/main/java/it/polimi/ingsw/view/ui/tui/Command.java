package it.polimi.ingsw.view.ui.tui;

public interface Command {
    void execute(TextualUserInterface userInterface, String[] tokens);
}
