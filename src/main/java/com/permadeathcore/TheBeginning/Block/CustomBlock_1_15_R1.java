package com.permadeathcore.TheBeginning.Block;

import com.permadeathcore.Util.Item.PermaDeathItems;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import net.minecraft.server.v1_15_R1.TileEntityMobSpawner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;

public class CustomBlock_1_15_R1 implements CustomBlock {

    public Location blockFaceToLocation(Block block, BlockFace face) {
        Location loc = block.getLocation();

        switch (face) {
            case DOWN:
                loc.setY(loc.getY() - 1);
                break;
            case EAST:
                loc.setX(loc.getX() + 1);
                break;
            case NORTH:
                loc.setZ(loc.getZ() - 1);
                break;
            case SOUTH:
                loc.setZ(loc.getZ() + 1);
                break;
            case UP:
                loc.setY(loc.getY() + 1);
                break;
            case WEST:
                loc.setX(loc.getX() - 1);
                break;
            default:
                break;
        }

        return loc;
    }

    public void placeCustomBlock(Location loc) {
        Block o = loc.getBlock();
        o.setType(Material.SPAWNER);

        BlockPosition bp = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        TileEntityMobSpawner spawner = (TileEntityMobSpawner) ((CraftWorld) loc.getWorld()).getHandle()
                .getTileEntity(bp);

        NBTTagCompound tileData = spawner.b();
        NBTTagCompound spawnData = new NBTTagCompound();
        NBTTagList armor = new NBTTagList();
        NBTTagCompound head = new NBTTagCompound();
        NBTTagCompound tags = new NBTTagCompound();

        tileData.setBoolean("InfernalNetherite", true);

        tags.setInt("Unbreakable", 1);

        head.setString("id", "minecraft:structure_block");
        head.setByte("Count", (byte) 1);
        head.set("tag", tags);

        armor.add(new NBTTagCompound());
        armor.add(new NBTTagCompound());
        armor.add(new NBTTagCompound());
        armor.add(head);

        spawnData.set("ArmorItems", armor);
        spawnData.setString("id", "minecraft:armor_stand");
        spawnData.setByte("Marker", (byte) 1);
        spawnData.setInt("Invisible", 1);

        tileData.setShort("SpawnRange", (short) 0);
        tileData.setShort("SpawnCount", (short) 0);
        tileData.setShort("RequiredPlayerRange", (short) 0);
        tileData.setShort("MaxNearbyEntities", (short) 0);
        tileData.set("SpawnData", spawnData);

        spawner.load(tileData);

        loc.getWorld().playSound(loc, Sound.BLOCK_STONE_BREAK, 1, 1);
    }

    public void checkForBreak(BlockBreakEvent e) {

        if (isInfernalNetherite(e.getBlock().getLocation())) {

            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0, 0.5, 0), PermaDeathItems.crearInfernalNetherite());
            e.setExpToDrop(0);
        }
    }

    @Override
    public boolean isInfernalNetherite(Location l) {

        BlockPosition bp = new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ());
        TileEntityMobSpawner spawner = (TileEntityMobSpawner) ((CraftWorld) l.getWorld()).getHandle()
                .getTileEntity(bp);

        NBTTagCompound tileData = spawner.b();

        boolean returning = false;

        try {

            if (tileData.getBoolean("InfernalNetherite")) {

                returning = true;
            }
        } catch (Exception e) {
        }

        if (!returning) {

            if (l.getBlock().getState() instanceof CreatureSpawner) {

                CreatureSpawner c = (CreatureSpawner) l.getBlock().getState();

                if (c.getSpawnedType() == EntityType.ARMOR_STAND) {

                    returning = true;
                }
            }
        }

        return returning;
    }
}
