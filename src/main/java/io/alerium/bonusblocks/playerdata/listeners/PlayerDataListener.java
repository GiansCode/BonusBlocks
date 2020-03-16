package io.alerium.bonusblocks.playerdata.listeners;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataListener implements Listener {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAsyncPlayerLogin(AsyncPlayerPreLoginEvent event) {
        plugin.getPlayerDataManager().loadPlayer(event.getUniqueId());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getPlayerDataManager().savePlayer(event.getPlayer().getUniqueId(), true));
    }
    
}
