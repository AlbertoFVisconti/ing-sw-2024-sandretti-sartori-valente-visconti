package it.polimi.ingsw.model.events.messages.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.model.events.messages.MessageType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientHandler;

public class JoinGameMessage extends ClientMessage {
    private final int gameID;
    private final boolean creatingGame;
    private final int expectedPlayers;
    private final String nickname;
    private final PlayerColor color;
    private ClientHandler clientHandler;
    public JoinGameMessage(int gameID, boolean creatingGame, int expectedPlayers, String nickname, PlayerColor color, ClientHandler clientHandler) {
        super(MessageType.CONNECT_JOIN_MESSAGE, "");
        this.gameID = gameID;
        this.creatingGame = creatingGame;
        this.expectedPlayers = expectedPlayers;
        this.nickname = nickname;
        this.color = color;
        this.clientHandler = clientHandler;
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

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
            selector.addPlayer(game, nickname, color, clientHandler);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
