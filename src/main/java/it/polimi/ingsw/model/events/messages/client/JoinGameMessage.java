package it.polimi.ingsw.model.events.messages.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientHandler;

/**
 * Message that the client sends when it's trying to connect to an existing game or to create a new one.
 */
public class JoinGameMessage extends ClientMessage {
    private final int gameID;
    private final boolean creatingGame;
    private final int expectedPlayers;
    private final String nickname;
    private final PlayerColor color;


    /**
     * Builds a JoinGameMessage that requests to join an existing game or to create a new one.
     * This message requires a ClientHandler to be effective, because that needs to be linked to be player and to the game,
     * in order to forward updates and accept messages from the client.
     *
     * @param gameID the identifier of the game the client's trying to join (if needed)
     * @param creatingGame {@code true} if the client wants to create a new game, {@code false} otherwise
     * @param expectedPlayers if the client's creating a new game, this value represents the number of clients that will play the game
     * @param nickname the nickname of the client that is trying to join
     * @param color the PlayerColor of the client that is trying to join
     * @param sender the CliendHandler of the player that is trying to join
     */
    public JoinGameMessage(int gameID, boolean creatingGame, int expectedPlayers, String nickname, PlayerColor color, ClientHandler sender) {
        super(MessageType.CONNECT_JOIN_MESSAGE, "");
        this.gameID = gameID;
        this.creatingGame = creatingGame;
        this.expectedPlayers = expectedPlayers;
        this.nickname = nickname;
        this.color = color;

        this.setSender(sender);
    }

    /**
     * Tries to add the player to the game (or to create a new game)
     *
     * @param selector the GameSelector instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing. Which should be null, because the client isn't playing yet.
     */
    @Override
    public void execute(GameSelector selector, GameController controller) {

        int game = gameID;
        if(creatingGame) {
            try {
                game = selector.CreateGame(expectedPlayers);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        try {
            selector.addPlayer(game, nickname, color, this.getSender());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
