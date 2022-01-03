package com.permadeathcore.CustomMobs.v1_16_R3;

import net.minecraft.server.v1_16_R3.EntityGhast;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

public class CustomGhast extends EntityGhast {

    public CustomGhast(EntityTypes<? extends EntityGhast> type, World world) {
        super(type, world);
    }
}