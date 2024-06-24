package it.polimi.ingsw.view.ui.tui.TUIscenes;

public abstract class TUIScene {
    protected int providedInput = 0;
    public void reset() {
        providedInput = 0;
    }
    public abstract void render(String statusMessage);

    public abstract void processInput(String[] tokens);
}
