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

/**
 * ChatController class manages the chat component in the GUI.
 * It handles displaying messages, selecting what chat to display and sending messages.
 */
public class ChatController extends AnchorPane {
    // Choice box for selecting a chat
    @FXML
    public ChoiceBox<String> selectChat;

    //  Container for the messages
    @FXML
    private VBox messageVBox;

    // Scroll pane for scrolling through chat messages
    @FXML
    private ScrollPane scrollPane;

    // Text field for entering a message
    @FXML
    private TextField messageTextField;

    // Button for sending messages
    @FXML
    private Button sendButton;

    // the currently selected chat to be displayed
    private String currentlyDisplayedChat = null;

    /**
     * Initializes the controller.
     * Sets up listeners and the choice box.
     */
    public void initialize() {
        messageVBox.heightProperty().addListener((observable, oldValue, newValue) -> scrollPane.setVvalue(1.0));

        sendButton.setOnAction(event -> handleSendMessage());
        selectChat.getSelectionModel().selectedItemProperty().addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> setCurrentlyDisplayedChat(newValue) );

        selectChat.getItems().add("");
    }

    /**
     * Handles sending a message.
     * Sends the message to the server if the text field is not empty.
     * The input text field gets cleared once the message is sent.
     */
    @FXML
    private void handleSendMessage() {
        String message = messageTextField.getText();
        if (!message.isEmpty()) {
            Client.getInstance().getServerHandler().sendMessage(new ClientChatMsgMessage(message, currentlyDisplayedChat));
            messageTextField.clear();
        }
    }

    /**
     * Updates the chat with current messages and new available chats
     */
    public void updateChat() {
        // retrieves the list of players connected to the same game as the local player
        List<Player> players = Client.getInstance().getView().getGameModel().getPlayers();

        // retrieves the current list of available chats
        List<String> currentNicks = new ArrayList<>( selectChat.getItems());

        // checking that the local player has actually joined a game
        if(Client.getInstance().getView().getLocalPlayer() == null) return;


        for (Player p : players) {
            // parsing all the connected players and adding them (if necessary) to the choice box for available chats
            if(Client.getInstance().getView().getLocalPlayerName().equals(p.nickname)) continue;
            if(!selectChat.getItems().contains(p.nickname)) selectChat.getItems().add(p.nickname);
        }

        for (String nick : currentNicks) {
            // parsing the chat that were available before the update
            // and removing all the chats that aren't available anymore
            if(nick.isEmpty()) continue;
            boolean found = false;
            for(Player p : players) {
                if(p.nickname.equals(nick)) {
                    found = true;
                    break;
                }
            }

            if(!found) this.selectChat.getItems().remove(nick);
        }

        // removing messages from the chat
        messageVBox.getChildren().clear();

        // adding messages to the chat
        for(ChatMessage chatMessage : Client.getInstance().getView().getGameModel().getChat().getMessagesChat(Client.getInstance().getView().getLocalPlayerName(), currentlyDisplayedChat)) {
            Label newMessage = new Label(chatMessage.getSenderNick() + ": " + chatMessage.getText());
            messageVBox.getChildren().add(newMessage);
        }

        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }

    /**
     * Sets the currently displayed chat and updates the chat.
     *
     * @param playerNickname nickname of the player whose chat needs to be displayed, null or empty for the public chat.
     */
    public void setCurrentlyDisplayedChat(String playerNickname) {
        if(playerNickname != null && playerNickname.isEmpty()) playerNickname = null;
        this.currentlyDisplayedChat = playerNickname;

        this.updateChat();
    }
}