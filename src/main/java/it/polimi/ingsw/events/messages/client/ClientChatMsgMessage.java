package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

/**
 * Message that the client can send to the server
 * in order to send a chat message.
 */
public class ClientChatMsgMessage extends ClientMessage {
    private final String message;
    private final String addressee;

    /**
     * Builds a ClientChatMsgMessage with a specific addressee
     *
     * @param message   the message that needs to be sent
     * @param addressee the nickname of the player who needs to receive this message
     */
    public ClientChatMsgMessage(String message, String addressee) {
        super(MessageType.CHAT_MESSAGE);
        this.message = message;
        this.addressee = addressee;
    }

    /**
     * Builds a ClientChatMsgMessage without a specific addressee (public message)
     *
     * @param message the message that needs to be sent
     */
    public ClientChatMsgMessage(String message) {
        super(MessageType.CHAT_MESSAGE);
        this.message = message;
        this.addressee = null;
    }

    /**
     * Execute the message by calling the matching method on the controller
     *
     * @param mainController   the MainController instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing.
     */
    @Override
    public void execute(VirtualMainController mainController, VirtualController controller) {
        if (controller == null) throw new RuntimeException("Client game matching failed");

        try {
            controller.sendChatMsg(this.getPlayerIdentifier(), this.message, this.addressee);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
