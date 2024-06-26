package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.ClientChatMsgMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.tui.Printer;

/**
 * ChatTUIScene handles the interface that allows the CLI user to read chat messages
 * and send messages.
 */
public class ChatTUIScene extends TUIScene{
    // the player whose chat with the local player is currently open (null for public chat)
    private Player player;

    /**
     * Builds a new ChatTUIScene
     *
     * @param player Player whose chat with the local player needs to be displayed, {@code null} to display the public chat
     */
    public ChatTUIScene(Player player) {
        this.player = player;
    }

    /**
     * Changes the displayed chat
     *
     * @param player Player whose chat with the local player needs to be displayed, {@code null} to display the public chat
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Allows to display the chat
     *
     * @param statusMessage String that contains a Message that the TUI wants the user to read
     */
    @Override
    public void render(String statusMessage) {
        Printer.printChat(Client.getInstance().getView().getGameModel().getChat(), Client.getInstance().getView().getLocalPlayer(), player);

        System.out.println("\n\n" + statusMessage + "\n\n");
    }

    /**
     * Allows to send message through the chat
     *
     * @param tokens user inputted tokens
     */
    @Override
    public void processInput(String[] tokens) {
        StringBuilder msgText = new StringBuilder();
        for (int h = 0; h < tokens.length; h++) {
            msgText.append(tokens[h]);
            if (h < tokens.length - 1) msgText.append(' ');
        }

        if(this.player == null) Client.getInstance().getServerHandler().sendMessage(new ClientChatMsgMessage(msgText.toString()));
        else Client.getInstance().getServerHandler().sendMessage(new ClientChatMsgMessage(msgText.toString(), player.nickname));
    }
}
