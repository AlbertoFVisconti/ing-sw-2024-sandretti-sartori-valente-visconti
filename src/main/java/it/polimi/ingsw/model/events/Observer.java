package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.events.messages.server.ServerMessage;

public interface Observer {
    void onUpdate(ServerMessage message);
}
