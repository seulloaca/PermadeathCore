package com.permadeathcore.Util.Item;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Library.HiddenStringUtils;
import com.permadeathcore.Util.Library.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

public class PermaDeathItems {

    public static ItemStack crearReliquia() {

        ItemStack s = new ItemBuilder(Material.LIGHT_BLUE_DYE).setCustomModelData(1, !Main.isOptifineEnabled()).setDisplayName(format("&6Reliquia Del Fin")).build();

        ItemMeta meta = s.getItemMeta();
        meta.setUnbreakable(true);
        meta.setLore(Arrays.asList(HiddenStringUtils.encodeString("{" + UUID.randomUUID().toString() + ": 0}")));
        s.setItemMeta(meta);

        return s;

    }

    public static ItemStack createLifeOrb() {
        return new ItemBuilder(Material.BROWN_DYE).setCustomModelData(1, !Main.isOptifineEnabled()).setUnbrekeable(true).setDisplayName(format("&6Orbe de Vida")).build();
    }

    public static ItemStack createBeginningRelic() {
        return new ItemBuilder(Material.CYAN_DYE).setCustomModelData(1, !Main.isOptifineEnabled()).setUnbrekeable(true).setDisplayName(format("&6Reliquia del Comienzo")).build();
    }

    public static ItemStack crearElytraInfernal() {

        ItemStack s = new ItemBuilder(Material.ELYTRA).setCustomModelData(1).setDisplayName(format("&6Elytras de Netherite Infernal")).build();

        ItemMeta meta = s.getItemMeta();

        AttributeModifier m = new AttributeModifier(UUID.randomUUID(), "generic.armor", 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        AttributeModifier m2 = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);

        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, m);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, m2);

        s.setItemMeta(meta);

        return s;
    }

    public static ItemStack createNetheriteSword() {

        ItemStack s = new ItemBuilder(Material.DIAMOND_SWORD).setCustomModelData(1, !Main.isOptifineEnabled()).setDisplayName(format("&6Espada de Netherite")).build();
        ItemMeta meta = s.getItemMeta();

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        meta.setUnbreakable(true);
        s.setItemMeta(meta);

        return s;
    }

    public static ItemStack createNetheritePickaxe() {

        ItemStack s = new ItemBuilder(Material.DIAMOND_PICKAXE).setCustomModelData(1, !Main.isOptifineEnabled()).setDisplayName(format("&6Pico de Netherite")).build();
        ItemMeta meta = s.getItemMeta();
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        meta.setUnbreakable(true);
        s.setItemMeta(meta);

        return s;
    }

    public static ItemStack createNetheriteHoe() {

        ItemStack s = new ItemBuilder(Material.DIAMOND_HOE).setCustomModelData(1, !Main.isOptifineEnabled()).setDisplayName(format("&6Azada de Netherite")).build();
        ItemMeta meta = s.getItemMeta();
        meta.setUnbreakable(true);
        s.setItemMeta(meta);

        return s;
    }

    public static ItemStack createNetheriteAxe() {

        ItemStack s = new ItemBuilder(Material.DIAMOND_AXE).setCustomModelData(1, !Main.isOptifineEnabled()).setDisplayName(format("&6Hacha de Netherite")).build();
        ItemMeta meta = s.getItemMeta();
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        meta.setUnbreakable(true);
        s.setItemMeta(meta);

        return s;
    }

    public static ItemStack createNetheriteShovel() {

        ItemStack s = new ItemBuilder(Material.DIAMOND_SHOVEL).setCustomModelData(1, !Main.isOptifineEnabled()).setDisplayName(format("&6Pala de Netherite")).build();
        ItemMeta meta = s.getItemMeta();
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 6.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        meta.setUnbreakable(true);
        s.setItemMeta(meta);

        return s;
    }

    public static ItemStack crearInfernalNetherite() {

        ItemStack s = new ItemBuilder(Material.DIAMOND).setCustomModelData(1, !Main.isOptifineEnabled()).setDisplayName(format("&6Infernal Netherite Block")).build();
        ItemMeta meta = s.getItemMeta();
        meta.setUnbreakable(true);
        meta.setLore(Arrays.asList(HiddenStringUtils.encodeString("{" + UUID.randomUUID().toString() + ": 0}")));
        s.setItemMeta(meta);

        return s;
    }

    private static String format(String texto) {

        return ChatColor.translateAlternateColorCodes('&', texto);
    }

    public static void slotBlock(Player p) {

        if (Main.getInstance().getDays() < 40) return;

        if (p.getGameMode() == GameMode.SPECTATOR || p.isDead() || !p.isOnline()) {
            return;
        }

        boolean hasEndRelic = false;
        boolean hasBeginningRelic = false;

        int[] beginningRelicLockedSlots = {40, 34, 33, 32, 30, 29, 28, 27, 26, 25, 24, 23, 21, 20, 19, 18, 17, 16, 15, 14, 12, 11, 10, 9, 8, 7};
        int[] endRelicLockedSlots;
        if (Main.getInstance().getDays() < 60) {
            endRelicLockedSlots = new int[]{40, 13, 22, 31, 4};
        } else {
            endRelicLockedSlots = new int[]{13, 22, 31, 4};
        }


        for (ItemStack contents : p.getInventory().getContents()) {
            if (isBeginningRelic(contents)) {
                hasBeginningRelic = true;
                hasEndRelic = true;
                break;
            }
        }
        if (!hasEndRelic) {
            for (ItemStack contents : p.getInventory().getContents()) {
                if (isEndRelic(contents)) {
                    hasEndRelic = true;
                    break;
                }
            }
        }

        if (Main.getInstance().getDays() >= 40) {
            if (hasEndRelic) {
                for (int i = 0; i < endRelicLockedSlots.length; i++) {
                    int slot = endRelicLockedSlots[i];
                    unlockSlot(p, slot);
                }
            } else {
                for (int i = 0; i < endRelicLockedSlots.length; i++) {
                    int slot = endRelicLockedSlots[i];
                    lockSlot(p, slot);
                }
            }
        }

        if (Main.getInstance().getDays() >= 60) {
            if (hasBeginningRelic) {
                for (int i = 0; i < beginningRelicLockedSlots.length; i++) {
                    int slot = beginningRelicLockedSlots[i];
                    unlockSlot(p, slot);
                }
            } else {
                for (int i = 0; i < beginningRelicLockedSlots.length; i++) {
                    int slot = beginningRelicLockedSlots[i];
                    lockSlot(p, slot);
                }
            }
        }
    }

    private static void lockSlot(Player p, int slot) {
        ItemStack item = p.getInventory().getItem(slot);

        if (item != null) {

            if (item.getType() != Material.AIR && item.getType() != Material.STRUCTURE_VOID) {
                p.getWorld().dropItem(p.getLocation(), item.clone());
            }

            item.setType(Material.STRUCTURE_VOID);
            item.setAmount(1);
        } else {
            p.getInventory().setItem(slot, new ItemStack(Material.STRUCTURE_VOID));
        }
    }

    private static void unlockSlot(Player p, int slot) {
        ItemStack item = p.getInventory().getItem(slot);

        if (item != null && item.getType() == Material.STRUCTURE_VOID) {
            p.getInventory().clear(slot);
        }
    }

    public static boolean isEndRelic(ItemStack stack) {

        if (stack == null) return false;
        if (!stack.hasItemMeta()) return false;

        if (stack.getType() == Material.LIGHT_BLUE_DYE && stack.getItemMeta().getDisplayName().endsWith(format("&6Reliquia Del Fin"))) {
            return true;
        }
        return false;
    }

    public static boolean isBeginningRelic(ItemStack stack) {

        if (stack == null) return false;
        if (!stack.hasItemMeta()) return false;

        if (stack.getType() == Material.CYAN_DYE && stack.getItemMeta().getDisplayName().endsWith(format("&6Reliquia del Comienzo"))) {
            return true;
        }
        return false;
    }
}
