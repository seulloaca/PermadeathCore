package com.permadeathcore.NMS;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public interface NMSHandler {

    EntityType convertEntityType(Object ogType);
    Class getEntityTypesClass();
    Class getNMSClass(String name);
    Object convertBukkitToNMS(EntityType ogType);
    Object craftEntity(String path);
    void sendActionBar(Player player, String actionbar);
    void sendTitle(Player player, String title, String subtitle, int time1, int time2, int time3);
    Object craftWorld(World world);
    Entity spawnNMSEntity(String className, EntityType type, org.bukkit.Location location, CreatureSpawnEvent.SpawnReason reason);
    Entity spawnNMSWither(org.bukkit.Location location);
    Entity spawnHostileMob(EntityType type, org.bukkit.Location location, CreatureSpawnEvent.SpawnReason reason);
    Entity spawnNMSCustomEntity(String classPath, EntityType type, org.bukkit.Location location, CreatureSpawnEvent.SpawnReason reason);
    Entity spawnCustomCreeper(org.bukkit.Location location, CreatureSpawnEvent.SpawnReason reason, boolean ender);
    Entity spawnCustomGhast(org.bukkit.Location location, CreatureSpawnEvent.SpawnReason reason, boolean isEnder);
    void addMushrooms();
}