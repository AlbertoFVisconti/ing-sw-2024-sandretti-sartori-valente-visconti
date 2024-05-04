package it.polimi.ingsw.events;

import it.polimi.ingsw.events.messages.server.ServerMessage;

/**
 * Generic representation for an object that can subscribe to Observable objects.
 */
public interface Observer {
    /**
     * Triggered when an update occurs in one of the observed observable.
     *
     * @param message ServerMessage that contains information about the update.
     */
    void onUpdate(ServerMessage message);
}
