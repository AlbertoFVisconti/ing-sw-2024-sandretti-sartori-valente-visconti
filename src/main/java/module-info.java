module it.polimi.ingsw {
    requires java.rmi;
    requires java.sql;
    requires org.json;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens it.polimi.ingsw.view.ui to javafx.fxml;
    exports it.polimi.ingsw.view.ui;
}