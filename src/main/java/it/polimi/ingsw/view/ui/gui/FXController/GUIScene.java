package it.polimi.ingsw.view.ui.gui.FXController;

public interface GUIScene {
    void setup();
    void update();
    void reportError(RuntimeException exception);

}
