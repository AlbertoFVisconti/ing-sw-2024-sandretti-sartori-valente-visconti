package it.polimi.ingsw.model.chat;
import it.polimi.ingsw.model.events.messages.ChatMessage;
import it.polimi.ingsw.model.player.Player;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;



/**
 * The Chat class allows users to communicate with each other through an in-game
 * text chat.
 */
public class Chat {
    // HashMap to store messages between pairs of players
    private final HashMap<HashSet<Player>, List<ChatMessage>> messages;

    /**
     * Constructs a Chat object with the given list of players.
     *
     * @param players the list of players in the game
     */
    public Chat(List<Player> players) {
        messages = new HashMap<>();

        // Initialize the messages map with all possible pairs of players
        for (Player p : players) {
            for (Player p1 : players) {
                if (!messages.containsKey(new HashSet<>(Arrays.asList(p, p1)))) {
                    messages.put(new HashSet<>(Arrays.asList(p, p1)), new ArrayList<>());
                }
            }
        }
    }

    /**
     * Retrieves the list of messages between two players.
     *
     * @param player  the first player
     * @param player1 the second player
     * @return the list of messages between the two players
     * @throws NoSuchElementException if the pair of players is not found in the
     *                                messages map
     */
    public List<ChatMessage> getMessagesChat(Player player, Player player1) {
        Set<Player> playersSet = new HashSet<>(Arrays.asList(player, player1));
        if (!messages.containsKey(playersSet))
            throw new NoSuchElementException("This playersSet is not in messages");
        return messages.get(playersSet);
    }

    /**
     * Sends a message from one player to another.
     *
     * @param player_send  the player sending the message
     * @param player_rec   the player receiving the message
     * @param message_text the text of the message
     * @throws NoSuchElementException if the pair of players is not found in the
     *                                messages map
     */
    public void sendMessage(Player player_send, Player player_rec, String message_text) {
        Set<Player> playersSet = new HashSet<>(Arrays.asList(player_send, player_rec));
        if (!messages.containsKey(playersSet))
            throw new NoSuchElementException("This playersSet is not in messages");
        ChatMessage message = new ChatMessage(player_send.getNickname(), message_text);
        messages.get(playersSet).add(message);
    }

    /**
     * Deletes a message from the chat.
     *
     * @param message the message to be deleted
     */
    public void deleteMessage(ChatMessage message) {
        // Implementation for deleting a message can be added here
    }
}
