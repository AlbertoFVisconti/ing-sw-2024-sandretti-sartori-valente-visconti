package it.polimi.ingsw.view.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GraphicalUserInterface extends UserInterface {


    public class MainFrame extends JFrame {

        private JButton creategame = new JButton("Create game");
        private Integer[] expectedplayersarray = {2, 3, 4};
        private JComboBox<Integer> expectedplayers = new JComboBox<>(expectedplayersarray);
        private static JTextField nickname = new JTextField("Insert nickname", 20);
        private JButton join = new JButton("join a game");
        private JButton submit = new JButton("Create Game");


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
                            gui.getServerHandler().createGame((int) expectedplayers.getSelectedItem(), nickname.getText());
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
                    gui.getServerHandler().getAvailableGames();

                    try {
                        // TODO: find a better way (UserInterface.update())
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }

                    if(gui.availableGames != null) {
                        for (Integer gameID : gui.availableGames) {
                            JButton joinGame = new JButton("Join game " + gameID);
                            ConfigGame.add(joinGame);
                            joinGame.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        gui.getServerHandler().joinGame(gameID, nickname.getText());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        }
                    }else{
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
    protected void update() {

    }
}
