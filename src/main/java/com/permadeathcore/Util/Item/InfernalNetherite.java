package com.permadeathcore.Util.Item;

import com.permadeathcore.Main;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.UUID;

public class InfernalNetherite implements Listener {
    private final Main plugin;

    private Color color;

    private String helmetName;
    private String chestName;
    private String legName;
    private String bootName;

    public InfernalNetherite(Main instance) {
        plugin = instance;

        this.color = Color.fromRGB(16711680);

        this.helmetName = instance.format("&5Infernal Netherite Helmet");
        this.chestName = instance.format("&5Infernal Netherite Chestplate");
        this.legName = instance.format("&5Infernal Netherite Leggings");
        this.bootName = instance.format("&5Infernal Netherite Boots");
    }

    public ItemStack craftNetheriteHelmet() {

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

    public ItemStack craftNetheriteChest() {

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

    public ItemStack craftNetheriteLegs() {

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

    public ItemStack craftNetheriteBoots() {

        ItemStack item = new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1)
                .setColor(Color.fromRGB(0xAC1617))
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
}
