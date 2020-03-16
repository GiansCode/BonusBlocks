package io.alerium.bonusblocks.playerdata.storage;

import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.playerdata.PlayerData;
import io.alerium.bonusblocks.utils.configuration.YAMLConfiguration;

import java.util.*;
import java.util.stream.Collectors;

public class YAMLStorage implements Storage {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();
    
    private YAMLConfiguration config;
    
    @Override
    public void setup() throws Exception {
        config = new YAMLConfiguration(plugin, "playerdata");
    }

    @Override
    public PlayerData loadData(UUID uuid) throws Exception {
        return new PlayerData(uuid, config.getConfig().getInt(uuid + ".brokenBlocks", 0));
    }

    @Override
    public void saveData(PlayerData playerData) throws Exception {
        config.getConfig().set(playerData.getUuid() + ".brokenBlocks", playerData.getBrokenBlocks());
        config.save();
    }

    @Override
    public List<UUID> getTop() throws Exception {
        List<PlayerData> players = new ArrayList<>();
        for (String uuid : config.getConfig().getKeys(false))
            players.add(new PlayerData(UUID.fromString(uuid), config.getConfig().getInt(uuid + ".brokenBlocks")));
        
        players.sort(Comparator.comparingInt(PlayerData::getBrokenBlocks));
        return players.stream()
                .sorted(Comparator.comparingInt(PlayerData::getBrokenBlocks))
                .limit(10)
                .map(PlayerData::getUuid)
                .collect(Collectors.toList());
    }

}
