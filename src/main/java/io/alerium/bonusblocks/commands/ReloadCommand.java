package io.alerium.bonusblocks.commands;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.utils.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    
    public ReloadCommand() {
        super("reload", "bonusblocks.command.reload", false);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getConfiguration().reload();
        plugin.getMessages().reload();
        
        plugin.getBonusBlockManager().reload();
        plugin.getMessages().getMessage("commands.reload").format().send(sender);
    }
    
}
