package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.events.messages.server.*;
import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.chat.ChatMessage;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.saving.ClientGameSaving;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.serverhandlers.RMIServerHandler;
import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

/**
 * Client-side remote object that receive Remote Method Invocations from the Server
 * and "transform" them into messages that will be processed by the actual View (View) asynchronously.
 */
public class ViewWrapper extends UnicastRemoteObject implements VirtualView {
    View view;

    public View getUserInterface() {
        return view;
    }

    public ViewWrapper(View view) throws RemoteException {
        super();
        this.view = view;
    }

    @Override
    public void setController(VirtualController controller) throws RemoteException {
        ServerHandler serverHandler = Client.getInstance().getServerHandler();
        if (serverHandler instanceof RMIServerHandler)
            ((RMIServerHandler) serverHandler).setController(controller);
    }

    @Override
    public void setPlayerIdentifier(String playerIdentifier) throws RemoteException {
        view.forwardMessage(new ConnectionConfirmationMessage(playerIdentifier));
    }

    @Override
    public void setStartingCard(StartCard card) throws RemoteException {
        view.forwardMessage(new StartCardUpdateMessage(card));
    }

    @Override
    public void setPublicGoal(Goal[] goals) throws RemoteException {
        view.forwardMessage(new PublicGoalsUpdateMessage(goals));
    }

    @Override
    public void setAvailablePrivateGoals(Goal[] goals) throws RemoteException {
        view.forwardMessage(new PrivateGoalUpdateMessage(goals));
    }

    @Override
    public void setDefinitivePrivateGoal(Goal goal) throws RemoteException {
        view.forwardMessage(new PrivateGoalUpdateMessage(goal));
    }

    @Override
    public void setPlayersCard(String playerNickname, PlayCard card, int index) throws RemoteException {
        view.forwardMessage(new PlayersHandUpdateMessage(playerNickname, card, index));
    }

    @Override
    public void setVisibleCard(PlayCard card, int index) throws RemoteException {
        view.forwardMessage(new VisibleCardUpdateMessage(card, index));
    }

    @Override
    public void setDeckTopResource(Resource resource, int index) throws RemoteException {
        view.forwardMessage(new DeckUpdateMessage(resource, index));
    }

    @Override
    public void placeCardOnPlayersBoard(String playerNickName, CardSlot cardSlot, CardLocation location) throws RemoteException {
        view.forwardMessage(new PlayersBoardUpdateMessage(playerNickName, cardSlot, location));
    }

    @Override
    public void updateGameList(Set<String> availableGames) throws RemoteException {
        view.forwardMessage(new GameListMessage(availableGames));
    }

    @Override
    public void confirmJoin(String nickname, ClientGameSaving savings) throws RemoteException {
        view.forwardMessage(new JoinConfirmationMessage(nickname, savings));
    }

    @Override
    public void updatePlayersList(String[] nicknames, PlayerColor[] colors) throws RemoteException {
        view.forwardMessage(new PlayersListUpdateMessage(nicknames, colors));
    }

    @Override
    public void updateGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playersTurn) throws RemoteException {
        view.forwardMessage(new GameStatusUpdateMessage(gameStatus, turnStatus, playersTurn));
    }

    @Override
    public void reportError(RuntimeException exception) throws RemoteException {
        view.forwardMessage(new ServerErrorMessage(exception));
    }

    @Override
    public void updateScore(ScoreBoard scoreBoard) throws RemoteException {
        view.forwardMessage(new ScoreUpdateMessage(scoreBoard));
    }

    @Override
    public void ping(boolean isAnswer) throws RemoteException {
        view.forwardMessage(new ServerToClientPingMessage(isAnswer));
    }

    @Override
    public void sendChatMsg(ChatMessage chatMessage, boolean isPrivate) throws RemoteException {
        view.forwardMessage(new ServerChatMsgMessage(chatMessage, isPrivate));
    }
}
