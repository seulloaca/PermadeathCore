package com.permadeathcore.Util.Item;

import com.permadeathcore.Main;
import org.bukkit.*;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class NetheriteDrops implements Listener {
    private final Main plugin;
    private Color color;

    private String helmetName;
    private String chestName;
    private String legName;
    private String bootName;

    public NetheriteDrops(Main instance){
        plugin=instance;
        this.color = Color.fromRGB(6116957);

        this.helmetName = instance.format("&5Netherite Helmet");
        this.chestName = instance.format("&5Netherite Chestplate");
        this.legName = instance.format("&5Netherite Leggings");
        this.bootName = instance.format("&5Netherite Boots");
    }

    @EventHandler
    public void dropNetherite(EntityDeathEvent event){
        LivingEntity Mob = event.getEntity();

        int HelmetProbability = Integer.parseInt(Objects.requireNonNull(Main.instance.getConfig().getString("Toggles.Netherite.Helmet")));
        int ChetsplateProbability = Integer.parseInt(Objects.requireNonNull(Main.instance.getConfig().getString("Toggles.Netherite.Chestplate")));
        int LeggingsProbability = Integer.parseInt(Objects.requireNonNull(Main.instance.getConfig().getString("Toggles.Netherite.Leggings")));
        int BootsProbability = Integer.parseInt(Objects.requireNonNull(Main.instance.getConfig().getString("Toggles.Netherite.Boots")));

        int RandProb = ThreadLocalRandom.current().nextInt(1, 100 + 1);

        if(Mob instanceof CaveSpider && plugin.getDays() >= 25 && plugin.getDays() < 30 && RandProb < HelmetProbability && event.getEntity().getKiller() != null){
            event.getDrops().clear();
            event.getDrops().add(craftNetheriteHelmet());
        }

        if(Mob instanceof Slime && plugin.getDays() >= 25 && plugin.getDays() < 30 && RandProb < ChetsplateProbability && event.getEntity().getKiller() != null){
            event.getDrops().clear();
            event.getDrops().add(craftNetheriteChest());
        }

        if(Mob instanceof MagmaCube && plugin.getDays() >= 25 && plugin.getDays() < 30 && RandProb < LeggingsProbability && event.getEntity().getKiller() != null){
            event.getDrops().clear();
            event.getDrops().add(craftNetheriteLegs());
        }

        if(Mob instanceof Ghast && plugin.getDays() >= 25 && plugin.getDays() < 30 && RandProb < BootsProbability && event.getEntity().getKiller() != null){
            event.getDrops().clear();
            event.getDrops().add(craftNetheriteBoots());
        }
    }

    @EventHandler
    public void onFireBallHit(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Ghast && plugin.getDays() >= 25) {

            Ghast ghast = (Ghast) e.getEntity().getShooter();
            Fireball f = (Fireball) e.getEntity();
            int yield = (e.getEntity().getWorld().getEnvironment() == World.Environment.THE_END || plugin.getDays() >= 50 ? 6 : ThreadLocalRandom.current().nextInt(3, 5 + 1));

            if (ghast.getPersistentDataContainer().has(new NamespacedKey(plugin, "demonio_flotante"), PersistentDataType.BYTE)) yield = 0;

            if (e.getEntity() instanceof Fireball) f.setYield(yield);
        }
    }

    @EventHandler
    public void onGhastFireballHit(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() == EntityType.FIREBALL) {
            Fireball f = (Fireball) e.getDamager();
            if (f.getShooter() instanceof Ghast) {
                Ghast ghast = (Ghast) f.getShooter();
                if (ghast.getPersistentDataContainer().has(new NamespacedKey(plugin, "demonio_flotante"), PersistentDataType.BYTE)) {
                    Entity entity = e.getEntity();
                    if (entity instanceof LivingEntity) {
                        LivingEntity liv = (LivingEntity) entity;
                        liv.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20*5, 49));
                        liv.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20*20, 4));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAnvil(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        if (e.getInventory().getType() == InventoryType.ANVIL) {

            if(e.getSlotType() == InventoryType.SlotType.RESULT) {

                if (e.getCurrentItem().getItemMeta() == null) return;

                if (e.getCurrentItem().getItemMeta().hasDisplayName()) {

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
    }

    // Prevenir tintes de armadura
    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {

        if (e.getInventory().getResult() == null) return;

        if (e.getInventory().getResult().getType() == Material.LEATHER_HELMET || e.getInventory().getResult().getType() == Material.LEATHER_CHESTPLATE || e.getInventory().getResult().getType() == Material.LEATHER_LEGGINGS || e.getInventory().getResult().getType() == Material.LEATHER_BOOTS) {

            LeatherArmorMeta meta = (LeatherArmorMeta) e.getInventory().getResult().getItemMeta();

            if (meta.getColor() != color && !meta.isUnbreakable()) {

                e.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
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

    public boolean isNetheritePiece(ItemStack s) {
        if (s == null) return false;

        if (s.hasItemMeta()) {

            if (s.getItemMeta().isUnbreakable() && ChatColor.stripColor(s.getItemMeta().getDisplayName()).startsWith("Netherite")) {

                return true;
            }
        }

        return false;
    }

    public boolean isInfernalPiece(ItemStack s) {
        if (s == null) return false;

        if (s.hasItemMeta()) {

            if (s.getType() == Material.ELYTRA && s.getItemMeta().isUnbreakable()) {

                return true;
            }

            if (s.getItemMeta().isUnbreakable() && ChatColor.stripColor(s.getItemMeta().getDisplayName()).startsWith("Infernal")) {

                return true;
            }
        }

        return false;
    }

    public void setupHealth(Player p) {
        Double maxHealth = getAvaibleMaxHealth(p);
        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
    }

    public Double getAvaibleMaxHealth(Player p) {

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
            maxHealth+=12.0D;
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0));
        }

        if (plugin.getDays() >= 40) {
            maxHealth-=8.0D; // 12HP - 6 corazones día 40
            if (plugin.getDays() >= 60) {
                maxHealth-=8.0D; // 4HP - 2 corazones Día 60

                boolean hasOrb = checkForOrb(p);
                if (!hasOrb) {
                    maxHealth-=16.0D;
                }
            }
        }

        return Math.max(maxHealth, 0.000001D);
    }

    public boolean checkForOrb(Player p) {
        if (plugin.getOrbEvent().isRunning()) {
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
