package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.events.Observable;
import it.polimi.ingsw.events.messages.server.ServerChatMsgMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.cliendhandlers.ClientHandler;
import javafx.scene.paint.Color;

import java.util.*;


/**
 * The Chat class allows users to communicate with each other through an in-game
 * text chat.
 */
public class Chat extends Observable {
    // HashMap to store messages between pairs of players
    private final HashMap<HashSet<String>, List<ChatMessage>> messages;

    /**
     * Constructs a Chat object.
     */
    public Chat() {
        messages = new HashMap<>();
        messages.put(new HashSet<>(), new ArrayList<>());
    }

    /**
     * Retrieves the list of messages between two players.
     *
     * @param nick1 the first player's nickname
     * @param nick2 the second player's nickname
     * @return the list of messages between the two players (or the public chat, if either of the two players is null)
     * @throws NoSuchElementException if the pair of players is not found in the
     *                                messages map
     */
    public List<ChatMessage> getMessagesChat(String nick1, String nick2) {
        if (nick1 == null || nick2 == null)
            return Collections.unmodifiableList(this.messages.get(new HashSet<String>()));

        HashSet<String> playersSet = new HashSet<>(Arrays.asList(nick1, nick2));
        if (!messages.containsKey(playersSet))
            return new ArrayList<>();
        return messages.get(playersSet);
    }

    /**
     * Sends a message from one player to another.
     *
     * @param sender       the player sending the message
     * @param receiver     the player receiving the message
     * @param message_text the text of the message
     * @throws NoSuchElementException if the pair of players is not found in the
     *                                messages map
     */
    public void sendMessage(Player sender, Player receiver, String message_text) {
        this.sendMessage(sender, receiver, message_text, Color.BLACK);
    }

    /**
     * Sends a message from one player to another.
     *
     * @param sender       the player sending the message
     * @param receiver     the player receiving the message
     * @param message_text the text of the message
     * @param displayColor the color that needs to be used to display the message
     * @throws NoSuchElementException if the pair of players is not found in the
     *                                messages map
     */
    public void sendMessage(Player sender, Player receiver, String message_text, Color displayColor) {
        if (sender == null) throw new RuntimeException("No sender provided");
        HashSet<String> playersSet;

        if (receiver != null) {
            playersSet = new HashSet<>(Arrays.asList(sender.nickname, receiver.nickname));
        } else {
            playersSet = new HashSet<>();
        }

        if (!messages.containsKey(playersSet))
            this.messages.put(playersSet, new ArrayList<>());

        ChatMessage message = new ChatMessage(message_text, sender.nickname, (receiver != null ? receiver.nickname : null), displayColor);
        messages.get(playersSet).add(message);

        if (receiver == null) {
            // all players (sender included) will receive the update
            this.notifyObservers(new ServerChatMsgMessage(message));
        } else {
            ClientHandler receiverHandler = receiver.getClientHandler();
            if (receiverHandler != null) {
                this.notifyObservers(new ServerChatMsgMessage(receiverHandler.getPlayerIdentifier(), message));
            }

            // this second notifyObservers sends the private update to the message sender
            // that will cause the sender to see the message in the chat
            // that also means that if the player don't see the message means that
            // something went wrong otherwise, the player can assume that the other player received the message as well
            if(sender.getClientHandler() != null) this.notifyObservers(new ServerChatMsgMessage(sender.getClientHandler().getPlayerIdentifier(), message));
        }

    }

    /**
     * Client side method to append messages to the local chat.
     * Server shouldn't use this method, because it won't update the clients' views.
     * If either of the provided nicknames is null, the message will be considered public.
     *
     * @param message the ChatMessage that contains the message information
     * @param nick1   the nickname of the first player
     * @param nick2   the nickname of the second player
     */
    public void appendMessage(ChatMessage message, String nick1, String nick2) {
        HashSet<String> playersSet;
        if (nick1 == null || nick2 == null) playersSet = new HashSet<>();
        else playersSet = new HashSet<>(Arrays.asList(nick1, nick2));

        if (!messages.containsKey(playersSet))
            this.messages.put(playersSet, new ArrayList<>());

        messages.get(playersSet).add(message);
    }
}
