package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.events.messages.updates.ModelUpdateMessage;

public interface Observer {
    void onUpdate(ModelUpdateMessage updateMessage);
}
