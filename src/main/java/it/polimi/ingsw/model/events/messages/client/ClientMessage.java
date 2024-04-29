package it.polimi.ingsw.model.events.messages.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.model.events.messages.Message;
import it.polimi.ingsw.model.events.messages.MessageType;

public abstract class ClientMessage extends Message {
    public final String playerIdentifier;
    public ClientMessage(MessageType messageType, String playerIdentifier) {
        super(messageType);
        this.playerIdentifier = playerIdentifier;
    }

    public abstract void execute(GameSelector selector, GameController controller);
}