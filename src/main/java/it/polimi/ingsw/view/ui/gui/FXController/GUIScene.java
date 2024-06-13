package it.polimi.ingsw.view.ui.gui.FXController;

import javafx.scene.layout.AnchorPane;

public abstract class GUIScene {
    protected abstract AnchorPane getChatContainer();

    public abstract void update();
    public abstract void reportError(RuntimeException exception);

    public final void addChat(AnchorPane chat) {
        if(this.getChatContainer() == null) return;

        this.getChatContainer().getChildren().add(chat);
        AnchorPane.setTopAnchor(chat, 0.0);
        AnchorPane.setBottomAnchor(chat, 0.0);
        AnchorPane.setLeftAnchor(chat, 0.0);
        AnchorPane.setRightAnchor(chat, 0.0);
    }

}
