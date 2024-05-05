package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.controller.TurnStatus;
import it.polimi.ingsw.events.messages.server.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.serverhandlers.RMIServerHandler;
import it.polimi.ingsw.network.serverhandlers.ServerHandler;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

public class ViewWrapper extends UnicastRemoteObject implements VirtualView{
    UserInterface userInterface;

    public UserInterface getUserInterface() {
        return userInterface;
    }

    public ViewWrapper(UserInterface userInterface) throws RemoteException {
        super();
        this.userInterface = userInterface;
    }

    @Override
    public void setController(VirtualController controller) throws RemoteException {
        ServerHandler serverHandler = userInterface.getServerHandler();
        if(serverHandler instanceof RMIServerHandler)
            ((RMIServerHandler) serverHandler).setController(controller);
    }

    @Override
    public void setPlayerIdentifier(String playerIdentifier) throws RemoteException {
        userInterface.forwardMessage(new ConnectionConfirmationMessage(playerIdentifier));
    }

    @Override
    public void setStartingCard(StartCard card) throws RemoteException {
        userInterface.forwardMessage(new StartCardUpdateMessage(card));
    }

    @Override
    public void setPublicGoal(Goal[] goals) throws RemoteException {
        userInterface.forwardMessage(new PublicGoalsUpdateMessage(goals));
    }

    @Override
    public void setAvailablePrivateGoals(Goal[] goals) throws RemoteException {
        userInterface.forwardMessage(new PrivateGoalUpdateMessage(goals));
    }

    @Override
    public void setDefinitivePrivateGoal(Goal goal) throws RemoteException {
        userInterface.forwardMessage(new PrivateGoalUpdateMessage(goal));
    }

    @Override
    public void setPlayersCard(String playerNickname, PlayCard card, int index) throws RemoteException {
        userInterface.forwardMessage(new PlayersHandUpdateMessage(playerNickname, card, index));
    }

    @Override
    public void setVisibleCard(PlayCard card, int index) throws RemoteException {
        userInterface.forwardMessage(new VisibleCardUpdateMessage(card, index));
    }

    @Override
    public void setDeckTopResource(Resource resource, int index) throws RemoteException {
        userInterface.forwardMessage(new DeckUpdateMessage(resource, index));
    }

    @Override
    public void placeCardOnPlayersBoard(String playerNickName, Card card, CardLocation location) throws RemoteException {
        userInterface.forwardMessage(new PlayersBoardUpdateMessage(playerNickName, card, location));
    }

    @Override
    public void updateGameList(Set<Integer> availableGames) throws RemoteException {
        userInterface.forwardMessage(new GameListMessage(availableGames));
    }

    @Override
    public void confirmJoin(String nickname) throws RemoteException {
        userInterface.forwardMessage(new JoinConfirmationMessage(nickname));
    }

    @Override
    public void updatePlayersList(String[] nicknames, PlayerColor[] colors) throws RemoteException {
        userInterface.forwardMessage(new PlayersListUpdateMessage(nicknames, colors));
    }

    @Override
    public void updateGameStatus(GameStatus gameStatus, TurnStatus turnStatus, String playersTurn) throws RemoteException {
        userInterface.forwardMessage(new GameStatusUpdateMessage(gameStatus, turnStatus, playersTurn));
    }
}
