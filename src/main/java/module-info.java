module it.polimi.ingsw {
    requires java.rmi;
    requires java.sql;
    requires org.json;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    exports it.polimi.ingsw.network.rmi to java.rmi;
    exports it.polimi.ingsw.view to java.rmi;

    opens it.polimi.ingsw.view.ui to javafx.fxml;
    opens it.polimi.ingsw.view.FXController to javafx.fxml;

    exports it.polimi.ingsw.view.ui;
}