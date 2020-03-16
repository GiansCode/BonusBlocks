package io.alerium.bonusblocks.utils.commands;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.utils.configuration.Message;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class Command implements CommandExecutor {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    
    private final Message helpMessage;
    private final List<SubCommand> commands;
    
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 0) {
            helpMessage.send(sender);
            return true;
        }
        
        String cmdName = args[0];
        SubCommand subCommand = getCommand(cmdName);
        if (subCommand == null) {
            plugin.getMessages().getMessage("commands.invalidCommand").format().send(sender);
            return true;
        }
        
        if (!sender.hasPermission(subCommand.getPermission())) {
            plugin.getMessages().getMessage("commands.noPermission").format().send(sender);
            return true;
        }
        
        if (!(sender instanceof Player) && subCommand.isOnlyPlayer()) {
            plugin.getMessages().getMessage("commands.onlyPlayer").format().send(sender);
            return true;
        }
        
        subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }

    /**
     * This method searches in the SubCommand(s) list if there is a SubCommand with the specified name
     * @param name The SubCommand name
     * @return The SubCommand if present, if not null
     */
    private SubCommand getCommand(String name) {
        for (SubCommand command : commands) {
            if (command.getName().equalsIgnoreCase(name))
                return command;
        }
        
        return null;
    }
    
}
