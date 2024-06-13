package it.polimi.ingsw.view.ui.gui.FXController;

import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

public abstract class GUIScene {
    protected abstract AnchorPane getChatContainer();

    public abstract void update();
    final public void reportError(RuntimeException exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(exception.getMessage());
        alert.showAndWait();
    }

    public final void addChat(AnchorPane chat) {
        if(this.getChatContainer() == null) return;

        this.getChatContainer().getChildren().add(chat);
        AnchorPane.setTopAnchor(chat, 0.0);
        AnchorPane.setBottomAnchor(chat, 0.0);
        AnchorPane.setLeftAnchor(chat, 0.0);
        AnchorPane.setRightAnchor(chat, 0.0);
    }

}
