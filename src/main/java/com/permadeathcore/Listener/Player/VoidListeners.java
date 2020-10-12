package com.permadeathcore.Listener.Player;

import com.permadeathcore.Task.GatoGalacticoTask;
import com.permadeathcore.Main;
import com.permadeathcore.Util.Item.HiddenStringUtils;
import com.permadeathcore.Util.Item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class VoidListeners implements Listener {

    private Main main;
    private ArrayList<Entity> gatosSupernova = new ArrayList<>();

    public VoidListeners(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.isCancelled()) return;
        if (e.getItemDrop().getItemStack() != null) {
            if (e.getItemDrop().getItemStack().getType() == Material.STRUCTURE_VOID) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClickVoid(InventoryClickEvent e) {
        if (e.isCancelled()) return;
        if (e.getCurrentItem() != null) {
            if (e.getCurrentItem().getType() == Material.STRUCTURE_VOID) {
                e.setCancelled(true);
                if (e.getClick() == ClickType.NUMBER_KEY) {
                    e.getInventory().remove(Material.STRUCTURE_VOID);
                }
            }
        }


        if (e.getCursor() != null) {
            if (e.getCursor().getType() == Material.STRUCTURE_VOID) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemCraft(PrepareItemCraftEvent e) {
        if (e.getInventory() != null) {
            if (e.getInventory().getResult() != null) {
                if (e.getInventory().getResult().getType() == Material.TORCH || e.getInventory().getResult().getType() == Material.REDSTONE_TORCH) {
                    e.getInventory().setResult(null);
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.STRUCTURE_VOID) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (e.isCancelled()) return;
        if (e.getOffHandItem() != null) {
            if (e.getOffHandItem().getType() == Material.STRUCTURE_VOID) {
                e.setCancelled(true);
            }
        }

        if (e.getMainHandItem() != null) {
            if (e.getMainHandItem().getType() == Material.STRUCTURE_VOID) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMoveItem(InventoryMoveItemEvent e) {

        if (e.isCancelled()) return;

        if (e.getItem() != null) {

            if (e.getItem().getType() == Material.STRUCTURE_VOID) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPickup(InventoryPickupItemEvent e) {

        if (e.isCancelled()) return;

        if (e.getItem().getItemStack() != null) {

            if (e.getItem().getItemStack().getType() == Material.STRUCTURE_VOID) {
                e.setCancelled(true);
            }
        }
    }



    @EventHandler
    public void onWitchThrow(ProjectileLaunchEvent e) {
        if (main.getDays() < 40) return;
        if (e.getEntity().getShooter() instanceof Witch) {

            if (e.getEntity() instanceof ThrownPotion) {
                ThrownPotion potion = (ThrownPotion) e.getEntity();
                int prob = new Random().nextInt(2) + 1;
                if (prob == 1) {

                    ItemStack s = new ItemStack(Material.SPLASH_POTION);
                    PotionMeta meta = (PotionMeta) s.getItemMeta();

                    if (!meta.getCustomEffects().isEmpty() || meta.getCustomEffects().size() >= 1) {
                        for (PotionEffect effect : meta.getCustomEffects()) {
                            meta.removeCustomEffect(effect.getType());
                        }
                    }

                    meta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 20, 3), true);
                    s.setItemMeta(meta);
                    potion.setItem(s);

                } else if (prob == 2) {

                    int min = 60*5;

                    ItemStack s = new ItemStack(Material.SPLASH_POTION);
                    PotionMeta meta = (PotionMeta) s.getItemMeta();

                    if (!meta.getCustomEffects().isEmpty() || meta.getCustomEffects().size() >= 1) {
                        for (PotionEffect effect : meta.getCustomEffects()) {
                            meta.removeCustomEffect(effect.getType());
                        }
                    }

                    meta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, min*20, 2), true);
                    s.setItemMeta(meta);
                    potion.setItem(s);
                } else {

                    ItemStack s = new ItemStack(Material.SPLASH_POTION);
                    PotionMeta meta = (PotionMeta) s.getItemMeta();

                    if (!meta.getCustomEffects().isEmpty() || meta.getCustomEffects().size() >= 1) {
                        for (PotionEffect effect : meta.getCustomEffects()) {
                            meta.removeCustomEffect(effect.getType());
                        }
                    }

                    meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 20*20, 4), true);
                    s.setItemMeta(meta);
                    potion.setItem(s);
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (!e.getNewItems().isEmpty()) {
            for (int i : e.getNewItems().keySet()) {
                ItemStack s = e.getNewItems().get(i);

                if (s != null) {

                    if (s.getType() == Material.STRUCTURE_VOID) {

                        e.getInventory().removeItem(s);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onIntWithEndRelic(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand() != null) {
            if (esReliquia(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand())) {
                e.setCancelled(true);
            }
        }

        if (e.getPlayer().getInventory().getItemInOffHand() != null) {
            if (esReliquia(e.getPlayer(), e.getPlayer().getInventory().getItemInOffHand())) {
                e.setCancelled(true);
            }
        }
    }

    public boolean esReliquia(Player p, ItemStack stack) {
        if (stack == null) return false;
        if (!stack.hasItemMeta()) return false;

        if (stack.getType() == Material.LIGHT_BLUE_DYE && stack.getItemMeta().getDisplayName().endsWith(main.format("&6Reliquia Del Fin"))) {
            return true;
        }
        return false;
    }
}
