package com.permadeathcore.Listener.Player;

import com.permadeathcore.Main;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class AnvilListener implements Listener {
    private final Main plugin;

    private String helmetName;
    private String chestName;
    private String legName;
    private String bootName;

    public AnvilListener(Main instance) {
        plugin = instance;

        this.helmetName = instance.format("&5Netherite Helmet");
        this.chestName = instance.format("&5Netherite Chestplate");
        this.legName = instance.format("&5Netherite Leggings");
        this.bootName = instance.format("&5Netherite Boots");
    }

    @EventHandler
    public void onAnvil(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;

        if (e.getInventory().getType() == InventoryType.ANVIL && e.getSlotType() == InventoryType.SlotType.RESULT &&
                e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().hasDisplayName()) {

            if (e.getCurrentItem().getType().name().toLowerCase().contains("diamond_") && e.getCurrentItem().getItemMeta().isUnbreakable()) {

                String name = "";
                Material type = e.getCurrentItem().getType();

                if (type == Material.DIAMOND_SWORD) {
                    name = "Espada de Netherite";
                } else if (type == Material.DIAMOND_PICKAXE) {
                    name = "Pico de Netherite";
                } else if (type == Material.DIAMOND_AXE) {
                    name = "Hacha de Netherite";
                } else if (type == Material.DIAMOND_HOE) {
                    name = "Azada de Netherite";
                } else if (type == Material.DIAMOND_SHOVEL) {
                    name = "Pala de Netherite";
                }

                if (!name.isEmpty()) {
                    ItemMeta meta = e.getCurrentItem().getItemMeta();
                    meta.setDisplayName(plugin.format("&6" + name));
                    e.getCurrentItem().setItemMeta(meta);
                }
            }

            if (e.getCurrentItem().getType() == Material.LEATHER_HELMET || e.getCurrentItem().getType() == Material.LEATHER_CHESTPLATE || e.getCurrentItem().getType() == Material.LEATHER_LEGGINGS || e.getCurrentItem().getType() == Material.LEATHER_BOOTS) {

                LeatherArmorMeta meta = (LeatherArmorMeta) e.getCurrentItem().getItemMeta();
                ItemStack item = e.getCurrentItem();
                String name = "";
                Material type = item.getType();

                if (meta.isUnbreakable() && type == Material.LEATHER_BOOTS) {
                    name = bootName;
                } else if (meta.isUnbreakable() && type == Material.LEATHER_HELMET) {
                    name = helmetName;
                } else if (meta.isUnbreakable() && type == Material.LEATHER_CHESTPLATE) {
                    name = chestName;
                } else if (meta.isUnbreakable() && type == Material.LEATHER_LEGGINGS) {
                    name = legName;
                }

                if (meta.getColor().equals(Color.fromRGB(16711680)) || meta.getColor() == Color.fromRGB(16711680)) {
                    if (!name.isEmpty()) {
                        name = plugin.format("&5Infernal " + ChatColor.stripColor(name));
                    }
                }

                if (!name.isEmpty()) {
                    meta.setDisplayName(name);
                    e.getCurrentItem().setItemMeta(meta);
                }
            }
        } else {

            if (e.getCurrentItem().getType() == Material.LEATHER_HELMET || e.getCurrentItem().getType() == Material.LEATHER_CHESTPLATE || e.getCurrentItem().getType() == Material.LEATHER_LEGGINGS || e.getCurrentItem().getType() == Material.LEATHER_BOOTS) {

                if (e.getCurrentItem().getItemMeta().isUnbreakable()) {

                    LeatherArmorMeta meta = (LeatherArmorMeta) e.getCurrentItem().getItemMeta();
                    ItemStack item = e.getCurrentItem();
                    String name = "";
                    Material type = item.getType();

                    if (meta.isUnbreakable() && type == Material.LEATHER_BOOTS) {
                        name = bootName;
                    } else if (meta.isUnbreakable() && type == Material.LEATHER_HELMET) {
                        name = helmetName;
                    } else if (meta.isUnbreakable() && type == Material.LEATHER_CHESTPLATE) {
                        name = chestName;
                    } else if (meta.isUnbreakable() && type == Material.LEATHER_LEGGINGS) {
                        name = legName;
                    }

                    if (meta.getColor().equals(Color.fromRGB(16711680)) || meta.getColor() == Color.fromRGB(16711680)) {
                        if (!name.isEmpty()) {
                            name = plugin.format("&5Infernal " + ChatColor.stripColor(name));
                        }
                    }

                    if (!name.isEmpty()) {
                        meta.setDisplayName(name);
                        e.getCurrentItem().setItemMeta(meta);
                    }
                }
            }
        }
    }
}
