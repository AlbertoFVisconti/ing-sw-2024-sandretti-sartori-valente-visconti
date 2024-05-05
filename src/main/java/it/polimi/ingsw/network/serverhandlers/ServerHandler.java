package it.polimi.ingsw.network.serverhandlers;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.utils.CardLocation;

public abstract class ServerHandler  {
    public UserInterface userInterface;
    public ServerHandler(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public UserInterface getUserInterface() {
        return userInterface;
    }

    public abstract void joinGame(int IDGame, String nick);


    public abstract void createGame(int expectedPlayers, String nick);

    public abstract void getAvailableGames();


    public abstract void placeCard(int index, boolean onBackSide, CardLocation location);
    public abstract void drawCard(int index);

    public abstract void placeStartCard(boolean onBackSide);

    public abstract void selectPrivateGoal(int index);

    public abstract void selectColor(PlayerColor color);

}
