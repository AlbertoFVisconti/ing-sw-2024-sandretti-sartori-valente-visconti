package it.polimi.ingsw.view.ui.gui.FXController;

import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

/**
 * Abstract representation for a GUI interface scene.
 * Allows to report errors through a pop-up.
 * Allows to display chat on interfaces that supports it-
 */
public abstract class GUIScene {
    /**
     * Allows GUIScene extenders to provide a chat container AnchorPane,
     * when the chat is supported.
     *
     * @return AnchorPane that will contain the chat, {@code null} if the GUI scene doesn't support chat
     */
    protected abstract AnchorPane getChatContainer();

    /**
     * Allows to request GUIScene extenders to update the displayed content
     */
    public abstract void update();

    /**
     * Allows to display and error to the user through a pop-up
     *
     * @param exception RuntimeException that contains the error data
     */
    final public void reportError(RuntimeException exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(exception.getMessage());
        alert.showAndWait();
    }

    /**
     * allows to insert the chat in the GUIScene (if the GUIScene supports chat)
     *
     * @param chat AnchorPane that contains the chat
     */
    public final void addChat(AnchorPane chat) {
        if(this.getChatContainer() == null) return;

        this.getChatContainer().getChildren().add(chat);
        AnchorPane.setTopAnchor(chat, 0.0);
        AnchorPane.setBottomAnchor(chat, 0.0);
        AnchorPane.setLeftAnchor(chat, 0.0);
        AnchorPane.setRightAnchor(chat, 0.0);
    }

}
