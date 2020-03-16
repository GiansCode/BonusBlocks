package io.alerium.bonusblocks.playerdata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor @Getter
public class PlayerData {

    private final UUID uuid;
    private int brokenBlocks;

    public void incrementBrokenBlocks(int amount) {
        brokenBlocks += amount;
    }
    
}
