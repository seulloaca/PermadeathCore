package com.permadeathcore.Listener.Player;

import com.permadeathcore.Discord.DiscordManager;
import com.permadeathcore.Main;
import com.permadeathcore.Util.Item.CustomItems;
import com.permadeathcore.Util.Item.HiddenStringUtils;
import com.permadeathcore.Util.Manager.EndDataManager;
import com.permadeathcore.Util.Manager.PlayerDataManager;
import com.permadeathcore.Util.Item.ItemBuilder;
import com.permadeathcore.Util.Library.UpdateChecker;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

import static com.permadeathcore.Main.*;
import static org.bukkit.Bukkit.getServer;

public class PlayerEvents implements Listener {

    ArrayList<Player> sleeping = new ArrayList<>();
    ArrayList<Player> globalSleeping = new ArrayList<>();

    long stormTicks;
    long stormHours;

    public PlayerEvents() {
        loadTicks();
    }

    public void loadTicks() {
        if (Main.getInstance().getDays() <= 24) {

            this.stormTicks = Main.getInstance().getDays() * 3600;
            this.stormHours = stormTicks / 60 / 60;
        }

        if (Main.getInstance().getDays() >= 25 && Main.getInstance().getDays() < 50) {

            long define = Main.getInstance().getDays() - 24;

            this.stormTicks = define * 3600;
            this.stormHours = stormTicks / 60 / 60;
        }

        if (Main.getInstance().getDays() == 50) {

            this.stormTicks = 3600 / 2;
            this.stormHours = stormTicks / 60 / 60;
        }

        if (Main.getInstance().getDays() > 50 && Main.getInstance().getDays() < 75) {

            long define = Main.getInstance().getDays() - 49;

            this.stormTicks = define * 3600 / 2;
            this.stormHours = stormTicks / 60 / 60;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();
        OfflinePlayer off = p;

        String victim = p.getName();
        boolean weather = instance.world.hasStorm();

        BukkitScheduler scheduler = getServer().getScheduler();

        for (Player player : Bukkit.getOnlinePlayers()) {

            String msg = Main.getInstance().getMessages().getMessage("DeathMessageChat", player).replace("%player%", victim);
            player.sendMessage(msg);

            String ServerMessageTitle = Main.getInstance().getMessages().getMessage("DeathMessageTitle", player);
            String ServerMessageSubtitle = Main.getInstance().getMessages().getMessage("DeathMessageSubtitle", player);

            player.sendTitle(ServerMessageTitle, ServerMessageSubtitle.replace("%player%", victim), 20, 20 * 5, 20);
            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, Float.MAX_VALUE, -0.1f);
        }

        loadTicks();
        int stormDuration = instance.world.getWeatherDuration();
        int stormTicksToSeconds = stormDuration / 20;
        long stormIncrement = stormTicksToSeconds + this.stormTicks;
        int intsTicks = (int) this.stormTicks;
        int inc = (int) stormIncrement;

        boolean doEnableOP = instance.getConfig().getBoolean("Toggles.OP-Ban");
        //boolean causingProblems = (!doEnableOP ? !p.hasPermission("permadeathcore.banoverride") : true);
        boolean causingProblems = true;

        if (!doEnableOP) {
            if (p.hasPermission("permadeathcore.banoverride")) {
                causingProblems = false;
            }
        }

        if (causingProblems) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:weather thunder");

            if (weather) {
                instance.world.setWeatherDuration(inc * 20);
            } else {
                instance.world.setWeatherDuration(intsTicks * 20);
            }

            if (instance.getDays() >= 25) {
                for (World w : Bukkit.getWorlds()) {
                    for (LivingEntity l : w.getLivingEntities()) {
                        instance.deathTrainEffects(l);
                    }
                }
            }

            if (instance.getDays() >= 50) {
                if (instance.getBeginningManager() != null) {
                    instance.getBeginningManager().closeBeginning();
                }

                Bukkit.broadcastMessage(instance.format(tag + "&e¡Ha comenzado el modo UHC!"));
                instance.world.setGameRule(GameRule.NATURAL_REGENERATION, false);
            }

            scheduler.scheduleSyncDelayedTask(instance, new Runnable() {

                @Override
                public void run() {

                    loadTicks();
                    if (Main.getInstance().getDays() < 50) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            String msg = Main.getInstance().getMessages().getMessage("DeathTrainMessage", p).replace("%tiempo%", String.valueOf(stormHours));
                            p.sendMessage(msg);
                            p.playSound(p.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, 10, 1);
                        }
                        Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("DeathTrainMessage").replace("%tiempo%", String.valueOf(stormHours)));
                        DiscordManager.getInstance().onDeathTrain(Main.getInstance().getMessages().getMsgForConsole("DeathTrainMessage").replace("%tiempo%", String.valueOf(stormHours)));
                    } else {
                        long hours = stormTicks / 60 / 60;
                        long minutes = stormTicks / 60 % 60;

                        String ct = String.valueOf(hours);
                        String path = "DeathTrainMessage";

                        if (minutes == 30 || minutes == 60) {
                            path = path + "Minutes";
                            if (hours >= 1) {
                                ct = "" + hours + " horas y " + minutes;
                            } else {
                                ct = String.valueOf(minutes);
                            }
                        }

                        if (minutes == 0) {
                            ct = String.valueOf(hours);
                        }

                        String time = ct;

                        for (Player p : Bukkit.getOnlinePlayers()) {

                            String msg = Main.getInstance().getMessages().getMessage(path, p).replace("%tiempo%", time);
                            p.sendMessage(msg);
                            p.playSound(p.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, 10, 1);
                        }

                        Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole(path).replace("%tiempo%", time));
                        DiscordManager.getInstance().onDeathTrain(Main.getInstance().getMessages().getMsgForConsole(path).replace("%tiempo%", time));
                    }
                }
            }, 100L);
        } else {
            Bukkit.broadcastMessage(String.format(instance.tag + instance.format("&eEl jugador &b" + p.getName() + " &eno puede dar más horas de tormenta.")));
        }

        PlayerDataManager man = new PlayerDataManager(e.getEntity().getPlayer().getName(), instance);
        man.setAutoDeathCause(e.getEntity().getPlayer().getLastDamageCause().getCause());
        man.setDeathTime();
        man.setDeathDay();
        man.setDeathCoords(e.getEntity().getPlayer().getLocation());
        DiscordManager.getInstance().banPlayer(off, false);

        if (instance.getConfig().contains("Server-Messages.CustomDeathMessages." + p.getName())) {
            String msg = instance.getConfig().getString("Server-Messages.CustomDeathMessages." + p.getName()).replace("%player%", p.getName());
            Bukkit.broadcastMessage(instance.format(StringUtils.capitalize(msg) + (msg.endsWith(".") ? "" : ".")));
        } else {
            String msg = instance.format(instance.getConfig().getString("Server-Messages.DefaultDeathMessage").replace("%player%", p.getName()));
            Bukkit.broadcastMessage(instance.format(StringUtils.capitalize(msg) + (msg.endsWith(".") ? "" : ".")));
        }
        Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("DeathMessageChat").replace("%player%", victim));
        if (instance.getConfig().getBoolean("Server-Messages.coords-msg-enable")) {
            int Dx = e.getEntity().getPlayer().getLocation().getBlockX();
            int Dy = e.getEntity().getPlayer().getLocation().getBlockY();
            int Dz = e.getEntity().getPlayer().getLocation().getBlockZ();
            Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "X: " + Dx + " || Y: " + Dy + " || Z: " + Dz + ChatColor.RESET);
        }

        p.setGameMode(GameMode.SPECTATOR);
        scheduler.runTaskLater(instance, new Runnable() {
            @Override
            public void run() {
                boolean isban = (doEnableOP ? !p.hasPermission("permadeathcore.banoverride") : true);
                if (instance.getConfig().getBoolean("ban-enabled") && isban) {
                    if (off.isOnline()) {
                        ((Player) off).kickPlayer(ChatColor.RED + "Has sido PERMABANEADO");
                    }
                    Bukkit.getBanList(BanList.Type.NAME).addBan(off.getName(), ChatColor.RED + "Has sido PERMABANEADO", null, "console");
                }
            }
        }, 40L);

        if (Main.getInstance().getConfig().getBoolean("Toggles.Player-Skulls")) {
            Location l = p.getEyeLocation().clone();
            if (l.getY() < 3) {
                l.setY(3);
            }
            Block skullBlock = l.getBlock();
            skullBlock.setType(Material.PLAYER_HEAD);

            Skull skullState = (Skull) skullBlock.getState();
            skullState.setOwningPlayer(p);
            skullState.update();

            Rotatable rotatable = (Rotatable) skullBlock.getBlockData();
            rotatable.setRotation(getRotation(p));
            skullBlock.setBlockData(rotatable);

            skullBlock.getRelative(BlockFace.DOWN).setType(Material.NETHER_BRICK_FENCE);
            skullBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).setType(Material.BEDROCK);
        }
    }

    public BlockFace getRotation(Player player) {
        float rotation = player.getLocation().getYaw();

        if (rotation < 0) {
            rotation += 360.0;
        }

        if (0 <= rotation && rotation < 22.5) {
            return BlockFace.NORTH;
        }
        if (22.5 <= rotation && rotation < 67.5) {
            return BlockFace.NORTH_EAST;
        }
        if (67.5 <= rotation && rotation < 112.5) {
            return BlockFace.EAST;
        }
        if (112.5 <= rotation && rotation < 157.5) {
            return BlockFace.SOUTH_EAST;
        }
        if (157.5 <= rotation && rotation < 202.5) {
            return BlockFace.SOUTH;
        }
        if (202.5 <= rotation && rotation < 247.5) {
            return BlockFace.SOUTH_WEST;
        }
        if (247.5 <= rotation && rotation < 292.5) {
            return BlockFace.WEST;
        }
        if (292.5 <= rotation && rotation < 337.5) {
            return BlockFace.NORTH_WEST;
        }
        if (337.5 <= rotation && rotation <= 360) {
            return BlockFace.NORTH;
        }

        return BlockFace.WEST;
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {

        if (event.getPlayer().getWorld().getEnvironment() != World.Environment.NORMAL) {
            event.getPlayer().sendMessage(instance.format("&cSolo puedes dormir en el Overworld."));
            return;
        }

        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) {

            event.getPlayer().sendMessage(instance.format("&cNo puedes dormir ahora."));
            return;
        }

        if (Main.getInstance().getDays() >= 20) {
            Location playerbed = event.getBed().getLocation().add(0, 1, 0);

            instance.world.playSound(playerbed, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
            instance.world.spawnParticle(Particle.EXPLOSION_HUGE, playerbed, 1);

            if (Main.getInstance().getDays() >= 50) {
                if (new SplittableRandom().nextInt(100) + 1 <= 10) {

                    event.getPlayer().sendMessage(Main.getInstance().format(tag + " &aHas restablecido el contador de Phantoms."));
                    event.getPlayer().setStatistic(Statistic.TIME_SINCE_REST, 0);
                }
            } else {
                event.getPlayer().sendMessage(Main.getInstance().format(tag + " &aHas restablecido el contador de Phantoms."));
                event.getPlayer().setStatistic(Statistic.TIME_SINCE_REST, 0);
            }

            event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();
        long time = instance.world.getTime();

        int neededPlayers = 1;

        if (instance.getDays() >= 10) {

            neededPlayers = 4;
        }

        if (Bukkit.getOnlinePlayers().size() < neededPlayers) {

            player.sendMessage(format("&cNo puedes dormir porque no hay suficientes personas en línea (" + neededPlayers + ")."));
            event.setCancelled(true);
            return;
        }

        if (time < 13000) {

            player.sendMessage(instance.format("&cSolo puedes dormir de noche."));
            event.setCancelled(true);
            return;
        }

        if (Main.getInstance().getDays() < 10 && time >= 13000) {

            ArrayList<Player> sent = new ArrayList<>();

            Bukkit.getServer().getScheduler().runTaskLater(instance, new Runnable() {

                @Override
                public void run() {

                    event.getPlayer().getWorld().setTime(0L);
                    player.setStatistic(Statistic.TIME_SINCE_REST, 0);

                    if (!sent.contains(player)) {

                        //Bukkit.broadcastMessage(instance.format(Objects.requireNonNull(instance.getConfig().getString("Server-Messages.Sleep").replace("%player%", player.getName()))));

                        Bukkit.getOnlinePlayers().forEach(p -> {

                            String msg = Main.getInstance().getMessages().getMessage("Sleep", p).replace("%player%", player.getName());

                            p.sendMessage(msg);

                        });

                        Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("Sleep").replace("%player%", player.getName()));

                        sent.add(player);
                        player.damage(0.1);
                    }
                }
            }, 60L);
        }

        if (Main.getInstance().getDays() >= 10 && Main.getInstance().getDays() <= 19 && time >= 13000) {

            globalSleeping.add(player);

            Bukkit.getOnlinePlayers().forEach(p -> {

                String msg = Main.getInstance().getMessages().getMessage("Sleeping", p).replace("%needed%", String.valueOf(4)).replace("%players%", String.valueOf(globalSleeping.size())).replace("%player%", player.getName());

                p.sendMessage(msg);

            });

            Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("Sleeping").replace("%needed%", String.valueOf(4)).replace("%players%", String.valueOf(globalSleeping.size())).replace("%player%", player.getName()));

            if (globalSleeping.size() >= neededPlayers && globalSleeping.size() < Bukkit.getOnlinePlayers().size()) {

                Bukkit.getServer().getScheduler().runTaskLater(instance, new Runnable() {
                    @Override
                    public void run() {

                        if (globalSleeping.size() >= 4) {

                            event.getPlayer().getWorld().setTime(0L);

                            for (Player all : Bukkit.getOnlinePlayers()) {
                                if (all.isSleeping()) {

                                    all.setStatistic(Statistic.TIME_SINCE_REST, 0);
                                    all.damage(0.1);
                                    Bukkit.broadcastMessage(instance.format(Objects.requireNonNull(instance.getConfig().getString("Server-Messages.Sleep").replace("%player%", all.getName()))));
                                }
                            }

                            Bukkit.broadcastMessage(instance.format("&eHan dormido suficientes jugadores (&b4&e)."));
                            globalSleeping.clear();
                        }
                    }
                }, 40L);
            }

            if (globalSleeping.size() == Bukkit.getOnlinePlayers().size()) {

                event.getPlayer().getWorld().setTime(0L);

                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.setStatistic(Statistic.TIME_SINCE_REST, 0);
                    all.damage(0.1);
                    Bukkit.broadcastMessage(instance.format(Objects.requireNonNull(instance.getConfig().getString("Server-Messages.Sleep").replace("%player%", all.getName()))));
                }

                Bukkit.broadcastMessage(instance.format("&eHan dormido todos los jugadores."));

                globalSleeping.clear();
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent e) {

        Player p = e.getPlayer();

        if (p.getWorld().getEnvironment() != World.Environment.NORMAL) {
            return;
        }

        if (sleeping.contains(p)) {

            sleeping.remove(p);
        }

        if (globalSleeping.contains(p)) {

            globalSleeping.remove(p);
        }

        if (p.getWorld().getTime() >= 0 && p.getWorld().getTime() < 13000) {

            return;
        }

        p.sendMessage(instance.format("&eHas abandonado la cama, ya no contarás para pasar la noche."));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        
        e.setJoinMessage(null);
        Bukkit.getOnlinePlayers().forEach(p -> {
            String JoinMessage = Main.getInstance().getMessages().getMessage("OnJoin", p).replace("%player%", e.getPlayer().getName());
            p.sendMessage(JoinMessage);
        });

        Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("OnJoin").replace("%player%", player.getName()));
        PlayerDataManager dataManager = new PlayerDataManager(e.getPlayer().getName(), instance);

        if (instance.getShulkerEvent().isRunning()) {
            instance.getShulkerEvent().addPlayer(e.getPlayer());
        }

        if (instance.getOrbEvent().isRunning()) {
            instance.getOrbEvent().addPlayer(e.getPlayer());
        }

        if (player.isOp()) {
            player.sendMessage(instance.tag + instance.format("&eSi tienes algún problema puedes unirte a nuestro discord, enlace con: &9/pdc discord"));
            new UpdateChecker(Main.getInstance()).getVersion(version -> {
                if (Main.getInstance().getDescription().getVersion().equalsIgnoreCase(version)) {
                    player.sendMessage(Main.getInstance().format(tag + "&3Estás utilizando la versión más reciente del Plugin."));
                } else {
                    player.sendMessage(Main.getInstance().format(tag + "&3Se ha encontrado una nueva versión del Plugin"));
                    player.sendMessage(Main.getInstance().format(tag + "&eDescarga en: &7http://permadeathcore.com/"));
                }
            });
        }

        if (instance.getDays() >= 50) {

            if (instance.getBeginningManager() != null) {

                if (instance.getBeginningManager().isClosed() && e.getPlayer().getWorld().getName().equalsIgnoreCase("pdc_the_beginning")) {

                    e.getPlayer().teleport(instance.world.getSpawnLocation());

                }
            }
        }

        if (instance.getBeginningManager() != null) {
            if (instance.getBeginningManager().getBeginningWorld() != null) {

                if (instance.getBeginningManager().isClosed() && e.getPlayer().getWorld().getName().equalsIgnoreCase(instance.getBeginningManager().getBeginningWorld().getName())) {
                    e.getPlayer().teleport(instance.world.getSpawnLocation());
                }

                if (!instance.getBeData().generatedOverWorldBeginningPortal()) {
                    instance.getBeginningManager().generatePortal(true, null);
                }

                if (!instance.getBeData().generatedBeginningPortal()) {
                    instance.getBeginningManager().generatePortal(false, new Location(instance.getBeginningManager().getBeginningWorld(), 50, 140, 50));
                    instance.getBeginningManager().getBeginningWorld().setSpawnLocation(new Location(instance.getBeginningManager().getBeginningWorld(), 50, 140, 50));
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Bukkit.getOnlinePlayers().forEach(p -> {
            String JoinMessage = Main.getInstance().getMessages().getMessage("OnLeave", p).replace("%player%", e.getPlayer().getName());
            p.sendMessage(JoinMessage);

        });

        Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("OnLeave").replace("%player%", e.getPlayer().getName()));
        instance.getShulkerEvent().removePlayer(e.getPlayer());
        instance.getOrbEvent().removePlayer(e.getPlayer());

        Player p = e.getPlayer();

        if (sleeping.contains(p)) {
            sleeping.remove(p);
        }

        if (globalSleeping.contains(p)) {
            globalSleeping.remove(p);
        }
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {

        if (instance.getConfig().getBoolean("anti-afk-enabled")) {

            if (instance.getConfig().getStringList("AntiAFK.Bypass").contains(e.getName())) {
                return;
            }

            PlayerDataManager dataManager = new PlayerDataManager(e.getName(), instance);

            long actualDay = instance.getDays();
            long lastConection = dataManager.getLastDay();
            if (actualDay < lastConection) {
                dataManager.setLastDay(actualDay);
                return;
            }

            OfflinePlayer off = Bukkit.getOfflinePlayer(e.getName());

            if (off == null) return;

            if (off.isBanned() || !off.isWhitelisted()) return;

            long result = actualDay - lastConection;

            if (result >= instance.getConfig().getInt("AntiAFK.DaysForBan")) {

                String reason = instance.format(
                        "&c&lHas sido PERMABANEADO\n" +
                                "&eRazón: AFK\n" +
                                "&7Si crees que es un\n" +
                                "&7error, contacta un\n" +
                                "&7administrador."
                );

                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, reason);
                Bukkit.getBanList(BanList.Type.NAME).addBan(e.getName(), reason, null, "console");
                DiscordManager.getInstance().banPlayer(Bukkit.getOfflinePlayer(e.getName()), true);
            } else {
                dataManager.setLastDay(instance.getDays());
            }
        }
    }

    @EventHandler
    public void onAirChange(EntityAirChangeEvent e) {
        if (!(e.getEntity() instanceof Player) || instance.getDays() < 50) return;

        Player p = (Player) e.getEntity();

        if (p.getRemainingAir() < e.getAmount()) return;

        int speed = (instance.getDays() < 60 ? 5 : 10);
        Double damage = (instance.getDays() < 60 ? 5.0D : 10.0D);

        if (e.getAmount() < 20) return;
        int seconds = e.getAmount() / 20;
        int remain = seconds / speed;
        int newAmount = remain * 20;

        if (remain <= 0) {
            newAmount = 0;
            e.setAmount(newAmount);
            instance.getNmsAccesor().drown(p, damage);
            return;
        }

        e.setAmount(newAmount);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {

        if (instance.getDays() >= 40) {
            if (e.getItem().hasItemMeta()) {
                if (e.getItem().getItemMeta().hasDisplayName()) {

                    if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Main.getInstance().format("&6Super Golden Apple +"))) {
                        Player p = e.getPlayer();
                        int fmin = 60 * 5;
                        if (!p.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * fmin, 0));
                        }

                    } else if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Main.getInstance().format("&6Hyper Golden Apple +"))) {
                        if (instance.getDays() < 60) {
                            if (e.getPlayer().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_one"), PersistentDataType.BYTE)) {
                                e.getPlayer().sendMessage(instance.format(instance.tag + "&c¡Ya has comido una Hyper Golden Apple!"));
                                return;
                            }
                            e.getPlayer().sendMessage(instance.format(instance.tag + "&a¡Has obtenido contenedores de vida extra!"));
                            e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "hyper_one"), PersistentDataType.BYTE, (byte) 1);
                        } else {

                            boolean doPlayerAteOne = e.getPlayer().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_one"), PersistentDataType.BYTE);
                            boolean doPlayerAteTwo = e.getPlayer().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_two"), PersistentDataType.BYTE);

                            if (!doPlayerAteOne) {
                                e.getPlayer().sendMessage(instance.format(instance.tag + "&a¡Has obtenido contenedores de vida extra! &e(Hyper Golden Apple 1/2)"));
                                e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "hyper_one"), PersistentDataType.BYTE, (byte) 1);
                            } else {
                                if (doPlayerAteTwo) {
                                    e.getPlayer().sendMessage(instance.format(instance.tag + "&c¡Ya has comido una Hyper Golden Apple #2!"));
                                    return;
                                }
                                e.getPlayer().sendMessage(instance.format(instance.tag + "&a¡Has obtenido contenedores de vida extra! &e(Hyper Golden Apple 2/2)"));
                                e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "hyper_two"), PersistentDataType.BYTE, (byte) 1);
                            }
                        }
                    } else if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(instance.format("&6Super Golden Apple +"))) {
                        Player p = e.getPlayer();
                        int fmin = 60 * 5;
                        if (!p.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * fmin, 0));
                        }
                    }
                }
            }
        }


        if (Main.getInstance().getDays() >= 50) {
            if (e.getItem() != null) {
                if (e.getItem().getType() == Material.MILK_BUCKET) {
                    if (e.getPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                        PotionEffect effect = e.getPlayer().getPotionEffect(PotionEffectType.SLOW_DIGGING);
                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                            @Override
                            public void run() {

                                e.getPlayer().addPotionEffect(effect);
                            }
                        }, 10L);
                    }
                }

                if (e.getItem().getType() == Material.PUMPKIN_PIE) {
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 5, 0));
                }

                if (e.getItem().getType() == Material.SPIDER_EYE) {
                    Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
                        @Override
                        public void run() {
                            e.getPlayer().removePotionEffect(PotionEffectType.POISON);
                            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, 0));
                        }
                    }, 5L);
                }

                if (e.getItem().getType() == Material.PUFFERFISH) {
                    Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
                        @Override
                        public void run() {

                            e.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
                            e.getPlayer().removePotionEffect(PotionEffectType.POISON);
                            e.getPlayer().removePotionEffect(PotionEffectType.HUNGER);
                            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, 3));
                            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 2));
                            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 1));
                        }
                    }, 5L);
                }

                if (e.getItem().getType() == Material.ROTTEN_FLESH) {
                    Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
                        @Override
                        public void run() {

                            e.getPlayer().removePotionEffect(PotionEffectType.HUNGER);

                            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 1));
                        }
                    }, 5L);
                }

                if (e.getItem().getType() == Material.POISONOUS_POTATO) {
                    Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
                        @Override
                        public void run() {

                            e.getPlayer().removePotionEffect(PotionEffectType.POISON);

                            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, 0));
                        }
                    }, 5L);
                }
            }
        }

        if (Main.getInstance().getDays() >= 60) {

            ItemStack s = e.getItem();

            if (s != null) {

                if (s.getType() == Material.PUMPKIN_PIE) {

                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 3));
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {

        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && instance.getDays() >= 60) {
            e.getPlayer().setCooldown(Material.ENDER_PEARL, 6*20);
        }
    }

    @EventHandler
    public void onWC(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase(Main.getInstance().endWorld.getName())) {
            createRegenZone(e.getPlayer().getLocation());
        }
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent e) {
        if (Main.getInstance().getDays() >= 40) {
            if (e.getChunk().getWorld().getName().equalsIgnoreCase(Main.getInstance().endWorld.getName())) {
                for (Entity entity : e.getChunk().getEntities()) {
                    if (entity instanceof ItemFrame) {
                        ItemFrame frame = (ItemFrame) entity;
                        if (frame.getItem() != null) {
                            if (frame.getItem().getType() == Material.ELYTRA) {
                                ItemStack s = new ItemBuilder(Material.ELYTRA).setDurability(431).build();
                                frame.setItem(s);
                            }
                        }
                    }
                }
            }
        }
    }

    private void createRegenZone(Location playerZone) {

        EndDataManager ma = Main.getInstance().getEndData();

        if (!ma.getConfig().getBoolean("CreatedRegenZone")) {

            Location added = playerZone.add(-10, 0, 0);
            Location toGenerate = Main.getInstance().endWorld.getHighestBlockAt(added).getLocation();

            // * * *
            // * * *
            // * * *

            if (toGenerate.getY() == -1) {

                toGenerate.setY(playerZone.getY());
            }

            Block centerBlock = Main.getInstance().endWorld.getBlockAt(toGenerate);
            generateBlocks(true, toGenerate);
            generateBlocks(false, toGenerate);

            centerBlock.getRelative(BlockFace.UP).setType(Material.RED_CARPET);
            centerBlock.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(Material.SEA_LANTERN);
            centerBlock.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(Material.RED_CARPET);

            AreaEffectCloud a = (AreaEffectCloud) Main.getInstance().endWorld.spawnEntity(centerBlock.getRelative(BlockFace.UP).getLocation(), EntityType.AREA_EFFECT_CLOUD);
            a.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 0), false);
            a.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 0), false);
            a.setDuration(999999);
            a.setParticle(Particle.BLOCK_CRACK, Material.AIR.createBlockData());
            a.setRadius(4.0F);

            ma.getConfig().set("CreatedRegenZone", true);
            ma.getConfig().set("RegenZoneLocation", locationToString(a.getLocation()));
            ma.saveFile();
            ma.reloadFile();

            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Main.getInstance(), new Runnable() {
                @Override
                public void run() {

                    for (Entity ents : Main.getInstance().endWorld.getEntities()) {

                        if (ents.getType() == EntityType.ENDERMAN || ents.getType() == EntityType.CREEPER) {

                            Block b = ents.getLocation().getBlock().getRelative(BlockFace.DOWN);

                            int structure = new Random().nextInt(4);

                            ArrayList<Block> toChange = new ArrayList<>();

                            if (structure == 0) {

                                toChange.add(b.getRelative(BlockFace.NORTH));
                                toChange.add(b.getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST));
                                toChange.add(b.getRelative(BlockFace.SOUTH));
                                toChange.add(b.getRelative(BlockFace.SOUTH_EAST));
                                toChange.add(b.getRelative(BlockFace.SOUTH_WEST));
                                toChange.add(b.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.SOUTH));
                                toChange.add(b.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.NORTH));
                                toChange.add(b.getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH));
                            } else if (structure == 1) {

                                toChange.add(b.getRelative(BlockFace.NORTH));
                                toChange.add(b.getRelative(BlockFace.NORTH_EAST));
                                toChange.add(b);
                            } else if (structure == 2) {

                                toChange.add(b.getRelative(BlockFace.SOUTH));
                                toChange.add(b.getRelative(BlockFace.SOUTH_WEST));
                                toChange.add(b);
                            } else if (structure == 3) {

                                toChange.add(b.getRelative(BlockFace.NORTH));
                                toChange.add(b.getRelative(BlockFace.NORTH_EAST));
                                toChange.add(b);
                                toChange.add(b.getRelative(BlockFace.SOUTH));
                                toChange.add(b.getRelative(BlockFace.EAST));
                            } else if (structure == 4) {

                                toChange.add(b.getRelative(BlockFace.SOUTH));
                                toChange.add(b.getRelative(BlockFace.NORTH_WEST));
                                toChange.add(b);
                                toChange.add(b.getRelative(BlockFace.NORTH));
                                toChange.add(b.getRelative(BlockFace.WEST));
                            }

                            for (Block all : toChange) {

                                Location used = Main.getInstance().endWorld.getHighestBlockAt(new Location(Main.getInstance().endWorld, all.getX(), all.getY(), all.getZ())).getLocation();

                                Block now = Main.getInstance().endWorld.getBlockAt(used);

                                if (now.getType() == Material.END_STONE) {

                                    now.setType(Material.END_STONE_BRICKS);
                                }
                            }
                        }
                    }
                }
            }, 100L);
        }
    }

    private void generateBlocks(boolean b, Location toGenerate) {

        if (b) {

            ArrayList<Block> blocks = new ArrayList<>();

            Block centerBlock = Main.getInstance().endWorld.getBlockAt(toGenerate);
            blocks.add(centerBlock);

            blocks.add(Main.getInstance().endWorld.getBlockAt(toGenerate).getRelative(BlockFace.EAST));
            blocks.add(Main.getInstance().endWorld.getBlockAt(toGenerate).getRelative(BlockFace.WEST));

            blocks.add(centerBlock.getRelative(BlockFace.NORTH));
            blocks.add(centerBlock.getRelative(BlockFace.NORTH_WEST));
            blocks.add(centerBlock.getRelative(BlockFace.NORTH_EAST));

            blocks.add(centerBlock.getRelative(BlockFace.SOUTH));
            blocks.add(centerBlock.getRelative(BlockFace.SOUTH_WEST));
            blocks.add(centerBlock.getRelative(BlockFace.SOUTH_EAST));

            for (Block all : blocks) {

                all.setType(Material.RED_WOOL);
            }
        } else {

            ArrayList<Block> blocks = new ArrayList<>();
            Block centerBlockOfWool = Main.getInstance().endWorld.getBlockAt(toGenerate);

            Block corner1 = centerBlockOfWool.getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST);

            blocks.add(corner1);
            blocks.add(corner1.getRelative(BlockFace.WEST));
            blocks.add(corner1.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST));
            blocks.add(corner1.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST));

            // CORNER 2
            blocks.add(corner1.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST));

            blocks.add(corner1.getRelative(BlockFace.SOUTH));
            blocks.add(corner1.getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH));
            blocks.add(corner1.getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH));

            // CORNER 3
            Block southC = corner1.getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH);
            blocks.add(southC);

            blocks.add(southC.getRelative(BlockFace.WEST));
            blocks.add(southC.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST));
            blocks.add(southC.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST));

            // CORNER 4
            Block finalC = southC.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST);
            blocks.add(finalC);

            blocks.add(finalC.getRelative(BlockFace.NORTH));
            blocks.add(finalC.getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH));
            blocks.add(finalC.getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH));

            for (Block all : blocks) {

                all.setType(Material.RED_GLAZED_TERRACOTTA);
            }
        }
    }

    private String locationToString(Location loc) {
        return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getWorld().getName();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void restrictCrafting(PrepareItemCraftEvent event) {

        CraftPrepareManager manager = new CraftPrepareManager(event);

        manager.runCheckForLifeOrb();
        manager.runCheckForBeginningRelic();
        manager.runCheckForInfernalPiece();
        manager.runCheckForInfernalElytra();
        manager.runCheckForGaps();
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        CraftingInventory inventory = e.getInventory();

        if (inventory.getResult() != null) {

            ItemStack res = e.getRecipe().getResult();

            if (e.isCancelled() || e.getResult() != Event.Result.ALLOW) return;

            if (res.hasItemMeta()) {

                if (CustomItems.isEndRelic(res)) {

                    ItemMeta meta = res.getItemMeta();
                    meta.setLore(Arrays.asList(HiddenStringUtils.encodeString("{" + UUID.randomUUID().toString() + ": 0}")));
                    res.setItemMeta(meta);

                    e.setCurrentItem(res);
                    return;
                }

                if (res.isSimilar(CustomItems.createBeginningRelic()) || res.isSimilar(CustomItems.createLifeOrb())) {
                    if (e.getWhoClicked() instanceof Player) {
                        e.getInventory().setMatrix(clearMatrix());
                        Player p = (Player) e.getWhoClicked();
                        p.setItemOnCursor(res);
                    }
                }

                if (res.getItemMeta().hasDisplayName() && res.getItemMeta().getDisplayName().contains(instance.format("&6Hyper Golden Apple +")) || res.getItemMeta().getDisplayName().contains(instance.format("&6Super Golden Apple +"))) {
                    if (e.getWhoClicked() instanceof Player) {

                        e.getInventory().setMatrix(clearMatrix());

                        Player p = (Player) e.getWhoClicked();

                        p.setItemOnCursor(res);

                    }
                }
            }
        }
    }

    public ItemStack[] clearMatrix() {

        return new ItemStack[]{
                new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.AIR),
                new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.AIR),
                new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.AIR)};
    }

    private class CraftPrepareManager {

        private PrepareItemCraftEvent e;
        private ItemStack result;

        public CraftPrepareManager(PrepareItemCraftEvent e) {
            this.e = e;
            this.result = e.getInventory().getResult();
        }

        public void runCheckForBeginningRelic() {

            if (result == null) return;

            if (result.isSimilar(CustomItems.createBeginningRelic())) {
                int diamondBlocks = 0;
                int r = 0;
                for (ItemStack s : e.getInventory().getMatrix()) {
                    if (s != null) {
                        if (s.getType() == Material.DIAMOND_BLOCK) {
                            if (s.getAmount() >= 32) {
                                diamondBlocks++;
                            }
                        }
                        if (CustomItems.isEndRelic(s)) {
                            r++;
                        }
                    }
                }

                if (diamondBlocks < 4 || r < 1) {
                    e.getInventory().setResult(null);
                }

                if (diamondBlocks >= 4 && r >= 1) {
                    e.getInventory().setResult(CustomItems.createBeginningRelic());
                }
            }
        }

        public void runCheckForInfernalPiece() {
            if (result == null) return;
            if (instance.getDrops().isInfernalPiece(result)) {
                if (result.getType() == Material.ELYTRA) return;
                int diamondsFound = 0;
                boolean foundPiece = false;

                for (ItemStack item : e.getInventory().getMatrix()) {
                    if (item != null) {
                        if (item.hasItemMeta()) {
                            ItemMeta meta = item.getItemMeta();
                            if (item.getType() == Material.DIAMOND) {
                                if (meta.isUnbreakable() && ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("Infernal")) {
                                    diamondsFound = diamondsFound + 1;
                                }
                            }
                            if (instance.getDrops().isNetheritePiece(item)) {
                                foundPiece = true;
                            }
                        }
                    }
                }

                if (diamondsFound < 5 || !foundPiece) {
                    e.getInventory().setResult(null);
                }

                if (diamondsFound >= 4 && foundPiece) {

                    Material mat = result.getType();

                    if (mat == Material.LEATHER_HELMET) {
                        e.getInventory().setResult(instance.getInfernalNetherite().craftNetheriteHelmet());
                    }

                    if (mat == Material.LEATHER_CHESTPLATE) {
                        e.getInventory().setResult(instance.getInfernalNetherite().craftNetheriteChest());
                    }

                    if (mat == Material.LEATHER_LEGGINGS) {
                        e.getInventory().setResult(instance.getInfernalNetherite().craftNetheriteLegs());
                    }

                    if (mat == Material.LEATHER_BOOTS) {
                        e.getInventory().setResult(instance.getInfernalNetherite().craftNetheriteBoots());
                    }
                }
            }
        }

        public void runCheckForInfernalElytra() {
            if (result == null) return;
            if (result.getType() == Material.ELYTRA) {

                int diamondsFound = 0;

                for (ItemStack item : e.getInventory().getMatrix()) {
                    if (item != null) {
                        if (item.hasItemMeta()) {
                            ItemMeta meta = item.getItemMeta();
                            if (item.getType() == Material.DIAMOND) {
                                if (meta.isUnbreakable() && ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("Infernal")) {
                                    diamondsFound = diamondsFound + 1;
                                }
                            }
                        }
                    }
                }

                if (diamondsFound >= 8) {
                    e.getInventory().setResult(CustomItems.crearElytraInfernal());
                } else {
                    e.getInventory().setResult(null);
                }
            }
        }

        public void runCheckForGaps() {
            if (result == null) return;
            if (result.getItemMeta().getDisplayName().startsWith(instance.format("&6Hyper"))) {

                if (instance.getDays() < 60) {
                    int found = 0;

                    for (ItemStack item : e.getInventory().getMatrix()) {
                        if (item != null) {
                            if (item.getType() == Material.GOLD_BLOCK) {
                                if (item.getAmount() >= 8) {
                                    found = found + 1;
                                }
                            }
                        }
                    }

                    if (found >= 8) {
                        e.getInventory().setResult(new ItemBuilder(Material.GOLDEN_APPLE, 1).setDisplayName(instance.format("&6Hyper Golden Apple +")).addEnchant(Enchantment.ARROW_INFINITE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
                    } else {

                        e.getInventory().setResult(null);
                    }
                } else {
                    int found = 0;
                    boolean enoughGaps = false;

                    for (ItemStack item : e.getInventory().getMatrix()) {
                        if (item != null) {
                            if (item.getType() == Material.GOLD_BLOCK) {
                                if (item.getAmount() >= 8) {
                                    found = found + 1;
                                }
                            }

                            if (item.getType() == Material.GOLDEN_APPLE && item.getAmount() == 64) {
                                enoughGaps = true;
                            }
                        }
                    }

                    if (found >= 8 && enoughGaps) {
                        e.getInventory().setResult(new ItemBuilder(Material.GOLDEN_APPLE, 1).setDisplayName(instance.format("&6Hyper Golden Apple +")).addEnchant(Enchantment.ARROW_INFINITE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
                    } else {

                        e.getInventory().setResult(null);
                    }
                }
            }

            if (result.getItemMeta().getDisplayName().startsWith(instance.format("&6Super"))) {

                int found = 0;
                for (ItemStack item : e.getInventory().getMatrix()) {
                    if (item != null) {
                        if (item.getType() == Material.GOLD_INGOT) {
                            if (item.getAmount() >= 8) {
                                found++;
                            }
                        }
                    }
                }
                if (found < 8) {
                    e.getInventory().setResult(null);
                    return;
                }
                if (found >= 8) {
                    e.getInventory().setResult(new ItemBuilder(Material.GOLDEN_APPLE, 1).setDisplayName(instance.format("&6Super Golden Apple +")).addEnchant(Enchantment.ARROW_INFINITE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
                }
            }
        }

        public void runCheckForLifeOrb() {
            if (result == null) return;
            if (!result.isSimilar(CustomItems.createLifeOrb())) return;
            if (!instance.getOrbEvent().isRunning()) return;
            int items = 0;

            for (ItemStack s : e.getInventory().getMatrix()) {
                if (s != null) {
                    if (s.getType() == Material.HEART_OF_THE_SEA) {
                        items++;
                    } else {
                        if (s.getAmount() >= 64) {
                            items++;
                        }
                    }
                }
            }
            if (items < 9) {
                e.getInventory().setResult(null);
            }
            if (items >= 9) {
                e.getInventory().setResult(CustomItems.createLifeOrb());
            }
        }
    }
}
