package com.permadeathcore.TheBeginning.Block;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;

public interface CustomBlock {

    Location blockFaceToLocation(Block b, BlockFace face);
    void placeCustomBlock(Location loc);
    void checkForBreak(BlockBreakEvent e);
    boolean isInfernalNetherite(Location l);
}