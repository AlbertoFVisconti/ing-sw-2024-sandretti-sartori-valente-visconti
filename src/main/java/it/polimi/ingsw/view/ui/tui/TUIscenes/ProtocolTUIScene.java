package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.serverhandlers.RMIServerHandler;
import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.network.serverhandlers.SocketServerHandler;
import it.polimi.ingsw.view.ViewWrapper;

import java.rmi.RemoteException;
import java.security.InvalidParameterException;

public class ProtocolTUIScene extends TUIScene{
    int protocol;
    String ip;
    int port;
    @Override
    public void render(String statusMessage) {
        System.out.println("Please select the desired protocol:");
        System.out.println("\t - 1: socket");
        System.out.println("\t - 2: RMI");

        System.out.println("\n" + statusMessage + "\n");

        System.out.print("\nYour selection: ");
        if(providedInput == 0) return;
        System.out.print(protocol+"\nInsert server IP [127.0.0.1]: ");
        if(providedInput == 1) return;
        System.out.print(ip + "\nInsert server port: ");
        if(providedInput == 2) return;
        System.out.print(port);

    }

    @Override
    public void processInput(String[] tokens) {
        if(tokens == null || tokens.length == 0) return;

        switch (providedInput) {
            case 0:
                try {
                    protocol = Integer.parseInt(tokens[0]);
                } catch (NumberFormatException e) {
                    throw new InvalidParameterException(e);
                }
                if (protocol != 1 && protocol != 2)
                    throw new InvalidParameterException("You need to select one of the available protocols");

                providedInput++;
                break;
            case 1:
                if(tokens[0].isEmpty()) this.ip = "127.0.0.1";
                else this.ip = tokens[0];

                providedInput++;
                break;
            case 2:
                try {
                    port = Integer.parseInt(tokens[0]);
                } catch (NumberFormatException e) {
                    throw new InvalidParameterException(e);
                }

                providedInput++;
                break;
        }

        if(providedInput == 3) {
            ServerHandler serverHandler;
            if (protocol == 1) {
                serverHandler = new SocketServerHandler(ip, port);
            } else {
                try {
                    serverHandler = new RMIServerHandler(new ViewWrapper(Client.getInstance().getView()), ip, port, "MainController");
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            Client.getInstance().setServerHandler(serverHandler);
        }
    }
}
