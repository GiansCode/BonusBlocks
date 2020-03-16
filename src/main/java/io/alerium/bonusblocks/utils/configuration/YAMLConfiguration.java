package io.alerium.bonusblocks.utils.configuration;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class YAMLConfiguration {

    private final Plugin plugin;
    private final File file;

    @Getter private FileConfiguration config;

    public YAMLConfiguration(Plugin plugin, String name) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), name + ".yml");
        
        if (!file.exists()) {
            plugin.saveResource(file.getName(), false);
        }
        
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Reloads all the configuration from the file
     */
    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Saves all changes to the file
     */
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while ");
        }
    }

    /**
     * This method gets a Location from the YAMLConfiguration file
     * @param path The path where the Location is
     * @return The Location, can be null
     */
    public Location getLocation(String path) {
        return LocationSerialize.deserialize(config.getString(path));
    }

    /**
     * This method sets a Location in the YAMLConfiguration file
     * @param path The path where set the Location
     * @param location The Location
     */
    public void setLocation(String path, Location location) {
        config.set(path, LocationSerialize.serialize(location));
    }

    /**
     * This method gets a List of Location(s) from the YAMLConfiguration file
     * @param path The path where the List of Location(s) is
     * @return The List of Location(s)
     */
    public List<Location> getLocationList(String path) {
        List<Location> locations = new ArrayList<>();
        config.getStringList(path).forEach(s -> locations.add(LocationSerialize.deserialize(s)));
        return locations;
    }

    /**
     * This method sets a List of Location(s) in the YAMLConfiguration file
     * @param path The path where set the List of Location(s)
     * @param locations The List of Location(s)
     */
    public void setLocationList(String path, List<Location> locations) {
        List<String> strings = new ArrayList<>();
        locations.forEach(loc -> strings.add(LocationSerialize.serialize(loc)));
        config.set(path, strings);
    }
    
    /**
     * This methods creates an instance of Message and returns it based on the config file
     * @param path The path where to check the Message data
     * @return The Message
     */
    public Message getMessage(String path) {
        List<String> msg = config.getStringList(path);
        if (msg == null || msg.isEmpty()) {
            msg = new ArrayList<>();
            msg.add(config.getString(path));
        }

        return new Message(msg);
    }
    
}
