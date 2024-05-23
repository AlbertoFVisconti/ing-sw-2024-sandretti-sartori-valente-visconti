package it.polimi.ingsw.network.serverhandlers;

import it.polimi.ingsw.events.messages.client.ClientMessage;

public abstract class ServerHandler {
    private String playerIdentifier;

    public final void setPlayerIdentifier(String playerIdentifier) {
        this.playerIdentifier = playerIdentifier;
    }

    public final String getPlayerIdentifier() {
        return this.playerIdentifier;
    }

    public final void sendMessage(ClientMessage message) {
        message.setPlayerIdentifier(this.playerIdentifier);
        this.forwardMessage(message);
    }

    protected abstract void forwardMessage(ClientMessage message);

    public abstract void connect() throws Exception;

}
