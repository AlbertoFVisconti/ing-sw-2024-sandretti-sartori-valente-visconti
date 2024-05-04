package it.polimi.ingsw.network.serverhandlers;

import it.polimi.ingsw.events.messages.server.ServerMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class ServerHandler extends Thread  {
    private final View clientView;

    private final BlockingQueue<ServerMessage> messageQueue;

    public ServerHandler(View clientView) {
        this.clientView = clientView;
        this.messageQueue = new ArrayBlockingQueue<>(100);
        this.start();
    }

    public View getClientView() {
        return clientView;
    }

    public void forwardMessage(ServerMessage message) {
        try {
            this.messageQueue.put(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public abstract void joinGame(int IDGame, PlayerColor color, String nick);


    public abstract void createGame(int expectedPlayers, PlayerColor color, String nick);

    public abstract void getAvailableGames();


    public abstract void placeCard(int index, boolean onBackSide, CardLocation location);
    public abstract void drawCard(int index);

    public abstract void placeStartCard(boolean onBackSide);

    public abstract void selectPrivateGoal(int index);

    @Override
    public void run() {
        ServerMessage message;

        while(true) {
            try {
                message = messageQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            message.updateView(clientView);

        }
    }
}
