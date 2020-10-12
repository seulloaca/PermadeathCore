package com.permadeathcore.NMS.Versions.NMSHandler;

import com.permadeathcore.Main;
import com.permadeathcore.CustomMobs.v1_14_R1.CustomCreeper;
import com.permadeathcore.CustomMobs.v1_14_R1.CustomGhast;
import com.permadeathcore.NMS.NMSHandler;
import com.permadeathcore.NMS.VersionManager;
import net.minecraft.server.v1_14_R1.*;
import net.minecraft.server.v1_14_R1.Entity;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NMSHandler_1_14_R1 implements NMSHandler {

    @Override
    public Class craftEntity(String path) {

        Class c = null;

        try {
            c = Class.forName("org.bukkit.craftbukkit.v1_14_R1.entity." + path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return c;
    }

    @Override
    public EntityType convertEntityType(Object ogType) {

        EntityTypes type = (EntityTypes) ogType;

        if (type != null) {

            if (type == EntityTypes.IRON_GOLEM) {

                return EntityType.IRON_GOLEM;
            }

            if (type == EntityTypes.SNOW_GOLEM) {

                return EntityType.SNOWMAN;
            }

            if (type == EntityTypes.WITHER) {

                return EntityType.WITHER;
            }

            if (type == EntityTypes.CHICKEN) {

                return EntityType.CHICKEN;
            }

            if (type == EntityTypes.COW) {

                return EntityType.COW;
            }
            if (type == EntityTypes.MOOSHROOM) {

                return EntityType.MUSHROOM_COW;
            }
            if (type == EntityTypes.PIG) {

                return EntityType.PIG;
            }
            if (type == EntityTypes.SHEEP) {

                return EntityType.SHEEP;
            }
            if (type == EntityTypes.SQUID) {

                return EntityType.SQUID;
            }
            if (type == EntityTypes.VILLAGER) {

                return EntityType.VILLAGER;
            }
            if (type == EntityTypes.WANDERING_TRADER) {

                return EntityType.WANDERING_TRADER;
            }
            if (type == EntityTypes.BAT) {

                return EntityType.BAT;
            }
            if (type == EntityTypes.OCELOT) {

                return EntityType.OCELOT;
            }
            if (type == EntityTypes.CAT) {

                return EntityType.CAT;
            }

            if (type == EntityTypes.DONKEY) {

                return EntityType.DONKEY;
            }

            if (type == EntityTypes.HORSE) {

                return EntityType.HORSE;
            }

            if (type == EntityTypes.MULE) {

                return EntityType.MULE;
            }

            if (type == EntityTypes.SKELETON_HORSE) {

                return EntityType.SKELETON_HORSE;
            }

            if (type == EntityTypes.ZOMBIE_HORSE) {

                return EntityType.ZOMBIE_HORSE;
            }

            if (type == EntityTypes.WOLF) {

                return EntityType.WOLF;
            }

            if (type == EntityTypes.FOX) {

                return EntityType.FOX;
            }

            if (type == EntityTypes.RABBIT) {

                return EntityType.RABBIT;
            }

            if (type == EntityTypes.PARROT) {

                return EntityType.PARROT;
            }

            if (type == EntityTypes.TURTLE) {

                return EntityType.TURTLE;
            }

            if (type == EntityTypes.COD) {

                return EntityType.COD;
            }

            if (type == EntityTypes.SALMON) {

                return EntityType.SALMON;
            }

            if (type == EntityTypes.PUFFERFISH) {

                return EntityType.PUFFERFISH;
            }

            if (type == EntityTypes.TROPICAL_FISH) {

                return EntityType.TROPICAL_FISH;
            }

            if (type == EntityTypes.ZOMBIE) {

                return EntityType.ZOMBIE;
            }

            if (type == EntityTypes.ENDERMAN) {

                return EntityType.ENDERMAN;
            }

            if (type == EntityTypes.DOLPHIN) {

                return EntityType.DOLPHIN;
            }

            if (type == EntityTypes.SPIDER) {

                return EntityType.SPIDER;
            }
            if (type == EntityTypes.CAVE_SPIDER) {

                return EntityType.CAVE_SPIDER;
            }
            if (type == EntityTypes.POLAR_BEAR) {

                return EntityType.POLAR_BEAR;
            }
            if (type == EntityTypes.LLAMA) {

                return EntityType.LLAMA;
            }
            if (type == EntityTypes.PANDA) {

                return EntityType.PANDA;
            }
            if (type == EntityTypes.BLAZE) {

                return EntityType.BLAZE;
            }
            if (type == EntityTypes.CREEPER) {

                return EntityType.CREEPER;
            }
            if (type == EntityTypes.GHAST) {

                return EntityType.GHAST;
            }
            if (type == EntityTypes.MAGMA_CUBE) {

                return EntityType.MAGMA_CUBE;
            }
            if (type == EntityTypes.SILVERFISH) {

                return EntityType.SILVERFISH;
            }
            if (type == EntityTypes.SKELETON) {

                return EntityType.SKELETON;
            }
            if (type == EntityTypes.SLIME) {

                return EntityType.SLIME;
            }
            if (type == EntityTypes.ZOMBIE_VILLAGER) {

                return EntityType.ZOMBIE_VILLAGER;
            }
            if (type == EntityTypes.DROWNED) {

                return EntityType.DROWNED;
            }
            if (type == EntityTypes.WITHER_SKELETON) {

                return EntityType.WITHER_SKELETON;
            }
            if (type == EntityTypes.VINDICATOR) {

                return EntityType.VINDICATOR;
            }
            if (type == EntityTypes.EVOKER) {

                return EntityType.EVOKER;
            }

            if (type == EntityTypes.PILLAGER) {

                return EntityType.PILLAGER;
            }

            if (type == EntityTypes.RAVAGER) {

                return EntityType.RAVAGER;
            }

            if (type == EntityTypes.WITCH) {

                return EntityType.WITCH;
            }

            if (type == EntityTypes.VEX) {

                return EntityType.VEX;
            }

            if (type == EntityTypes.ENDERMITE) {

                return EntityType.ENDERMITE;
            }

            if (type == EntityTypes.GUARDIAN) {

                return EntityType.GUARDIAN;
            }

            if (type == EntityTypes.ELDER_GUARDIAN) {

                return EntityType.ELDER_GUARDIAN;
            }

            if (type == EntityTypes.SHULKER) {

                return EntityType.SHULKER;
            }

            if (type == EntityTypes.HUSK) {

                return EntityType.HUSK;
            }

            if (type == EntityTypes.STRAY) {

                return EntityType.STRAY;
            }

            if (type == EntityTypes.PHANTOM) {

                return EntityType.PHANTOM;
            }
        }
        return null;
    }

    @Override
    public Class getEntityTypesClass() {

        Class c = null;

        try {
            c = Class.forName("net.minecraft.server.v1_14_R1.EntityTypes");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return c;
    }

    @Override
    public Class getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server.v1_14_R1." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EntityTypes convertBukkitToNMS(EntityType type) {

        if (type == EntityType.IRON_GOLEM) {

            return EntityTypes.IRON_GOLEM;
        }

        if (type == EntityType.SNOWMAN) {

            return EntityTypes.SNOW_GOLEM;
        }

        if (type == EntityType.WITHER) {

            return EntityTypes.WITHER;
        }

        if (type == EntityType.CHICKEN) {

            return EntityTypes.CHICKEN;
        }

        if (type == EntityType.COW) {

            return EntityTypes.COW;
        }
        if (type == EntityType.MUSHROOM_COW) {

            return EntityTypes.MOOSHROOM;
        }
        if (type == EntityType.PIG) {

            return EntityTypes.PIG;
        }
        if (type == EntityType.SHEEP) {

            return EntityTypes.SHEEP;
        }
        if (type == EntityType.SQUID) {

            return EntityTypes.SQUID;
        }
        if (type == EntityType.VILLAGER) {

            return EntityTypes.VILLAGER;
        }
        if (type == EntityType.WANDERING_TRADER) {

            return EntityTypes.WANDERING_TRADER;
        }
        if (type == EntityType.BAT) {

            return EntityTypes.BAT;
        }
        if (type == EntityType.OCELOT) {

            return EntityTypes.OCELOT;
        }
        if (type == EntityType.CAT) {

            return EntityTypes.CAT;
        }

        if (type == EntityType.DONKEY) {

            return EntityTypes.DONKEY;
        }

        if (type == EntityType.HORSE) {

            return EntityTypes.HORSE;
        }

        if (type == EntityType.MULE) {

            return EntityTypes.MULE;
        }

        if (type == EntityType.SKELETON_HORSE) {

            return EntityTypes.SKELETON_HORSE;
        }

        if (type == EntityType.ZOMBIE_HORSE) {

            return EntityTypes.ZOMBIE_HORSE;
        }

        if (type == EntityType.WOLF) {

            return EntityTypes.WOLF;
        }

        if (type == EntityType.FOX) {

            return EntityTypes.FOX;
        }

        if (type == EntityType.RABBIT) {

            return EntityTypes.RABBIT;
        }

        if (type == EntityType.PARROT) {

            return EntityTypes.PARROT;
        }

        if (type == EntityType.TURTLE) {

            return EntityTypes.TURTLE;
        }

        if (type == EntityType.COD) {

            return EntityTypes.COD;
        }

        if (type == EntityType.SALMON) {

            return EntityTypes.SALMON;
        }

        if (type == EntityType.PUFFERFISH) {

            return EntityTypes.PUFFERFISH;
        }

        if (type == EntityType.TROPICAL_FISH) {

            return EntityTypes.TROPICAL_FISH;
        }

        if (type == EntityType.ZOMBIE) {

            return EntityTypes.ZOMBIE;
        }

        if (type == EntityType.ENDERMAN) {

            return EntityTypes.ENDERMAN;
        }

        if (type == EntityType.DOLPHIN) {

            return EntityTypes.DOLPHIN;
        }

        if (type == EntityType.SPIDER) {

            return EntityTypes.SPIDER;
        }
        if (type == EntityType.CAVE_SPIDER) {

            return EntityTypes.CAVE_SPIDER;
        }
        if (type == EntityType.POLAR_BEAR) {

            return EntityTypes.POLAR_BEAR;
        }
        if (type == EntityType.LLAMA) {

            return EntityTypes.LLAMA;
        }
        if (type == EntityType.PANDA) {

            return EntityTypes.PANDA;
        }
        if (type == EntityType.BLAZE) {

            return EntityTypes.BLAZE;
        }
        if (type == EntityType.CREEPER) {

            return EntityTypes.CREEPER;
        }
        if (type == EntityType.GHAST) {

            return EntityTypes.GHAST;
        }
        if (type == EntityType.MAGMA_CUBE) {

            return EntityTypes.MAGMA_CUBE;
        }
        if (type == EntityType.SILVERFISH) {

            return EntityTypes.SILVERFISH;
        }
        if (type == EntityType.SKELETON) {

            return EntityTypes.SKELETON;
        }
        if (type == EntityType.SLIME) {

            return EntityTypes.SLIME;
        }
        if (type == EntityType.ZOMBIE_VILLAGER) {

            return EntityTypes.ZOMBIE_VILLAGER;
        }
        if (type == EntityType.DROWNED) {

            return EntityTypes.DROWNED;
        }
        if (type == EntityType.WITHER_SKELETON) {

            return EntityTypes.WITHER_SKELETON;
        }
        if (type == EntityType.VINDICATOR) {

            return EntityTypes.VINDICATOR;
        }
        if (type == EntityType.EVOKER) {

            return EntityTypes.EVOKER;
        }

        if (type == EntityType.PILLAGER) {

            return EntityTypes.PILLAGER;
        }

        if (type == EntityType.RAVAGER) {

            return EntityTypes.RAVAGER;
        }

        if (type == EntityType.WITCH) {

            return EntityTypes.WITCH;
        }

        if (type == EntityType.VEX) {

            return EntityTypes.VEX;
        }

        if (type == EntityType.ENDERMITE) {

            return EntityTypes.ENDERMITE;
        }

        if (type == EntityType.GUARDIAN) {

            return EntityTypes.GUARDIAN;
        }

        if (type == EntityType.ELDER_GUARDIAN) {

            return EntityTypes.ELDER_GUARDIAN;
        }

        if (type == EntityType.SHULKER) {

            return EntityTypes.SHULKER;
        }

        if (type == EntityType.HUSK) {

            return EntityTypes.HUSK;
        }

        if (type == EntityType.STRAY) {

            return EntityTypes.STRAY;
        }

        if (type == EntityType.PHANTOM) {

            return EntityTypes.PHANTOM;
        }
        return null;
    }

    @Override
    public void sendActionBar(Player player, String actionbar) {

        IChatBaseComponent chat = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + actionbar + "\"}");
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(chat, ChatMessageType.GAME_INFO);
        CraftPlayer craft = (CraftPlayer) player;
        craft.getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int time1, int time2, int time3) {

        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        IChatBaseComponent chatSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
        PacketPlayOutTitle titleT = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle subtitleT = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(time1, time2, time3);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titleT);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitleT);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    @Override
    public org.bukkit.entity.Entity spawnNMSEntity(String name, EntityType type, Location location, CreatureSpawnEvent.SpawnReason reason) {

        World nmsW = ((CraftWorld) location.getWorld()).getHandle();
        Entity nmsEntity = null;

        try {

            if (name.toLowerCase().contains("bee") && VersionManager.getVersion().equalsIgnoreCase("1_14_R1")) {

                return null;
            }

            Class c = getNMSClass("Entity" + name);

            if (name.toLowerCase().contains("bat") || name.toLowerCase().contains("cod") || name.toLowerCase().contains("salmon") || name.toLowerCase().contains("squid") || name.toLowerCase().contains("pufferfish") || name.toLowerCase().contains("tropicalfish")) {

                if (reason == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {

                    if (new Random().nextInt(499) <= 10) {

                        nmsEntity = (Entity) c.getConstructor(getNMSClass("EntityTypes"), getNMSClass("World")).newInstance(convertBukkitToNMS(type), nmsW);
                    }
                } else {

                    nmsEntity = (Entity) c.getConstructor(getNMSClass("EntityTypes"), getNMSClass("World")).newInstance(convertBukkitToNMS(type), nmsW);
                }

            } else {

                nmsEntity = (Entity) c.getConstructor(getNMSClass("EntityTypes"), getNMSClass("World")).newInstance(convertBukkitToNMS(type), nmsW);
            }

        } catch (NoSuchMethodException ex) {
        } catch (IllegalAccessException ex) {
        } catch (InstantiationException ex) {
        } catch (InvocationTargetException ex) {

        }

        if (nmsEntity == null) return null;

        nmsEntity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsW.addEntity(nmsEntity, reason);

        return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
    }

    @Override
    public org.bukkit.entity.Entity spawnNMSWither(Location location) {

        net.minecraft.server.v1_14_R1.World nmsW = ((org.bukkit.craftbukkit.v1_14_R1.CraftWorld) location.getWorld()).getHandle();

        net.minecraft.server.v1_14_R1.NBTTagCompound compound = new net.minecraft.server.v1_14_R1.NBTTagCompound();

        compound.setInt("Invul", 20*8);

        net.minecraft.server.v1_14_R1.EntityWither wither = net.minecraft.server.v1_14_R1.EntityTypes.WITHER.spawnCreature(nmsW, compound, null, null,
                new net.minecraft.server.v1_14_R1.BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()),
                EnumMobSpawn.EVENT, true, false);


        return wither == null ? null : wither.getBukkitEntity();
    }

    @Override
    public org.bukkit.entity.Entity spawnHostileMob(EntityType type, Location location, CreatureSpawnEvent.SpawnReason reason) {

        String name;
        String s = type.toString().toLowerCase();
        Entity nms = null;

        if (s.contains("_")) {

            String[] split = s.split("_");

            String wordOne = split[0].toLowerCase();
            String wordTwo = split[1].toLowerCase();

            name = StringUtils.capitalize(wordOne) + StringUtils.capitalize(wordTwo);
        } else {

            name = StringUtils.capitalize(s);
        }

        try {

            if (name.toLowerCase().contains("bee") && VersionManager.getVersion().equalsIgnoreCase("1_14_R1")) {
                return null;
            }

            if (name.toLowerCase().contains("cod") && Main.getInstance().getDays() >= 50) {
                return null;
            }

            Class c = Class.forName("com.permadeathcore.NMS.EntityRegistry.Entities.v" + VersionManager.getVersion() + ".Hostile" + name);

            if (name.toLowerCase().contains("villager") || name.toLowerCase().contains("wanderingtrader")) {

                if (name.toLowerCase().contains("villager")) {

                    nms = (Entity) c.getConstructor(Location.class, Main.getInstance().getNmsHandler().getNMSClass("EntityTypes"), Villager.class).newInstance(location, Main.getInstance().getNmsHandler().convertBukkitToNMS(type), null);
                } else {

                    nms = (Entity) c.getConstructor(Location.class, Main.getInstance().getNmsHandler().getNMSClass("EntityTypes"), WanderingTrader.class).newInstance(location, Main.getInstance().getNmsHandler().convertBukkitToNMS(type), null);
                }

            } else {

                if (name.toLowerCase().contains("bat") || name.toLowerCase().contains("cod") || name.toLowerCase().contains("salmon") || name.toLowerCase().contains("squid") || name.toLowerCase().contains("pufferfish") || name.toLowerCase().contains("tropicalfish")) {

                    if (new Random().nextInt(499) <= 10) {

                        nms = (Entity) c.getConstructor(Location.class, Main.getInstance().getNmsHandler().getNMSClass("EntityTypes")).newInstance(location, Main.getInstance().getNmsHandler().convertBukkitToNMS(type));
                    }
                } else {

                    nms = (Entity) c.getConstructor(Location.class, Main.getInstance().getNmsHandler().getNMSClass("EntityTypes")).newInstance(location, Main.getInstance().getNmsHandler().convertBukkitToNMS(type));
                }
            }

        } catch (ClassNotFoundException | NoSuchMethodException ex) {
        } catch (IllegalAccessException ex) {
        } catch (InstantiationException ex) {
        } catch (InvocationTargetException ex) {
        }

        return nms == null ? null : nms.getBukkitEntity();
    }

    @Override
    public org.bukkit.entity.Entity spawnNMSCustomEntity(String name, EntityType type, Location location, CreatureSpawnEvent.SpawnReason reason) {

        World nmsW = ((CraftWorld) location.getWorld()).getHandle();
        Entity nmsEntity = null;

        try {

            if (name.toLowerCase().contains("bee") && VersionManager.getVersion().equalsIgnoreCase("1_14_R1")) {

                return null;
            }

            Class c = Class.forName("com.permadeathcore.CustomMobs.v" + VersionManager.getVersion() + "." + name);
            nmsEntity = (Entity) c.getConstructor(Location.class).newInstance(location);

        } catch (NoSuchMethodException | ClassNotFoundException ex) {
        } catch (IllegalAccessException ex) {
        } catch (InstantiationException ex) {
        } catch (InvocationTargetException ex) {
        }

        if (nmsEntity == null) return null;

        nmsEntity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsW.addEntity(nmsEntity, reason);

        return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
    }

    @Override
    public org.bukkit.entity.Entity spawnCustomCreeper(Location location, CreatureSpawnEvent.SpawnReason reason, boolean is) {

        World nmsW = ((CraftWorld) location.getWorld()).getHandle();
        CustomCreeper nmsEntity = new CustomCreeper(EntityTypes.CREEPER, nmsW, is);

        nmsEntity.setPosition(location.getX(), location.getY(), location.getZ());
        nmsW.addEntity(nmsEntity, reason);

        return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
    }

    @Override
    public org.bukkit.entity.Entity spawnCustomGhast(Location location, CreatureSpawnEvent.SpawnReason reason, boolean isEnder) {

        World nmsW = ((CraftWorld) location.getWorld()).getHandle();
        CustomGhast nmsEntity = new CustomGhast(EntityTypes.GHAST, nmsW);

        nmsEntity.setPosition(location.getX(), location.getY(), location.getZ());
        nmsW.addEntity(nmsEntity, reason);

        if (isEnder) {

            nmsEntity.getAttributeInstance(net.minecraft.server.v1_14_R1.GenericAttributes.MAX_HEALTH).setValue(200.0D);

            LivingEntity e2 = (LivingEntity) nmsEntity.getBukkitEntity();
            e2.setHealth(200.0D);
            e2.setCustomName("§6Ender Ghast");
            e2.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "ender_ghast"), PersistentDataType.BYTE, (byte) 1);
            e2.setCustomNameVisible(false);
        }

        return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
    }

    @Override
    public void addMushrooms() {

    }

    @Override
    public World craftWorld(org.bukkit.World world) {

        return ((CraftWorld) world).getHandle();
    }
}
