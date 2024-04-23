package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.events.messages.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private final List<Observer> subscribers = new ArrayList<>();

    public void subscribe(Observer subscriber) {
        this.subscribers.add(subscriber);
    }

    public void unsubscribe(Observer subscriber) {
        this.subscribers.remove(subscriber);
    }

    protected void notifyObservers(Message updateMessage) {
        for(Observer observer : subscribers) {
            observer.onUpdate(updateMessage);
        }
    }
}