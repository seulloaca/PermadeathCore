package com.permadeathcore.CustomMobs.v1_16_R2;

import net.minecraft.server.v1_16_R2.EntityGhast;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;

public class CustomGhast extends EntityGhast {

    public CustomGhast(EntityTypes<? extends EntityGhast> type, World world) {
        super(type, world);
    }
}