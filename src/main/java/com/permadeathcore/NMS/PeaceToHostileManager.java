package com.permadeathcore.NMS;

import com.permadeathcore.Main;
import com.permadeathcore.NMS.VersionManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class PeaceToHostileManager implements Listener {

    private Main instance;
    private ArrayList<Entity> peaceMobs = new ArrayList<>();

    public PeaceToHostileManager(Main instance) {
        this.instance = instance;

        initialize();
    }

    public void initialize() {

        if (instance.getDays() >= 20 && !VersionManager.isRunningNetherUpdate_v2()) {

            for (World w : Bukkit.getWorlds()) {

                for (Entity entity : w.getEntities()) {

                    EntityType type = entity.getType();

                    if (!isHostileMob(type) && entity instanceof LivingEntity) {

                        if (type == EntityType.ENDERMAN || type == EntityType.WITHER || type == EntityType.ENDER_DRAGON) {
                            return;
                        }

                        if (type == EntityType.DOLPHIN || type == EntityType.FOX || type == EntityType.WOLF || type == EntityType.CAT || type == EntityType.OCELOT || type == EntityType.PANDA
                                || type == EntityType.POLAR_BEAR || type == EntityType.SNOWMAN) {

                            instance.getNmsAccesor().injectHostilePathfinders(entity);

                            return;
                        }

                        instance.getNmsAccesor().injectHostilePathfinders(entity);
                        instance.getNmsAccesor().registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 8.0D, entity);



                    }
                }
            }
        }
    }
    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (e.isCancelled() || VersionManager.isRunningNetherUpdate_v2()) return;

        if (instance.getDays() >= 20) {

            //spawnEntity(e.getEntity(), e.getLocation(), e);

            LivingEntity entity = e.getEntity();

            if (entity instanceof LivingEntity) {

                if (entity instanceof Player) return;

                if (!isHostileMob(e.getEntityType())) {

                    EntityType type = e.getEntityType();

                    if (type == EntityType.ENDERMAN || type == EntityType.WITHER || type == EntityType.ENDER_DRAGON) {
                        return;
                    }

                    if (type == EntityType.DOLPHIN || type == EntityType.FOX || type == EntityType.WOLF || type == EntityType.CAT || type == EntityType.OCELOT || type == EntityType.PANDA
                            || type == EntityType.POLAR_BEAR|| type == EntityType.SNOWMAN) {

                        instance.getNmsAccesor().injectHostilePathfinders(entity);

                        return;
                    }

                    instance.getNmsAccesor().injectHostilePathfinders(entity);
                    instance.getNmsAccesor().registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 8.0D, entity);

                }
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {

        if (instance.getDays() < 20 || VersionManager.isRunningNetherUpdate_v2()) return;
        if (e.isNewChunk()) return;

        if (e.getChunk().getEntities().length < 1) return;
        for (Entity entity : e.getChunk().getEntities()) {

            boolean isNull = false;

            if (entity == null) {

                isNull = true;
                return;
            }

            if (!entity.isValid() || entity.isDead()) {

                isNull = true;
            }

            if (entity instanceof Villager && instance.getDays() >= 60) {
                org.bukkit.Location savedLocation = entity.getLocation();
                entity.remove();
                savedLocation.getWorld().spawn(savedLocation, Vindicator.class);
                return;
            }

            if (entity instanceof LivingEntity && !isNull) {

                if (entity instanceof Player) return;

                EntityType type = entity.getType();

                if (!isHostileMob(type)) {

                    if (type == EntityType.ENDERMAN || type == EntityType.WITHER || type == EntityType.ENDER_DRAGON) {
                        return;
                    }

                    if (type == EntityType.DOLPHIN || type == EntityType.FOX || type == EntityType.WOLF || type == EntityType.CAT || type == EntityType.OCELOT || type == EntityType.PANDA
                            || type == EntityType.POLAR_BEAR|| type == EntityType.SNOWMAN) {

                        instance.getNmsAccesor().injectHostilePathfinders(entity);

                        return;
                    }

                    instance.getNmsAccesor().injectHostilePathfinders(entity);
                    instance.getNmsAccesor().registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 8.0D, entity);

                }
            }
        }
    }

    public boolean isHostileMob(EntityType type) {
        if (type == EntityType.ENDER_DRAGON || type == EntityType.WITHER || type == EntityType.BLAZE ||type == EntityType.CREEPER ||type == EntityType.GHAST ||type == EntityType.MAGMA_CUBE ||type == EntityType.SILVERFISH ||type == EntityType.SKELETON ||type == EntityType.SLIME ||type == EntityType.ZOMBIE ||type == EntityType.ZOMBIE_VILLAGER ||type == EntityType.DROWNED ||type == EntityType.WITHER_SKELETON ||type == EntityType.WITCH ||type == EntityType.PILLAGER ||type == EntityType.EVOKER ||type == EntityType.VINDICATOR ||type == EntityType.RAVAGER ||type == EntityType.VEX ||type == EntityType.GUARDIAN ||type == EntityType.ELDER_GUARDIAN ||type == EntityType.SHULKER ||type == EntityType.HUSK ||type == EntityType.STRAY ||type == EntityType.PHANTOM) {
            return true;
        } else {
            return false;
        }
    }
}
