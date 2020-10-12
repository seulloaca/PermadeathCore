package com.permadeathcore.CustomMobs.v1_15_R1;

import com.permadeathcore.Main;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;

public class CustomGhast extends EntityGhast {

    public CustomGhast(EntityTypes<? extends EntityGhast> type, World world) {
        super(type, world);
    }
}