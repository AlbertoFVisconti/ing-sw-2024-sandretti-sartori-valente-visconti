package it.polimi.ingsw.view.ui.tui;

import it.polimi.ingsw.network.Client;

import java.security.InvalidParameterException;
import java.util.*;


/**
 * CommandParser is a Runnable object that allows to asynchronously process user input through
 * the console and to execute the commands.
 * Commands can be defined and added dynamically through the registerCommand() using the Command
 * functional interface.
 */
public class CommandParser extends Thread {
    // Scanner that needs to be used to read the user input
    private final Scanner scanner;

    // TextualUserInterface that needs to be used to present the commands' execution results
    private final TextualUserInterface textualUserInterface;

    // Hashmap that stores the registered commands
    private final HashMap<String, Command> registeredCommands;

    // the standard command that can be used to process contextual commands
    private Command contextualCommandExecutor = null;

    /**
     * Builds a CommandsParser objet that reads from a given Scanner
     * and present results through the given TextualUserInterface.
     *
     * @param scanner Scanner object that needs to be used to read user input
     * @param textualUserInterface TextualUserInterface that needs to be used to present commands' execution results
     */
    public CommandParser(Scanner scanner, TextualUserInterface textualUserInterface) {
        this.textualUserInterface = textualUserInterface;
        this.scanner = scanner;
        this.registeredCommands = new HashMap<>();
    }

    /**
     * Allows to provide the Command that needs to be used to process
     * contextual commands.
     *
     * @param contextualCommandExecutor contextual command executor
     */
    public void setContextualCommandExecutor(Command contextualCommandExecutor) {
        this.contextualCommandExecutor = contextualCommandExecutor;
    }

    /**
     * Allows to provide a command that needs to be used to process
     * a command identified by a certain string (first token of the user input)
     *
     * @param commandString the name of the command that needs to be used as the first token in order to use the command
     * @param commandExecutor Command object that needs to be used to process the newly registered command
     */
    public void registerCommand(String commandString, Command commandExecutor) {
        this.registeredCommands.put(commandString, commandExecutor);
    }

    /**
     * Waits for the next command to be inputted.
     *
     * @return the latest command the user inputted
     */
    private String nextCommand()  {
        String command;
        do {
            command = scanner.nextLine();
        } while (command.isEmpty());

        return command;
    }

    /**
     * Allows to process a command by calling the appropriate Command executor.
     *
     * @param commandString the String that contains the user inputted command
     */
    private void processCommand(String commandString) {
        // splitting the command into tokens
        String[] tokens = commandString.split(" ");

        // if the user input's empty, there's nothing to execute
        if (tokens.length == 0) return;

        // checking if the commands is contextual or not (! means non-contextual)
        if(commandString.startsWith("!")) {
            // the user is not allowed to switch interface if the protocol hasn't been chosen
            if(Client.getInstance().getServerHandler() == null) return;

            // retrieving the command executor from the registered commands
            Command command = this.registeredCommands.get(tokens[0]);

            // checking if the command was recognized
            if(command == null) throw new UnsupportedOperationException("unknown command");
            else {
                command.execute(textualUserInterface, tokens);
            }
        }
        else if(this.contextualCommandExecutor != null) {
            // contextual command (and the contextual executor was specified)
            this.contextualCommandExecutor.execute(textualUserInterface, tokens);
            textualUserInterface.update();
        }
    }

    /**
     * Thread method that endlessly process user inputted commands.
     */
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
