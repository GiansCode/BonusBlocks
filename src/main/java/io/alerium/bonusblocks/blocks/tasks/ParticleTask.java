package io.alerium.bonusblocks.blocks.tasks;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.blocks.BonusBlock;
import io.alerium.bonusblocks.blocks.BonusBlockManager;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleTask extends BukkitRunnable {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    private final BonusBlockManager bonusBlockManager = plugin.getBonusBlockManager();
    
    @Override
    public void run() {
        for (BonusBlock block : bonusBlockManager.getBlocks()) {
            if (!block.isSpawned())
                continue;
            
            if (block.getParticle() == null)
                continue;

            block.getSpawnedLocation().getWorld().spawnParticle(block.getParticle(), block.getSpawnedLocation(), 5, 0, 0, 0, 0);
        }
    }
    
}
