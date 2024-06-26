package it.polimi.ingsw.view.ui.tui.TUIscenes;

/**
 * MainScreenTUIScene handles the interface that the TUI user is presented to upon
 * connecting to the server
 */
public class MainScreenTUIScene extends TUIScene {
    /**
     * Renders the main scene of the game
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    @Override
    public void render(String statusMessage) {
        System.out.println("""
                                ____          _             _   _       _                   _ _    \s
                              / ___|___   __| | _____  __ | \\ | | __ _| |_ _   _ _ __ __ _| (_)___\s
                             | |   / _ \\ / _` |/ _ \\ \\/ / |  \\| |/ _` | __| | | | '__/ _` | | / __|
                             | |__| (_) | (_| |  __/>  <  | |\\  | (_| | |_| |_| | | | (_| | | \\__ \\
                              \\____\\___/ \\__,_|\\___/_/\\_\\ |_| \\_|\\__,_|\\__|\\__,_|_|  \\__,_|_|_|___/       \s
                """);

        System.out.println(System.lineSeparator().repeat(4));

        System.out.println("""
                Use:
                \t -!join          to join one of the available games
                \t -!create        to create a new game""");
    }

    /**
     * MainScreenTUIScene doesn't need any data to be inputted.
     * This method does nothing.
     *
     * @param tokens user inputted tokens
     */
    @Override
    public void processInput(String[] tokens) {}
}
