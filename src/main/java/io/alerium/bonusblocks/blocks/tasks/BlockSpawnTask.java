package io.alerium.bonusblocks.blocks.tasks;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.blocks.BonusBlock;
import io.alerium.bonusblocks.blocks.BonusBlockManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class BlockSpawnTask extends BukkitRunnable {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    private final BonusBlockManager bonusBlockManager = plugin.getBonusBlockManager();
    
    private long nextChangeTime = 0;
    
    @Override
    public void run() {
        if (System.currentTimeMillis() < nextChangeTime)
            return;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        nextChangeTime = System.currentTimeMillis() + random.nextInt(bonusBlockManager.getMinTime(), bonusBlockManager.getMaxTime());
        for (int i = 0; i < bonusBlockManager.getAmount(); i++) {
            int r = random.nextInt(100);
            for (BonusBlock block : bonusBlockManager.getBlocks()) {
                if (r < block.getSpawnChange()) {
                    if (block.isSpawned())
                        block.despawnBlock();
                    block.spawnBlock();
                }
            }
        }
    }
    
}
