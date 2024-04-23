package it.polimi.ingsw.view;

import it.polimi.ingsw.model.events.Observer;
import it.polimi.ingsw.model.events.messages.Message;

import java.net.Socket;

public class ViewWrapperSocket implements Observer {
    private Socket socket;

    public ViewWrapperSocket(Socket socket) {
        this.socket = socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void onUpdate(Message message) {
        // TODO: send updateMessage through socket
    }
}
