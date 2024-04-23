package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.events.messages.Message;

public interface Observer {
    void onUpdate(Message message);
}
