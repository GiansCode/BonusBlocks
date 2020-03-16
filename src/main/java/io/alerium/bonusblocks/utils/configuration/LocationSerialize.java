package io.alerium.bonusblocks.utils.configuration;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class LocationSerialize {

    private LocationSerialize() {
    }

    /**
     * This method is used to serialize a Location in a String
     * @param loc The location
     * @return The string
     */
    public static String serialize(Location loc) {
        if(loc == null)
            return null;

        return loc.getWorld().getName() + ":" +
                loc.getX() + ":" +
                loc.getY() + ":" +
                loc.getZ() + ":" +
                loc.getYaw() + ":" +
                loc.getPitch();
    }

    /**
     * This method is used to deserialize a String in a Location
     * @param s The string
     * @return The location
     */
    public static Location deserialize(String s) {
        if(s == null || s.isEmpty())
            return null;

        String[] splitted = s.split(":");

        if(splitted.length < 6)
            return null;

        return new Location(
                Bukkit.getWorld(splitted[0]),
                Double.parseDouble(splitted[1]),
                Double.parseDouble(splitted[2]),
                Double.parseDouble(splitted[3]),
                Float.parseFloat(splitted[4]),
                Float.parseFloat(splitted[5])
        );
    }
    
}
