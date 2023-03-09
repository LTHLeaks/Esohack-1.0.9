package com.esoterik.client.features.command.commands;

import com.esoterik.client.esohack;
import com.esoterik.client.features.command.Command;
import com.esoterik.client.manager.FriendManager;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("friend", new String[]{"<add/del/name/clear>", "<name>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            if (esohack.friendManager.getFriends().isEmpty()) {
                FriendCommand.sendMessage("You currently dont have any friends added.");
            } else {
                String f = "Friends: ";
                for (FriendManager.Friend friend : esohack.friendManager.getFriends()) {
                    try {
                        f = f + friend.getUsername() + ", ";
                    }
                    catch (Exception e) {}
                }
                FriendCommand.sendMessage(f);
            }
            return;
        }
        if (commands.length == 2) {
            switch (commands[0]) {
                case "reset": {
                    esohack.friendManager.onLoad();
                    FriendCommand.sendMessage("Friends got reset.");
                    break;
                }
                default: {
                    FriendCommand.sendMessage(commands[0] + (esohack.friendManager.isFriend(commands[0]) ? " is friended." : " isnt friended."));
                }
            }
            return;
        }
        if (commands.length >= 2) {
            switch (commands[0]) {
                case "add": {
                    esohack.friendManager.addFriend(commands[1]);
                    FriendCommand.sendMessage("\u00a7b" + commands[1] + " has been friended");
                    break;
                }
                case "del": {
                    esohack.friendManager.removeFriend(commands[1]);
                    FriendCommand.sendMessage("\u00a7c" + commands[1] + " has been unfriended");
                    break;
                }
                default: {
                    FriendCommand.sendMessage("\u00a7cBad Command, try: friend <add/del/name> <name>.");
                }
            }
        }
    }
}

