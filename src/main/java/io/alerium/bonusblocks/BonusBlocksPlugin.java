package io.alerium.bonusblocks;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.alerium.bonusblocks.blocks.BonusBlockManager;
import io.alerium.bonusblocks.blocks.listeners.BonusBlockListener;
import io.alerium.bonusblocks.commands.AddBlockCommand;
import io.alerium.bonusblocks.commands.ListCommand;
import io.alerium.bonusblocks.commands.RemoveBlockCommand;
import io.alerium.bonusblocks.integrations.PlaceholderAPI;
import io.alerium.bonusblocks.playerdata.PlayerDataManager;
import io.alerium.bonusblocks.playerdata.listeners.PlayerDataListener;
import io.alerium.bonusblocks.utils.commands.Command;
import io.alerium.bonusblocks.utils.configuration.YAMLConfiguration;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class BonusBlocksPlugin extends JavaPlugin {

    @Getter private static BonusBlocksPlugin instance;
    
    @Getter private YAMLConfiguration configuration;
    @Getter private YAMLConfiguration messages;
    @Getter private YAMLConfiguration blocksConfig;
    
    @Getter private PlayerDataManager playerDataManager;
    @Getter private BonusBlockManager bonusBlockManager;
    
    @Override
    public void onEnable() {
        instance = this;
        long time = System.currentTimeMillis();
        
        registerConfigs();
        registerInstances();
        registerListeners();
        registerCommands();
        registerIntegrations();
        
        getLogger().info("Plugin enabled in " + (System.currentTimeMillis()-time) + "ms.");
    }

    @Override
    public void onDisable() {
        playerDataManager.saveAllPlayers(true);
        bonusBlockManager.despawnAll();
        HologramsAPI.getHolograms(this).forEach(Hologram::delete);
    }
    
    private void registerConfigs() {
        configuration = new YAMLConfiguration(this, "config");
        messages = new YAMLConfiguration(this, "messages");
        blocksConfig = new YAMLConfiguration(this, "blocks");
    }
    
    private void registerInstances() {
        playerDataManager = new PlayerDataManager();
        playerDataManager.enable();

        bonusBlockManager = new BonusBlockManager();
        bonusBlockManager.enable();
    }
 
    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        
        pm.registerEvents(new PlayerDataListener(), this);
        pm.registerEvents(new BonusBlockListener(), this);
    }
    
    private void registerCommands() {
        getCommand("bonusblocks").setExecutor(new Command(messages.getMessage("commands.help").format(), Arrays.asList(new ListCommand(), new AddBlockCommand(), new RemoveBlockCommand())));
    }
    
    private void registerIntegrations() {
        new PlaceholderAPI().register();
    }
    
}
