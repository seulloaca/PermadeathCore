package com.permadeathcore.Util.Manager.Data;

import com.mysql.fabric.xmlrpc.base.Array;
import com.permadeathcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Location;

public class BeginningDataManager {

    private File begginingFile;
    private FileConfiguration config;

    private Main instance;

    public BeginningDataManager(Main instance) {
        this.instance = instance;

        this.begginingFile = new File(instance.getDataFolder(), "theBeginning.yml");
        this.config = YamlConfiguration.loadConfiguration(begginingFile);

        if (!begginingFile.exists()) {

            try {
                begginingFile.createNewFile();
            } catch (IOException e) {
                System.out.println("[ERROR] Ha ocurrido un error al crear el archivo 'theBeginning.yml'");
            }
        }

        if (!config.contains("GeneratedOverWorldBeginningPortal")) {

            config.set("GeneratedOverWorldBeginningPortal", false);
        }

        if (!config.contains("GeneratedBeginningPortal")) {

            config.set("GeneratedBeginningPortal", false);
        }

        if (!config.contains("OverWorldPortal")) {

            config.set("OverWorldPortal", "");
        }

        if (!config.contains("BeginningPortal")) {

            config.set("BeginningPortal", "");
        }

        if (!config.contains("KilledED")) {

            config.set("KilledED", false);
        }

        if (!config.contains("PopulatedChests")) {

            config.set("PopulatedChests", new ArrayList<>());
        }

        saveFile();
        reloadFile();
    }

    public boolean hasPopulatedChest(Location l) {

        String s = locationToString(l);

        return config.getStringList("PopulatedChests").contains(s);
    }

    public void addPopulatedChest(Location l) {

        ArrayList<String> chests = (ArrayList<String>) config.getStringList("PopulatedChests");

        chests.add(locationToString(l));

        config.set("PopulatedChests", chests);
        saveFile();
        reloadFile();
    }

    public boolean generatedOverWorldBeginningPortal() {

        return config.getBoolean("GeneratedOverWorldBeginningPortal");
    }

    public boolean generatedBeginningPortal() {

        return config.getBoolean("GeneratedBeginningPortal");
    }

    public Location getBeginningPortal() {

        if (!generatedBeginningPortal()) {

            return null;
        }

        return buildLocation(config.getString("BeginningPortal"));
    }

    public void setBeginningPortal(Location loc) {

        if (generatedBeginningPortal()) {

            return;
        }

        config.set("GeneratedBeginningPortal", true);
        config.set("BeginningPortal", locationToString(loc));

        saveFile();
        reloadFile();
    }

    public Location getOverWorldPortal() {

        if (!generatedOverWorldBeginningPortal()) {

            return null;
        }

        return buildLocation(config.getString("OverWorldPortal"));
    }

    public void setOverWorldPortal(Location loc) {

        if (generatedOverWorldBeginningPortal()) {

            return;
        }

        config.set("GeneratedOverWorldBeginningPortal", true);
        config.set("OverWorldPortal", locationToString(loc));

        saveFile();
        reloadFile();
    }

    public boolean killedED() {

        return config.getBoolean("KilledED");
    }

    public void setKilledED() {

        config.set("KilledED", true);

        saveFile();
        reloadFile();
    }

    public static Location buildLocation(String s) {

        // X;Y;Z;WORLD
        String[] split = s.split(";");

        Double x = Double.valueOf(split[0]);
        Double y = Double.valueOf(split[1]);
        Double z = Double.valueOf(split[2]);
        World w = Bukkit.getWorld(split[3]);

        return new Location(w, x, y, z);
    }

    public static String locationToString(Location loc) {
        return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getWorld().getName();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveFile() {

        try {
            config.save(begginingFile);
        } catch (IOException e) {
            System.out.println("[ERROR] Ha ocurrido un error al guardar el archivo 'players.yml'");
        }
    }

    public void reloadFile() {

        try {
            config.load(begginingFile);
        } catch (IOException e) {
            System.out.println("[ERROR] Ha ocurrido un error al guardar el archivo 'players.yml'");
        } catch (InvalidConfigurationException e) {
            System.out.println("[ERROR] Ha ocurrido un error al guardar el archivo 'players.yml'");
        }
    }
}
