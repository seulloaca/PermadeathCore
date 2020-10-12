package com.permadeathcore.Listener.Entity;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Manager.EntityTeleport;
import com.permadeathcore.Util.Manager.PlayerDataManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.permadeathcore.Main.instance;

public class EntityEvents implements Listener {



    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof Creeper) {
            Creeper c = (Creeper) e.getEntity();
            if (c.hasMetadata("nether_creeper")) {
                if (e.blockList() != null) {
                    e.blockList().forEach(block -> {
                        if (block.getType() != Material.BEDROCK) block.setType(Material.MAGMA_BLOCK);
                    });
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBreakSkull(EntityPickupItemEvent e) {

        if (e.isCancelled()) return;

        if (e.getEntity() instanceof Player) {
            ItemStack i = e.getItem().getItemStack();

            if (i.getType() == Material.PLAYER_HEAD) {

                SkullMeta meta = (SkullMeta) i.getItemMeta();

                PlayerDataManager man = new PlayerDataManager(meta.getOwner(), instance);
                man.craftHead(i);
            }

            if (i.getType() == Material.STRUCTURE_VOID) {
                e.setCancelled(true);
                e.getItem().remove();
            }
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e) {

        if (e.getCause() == EntityDamageEvent.DamageCause.DROWNING && instance.getDays() >= 50) {
            if (e.getEntity() instanceof Player) {
                if (instance.getDays() < 60) {
                    e.setDamage(5.0D);
                } else {
                    e.setDamage(10.0D);
                }
            }
        }

        if (e.getEntity().getType() == EntityType.DROPPED_ITEM && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && e.getEntity().getWorld().getEnvironment() == World.Environment.THE_END) {
            Item item = (Item) e.getEntity();
            if (item.getItemStack().getType() == Material.SHULKER_SHELL) {
                e.setCancelled(true);
            }
        }

        // TP de entidades
        //if (e.getEntity() instanceof Creeper && instance.getDays() >= 50 && e.getEntity().getWorld().getEnvironment() == World.Environment.NORMAL) {
        if (e.getEntity() instanceof Creeper || e.getEntity() instanceof Ghast) {
            new EntityTeleport(e.getEntity(), e);
        }
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        if (Main.getInstance().getDays() >= 50) {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof PolarBear) {

                Player p = (Player) e.getEntity();
                PolarBear b = (PolarBear) e.getDamager();

                b.setAI(false);

                p.getWorld().playSound(b.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
                final Location l = b.getLocation();

                Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
                    @Override
                    public void run() {

                        l.getWorld().createExplosion(l, 1.5f, true, false, b);
                        b.remove();
                    }
                }, 10L);

                e.setCancelled(true);
            }
            if (e.getEntity() instanceof Player && e.getDamager() instanceof LlamaSpit) {

                Player p = (Player) e.getEntity();

                p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 30*20, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10*20, 0));

                p.setVelocity(p.getVelocity().multiply(3));
            }
        }

        if (instance.getDays() >= 60) {

            if (e.getDamager() instanceof Drowned) {
                e.setDamage(e.getDamage() * 3);
            }
        }

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {

            if (Main.getInstance().getDays() >= 40) {

                e.setCancelled(false);
            } else if (Main.getInstance().getDays() <= 39) {

                e.setCancelled(true);
            }
        }
    }
}