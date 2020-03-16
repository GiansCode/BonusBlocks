package io.alerium.bonusblocks.integrations;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.playerdata.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlaceholderAPI extends PlaceholderExpansion {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    
    @Override
    public String getIdentifier() {
        return "bonusblocks";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (params.equalsIgnoreCase("broken")) {
            PlayerData playerData = plugin.getPlayerDataManager().getPlayer(p.getUniqueId());
            if (playerData == null)
                return null;
            
            return Integer.toString(playerData.getBrokenBlocks());
        }
        
        if (params.startsWith("top_")) {
            int position = Integer.parseInt(params.substring(params.indexOf("_") + 1)) - 1;
            UUID uuid = plugin.getPlayerDataManager().getTopPosition(position);
            if (uuid == null)
                return null;
            
            return Bukkit.getPlayer(uuid).getName();
        }
        return null;
    }
}
