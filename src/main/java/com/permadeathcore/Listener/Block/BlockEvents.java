package com.permadeathcore.Listener.Block;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Manager.EndDataManager;
import com.permadeathcore.Util.Item.CustomItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static com.permadeathcore.Main.instance;

public class BlockEvents implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBurn(BlockBurnEvent e) {
        if (Main.getInstance().getEndData() != null && Main.getInstance().getDays() >= 30) {
            EndDataManager ma = Main.getInstance().getEndData();
            if (ma.getConfig().contains("RegenZoneLocation")) {
                Location loc = buildLocation(ma.getConfig().getString("RegenZoneLocation"));
                if (e.getBlock().getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())) {
                    if (e.getBlock().getLocation().distance(loc) <= 10) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockExplode(EntityExplodeEvent e) {
        if (Main.getInstance().getEndData() != null) {
            EndDataManager ma = Main.getInstance().getEndData();
            if (ma.getConfig().contains("RegenZoneLocation")) {
                Location loc = buildLocation(ma.getConfig().getString("RegenZoneLocation"));
                for (Block b : e.blockList()) {
                    if (b.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())) {
                        if (b.getLocation().distance(loc) <= 10) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockCombust(BlockIgniteEvent e) {

        if (Main.getInstance().getEndData() != null && Main.getInstance().getDays() >= 30) {

            EndDataManager ma = Main.getInstance().getEndData();

            if (ma.getConfig().contains("RegenZoneLocation")) {

                Location loc = buildLocation(ma.getConfig().getString("RegenZoneLocation"));

                if (e.getBlock().getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())) {

                    if (e.getBlock().getLocation().distance(loc) <= 3) {

                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent e) {

        if (Main.getInstance().getEndData() != null && Main.getInstance().getDays() >= 30) {

            EndDataManager ma = Main.getInstance().getEndData();

            if (ma.getConfig().contains("RegenZoneLocation")) {

                Location loc = buildLocation(ma.getConfig().getString("RegenZoneLocation"));

                if (e.getBlock().getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())) {

                    if (e.getBlock().getLocation().distance(loc) <= 3) {

                        e.setCancelled(true);
                        e.getPlayer().sendMessage(Main.getInstance().format("&cNo puedes colocar bloques cerca de la Zona de Regeneración."));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        if (Main.getInstance().getEndData() != null && Main.getInstance().getDays() >= 30) {

            EndDataManager ma = Main.getInstance().getEndData();

            if (ma.getConfig().contains("RegenZoneLocation")) {

                Location loc = buildLocation(ma.getConfig().getString("RegenZoneLocation"));

                if (e.getBlock().getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())) {

                    if (e.getBlock().getLocation().distance(loc) <= 4) {

                        e.setCancelled(true);
                        e.getPlayer().sendMessage(Main.getInstance().format("&cNo puedes romper bloques cerca de la Zona de Regeneración."));
                    }
                }
            }
        }

        // Daño por picar bloques
        if (Main.getInstance().getDays() >= 50) {

            ArrayList<ItemStack> items = new ArrayList<>();
            items.add(CustomItems.createNetheriteAxe());
            items.add(CustomItems.createNetheriteShovel());
            items.add(CustomItems.createNetheriteSword());
            items.add(CustomItems.createNetheritePickaxe());
            items.add(CustomItems.createNetheriteHoe());

            boolean damage = true;

            if (e.getPlayer().getInventory().getItemInMainHand() != null) {
                for (ItemStack s : items) {
                    ItemStack i = e.getPlayer().getInventory().getItemInMainHand();
                    if (i.getType() == s.getType() && i.getItemMeta().isUnbreakable() && s.getItemMeta().isUnbreakable()) {
                        damage = false;
                    }
                }
            }

            if (damage) {
                if (instance.getDays() < 60) {
                    e.getPlayer().damage(1.0D);
                } else {
                    e.getPlayer().damage(16.0D);
                }
            }
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {

        if (Main.getInstance().getDays() >= 50) {

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFurnace(FurnaceSmeltEvent e) {

        if (Main.getInstance().getDays() >= 50) {

            if (e.getResult() != null) {

                if (e.getResult().getType() == Material.IRON_INGOT) {

                    ItemStack resu = e.getResult();
                    resu.setType(Material.IRON_NUGGET);
                    e.setResult(resu);
                }

                if (e.getResult().getType() == Material.GOLD_INGOT) {

                    ItemStack resu = e.getResult();
                    resu.setType(Material.GOLD_NUGGET);
                    e.setResult(resu);
                }

            }
        }
    }
    private Location buildLocation(String s) {

        // X;Y;Z;WORLD
        String[] split = s.split(";");

        Double x = Double.valueOf(split[0]);
        Double y = Double.valueOf(split[1]);
        Double z = Double.valueOf(split[2]);
        World w = Bukkit.getWorld(split[3]);

        return new Location(w, x, y, z);
    }
}
