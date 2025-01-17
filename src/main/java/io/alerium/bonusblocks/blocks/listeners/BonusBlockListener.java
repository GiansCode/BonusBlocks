package io.alerium.bonusblocks.blocks.listeners;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.blocks.BonusBlock;
import io.alerium.bonusblocks.blocks.events.BonusBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BonusBlockListener implements Listener {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    
    @EventHandler
    public void onSpawnedBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BonusBlock bonusBlock = plugin.getBonusBlockManager().getBonusBlockBySpawned(block.getLocation());
        if (bonusBlock == null)
            return;
        
        event.setCancelled(true);
        Player player = event.getPlayer();

        BonusBlockBreakEvent breakEvent = new BonusBlockBreakEvent(player, bonusBlock, block);
        Bukkit.getPluginManager().callEvent(breakEvent);
        if (breakEvent.isCancelled())
            return;
        
        bonusBlock.executeBreak(player);
        plugin.getPlayerDataManager().getPlayer(player.getUniqueId()).incrementBrokenBlocks(1);
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBonusBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BonusBlock bonusBlock = plugin.getBonusBlockManager().getBonusBlock(block.getLocation());
        if (bonusBlock == null)
            return;
        
        event.setCancelled(true);
    }
    
}
