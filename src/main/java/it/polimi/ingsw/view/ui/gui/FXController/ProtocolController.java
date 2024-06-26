package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.serverhandlers.RMIServerHandler;
import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.network.serverhandlers.SocketServerHandler;
import it.polimi.ingsw.view.ViewWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.rmi.RemoteException;

/**
 * ProtocolController handles the interface that the GUI user uses to connect to the server.
 * It is the first scene that the GUI user sees upon launching the application.
 */
public class ProtocolController extends GUIScene {
    // TextField where the user can input the server's IP address
    @FXML
    public TextField ipField;

    // TextField where the user can input the server's port number
    @FXML
    public TextField portField;

    /**
     * When the interface is loaded, sets up the text property of the port field.
     * It makes sure that the user cannot input anything but numbers in the field.
     */
    @FXML
    public void initialize() {
        portField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                portField.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    /**
     * Used to retrieve the chat container from the controller.
     * Join game interface doesn't support chat, thus this method simply returns null
     *
     * @return {@code null}
     */
    @Override
    protected AnchorPane getChatContainer() {return null;}

    /**
     * Used to update the interface when the server provides data.
     * Protocol/connection interface doesn't support any dynamic element, thus this method does nothing
     */
    @Override
    public void update() {}

    /**
     * Triggered when the user clicks on the "connect with socket" button.
     * It tries to create a ServerHandles that the Client will use to connect
     * to the server. If connection fails, the user will be notified, and they
     * will be allowed to try again.
     */
    public void connectSocket() {
        int port;
        try {
            // trying to extract the port number
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            // failed to parse the portField
            this.reportError(new RuntimeException("Invalid port number"));
            return;
        }

        // creating the server handler
        Client.getInstance().setServerHandler(
                new SocketServerHandler(
                        ipField.getText(),
                        port
                )
        );
    }

    /**
     * Triggered when the user clicks on the "connect with RMI" button.
     * It tries to create a ServerHandles that the Client will use to connect
     * to the server. If connection fails, the user will be notified, and they
     * will be allowed to try again.
     */
    public void connectRMI() {
        int port;
        try {
            // trying to extract the port number
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            // failed to parse the portField
            this.reportError(new RuntimeException("Invalid port number"));
            return;
        }

        ServerHandler serverHandler;
        try {
            // trying creating the server handler
            serverHandler = new RMIServerHandler(
                    new ViewWrapper(Client.getInstance().getView()),
                    ipField.getText(),
                    port,
                    "MainController"
            );
        }
        catch (RemoteException e) {
            // server handles creation failed (ViewWrapper actually)
            reportError(new RuntimeException(e));
            return;
        }

        Client.getInstance().setServerHandler(serverHandler);
    }
}
