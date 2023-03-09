package com.esoterik.client.features.command.commands;

import com.esoterik.client.esohack;
import com.esoterik.client.features.command.Command;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config", new String[]{"<save/load>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            ConfigCommand.sendMessage("You`ll find the config files in your gameProfile directory under client/config");
            return;
        }
        if (commands.length == 2) {
            if ("list".equals(commands[0])) {
                String configs = "Configs: ";
                final File file = new File("phobos/");
                final List<File> directories = Arrays.stream(file.listFiles()).filter(File::isDirectory).filter(f -> !f.getName().equals("util")).collect(Collectors.toList());
                final StringBuilder builder = new StringBuilder(configs);
                for (final File file2 : directories) {
                    builder.append(file2.getName() + ", ");
                }
                configs = builder.toString();
                Command.sendMessage("§a" + configs);
            }
            else {
                Command.sendMessage("§cNot a valid command... Possible usage: <list>");
            }
        }
        if (commands.length >= 3) {
            switch (commands[0]) {
                case "save": {
                    esohack.configManager.saveConfig(commands[1]);
                    ConfigCommand.sendMessage("\u00a7aConfig has been saved.");
                    break;
                }
                case "load": {
                    esohack.configManager.loadConfig(commands[1]);
                    ConfigCommand.sendMessage("\u00a7aConfig has been loaded.");
                    break;
                }
                default: {
                    ConfigCommand.sendMessage("\u00a7cNot a valid command... Possible usage: <save/load>");
                }
            }
        }
    }
}

