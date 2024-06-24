package it.polimi.ingsw.view.ui.tui.TUIscenes;

public class MainScreenTUIScene extends TUIScene {
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

    @Override
    public void processInput(String[] tokens) {

    }
}
