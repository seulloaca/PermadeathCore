package com.permadeathcore.CustomMobs.v1_16_R1;

import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;

public class CustomCod extends EntityCod {

    public CustomCod(Location loc) {
        super(EntityTypes.COD, ((CraftWorld) loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(0, new PathfinderGoalMeleeAttack(this, 1.0D, true));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
    }
}