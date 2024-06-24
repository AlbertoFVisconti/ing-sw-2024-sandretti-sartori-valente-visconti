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

public class ProtocolController extends GUIScene {
    @FXML
    public TextField ipField;
    @FXML
    public TextField portField;

    @FXML
    public void initialize() {
        portField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                portField.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    @Override
    protected AnchorPane getChatContainer() {return null;}

    @Override
    public void update() {}

    public void connectSocket() {
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            this.reportError(new RuntimeException("Invalid port number"));
            return;
        }

        Client.getInstance().setServerHandler(
                new SocketServerHandler(
                        ipField.getText(),
                        port
                )
        );
    }

    public void connectRMI() {
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            this.reportError(new RuntimeException("Invalid port number"));
            return;
        }

        ServerHandler serverHandler;
        try {
            serverHandler = new RMIServerHandler(
                    new ViewWrapper(Client.getInstance().getView()),
                    ipField.getText(),
                    port,
                    "MainController"
            );
        }
        catch (RemoteException e) {
            reportError(new RuntimeException(e));
            return;
        }

        Client.getInstance().setServerHandler(serverHandler);
    }
}
