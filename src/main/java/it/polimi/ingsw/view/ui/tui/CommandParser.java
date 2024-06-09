package it.polimi.ingsw.view.ui.tui;

import java.security.InvalidParameterException;
import java.util.*;


public class CommandParser extends Thread {
    private final Scanner scanner;
    private final TextualUserInterface textualUserInterface;
    private final HashMap<String, Command> registeredCommands;
    private Command contextualCommandExecutor = null;

    public CommandParser(Scanner scanner, TextualUserInterface textualUserInterface) {
        this.textualUserInterface = textualUserInterface;
        this.scanner = scanner;
        this.registeredCommands = new HashMap<>();
    }

    public void setContextualCommandExecutor(Command contextualCommandExecutor) {
        this.contextualCommandExecutor = contextualCommandExecutor;
    }

    public void registerCommand(String commandString, Command commandExecutor) {
        this.registeredCommands.put(commandString, commandExecutor);
    }

    private String nextCommand()  {
        String command;
        do {
            command = scanner.nextLine();
        } while (command.isEmpty());

        return command;
    }

    private void processCommand(String commandString) {
        String[] tokens = commandString.split(" ");
        if (tokens.length == 0) return;

        if(commandString.startsWith("!")) {
            Command command = this.registeredCommands.get(tokens[0]);
            if(command == null) throw new UnsupportedOperationException("unknown command");
            else {
                command.execute(textualUserInterface, tokens);
            }
        }
        else if(this.contextualCommandExecutor != null) {
            this.contextualCommandExecutor.execute(textualUserInterface, tokens);
            textualUserInterface.update();
        }
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                String commandString = this.nextCommand();
                textualUserInterface.resetError();
                this.processCommand(commandString);
            }
            catch (InvalidParameterException | UnsupportedOperationException e) {
                this.textualUserInterface.reportError(e);
            }
        }
    }
}
