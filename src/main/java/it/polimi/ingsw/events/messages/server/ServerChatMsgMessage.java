package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.chat.ChatMessage;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

/**
 * Message that the server sends to the client in order to forward a chat message.
 */
public class ServerChatMsgMessage extends ServerMessage {
    private final ChatMessage chatMessage;
    private final boolean isPrivate;

    /**
     * Builds a ServerChatMsgMessage delivering a private message
     *
     * @param addresseeIdentifier the identifier of the player who needs to receive this message
     * @param chatMessage the ChatMessage object that contains the chat message data
     */
    public ServerChatMsgMessage(String addresseeIdentifier, ChatMessage chatMessage) {
        super((addresseeIdentifier == null ? MessageType.MODEL_UPDATE_MESSAGE : MessageType.PRIVATE_MODEL_UPDATE_MESSAGE), addresseeIdentifier);
        this.chatMessage = chatMessage;

        this.isPrivate = addresseeIdentifier != null;
    }

    /**
     * Builds a ServerChatMsgMessage delivering a public message
     *
     * @param chatMessage the ChatMessage object that contains the chat message data
     */
    public ServerChatMsgMessage(ChatMessage chatMessage) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.chatMessage = chatMessage;

        this.isPrivate = false;
    }

    /**
     * Builds a ServerChatMsgMessage without the need to specify an addressee.
     * This constructor is used internally by the client.
     *
     * @param chatMessage the ChatMessage object that contains the chat message data
     * @param isPrivate {@code true} if the message is private, {@code false} otherwise
     */
    public ServerChatMsgMessage(ChatMessage chatMessage, boolean isPrivate) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.chatMessage = chatMessage;

        this.isPrivate = isPrivate;
    }

    /**
     * Updates the client's View calling the method matching the message
     *
     * @param view the client's view that needs to be updated.
     */
    @Override
    public void updateView(VirtualView view) {
        try {
            view.sendChatMsg(chatMessage, this.isPrivate);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
