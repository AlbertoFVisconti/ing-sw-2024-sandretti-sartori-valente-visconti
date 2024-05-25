package it.polimi.ingsw.view.ui.gui;


import it.polimi.ingsw.events.messages.client.GameListRequestMessage;
import it.polimi.ingsw.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ui.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;


public class GraphicalUserInterface extends UserInterface {


    @Override
    public void reportError(RuntimeException exception) throws RemoteException {
        System.err.println(exception.getMessage());
    }

    public static class MainFrame extends JFrame {

        private final JButton creategame = new JButton("Create game");
        private final Integer[] expectedplayersarray = {2, 3, 4};
        private final JComboBox<Integer> expectedplayers = new JComboBox<>(expectedplayersarray);
        private static final JTextField nickname = new JTextField("Insert nickname", 20);
        private final JButton join = new JButton("join a game");
        private final JButton submit = new JButton("Create Game");


        boolean creatingGame = false;

        public MainFrame(GraphicalUserInterface gui) {

            super("Lobby");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1000, 400);
            setLayout(new GridLayout(2, 1));
            //add the join and create buttons
            JPanel SelectGameType = new JPanel();
            SelectGameType.setLayout(new FlowLayout());
            add(SelectGameType);
            SelectGameType.add(creategame);
            SelectGameType.add(join);
            //add the configuration for the game
            JPanel ConfigGame = new JPanel();
            ConfigGame.setLayout(new FlowLayout());
            add(ConfigGame);
            ConfigGame.add(expectedplayers);
            ConfigGame.add(nickname);
            ConfigGame.setVisible(false);
            ConfigGame.add(submit);
            //add functionality to the buttons
            creategame.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SelectGameType.setVisible(false);
                    ConfigGame.setVisible(true);
                    creatingGame = true;
                    submit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JLabel created = new JLabel("Game created! Waiting for players to join");
                            ConfigGame.add(created);
                            Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(-1, true, (int) expectedplayers.getSelectedItem(), nickname.getText()));
                            submit.setEnabled(false);
                        }
                    });
                }
            });
            join.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SelectGameType.setVisible(false);
                    expectedplayers.setVisible(false);
                    submit.setVisible(false);
                    ConfigGame.setVisible(true);
                    Client.getInstance().getServerHandler().sendMessage(new GameListRequestMessage());

                    try {
                        // TODO: find a better way (UserInterface.update())
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }

                    if (gui.availableGames != null) {
                        for (Integer gameID : gui.availableGames) {
                            JButton joinGame = new JButton("Join game " + gameID);
                            ConfigGame.add(joinGame);
                            joinGame.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        Client.getInstance().getServerHandler().sendMessage(new JoinGameMessage(gameID, false, 0, nickname.getText()));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        }
                    } else {
                        JLabel noGames = new JLabel("No games available");
                        ConfigGame.add(noGames);
                    }
                }
            });
        }
    }

    public void run() {
        try {
            MainFrame frame = new MainFrame(this);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update() {

    }
}
