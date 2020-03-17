package io.alerium.bonusblocks.commands;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.blocks.BonusBlock;
import io.alerium.bonusblocks.utils.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AddBlockCommand extends SubCommand {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    
    public AddBlockCommand() {
        super("add", "bonusblocks.command.add", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Block block = player.getTargetBlock(null, 3);
        if (block == null)
            return;
        
        if (plugin.getBonusBlockManager().getBonusBlock(block.getLocation()) != null) {
            plugin.getMessages().getMessage("commands.add.alreadyBonusBlock").format().send(player);
            return;
        }

        BonusBlock bonusBlock = plugin.getBonusBlockManager().getBonusBlock(block.getType());
        if (bonusBlock == null) {
            plugin.getMessages().getMessage("commands.add.noBonusBlockType").format().send(player);
            return;
        }
        
        block.setType(bonusBlock.getInactiveMaterial());
        
        List<Location> locations = bonusBlock.getLocations();
        locations.add(block.getLocation());
        plugin.getBlocksConfig().setLocationList(bonusBlock.getMaterial().name(), locations);
        plugin.getBlocksConfig().save();
        
        plugin.getMessages().getMessage("commands.add.added").format().send(player);
    }
    
}
