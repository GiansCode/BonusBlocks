package io.alerium.bonusblocks.playerdata;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.playerdata.storage.MYSQLStorage;
import io.alerium.bonusblocks.playerdata.storage.Storage;
import io.alerium.bonusblocks.playerdata.storage.YAMLStorage;
import org.bukkit.Bukkit;
import sh.okx.timeapi.api.TimeAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerDataManager {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    private final List<PlayerData> players = new ArrayList<>();
    
    private Storage storage;
    private List<UUID> top;

    /**
     * This method enables the PlayerDataManager
     */
    public void enable() {
        String storageType = plugin.getConfiguration().getConfig().getString("storage.type").toUpperCase();
        if ("MYSQL".equals(storageType)) {
            storage = new MYSQLStorage();
        } else {
            storage = new YAMLStorage();
        }

        try {
            storage.setup();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while the database setup, stopping the plugin.", e);
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
        
        Bukkit.getOnlinePlayers().forEach(player -> loadPlayer(player.getUniqueId()));
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> saveAllPlayers(false), 20*60*5, 20*60*5);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::updateTop, 0, 20 * (int) new TimeAPI(plugin.getConfiguration().getConfig().getString("update-top-every")).getSeconds());
    }

    /**
     * This method saves all the loaded PlayerData to the Storage defined in the config
     * @param remove True if the PlayerData(s) has to be removed from the cache
     */
    public void saveAllPlayers(boolean remove) {
        for (PlayerData player : players.toArray(new PlayerData[0])) 
            savePlayer(player, remove);
    }

    /**
     * This method loads a PlayerData from the Storage
     * @param uuid The UUID of the Player
     */
    public void loadPlayer(UUID uuid) {
        try {
            players.add(storage.loadData(uuid));
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "An error occurred while loading " + uuid + " player data", e);
        }
    }

    /**
     * This method saves a PlayerData in the Storage
     * @param uuid The UUID of the Player
     * @param remove True if the PlayerData has to be removed from the cache
     */
    public void savePlayer(UUID uuid, boolean remove) {
        PlayerData playerData = getPlayer(uuid);
        if (playerData == null)
            return;
        
        savePlayer(playerData, remove);
    }
    
    /**
     * This method saves a PlayerData in the Storage
     * @param playerData The PlayerData
     * @param remove True if the PlayerData has to be removed from the cache
     */
    public void savePlayer(PlayerData playerData, boolean remove) {
        try {
            storage.saveData(playerData);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "An error occurred while saving player data of " + playerData.getUuid(), e);
        }

        if (remove)
            players.remove(playerData);
    }

    /**
     * This method gets a PlayerData from the UUID of the Player
     * @param uuid The UUID of the Player
     * @return The PlayerData object, null if not found
     */
    public PlayerData getPlayer(UUID uuid) {
        for (PlayerData player : players) {
            if (player.getUuid().equals(uuid))
                return player;
        }
        
        return null;
    }

    /**
     * This method gets the UUID of the Player that is in a specific top position
     * @param t The position
     * @return The UUID of the Player, null if not found
     */
    public UUID getTopPosition(int t) {
        if (t >= top.size())
            return null;
        
        return top.get(t);
    }

    /**
     * This method updates the top from the Storage
     */
    private void updateTop() {
        try {
            top = storage.getTop();
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "An error occurred while updading the top players", e);
        }
    }
    
    
}
