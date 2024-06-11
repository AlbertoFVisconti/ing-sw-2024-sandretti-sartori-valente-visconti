package it.polimi.ingsw.network.serverhandlers;

import it.polimi.ingsw.events.messages.client.ClientMessage;

/**
 * Abstract object that Handle the client side connection to the server.
 * Subclasses can define different protocol and modalities for communication and server handling
 */
public abstract class ServerHandler {
    // Identifier of the local player, encapsulated into outgoing messages.
    private String playerIdentifier;

    /**
     * Sets the identifier of the local player.
     *
     * @param playerIdentifier local player's identifier
     */
    public final void setPlayerIdentifier(String playerIdentifier) {
        this.playerIdentifier = playerIdentifier;
    }

    /**
     * Retrieves the identifier of the local player.
     *
     * @return local player's identifier
     */
    public final String getPlayerIdentifier() {
        return this.playerIdentifier;
    }

    /**
     * Sends a message to the server.
     *
     * @param message ClientMessage that needs to be sent
     */
    public final void sendMessage(ClientMessage message) {
        message.setPlayerIdentifier(this.playerIdentifier);
        this.forwardMessage(message);
    }

    /**
     * Forwards a ClientMessage that needs to be sent to the subclass that implements the
     * protocol.
     *
     * @param message ClientMessage that needs to be sent
     */
    protected abstract void forwardMessage(ClientMessage message);

    /**
     * Tries to connect to the server.
     *
     * @throws Exception in case of error with the connection.
     */
    public abstract void connect() throws Exception;

}
