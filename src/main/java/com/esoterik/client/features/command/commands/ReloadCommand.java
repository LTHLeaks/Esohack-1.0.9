package com.esoterik.client.features.command.commands;

import com.esoterik.client.esohack;
import com.esoterik.client.features.command.Command;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        esohack.reload();
    }
}

