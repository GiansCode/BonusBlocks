package io.alerium.bonusblocks.blocks;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.blocks.tasks.BlockSpawnTask;
import io.alerium.bonusblocks.blocks.tasks.ParticleTask;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import sh.okx.timeapi.api.TimeAPI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BonusBlockManager {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    @Getter private final List<BonusBlock> blocks = new ArrayList<>();
    
    @Getter private int minTime;
    @Getter private int maxTime;
    @Getter private int amount;

    /**
     * This method enables the BonusBlockManager
     */
    public void enable() {
        minTime = (int) new TimeAPI(plugin.getConfiguration().getConfig().getString("change-block-range.min")).getMilliseconds();
        maxTime = (int) new TimeAPI(plugin.getConfiguration().getConfig().getString("change-block-range.max")).getMilliseconds();
        amount = plugin.getConfiguration().getConfig().getInt("change-block-range.amount");
        
        loadBlocks();

        new ParticleTask().runTaskTimer(plugin, 10, 10);
        new BlockSpawnTask().runTaskTimer(plugin, 20*60, 20*60);
    }

    /**
     * This method despawns all the BonusBlock(s)
     */
    public void despawnAll() {
        for (BonusBlock block : blocks)
            block.despawnBlock();
    }

    /**
     * This method gets a BonusBlock by his Location
     * @param location The Location
     * @return The BonusBlock, null if not found
     */
    public BonusBlock getBonusBlock(Location location) {
        for (BonusBlock block : blocks) {
            if (block.getLocations().contains(location))
                return block;
        }
        
        return null;
    }

    /**
     * This method gets a BonusBlock by his Material
     * @param material The Material of the BonusBlock
     * @return The BonusBlock, null if not found
     */
    public BonusBlock getBonusBlock(Material material) {
        for (BonusBlock block : blocks) {
            if (block.getMaterial() == material)
                return block;
        }
        
        return null;
    }

    /**
     * This method gets the BonusBlock by his spawned Location
     * @param location The Location
     * @return The BonusBlock, null if not found
     */
    public BonusBlock getBonusBlockBySpawned(Location location) {
        for (BonusBlock block : blocks) {
            if (block.getSpawnedLocation() == null)
                continue;
            
            if (block.getSpawnedLocation().equals(location))
                return block;
        }
        
        return null;
    }

    /**
     * This method loads all the BonusBlock(s) from the config
     */
    private void loadBlocks() {
        ConfigurationSection blocksSection = plugin.getConfiguration().getConfig().getConfigurationSection("bonus-blocks");
        for (String materialName : blocksSection.getKeys(false)) {
            Optional<Material> material = Enums.getIfPresent(Material.class, materialName);
            if (!material.isPresent()) {
                plugin.getLogger().warning(materialName + " material is not a valid material, skipping this block.");
                continue;
            }

            ConfigurationSection section = blocksSection.getConfigurationSection(materialName);
            
            Optional<Material> inactiveMaterial = Enums.getIfPresent(Material.class, section.getString("inactiveMaterial"));
            if (!inactiveMaterial.isPresent()) {
                plugin.getLogger().warning(materialName + " block was skipped because the inactiveMaterial is not valid.");
                continue;
            }
            
            int spawnChange = section.getInt("change");
            int minRewards = section.getInt("min-rewards");
            int maxRewards = section.getInt("max-rewards");
            Optional<Particle> particle = Enums.getIfPresent(Particle.class, section.getString("particle"));
            List<String> hologram = section.getStringList("hologram");
            List<String> rewards = section.getStringList("rewards");
            List<Location> locations = plugin.getBlocksConfig().getLocationList(materialName);

            blocks.add(new BonusBlock(material.get(), inactiveMaterial.get(), spawnChange, minRewards, maxRewards, particle.orNull(), hologram, rewards, locations));
            plugin.getLogger().info("Loaded " + locations.size() + " bonus blocks of " + materialName + " type.");
        }
        
        blocks.sort(Comparator.comparingInt(BonusBlock::getSpawnChange));
        plugin.getLogger().info("Loaded " + blocks.size() + " bonus block types.");
    }
    
}
