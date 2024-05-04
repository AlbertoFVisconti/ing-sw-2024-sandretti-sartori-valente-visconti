package it.polimi.ingsw.network.cliendhandlers;

import it.polimi.ingsw.events.Observer;
import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.network.rmi.GameControllerWrapper;

/**
 * Generic client handler object.
 * A ClientHandler is a kind of Observer that sends the updates generated by the observed object
 * to the client.
 */
public abstract class ClientHandler implements Observer {
    private String playerIdentifier;

    public String getPlayerIdentifier() {
        return playerIdentifier;
    }

    public void setPlayerIdentifier(String playerIdentifier) {
        this.playerIdentifier = playerIdentifier;
    }

    /**
     * Triggered when an observed object notify its subscribers.
     * Can also be used to send messages to the client.
     *
     * @param message the message that needs to be delivered to the client.
     */
    @Override
    public void onUpdate(ServerMessage message) {
        this.sendMessage(message);
    }

    public abstract void sendMessage(ServerMessage message);

    /**
     * Allows to link the client handler to the game the client is playing.
     *
     * @param gameControllerWrapper the GameControllerWrapper object that contains a reference to the game the client is playing.
     */
    public abstract void setController(GameControllerWrapper gameControllerWrapper);


}
