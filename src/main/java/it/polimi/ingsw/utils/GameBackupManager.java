package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.saving.GameBackup;

import java.io.*;

/**
 * GameBackupManager, if enabled, allows to save and reload games from the disk.
 */
public class GameBackupManager {
    private static boolean backupEnabled = false;

    /**
     * Allows to save a game using a certain game.
     *
     * @param key the Key that uniquely represents the game (the set of players)
     * @param gameBackup the game data that needs to be saved
     */
    public synchronized static void saveGame(String key, GameBackup gameBackup) {
        if(!backupEnabled) return;

        // creating the directory (if needed)
        new File("saves").mkdirs();

        String filename = "saves/" + key + ".ser";

        try {
            // trying to save the game
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(gameBackup);
            out.close();
            file.close();
        }
        catch (IOException e){
            System.out.println("\t failed to save");
        }
    }

    /**
     * Allows to delete a game backup.
     * This method is used when a game that was previously saved ends
     *
     * @param backupKey the Key that uniquely represents the game (the set of players)
     */
    public synchronized static void deleteBackup(String backupKey) {
        if(!backupEnabled) return;

        String filename = "saves/" + backupKey + ".ser";

        try {
            // trying to delete the game backup
            File file = new File(filename);
            file.delete();
        }
        catch (Exception e){
            System.out.println("\t failed to delete the backup");
        }
    }

    /**
     * Allows to load a game from the disk given the key.
     *
     * @param key the Key that uniquely represents the game (the set of players)
     * @return the game data that was requested
     */
    public static GameBackup retrieveGame(String key) {
        if(!backupEnabled) return null;

        File inputFile = new File("saves/"+ key + ".ser");
        if(!inputFile.exists() || inputFile.isDirectory()) {
            // there's no file identified by this key
            return null;
        }

        GameBackup restoredGame = null;
        try {
            // trying to load the game data
            FileInputStream inputStream = new FileInputStream(inputFile);
            ObjectInputStream in = new ObjectInputStream(inputStream);
            restoredGame = (GameBackup) in.readObject();
            in.close();
            inputStream.close();
        }
        catch (Exception ignored) {}

        // returning the restored game data (might be null if the procedure failed)
        return restoredGame;
    }

    /**
     * Allows to enable the GameBackupManager
     */
    public static void enableBackup() {
        backupEnabled = true;
    }

    /**
     * Allows to disable the GameBackupManager
     */
    public static void disableBackup() {
        backupEnabled = false;
    }
}
