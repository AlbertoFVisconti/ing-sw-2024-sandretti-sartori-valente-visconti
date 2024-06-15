module it.polimi.ingsw {
    requires java.rmi;
    requires java.sql;
    requires org.json;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    exports it.polimi.ingsw.model;
    exports it.polimi.ingsw.model.decks;
    exports it.polimi.ingsw.utils;
    exports it.polimi.ingsw.model.cards;
    exports it.polimi.ingsw.model.cards.corners;
    exports it.polimi.ingsw.model.saving;
    exports it.polimi.ingsw.events.messages.server;
    exports it.polimi.ingsw.events.messages.client;
    exports it.polimi.ingsw.controller;
    exports it.polimi.ingsw.model.player;
    exports it.polimi.ingsw.model.goals;
    exports it.polimi.ingsw.network.serverhandlers;
    exports it.polimi.ingsw.view.ui.gui.FXController;
    exports it.polimi.ingsw.network.cliendhandlers;
    exports it.polimi.ingsw.model.chat;
    exports it.polimi.ingsw.model.cards.scoring;
    exports it.polimi.ingsw.events.messages;

    opens it.polimi.ingsw.view.ui to javafx.fxml;
    opens it.polimi.ingsw.view.ui.gui.FXController to javafx.fxml;

    exports it.polimi.ingsw.view.ui;
    exports it.polimi.ingsw.network.rmi;
    exports it.polimi.ingsw.view.ui.tui;
    opens it.polimi.ingsw.view.ui.tui to javafx.fxml;
    exports it.polimi.ingsw.view.ui.gui;
    opens it.polimi.ingsw.view.ui.gui to javafx.fxml;
    exports it.polimi.ingsw.view;
    opens it.polimi.ingsw.view to javafx.fxml;
}