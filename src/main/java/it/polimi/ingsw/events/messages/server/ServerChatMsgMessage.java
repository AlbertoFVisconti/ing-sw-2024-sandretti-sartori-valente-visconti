package it.polimi.ingsw.events.messages.server;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.model.chat.ChatMessage;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;

public class ServerChatMsgMessage extends ServerMessage{
    private final ChatMessage chatMessage;
    private final boolean isPrivate;

    public ServerChatMsgMessage(String addresseeIdentifier, ChatMessage chatMessage) {
        super(( addresseeIdentifier == null ? MessageType.MODEL_UPDATE_MESSAGE : MessageType.PRIVATE_MODEL_UPDATE_MESSAGE), addresseeIdentifier);
        this.chatMessage = chatMessage;

        this.isPrivate = addresseeIdentifier != null;
    }

    public ServerChatMsgMessage(ChatMessage chatMessage) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.chatMessage = chatMessage;

        this.isPrivate = false;
    }

    public ServerChatMsgMessage(ChatMessage chatMessage, boolean isPrivate) {
        super(MessageType.MODEL_UPDATE_MESSAGE);
        this.chatMessage = chatMessage;

        this.isPrivate = isPrivate;
    }

    @Override
    public void updateView(VirtualView view) {
        try {
            view.receiveMessage(chatMessage, this.isPrivate);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
