package io.alerium.bonusblocks.utils.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor @Getter
public abstract class SubCommand {
    
    private final String name;

    private final String permission;
    private final boolean onlyPlayer;

    /**
     * This method is executed when the SubCommand is called
     * @param sender The CommandSender 
     * @param args The arguments of the SubCommand
     */
    public abstract void execute(CommandSender sender, String[] args);
    
}
