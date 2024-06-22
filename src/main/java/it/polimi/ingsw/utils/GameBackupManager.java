package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.saving.GameBackup;
import it.polimi.ingsw.model.saving.PlayerSaving;

import java.io.*;
import java.util.ArrayList;

public class GameBackupManager {
    private static boolean backupEnabled = false;

    public static void saveGame(String key, GameBackup gameBackup) {
        if(!backupEnabled) return;

        new File("saves").mkdirs();
        String filename = "saves/" + key + ".ser";

        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(gameBackup);
            out.close();
            file.close();
        }
        catch (IOException ignored){}
    }

    public static GameBackup retrieveGame(String key) {
        if(!backupEnabled) return null;

        File inputFile = new File("saves/"+ key + ".ser");
        if(!inputFile.exists() || inputFile.isDirectory()) {
            return null;
        }

        System.out.println(inputFile.getAbsolutePath());

        GameBackup restoredGame = null;
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            ObjectInputStream in = new ObjectInputStream(inputStream);
            restoredGame = (GameBackup) in.readObject();
            in.close();
            inputStream.close();
        }
        catch (Exception ignored) {}

        return restoredGame;
    }

    public static void enableBackup() {
        backupEnabled = true;
    }

    public static void disableBackup() {
        backupEnabled = false;
    }
}
