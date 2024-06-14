package it.polimi.ingsw.view.ui.gui.FXController;

import it.polimi.ingsw.events.messages.client.ClientChatMsgMessage;
import it.polimi.ingsw.model.chat.ChatMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Client;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ChatController extends AnchorPane {
    @FXML
    public ChoiceBox<String> selectChat;
    @FXML
    private VBox messageVBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField messageTextField;
    @FXML
    private Button sendButton;

    private String currentlyDisplayedChat = null;

    public void initialize() {
        messageVBox.heightProperty().addListener((observable, oldValue, newValue) -> scrollPane.setVvalue(1.0));

        sendButton.setOnAction(event -> handleSendMessage());
        selectChat.getSelectionModel().selectedItemProperty().addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> setCurrentlyDisplayedChat(newValue) );

        selectChat.getItems().add("");
    }

    @FXML
    private void handleSendMessage() {
        String message = messageTextField.getText();
        if (!message.isEmpty()) {
            Client.getInstance().getServerHandler().sendMessage(new ClientChatMsgMessage(message, currentlyDisplayedChat));
        }
    }

    public void updateChat() {
        List<Player> players = Client.getInstance().getView().getGameModel().getPlayers();
        List<String> currentNicks = new ArrayList<>( selectChat.getItems());

        if(Client.getInstance().getView().getLocalPlayer() == null) return;

        for (Player p : players) {
            if(Client.getInstance().getView().getLocalPlayerName().equals(p.nickName)) continue;
            if(!selectChat.getItems().contains(p.nickName)) selectChat.getItems().add(p.nickName);
        }

        for (String nick : currentNicks) {
            if(nick.isEmpty()) continue;
            boolean found = false;
            for(Player p : players) {
                if(p.nickName.equals(nick)) {
                    found = true;
                    break;
                }
            }

            if(!found) this.selectChat.getItems().remove(nick);
        }



        messageVBox.getChildren().clear();
        for(ChatMessage chatMessage : Client.getInstance().getView().getGameModel().getChat().getMessagesChat(Client.getInstance().getView().getLocalPlayerName(), currentlyDisplayedChat)) {
            Label newMessage = new Label(chatMessage.getSenderNick() + ": " + chatMessage.getText());
            messageVBox.getChildren().add(newMessage);
        }

        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }

    public void setCurrentlyDisplayedChat(String playerNickname) {
        if(playerNickname != null && playerNickname.isEmpty()) playerNickname = null;
        this.currentlyDisplayedChat = playerNickname;

        this.updateChat();
    }
}