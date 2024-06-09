package it.polimi.ingsw.view.ui.tui.TUIscenes;

public interface TUIScene {
    void render(String statusMessage);

    void processInput(String[] tokens);
}
