package it.polimi.ingsw.events.messages.client;

import it.polimi.ingsw.events.messages.MessageType;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;

import java.rmi.RemoteException;

/**
 * Message that the client sends when it's trying to connect to an existing game or to create a new one.
 */
public class JoinGameMessage extends ClientMessage {
    private final String gameID;
    private final boolean creatingGame;
    private final int expectedPlayers;
    private final String nickname;


    /**
     * Builds a JoinGameMessage that requests to join an existing game or to create a new one.
     * This message requires a ClientHandler to be effective, because that needs to be linked to be player and to the game,
     * in order to forward updates and accept messages from the client.
     *
     * @param gameID          the identifier of the game the client's trying to join (if needed)
     * @param creatingGame    {@code true} if the client wants to create a new game, {@code false} otherwise
     * @param expectedPlayers if the client's creating a new game, this value represents the number of clients that will play the game
     * @param nickname        the nickname of the client that is trying to join
     */
    public JoinGameMessage(String gameID, boolean creatingGame, int expectedPlayers, String nickname) {
        super(MessageType.CONNECT_JOIN_MESSAGE);
        this.gameID = gameID;
        this.creatingGame = creatingGame;
        this.expectedPlayers = expectedPlayers;
        this.nickname = nickname;
    }

    /**
     * Tries to add the player to the game (or to create a new game)
     *
     * @param mainController   the MainController instance that handles the game the player's playing.
     * @param controller the GameController that handles the game the player's playing. Which should be null, because the client isn't playing yet.
     */
    @Override
    public void execute(VirtualMainController mainController, VirtualController controller) {
        if (this.creatingGame) {
            try {
                mainController.createGame(this.getPlayerIdentifier(), expectedPlayers, nickname);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                mainController.joinGame(this.getPlayerIdentifier(), gameID, nickname);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
