package it.polimi.ingsw.network.serverhandlers;

import it.polimi.ingsw.events.messages.client.ClientMessage;

public abstract class ServerHandler  {
    private String playerIdentifier;

    public final void setPlayerIdentifier(String playerIdentifier) {
        this.playerIdentifier = playerIdentifier;
    }

    public final String getPlayerIdentifier() {
        return this.playerIdentifier;
    }

    public final void sendMessage(ClientMessage message) {
        message.setPlayerIdentifier(this.playerIdentifier);
        this.forwardMessage(message);
    }

    protected abstract void forwardMessage(ClientMessage message);

    public abstract void connect() throws Exception;


//    public abstract void joinGame(int IDGame, String nick);
//
//
//    public abstract void createGame(int expectedPlayers, String nick);
//
//    public abstract void getAvailableGames();
//
//
//    public abstract void placeCard(int index, boolean onBackSide, CardLocation location);
//    public abstract void drawCard(int index);
//
//    public abstract void placeStartCard(boolean onBackSide);
//
//    public abstract void selectPrivateGoal(int index);
//
//    public abstract void selectColor(PlayerColor color);

}
