package io.alerium.bonusblocks.blocks;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.alerium.bonusblocks.BonusBlocksPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class BonusBlock {
    
    @Getter private final Material material;
    @Getter private final Material inactiveMaterial;
    @Getter private final int spawnChange;
    
    private final int minRewards;
    private final int maxRewards;
    
    @Getter private final Particle particle;

    private final List<String> hologram;
    private final List<String> rewards;
    
    @Getter private final List<Location> locations;
    
    @Getter private Location spawnedLocation;
    @Getter private Hologram spawnedHologram;

    /**
     * This method spawns the BonusBlock
     * @return True if spawned
     */
    public boolean spawnBlock() {
        if (spawnedLocation != null)
            return false;
        
        spawnedLocation = getRandomLocation();
        if (spawnedLocation == null)
            return false;
        
        spawnedLocation.getBlock().setType(material);
        
        spawnedHologram = HologramsAPI.createHologram(BonusBlocksPlugin.getInstance(), spawnedLocation.clone().add(0.5, 2, 0.5));
        hologram.forEach(s -> spawnedHologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', s)));
        return true;
    }

    /**
     * This method despawns the BonusBlock
     * @return True if despawned
     */
    public boolean despawnBlock() {
        if (spawnedLocation == null)
            return false;


        spawnedHologram.delete();
        
        spawnedLocation.getBlock().setType(inactiveMaterial);
        spawnedLocation = null;
        return true;
    }

    /**
     * This method executes the break actions on a Player
     * @param player The Player
     */
    public void executeBreak(Player player) {
        if (!despawnBlock())
            return;
        
        int rewardAmount = ThreadLocalRandom.current().nextInt(minRewards, maxRewards);
        for (int i = 0; i < rewardAmount; i++)
            executeRandomReward(player);
    }

    /**
     * This method checks if the BonusBlock is already spawned
     * @return
     */
    public boolean isSpawned() {
        return spawnedLocation != null;
    }

    /**
     * This method gets a random Location from the List
     * @return A random Location
     */
    private Location getRandomLocation() {
        if (locations.isEmpty())
            return null;
        
        int i = ThreadLocalRandom.current().nextInt(locations.size());
        return locations.get(i);
    }

    /**
     * This method executes a random reward to a Player
     * @param player The Player
     */
    private void executeRandomReward(Player player) {
        int i = ThreadLocalRandom.current().nextInt(rewards.size());
        BonusBlocksPlugin.getInstance().getActionUtil().executeActions(player, rewards.get(i));
    }
    
}
