package com.permadeathcore.Util.Item;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Library.LeatherArmorBuilder;
import org.bukkit.*;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public final class NetheriteArmor implements Listener {
    private static Color color = Color.fromRGB(6116957);

    private static String helmetName = Main.format("&5Netherite Helmet");
    private static String chestName = Main.format("&5Netherite Chestplate");
    private static String legName = Main.format("&5Netherite Leggings");
    private static String bootName = Main.format("&5Netherite Boots");

    public static ItemStack craftNetheriteHelmet() {

        ItemStack item = new LeatherArmorBuilder(Material.LEATHER_HELMET, 1)
                .setColor(color)
                .setDisplayName(helmetName)
                .build();

        ItemMeta meta = item.getItemMeta();

        EquipmentSlot slot = EquipmentSlot.HEAD;
        // CASCO 3, PECHERA 8, PANTALONES 6, BOTAS 3

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);

        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 3, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);

        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack craftNetheriteChest() {

        ItemStack item = new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE, 1)
                .setColor(color)
                .setDisplayName(chestName)
                .build();

        ItemMeta meta = item.getItemMeta();

        EquipmentSlot slot = EquipmentSlot.CHEST;
        // CASCO 3, PECHERA 8, PANTALONES 6, BOTAS 3

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 8, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);

        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 3, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);


        //AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(), "generic.maxHealth", 2, AttributeModifier.Operation.ADD_NUMBER, slot);
        //meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, modifier3);

        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack craftNetheriteLegs() {

        ItemStack item = new LeatherArmorBuilder(Material.LEATHER_LEGGINGS, 1)
                .setColor(color)
                .setDisplayName(legName)
                .build();

        ItemMeta meta = item.getItemMeta();

        EquipmentSlot slot = EquipmentSlot.LEGS;
        // CASCO 3, PECHERA 8, PANTALONES 6, BOTAS 3

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);

        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 3, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);

        //AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(), "generic.maxHealth", 2, AttributeModifier.Operation.ADD_NUMBER, slot);
        //meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, modifier3);

        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack craftNetheriteBoots() {

        ItemStack item = new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1)
                .setColor(color)
                .setDisplayName(bootName)
                .build();

        ItemMeta meta = item.getItemMeta();

        EquipmentSlot slot = EquipmentSlot.FEET;
        // CASCO 3, PECHERA 8, PANTALONES 6, BOTAS 3

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);

        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 3, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);

        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        return item;
    }

    public static boolean isNetheritePiece(ItemStack s) {
        if (s == null) return false;

        if (s.hasItemMeta()) {

            if (s.getItemMeta().isUnbreakable() && ChatColor.stripColor(s.getItemMeta().getDisplayName()).startsWith("Netherite")) {

                return true;
            }
        }

        return false;
    }

    public static boolean isInfernalPiece(ItemStack s) {
        if (s == null) return false;

        if (s.hasItemMeta()) {

            if (s.getType() == Material.ELYTRA && s.getItemMeta().getCustomModelData() == 1) {

                return true;
            }

            if (s.getItemMeta().isUnbreakable() && ChatColor.stripColor(s.getItemMeta().getDisplayName()).startsWith("Infernal")) {

                return true;
            }
        }

        return false;
    }

    public static void setupHealth(Player p) {
        Double maxHealth = getAvaibleMaxHealth(p);
        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
    }

    public static Double getAvaibleMaxHealth(Player p) {

        int currentNetheritePieces = 0;
        int currentInfernalPieces = 0;
        boolean doPlayerAteOne = p.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_one"), PersistentDataType.BYTE);
        boolean doPlayerAteTwo = p.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_two"), PersistentDataType.BYTE);

        for (ItemStack contents : p.getInventory().getArmorContents()) {
            if (isNetheritePiece(contents)) {
                currentNetheritePieces++;
            }
            if (isInfernalPiece(contents)) {
                currentInfernalPieces++;
            }
        }

        Double maxHealth = 20.0D;

        if (doPlayerAteOne) {
            maxHealth+=4.0;
        }
        if (doPlayerAteTwo) {
            maxHealth+=4.0;
        }

        if (currentNetheritePieces >= 4) {
            maxHealth+=8.0D;
        }

        if (currentInfernalPieces >= 4) {
            maxHealth+=10.0D;
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0));
        }

        if (Main.getInstance().getDays() >= 40) {
            maxHealth-=8.0D; // 12HP - 6 corazones día 40
            if (Main.getInstance().getDays() >= 60) {
                maxHealth-=8.0D; // 4HP - 2 corazones Día 60

                boolean hasOrb = checkForOrb(p);
                if (!hasOrb) {
                    maxHealth-=16.0D;
                }
            }
        }

        return Math.max(maxHealth, 0.000001D);
    }

    public static boolean checkForOrb(Player p) {
        if (Main.getInstance().getOrbEvent().isRunning()) {
            return true;
        } else {
            for (ItemStack stack : p.getInventory().getContents()) {
                if (stack != null) {
                    if (stack.getItemMeta() != null && stack.getType() == Material.BROWN_DYE && stack.getItemMeta().isUnbreakable()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
