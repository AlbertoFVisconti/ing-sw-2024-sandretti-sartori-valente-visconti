package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.events.messages.client.*;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Remote Object that allows RMI clients to send message directly calling method on this object.
 * Remote calls will be "converted" into messages. Those messages will be
 * forwarded to the GameController and then processed asynchronously.
 */
public class GameControllerWrapper extends UnicastRemoteObject implements VirtualController {
    private final GameController gameController;

    /**
     * Builds a new GameControllerWrapper remote objet that wraps the provided GameController
     *
     * @param gameController GameController that needs to be wrapped
     * @throws RemoteException in case of errors with the remote communication.
     */
    public GameControllerWrapper(GameController gameController) throws RemoteException {
        super();
        this.gameController = gameController;
    }

    /**
     * Retrieves the wrapped GameController
     *
     * @return GameController object wrapper by this GameControllerWrapper
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Allows to place a card on the player's board.
     *
     * @param playerIdentifier The string that the player received while joining/creating the game.
     * @param index            The index of the player's hand that contains the cards that needs to be placed.
     * @param onBackSide       {@code true} if the cards should be placed with the back side up, {@code false} otherwise.
     * @param location         The CardLocation that represents the desired placement location of the card.
     * @throws RemoteException in case of errors with the remote communication.
     */
    @Override
    public void placeCard(String playerIdentifier, int index, boolean onBackSide, CardLocation location) throws RemoteException {
        ClientMessage message = new PlaceCardMessage(index, onBackSide, location);
        message.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(message);
    }

    /**
     * Allows to draw a card.
     *
     * @param playerIdentifier The string that the player received while joining/creating the game.
     * @param index            The number that represents what the player's trying to draw/pick up.
     * @throws RemoteException in case of errors with the remote communication.
     */
    @Override
    public void drawCard(String playerIdentifier, int index) throws RemoteException {
        ClientMessage message = new DrawCardMessage(index);
        message.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(message);
    }

    /**
     * Allows to place the starting card
     *
     * @param playerIdentifier The string that the player received while joining/creating the game.
     * @param onBackSide       {@code true} if the cards should be placed with the back side up, {@code false} otherwise.
     * @throws RemoteException in case of errors with the remote communication.
     */
    @Override
    public void placeStartCard(String playerIdentifier, boolean onBackSide) throws RemoteException {
        ClientMessage message = new PlaceStartCardMessage(onBackSide);
        message.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(message);
    }

    /**
     * Allows to select the private goal
     *
     * @param playerIdentifier The string that the player received while joining/creating the game.
     * @param index            The index of the available goal the player has chosen.
     * @throws RemoteException in case of errors with the remote communication.
     */
    @Override
    public void selectPrivateGoal(String playerIdentifier, int index) throws RemoteException {
        ClientMessage message = new SelectGoalMessage(index);
        message.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(message);
    }

    /**
     * Allows to select the player's color.
     *
     * @param playerIdentifier the string that the player received while joining/creating the game.
     * @param color the PlayerColor that represent the player's choice
     * @throws RemoteException in case of errors with the remote communication.
     */
    @Override
    public void selectColor(String playerIdentifier, PlayerColor color) throws RemoteException {
        ClientMessage message = new SelectColorMessage(color);
        message.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(message);
    }

    /**
     * Allows to send a chat message
     *
     * @param playerIdentifier the string that the player received while joining/creating the game
     * @param message the message that the player want to send
     * @param addressee the nickname of the player that needs to receive this message, {@code null} for public messages
     * @throws RemoteException in case of errors with the remote communication.
     */
    @Override
    public void sendChatMsg(String playerIdentifier, String message, String addressee) throws RemoteException {
        ClientMessage clientMessage = new ClientChatMsgMessage(message, addressee);
        clientMessage.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(clientMessage);
    }
}
