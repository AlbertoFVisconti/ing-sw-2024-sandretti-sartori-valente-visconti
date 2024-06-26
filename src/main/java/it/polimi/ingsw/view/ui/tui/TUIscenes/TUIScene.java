package it.polimi.ingsw.view.ui.tui.TUIscenes;

/**
 * Abstract representation of an interface that can be used to present
 * information to the TUI user.
 */
public abstract class TUIScene {
    // allows to monitor how many input were provided (among the needed ones)
    protected int providedInput = 0;

    /**
     * resets the TUI scene's recorded input
     */
    public void reset() {
        providedInput = 0;
    }

    /**
     * Allows to display the scene's data.
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    public abstract void render(String statusMessage);

    /**
     * Allows to forward user input to the TUI scene-
     *
     * @param tokens user inputted tokens
     */
    public abstract void processInput(String[] tokens);
}
