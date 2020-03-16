package io.alerium.bonusblocks.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class CommandUtil {

    private CommandUtil() {
    }
    
    public static void executeCommand(String command, Player player) {
        String commandExec = command.substring(command.indexOf("]") + 2).replaceAll("%player%", player.getName());

        if (command.startsWith("[CONSOLE]"))
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandExec);
        else if (command.startsWith("[PLAYER]"))
            Bukkit.dispatchCommand(player, commandExec);
    }
    
}
