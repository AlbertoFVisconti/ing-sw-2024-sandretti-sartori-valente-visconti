package it.polimi.ingsw.view.ui.tui;

/**
 * Command allows to define a method that takes a TextualUserInterface and an array of tokens as parameters.
 * Defining a Command allows to specify how to process user input.
 */
public interface Command {
    /**
     * Allows to execute the command whose parameter are given as tokens
     * and to present the result through the given TextualUserInterface.
     *
     * @param userInterface TextualUserInterface that can be used to present execution result to the user
     * @param tokens Array of string representing the user input
     */
    void execute(TextualUserInterface userInterface, String[] tokens);
}
