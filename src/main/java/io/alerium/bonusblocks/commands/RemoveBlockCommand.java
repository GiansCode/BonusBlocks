package io.alerium.bonusblocks.commands;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.blocks.BonusBlock;
import io.alerium.bonusblocks.utils.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RemoveBlockCommand extends SubCommand {

    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();

    public RemoveBlockCommand() {
        super("remove", "bonusblocks.command.add", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Block block = player.getTargetBlock(null, 3);
        if (block == null)
            return;

        BonusBlock bonusBlock = plugin.getBonusBlockManager().getBonusBlock(block.getLocation());
        if (bonusBlock == null) {
            plugin.getMessages().getMessage("commands.remove.bonusBlockNotFound").format().send(player);
            return;
        }

        if (bonusBlock.isSpawned() && bonusBlock.getSpawnedLocation().equals(block.getLocation()))
            bonusBlock.despawnBlock();

        List<Location> locations = bonusBlock.getLocations();
        locations.remove(block.getLocation());
        plugin.getBlocksConfig().setLocationList(bonusBlock.getMaterial().name(), locations);
        plugin.getBlocksConfig().save();
        
        plugin.getMessages().getMessage("commands.remove.removed").format().send(player);
    }
    
}
