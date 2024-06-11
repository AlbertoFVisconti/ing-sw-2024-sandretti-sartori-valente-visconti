package it.polimi.ingsw.network.serverhandlers;

import it.polimi.ingsw.events.messages.client.ClientMessage;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.view.ViewWrapper;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Handle the client-side RMI (Remote Method Invocation) connection to the server
 */
public class RMIServerHandler extends ServerHandler {
    // the GameController's remote object
    private VirtualController controller;
    // the MainController's remote object
    private VirtualMainController mainController;

    // Server's ip
    private final String ip;
    // Server's port
    private final int port;
    // local view wrapper (remote object)
    private final ViewWrapper viewWrapper;

    // name on the RMI registry of the MainController remote object
    private final String mainControllerRemoteObjectName;

    /**
     * Builds an RMIServerHandler with the specified parameters.
     *
     * @param viewWrapper the local view's wrapper
     * @param ip the IP address of the server
     * @param port the IP address of the RMI server
     * @param mainControllerRemoteObjectName the name fo the MainController's remote object
     */
    public RMIServerHandler(ViewWrapper viewWrapper, String ip, int port, String mainControllerRemoteObjectName) {
        this.viewWrapper = viewWrapper;
        this.ip = ip;
        this.port = port;

        this.mainControllerRemoteObjectName = mainControllerRemoteObjectName;
    }

    /**
     * Sets the GameController remote object used by the server handler to send message
     * when the game is running.
     *
     * @param controller GameController remote object
     */
    public void setController(VirtualController controller) {
        this.controller = controller;
    }


    /**
     * Sends a message to the server through RMI.
     *
     * @param message ClientMessage that needs to be sent
     */
    @Override
    protected void forwardMessage(ClientMessage message) {
        message.execute(mainController, controller);
    }

    /**
     * Connects to the clients.
     * <p>
     * Retrieves the MainController remote object from the server's RMI registry
     * and calls the method that allows to establish a connection.
     *
     * @throws Exception if an error occurs during the connection
     */
    @Override
    public void connect() throws Exception {
        Registry registry = LocateRegistry.getRegistry(ip, port);

        mainController = (VirtualMainController) registry.lookup(this.mainControllerRemoteObjectName);

        mainController.connect(viewWrapper);
    }
}
