package com.esoterik.client.features.command.commands;

import com.esoterik.client.esohack;
import com.esoterik.client.features.command.Command;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("commands");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("You can use following commands: ");
        for (Command command : esohack.commandManager.getCommands()) {
            HelpCommand.sendMessage(esohack.commandManager.getPrefix() + command.getName());
        }
    }
}

