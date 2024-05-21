package it.polimi.ingsw.view.ui.gui;

import it.polimi.ingsw.view.ui.UserInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.Objects;


public class FXGraphicalUserInterface extends UserInterface {
    public static UserInterface currentInterface = null;

    @Override
    public void reportError(RuntimeException exception) throws RemoteException {

    }

    @Override
    public void update() {
        if(currentInterface != null) currentInterface.update();
    }

    //run method that launches the GUI
    public void run() {
        Application.launch(GUI.class);
    }

     public static class GUI extends Application{

        //start method that loads the Lobby.fxml file
        public void start(Stage stage) throws Exception {
            Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/fxml/Lobby.fxml")));
            stage.setScene(new Scene(root));
            stage.show();
        }

        public static void main(String[] args) {
           launch(args);
        }
    }
}

