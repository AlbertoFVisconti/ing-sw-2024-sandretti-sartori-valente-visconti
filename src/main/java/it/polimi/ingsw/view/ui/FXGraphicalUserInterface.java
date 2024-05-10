package it.polimi.ingsw.view.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.Objects;


public class FXGraphicalUserInterface extends UserInterface {
    @Override
    public void reportError(RuntimeException exception) throws RemoteException {

    }

    @Override
    protected void update() {

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

