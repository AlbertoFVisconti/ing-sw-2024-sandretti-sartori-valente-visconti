package it.polimi.ingsw.events;

import it.polimi.ingsw.events.messages.server.ServerMessage;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract representation of an object that can be observer by various
 * observers. The Observable can notify subscribers with a ServerMessage
 * that contains information about the update.
 */
public abstract class Observable {
    private final List<Observer> subscribers = new ArrayList<>();

    /**
     * Allows to add an Observer to the subscribers list.
     *
     * @param subscriber the new Observer that wants to observe the object
     */
    public void subscribe(Observer subscriber) {
        this.subscribers.add(subscriber);
    }

    /**
     * Allows to remove an Observer from the subscribers list.
     *
     * @param subscriber the Observer that wants to unsubscribe from the object
     */
    public void unsubscribe(Observer subscriber) {
        this.subscribers.remove(subscriber);
    }

    /**
     * Allows the object to notify its subscribers that an update occurred.
     *
     * @param updateMessage a ServerMessage that contains information regarding the update.
     */
    protected void notifyObservers(ServerMessage updateMessage) {
        for(Observer observer : subscribers) {
            observer.onUpdate(updateMessage);
        }
    }
}