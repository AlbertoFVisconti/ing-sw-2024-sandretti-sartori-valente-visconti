package it.polimi.ingsw.network.serverhandlers;

import it.polimi.ingsw.events.messages.client.ClientMessage;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.view.ViewWrapper;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerHandler extends ServerHandler {

    private VirtualController controller;
    private VirtualMainController mainController;

    private final String ip;
    private final int port;
    private final ViewWrapper viewWrapper;

    private final String mainControllerRemoteObjectName;

    public RMIServerHandler(ViewWrapper viewWrapper, String ip, int port, String mainControllerRemoteObjectName) {
        this.viewWrapper = viewWrapper;
        this.ip = ip;
        this.port = port;

        this.mainControllerRemoteObjectName = mainControllerRemoteObjectName;
    }

    public void setController(VirtualController controller) {
        this.controller = controller;
    }


    @Override
    protected void forwardMessage(ClientMessage message) {
        message.execute(mainController, controller);
    }

    @Override
    public void connect() throws Exception {
        Registry registry = LocateRegistry.getRegistry(ip, port);

        mainController = (VirtualMainController) registry.lookup(this.mainControllerRemoteObjectName);

        mainController.connect(viewWrapper);
    }
}
