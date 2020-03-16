package io.alerium.bonusblocks.commands;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.utils.NumberUtil;
import io.alerium.bonusblocks.utils.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListCommand extends SubCommand {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    
    public ListCommand() {
        super("list", "bonusblocks.command.list", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            plugin.getMessages().getMessage("commands.list.usage").format().send(player);
            return;
        }
        
        Integer radius = NumberUtil.parseInteger(args[0]);
        if (radius == null) {
            plugin.getMessages().getMessage("commands.list.radiusNotValid").format().send(player);
            return;
        }

        Location startLoc = player.getLocation().toBlockLocation();
        startLoc.setYaw(0);
        startLoc.setPitch(0);
        
        List<Location> foundBlocks = new ArrayList<>();
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Location newLoc = startLoc.clone().add(x, y, z);
                    if (plugin.getBonusBlockManager().getBonusBlock(newLoc) != null)
                        foundBlocks.add(newLoc);
                }
            }
        }
        
        if (foundBlocks.isEmpty()) {
            plugin.getMessages().getMessage("commands.list.noBlocksFound").format().send(player);
            return;
        }

        for (Location block : foundBlocks) {
            plugin.getMessages().getMessage("commands.list.blockFound")
                    .format()
                    .addPlaceholder("x", block.getBlockX())
                    .addPlaceholder("y", block.getBlockY())
                    .addPlaceholder("z", block.getBlockZ())
                    .send(player);
        }
    }
    
}
