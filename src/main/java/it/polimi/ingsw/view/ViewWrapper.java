package it.polimi.ingsw.view;

import it.polimi.ingsw.events.messages.server.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.serverhandlers.RMIServerHandler;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

public class ViewWrapper extends UnicastRemoteObject implements VirtualView{
    RMIServerHandler serverHandler;

    public ViewWrapper(RMIServerHandler serverHandler) throws RemoteException {
        super();
        this.serverHandler = serverHandler;
    }

    @Override
    public void setController(VirtualController controller) throws RemoteException {
        serverHandler.setController(controller);
    }

    @Override
    public void setPlayerIdentifier(String playerIdentifier) throws RemoteException {
        serverHandler.forwardMessage(new ConnectionConfirmationMessage(playerIdentifier));
    }

    @Override
    public void setStartingCard(StartCard card) throws RemoteException {
        serverHandler.forwardMessage(new StartCardUpdateMessage(card));
    }

    @Override
    public void setPublicGoal(Goal[] goals) throws RemoteException {
        serverHandler.forwardMessage(new PublicGoalsUpdateMessage(goals));
    }

    @Override
    public void setAvailablePrivateGoals(Goal[] goals) throws RemoteException {
        serverHandler.forwardMessage(new PrivateGoalUpdateMessage(goals));
    }

    @Override
    public void setDefinitivePrivateGoal(Goal goal) throws RemoteException {
        serverHandler.forwardMessage(new PrivateGoalUpdateMessage(goal));
    }

    @Override
    public void setPlayersCard(String playerNickname, PlayCard card, int index) throws RemoteException {
        serverHandler.forwardMessage(new PlayersHandUpdateMessage(playerNickname, card, index));
    }

    @Override
    public void setVisibleCard(PlayCard card, int index) throws RemoteException {
        serverHandler.forwardMessage(new VisibleCardUpdateMessage(card, index));
    }

    @Override
    public void setDeckTopResource(Resource resource, int index) throws RemoteException {
        serverHandler.forwardMessage(new DeckUpdateMessage(resource, index));
    }

    @Override
    public void placeCardOnPlayersBoard(String playerNickName, Card card, CardLocation location) throws RemoteException {
        serverHandler.forwardMessage(new PlayersBoardUpdateMessage(playerNickName, card, location));
    }

    @Override
    public void updateGameList(Set<Integer> availableGames) throws RemoteException {
        serverHandler.forwardMessage(new GameListMessage(availableGames));
    }
}
