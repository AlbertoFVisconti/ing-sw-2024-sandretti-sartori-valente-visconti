package it.polimi.ingsw.view.ui.tui.TUIscenes;

import it.polimi.ingsw.events.messages.client.ClientChatMsgMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.tui.Printer;

public class ChatTUIScene implements TUIScene{

    private Player player;

    public ChatTUIScene(Player player) {
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void render(String statusMessage) {
        Printer.printChat(Client.getInstance().getView().getGameModel().getChat(), Client.getInstance().getView().getLocalPlayer(), player);

        System.out.println("\n\n" + statusMessage + "\n\n");
    }

    @Override
    public void processInput(String[] tokens) {
        StringBuilder msgText = new StringBuilder();
        for (int h = 0; h < tokens.length; h++) {
            msgText.append(tokens[h]);
            if (h < tokens.length - 1) msgText.append(' ');
        }

        if(this.player == null) Client.getInstance().getServerHandler().sendMessage(new ClientChatMsgMessage(msgText.toString()));
        else Client.getInstance().getServerHandler().sendMessage(new ClientChatMsgMessage(msgText.toString(), player.nickName));
    }
}
