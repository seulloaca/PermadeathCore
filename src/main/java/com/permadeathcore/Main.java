package com.permadeathcore;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Filter;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.permadeathcore.Discord.DiscordManager;
import com.permadeathcore.Listener.Player.AnvilListener;
import com.permadeathcore.Util.Configurations.Messages;
import com.permadeathcore.Listener.Block.BlockEvents;
import com.permadeathcore.Listener.Entity.EntityEvents;
import com.permadeathcore.Listener.Entity.TotemConsumeEvent;
import com.permadeathcore.Listener.Player.PlayerEvents;
import com.permadeathcore.Listener.Raid.RaidEvents;
import com.permadeathcore.Listener.World.WorldEvents;
import com.permadeathcore.Task.EndTask;
import com.permadeathcore.Entity.MobFactory.MobFactory;
import com.permadeathcore.Util.GameEvent.LifeOrbEvent;
import com.permadeathcore.Util.GameEvent.ShellEvent;
import com.permadeathcore.Listener.PaperSpigot.PaperListeners;
import com.permadeathcore.End.EndManager;
import com.permadeathcore.TheBeginning.Block.CustomBlock;
import com.permadeathcore.Listener.Entity.SkeletonClasses;
import com.permadeathcore.Listener.Entity.SpawnListener;
import com.permadeathcore.Listener.Player.VoidListeners;
import com.permadeathcore.Util.Library.UpdateChecker;
import com.permadeathcore.Util.Manager.Log.Log4JFilter;
import com.permadeathcore.Util.Manager.Log.PDCLog;
import com.permadeathcore.Util.Manager.Data.BeginningDataManager;
import com.permadeathcore.Util.Manager.Data.EndDataManager;
import com.permadeathcore.Util.Manager.Data.PlayerDataManager;
import com.permadeathcore.NMS.PeaceToHostileManager;
import com.permadeathcore.NMS.NMSAccesor;
import com.permadeathcore.NMS.NMSHandler;
import com.permadeathcore.NMS.VersionManager;
import com.permadeathcore.TheBeginning.BeginningManager;
import com.permadeathcore.NMS.NMSFinder;
import com.permadeathcore.Util.Library.FileAPI;
import com.permadeathcore.Util.Item.*;
import com.permadeathcore.Util.Manager.Data.DateManager;
import com.permadeathcore.Util.Manager.RecipeManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Main extends JavaPlugin implements Listener {

    public static boolean DEBUG = false;
    public static boolean DISABLED_LINGERING = false;

    public static boolean SPEED_RUN_MODE = false;
    private int playTime = 0;

    public static Main instance;
    public static String tag = "";

    // NMS
    private NMSHandler nmsHandler;
    private NMSAccesor nmsAccesor;

    private PeaceToHostileManager hostile;
    private RecipeManager recipes;
    private CustomBlock netheriteBlock;

    private Messages messages;

    public World world = null;
    public World endWorld = null;

    private EndTask task = null;

    private EndManager endManager;
    private MobFactory factory;

    private BeginningManager begginingManager;
    private BeginningDataManager beData;

    private EndDataManager endData;

    public static boolean runningPaperSpigot = false;

    private Map<Integer, Boolean> registeredDays = new HashMap<>();
    private ArrayList<Player> doneEffectPlayers = new ArrayList<>();

    private boolean loaded = false;
    private boolean alreadyRegisteredChanges = false;

    private ShellEvent shulkerEvent;
    private LifeOrbEvent orbEvent;

    private SpawnListener spawnListener;
    private SplittableRandom r = new SplittableRandom();

    private boolean fo

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        setupConsoleFilter();
        setupListeners();
        setupCommands();

        tag = format((getConfig().contains("Prefix") ? getConfig().getString("Prefix") : "&c&lPERMADEATH&4&lCORE &7➤ &f"));

        tickAll();

        this.playTime = getConfig().getInt("DontTouch.PlayTime");
    }

    @Override
    public void onLoad() {

        instance = this;

        NMSFinder f = new NMSFinder(this);

        this.nmsHandler = (NMSHandler) f.getNMSHandler();
        this.nmsAccesor = (NMSAccesor) f.getNMSAccesor();
        this.netheriteBlock = (CustomBlock) f.getCustomBlock();

        this.nmsAccesor.registerHostileMobs();
    }

    @Override
    public void onDisable() {

        getConfig().set("DontTouch.PlayTime", this.playTime);
        saveConfig();
        reloadConfig();

        DiscordManager.getInstance().onDisable();

        Bukkit.getConsoleSender().sendMessage(format("&f&m------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(format("         &c&lPERMADEATH&4&lCORE"));
        Bukkit.getConsoleSender().sendMessage(format("     &7- Desactivando el Plugin."));
        Bukkit.getConsoleSender().sendMessage(format("&f&m------------------------------------------"));

        this.instance = null;
    }

    private void tickAll() {
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {

                if (!getFile().exists()) {
                    saveDefaultConfig();
                }

                if (!loaded) {

                    if (Bukkit.getPluginManager().getPlugin("JDASpigot") != null) {
                        Bukkit.getConsoleSender().sendMessage(format(tag +"&aSe ha encontrado JDASpigot, cargando bot de Discord."));
                        DiscordManager.getInstance();
                    } else {
                        Bukkit.getConsoleSender().sendMessage(format(tag + "&cNo se ha encontrado JDASpigot, es necesario para utilizar el bot de Discord."));
                        Bukkit.getConsoleSender().sendMessage(format("&eDescarga aquí: &fhttps://www.dropbox.com/s/qdtqgfgv51lvag4/JDASpigot.jar?dl=0"));
                        Bukkit.getConsoleSender().sendMessage(format("&eSi no puedes descargarlo allí, únete a este Discord y te daremos acceso al enlace: &ehttps://discord.gg/8evPbuxPke"));
                    }

                    startPlugin();
                    setupConfig();

                    if (!getConfig().contains("config-version")) {
                        PDCLog.getInstance().log("Eliminando config.yml por versión antigua.");
                        getFile().delete();
                        saveDefaultConfig();
                    } else {
                        try {
                            int version = getConfig().getInt("config-version");
                            if (version != 2) {
                                Bukkit.getConsoleSender().sendMessage(format(tag + "&eEstamos eliminando config.yml debido a que está desactualizado."));
                                PDCLog.getInstance().log("Eliminando config.yml por versión antigua.");
                                getFile().delete();
                                saveDefaultConfig();
                            }
                        } catch (Exception x) {
                            getFile().delete();
                        }
                    }

                    if (getConfig().getBoolean("Toggles.Replace-Mobs-On-Chunk-Load")) {
                        for (World worlds : Bukkit.getWorlds()) {
                            for (LivingEntity liv : worlds.getLivingEntities()) {
                                spawnListener.applyDayChanges(liv);
                            }
                        }
                    }
                    loaded = true;
                }

                DateManager.getInstance().tick();
                registerListeners();

                if (Bukkit.getOnlinePlayers().size() >= 1 && SPEED_RUN_MODE) {
                    playTime++;

                    if (playTime % (3600) == 0) {
                        Bukkit.broadcastMessage(tag + format("&cFelicitaciones, han avanzado a la hora número: " + getDays()));
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.0F, 100.0F);
                        }
                    }
                }

                if (getDays() >= 60 && !getConfig().getBoolean("DontTouch.Event.LifeOrbEnded") && !getOrbEvent().isRunning()) {
                    if (SPEED_RUN_MODE) orbEvent.setTimeLeft(60*8);
                    orbEvent.setRunning(true);
                }

                tickEvents();
                tickPlayers();
                tickWorlds();
            }
        }, 0, 20L);
    }

    private void tickWorlds() {
        if (this.getDays() >= 40) {
            for (World w : Bukkit.getWorlds().stream().filter(world1 -> world1.getEnvironment() != World.Environment.THE_END).collect(Collectors.toList())) {
                for (Ravager ravager : w.getEntitiesByClass(Ravager.class)) {
                    if (ravager.getPersistentDataContainer().has(new NamespacedKey(instance, "ultra_ravager"), PersistentDataType.BYTE)) {
                        List<Block> b = ravager.getLineOfSight(null, 5);

                        for (Block block : b) {
                            for (int i = -1; i < 1; i++) {
                                for (int j = -1; j < 1; j++) {
                                    for (int k = -1; k < 1; k++) {
                                        Block r = block.getRelative(i, j, k);
                                        if (r.getType() == Material.NETHERRACK) {
                                            r.setType(Material.AIR);
                                            r.getWorld().playSound(r.getLocation(), Sound.BLOCK_STONE_BREAK, 2.0F, 1.0F);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void tickPlayers() {

        if (Bukkit.getOnlinePlayers().size() < 1) return;

        long segundosbrutos = world.getWeatherDuration() / 20;

        long hours = segundosbrutos % 86400 / 3600;
        long minutes = (segundosbrutos % 3600) / 60;
        long seconds = segundosbrutos % 60;
        long days = segundosbrutos / 86400;

        final String time = String.format((days >= 1 ? String.format("%02d día(s) ", days) : "") + "%02d:%02d:%02d", hours, minutes, seconds);

        for (Player player : Bukkit.getOnlinePlayers()) {

            World w = player.getWorld();

            if (this.shulkerEvent.isRunning()) {
                if (!this.shulkerEvent.getBossBar().getPlayers().contains(player)) {
                    this.shulkerEvent.getBossBar().addPlayer(player);
                }
            }

            if (this.orbEvent.isRunning()) {
                if (!this.orbEvent.getBossBar().getPlayers().contains(player)) {
                    this.orbEvent.getBossBar().addPlayer(player);
                }
            }

            NetheriteArmor.setupHealth(player);
            PermaDeathItems.slotBlock(player);

            if (SPEED_RUN_MODE) {
                String actionBar = "";

                if (world.hasStorm()) {
                    actionBar = getMessages().getMessageByPlayer("Server-Messages.ActionBarMessage", player.getName()).replace("%tiempo%", time) + " - ";
                }
                actionBar = actionBar + ChatColor.GRAY + "Tiempo total: " + formatInterval(playTime);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));

            } else {
                if (world.hasStorm()) {
                    String msg = getMessages().getMessageByPlayer("Server-Messages.ActionBarMessage", player.getName()).replace("%tiempo%", time);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
                }
            }

            if (player.getWorld().getEnvironment() == World.Environment.THE_END && getDays() >= 30) {
                if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 10 * 20, 9));
                }
                if (player.getWorld().getName().equalsIgnoreCase("pdc_the_beginning")) {
                    if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    }
                }
            }

            if (getDays() >= 40) {

                if (player.getWorld().hasStorm()) {
                    Location block = player.getWorld().getHighestBlockAt(player.getLocation().clone()).getLocation();
                    int highestY = block.getBlockY();

                    if (highestY < player.getLocation().getY()) {

                        int probability = r.nextInt(10000) + 1;

                        int blind = (getDays() < 50 ? 1 : 300);
                        if (probability <= blind) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 60, 0));
                        }
                        if (getDays() >= 50) {
                            if (probability == 301) {
                                int duration = r.nextInt(17);
                                duration = duration + 3;
                                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, duration * 20, 0));
                            }
                        }
                    }
                }
            }

            if (getDays() >= 50) {

                if (getBeData() != null && getBeginningManager() != null) {

                    BeginningDataManager data = getBeData();
                    World beginningWorld = getBeginningManager().getBeginningWorld();

                    if (!data.killedED()) {
                        Chunk c = beginningWorld.getBlockAt(0, 100, 0).getChunk();
                        for (int X = 0; X < 16; X++)
                            for (int y = beginningWorld.getMaxHeight() - 1; y > 0; y--)
                                for (int Z = 0; Z < 16; Z++) {
                                    Block b = c.getBlock(X, y, Z);
                                    if (b.getType() == Material.END_GATEWAY || b.getType() == Material.BEDROCK) {
                                        b.setType(Material.AIR);
                                    }
                                }
                        if (beginningWorld.getEntitiesByClass(EnderDragon.class).size() >= 1) {
                            for (EnderDragon d : beginningWorld.getEntitiesByClass(EnderDragon.class)) {
                                d.remove();
                            }
                            data.setKilledED();
                        }
                    }
                }

                if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                    PotionEffect e = player.getPotionEffect(PotionEffectType.SLOW_DIGGING);
                    if (e.getDuration() >= 4 * 60 * 20 && !getDoneEffectPlayers().contains(player)) {
                        int min = 10 * 60;
                        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, min * 20, 2));
                        getDoneEffectPlayers().add(player);
                    }

                    if (e.getDuration() == 4 * 60 * 20 - 1 && getDoneEffectPlayers().contains(player)) {
                        getDoneEffectPlayers().remove(player);
                    }
                }

                if (player.getWorld().getEnvironment() == World.Environment.NETHER && getDays() < 60) {

                    int random = r.nextInt(4500) + 1;

                    if (random <= 10 && player.getWorld().getLivingEntities().size() < 110) {

                        Location ploc = player.getLocation().clone();

                        ArrayList<Location> spawns = new ArrayList<>();
                        spawns.add(ploc.clone().add(10, 25, -5));
                        spawns.add(ploc.clone().add(5, 25, 5));
                        spawns.add(ploc.clone().add(-5, 25, 5));

                        for (Location l : spawns) {
                            if (w.getBlockAt(l).getType() == Material.AIR && w.getBlockAt(l.clone().add(0, 1, 0)).getType() == Material.AIR) {
                                int randomEntities = r.nextInt(3) + 1;
                                for (int i = 0; i < randomEntities; i++) {
                                    getNmsHandler().spawnNMSEntity("PigZombie", EntityType.valueOf(VersionManager.isRunningNetherUpdate() ? "ZOMBIFIED_PIGLIN" : "PIG_ZOMBIE"), l, CreatureSpawnEvent.SpawnReason.CUSTOM);
                                }
                            }
                        }
                    }
                }
            }

            if (getDays() >= 60) {
                if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SOUL_SAND) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * 20, 2));
                }
                Integer timeForWither = player.getPersistentDataContainer().get(new NamespacedKey(this, "wither"), PersistentDataType.INTEGER);
                if (timeForWither == null) {
                    timeForWither = 0;
                }
                player.getPersistentDataContainer().set(new NamespacedKey(this, "wither"), PersistentDataType.INTEGER, ++timeForWither);
                if (timeForWither % (60 * 60) == 0 && player.getGameMode() == GameMode.SURVIVAL) {
                    Wither wither = player.getWorld().spawn(player.getLocation().clone().add(0, 5, 0), Wither.class);
                    try {
                        Object nmsw = wither.getClass().getDeclaredMethod("getHandle").invoke(wither);
                        nmsw.getClass().getDeclaredMethod("r", int.class).invoke(nmsw, 100);
                    } catch (Exception x) {}
                }

                if (getConfig().getBoolean("Toggles.Mike-Creeper-Spawn")) {

                    Location l = player.getLocation().clone();

                    if (r.nextInt(30) == 0 && player.getNearbyEntities(30, 30, 30)
                            .stream()
                            .filter(entity -> entity instanceof Creeper)
                            .map(Creeper.class::cast)
                            .collect(Collectors.toList()).size() < 10) {
                        int pX = (r.nextBoolean() ? -1 : 1) * (r.nextInt(15)) + 15;
                        int pZ = (r.nextBoolean() ? -1 : 1) * (r.nextInt(15)) + 15;
                        int y = (int) l.getY();

                        Block block = l.getWorld().getBlockAt(l.getBlockX() + pX, y, l.getBlockZ() + pZ);
                        Block up = block.getRelative(BlockFace.UP);

                        if (block.getType() != Material.AIR && up.getType() == Material.AIR) {
                            getFactory().spawnEnderQuantumCreeper(up.getLocation(), null);
                        }
                    }
                }
            }
        }
    }

    private void tickEvents() {

        if (this.orbEvent.isRunning()) {
            if (this.orbEvent.getTimeLeft() > 0) {

                this.orbEvent.reduceTime();

                int res = this.orbEvent.getTimeLeft();

                int hrs = res / 3600;
                int minAndSec = res % 3600;
                int min = minAndSec / 60;
                int sec = minAndSec % 60;

                String tiempo = String.format("%02d:%02d:%02d", hrs, min, sec);

                this.orbEvent.getBossBar().setColor(BarColor.values()[r.nextInt(BarColor.values().length)]);
                this.orbEvent.setTitle(instance.format("&6&l" + tiempo + " para obtener el Life Orb"));
            } else {

                Bukkit.broadcastMessage(instance.format(instance.tag + "&cSe ha acabado el tiempo para obtener el Life Orb, ¡sufrid! ahora tendreís 8 contenedores de vida menos."));
                this.orbEvent.setRunning(false);
                this.orbEvent.clearPlayers();
                this.orbEvent.setTimeLeft((SPEED_RUN_MODE ? 60*8 : 60 * 60 * 8));
                this.orbEvent.getBossBar().removeAll();

                getConfig().set("DontTouch.Event.LifeOrbEnded", true);
                saveConfig();
                reloadConfig();
            }
        }

        if (this.shulkerEvent.isRunning()) {

            if (this.shulkerEvent.getTimeLeft() > 0) {

                this.shulkerEvent.setTimeLeft(this.shulkerEvent.getTimeLeft() - 1);

                int res = this.shulkerEvent.getTimeLeft();

                int hrs = res / 3600;
                int minAndSec = res % 3600;
                int min = minAndSec / 60;
                int sec = minAndSec % 60;

                String tiempo = String.format("%02d:%02d:%02d", hrs, min, sec);

                this.shulkerEvent.setTitle(instance.format("&e&lX2 Shulker Shells: &b&n" + tiempo));
            } else {

                Bukkit.broadcastMessage(instance.format(instance.tag + "&eEl evento de &c&lX2 Shulker Shells &eha acabado."));
                this.shulkerEvent.setRunning(false);
                this.shulkerEvent.clearPlayers();
                this.shulkerEvent.setTimeLeft(60 * 60 * 4);
                this.shulkerEvent.getBossBar().removeAll();
            }
        }
    }

    private void startPlugin() {
        this.messages = new Messages(this);
        this.shulkerEvent = new ShellEvent(this);
        this.orbEvent = new LifeOrbEvent(this);
        this.factory = new MobFactory(this);

        new FileAPI.FileOut(instance, "beginning_portal.schem", "schematics/", true);
        new FileAPI.FileOut(instance, "ytic.schem", "schematics/", true);
        new FileAPI.FileOut(instance, "island1.schem", "schematics/", true);
        new FileAPI.FileOut(instance, "island2.schem", "schematics/", true);
        new FileAPI.FileOut(instance, "island3.schem", "schematics/", true);
        new FileAPI.FileOut(instance, "island4.schem", "schematics/", true);
        new FileAPI.FileOut(instance, "island5.schem", "schematics/", true);

        int HelmetValue = Integer.parseInt(Objects.requireNonNull(instance.getConfig().getString("Toggles.Netherite.Helmet")));
        int ChestplateValue = Integer.parseInt(Objects.requireNonNull(instance.getConfig().getString("Toggles.Netherite.Chestplate")));
        int LeggingsValue = Integer.parseInt(Objects.requireNonNull(instance.getConfig().getString("Toggles.Netherite.Leggings")));
        int BootsValue = Integer.parseInt(Objects.requireNonNull(instance.getConfig().getString("Toggles.Netherite.Boots")));
        if (HelmetValue > 100 || HelmetValue < 1) {
            System.out.println("[ERROR] Error al cargar la probabilidad de 'Helmet' en 'config.yml', asegurate de introducir un numero valido del 1 al 100.");
            System.out.println("[ERROR] Ha ocurrido un error al cargar el archivo config.yml, si este error persiste avisanos por discord.");
        }
        if (ChestplateValue > 100 || ChestplateValue < 1) {
            System.out.println("[ERROR] Error al cargar la probabilidad de 'Chestplate' en 'config.yml', asegurate de introducir un numero valido del 1 al 100.");
            System.out.println("[ERROR] Ha ocurrido un error al cargar el archivo config.yml, si este error persiste avisanos por discord.");
        }
        if (LeggingsValue > 100 || LeggingsValue < 1) {
            System.out.println("[ERROR] Error al cargar la probabilidad de 'Leggings' en 'config.yml', asegurate de introducir un numero valido del 1 al 100.");
            System.out.println("[ERROR] Ha ocurrido un error al cargar el archivo config.yml, si este error persiste avisanos por discord.");
        }
        if (BootsValue > 100 || BootsValue < 1) {
            System.out.println("[ERROR] Error al cargar la probabilidad de 'BootsValue' en 'config.yml', asegurate de introducir un numero valido del 1 al 100.");
            System.out.println("[ERROR] Ha ocurrido un error al cargar el archivo config.yml, si este error persiste avisanos por discord.");
        }
        String compatibleVersion = "&cIncompatible";

        if (VersionManager.getFormatedVersion().equalsIgnoreCase("1.15.x") || VersionManager.getFormatedVersion().equalsIgnoreCase("1.14.x") || VersionManager.getFormatedVersion().equalsIgnoreCase("1.16.x")) {
            compatibleVersion = "&aCompatible";
        }

        String software = "";
        try {
            if (Class.forName("org.spigotmc.SpigotConfig") != null) {
                software = "SpigotMC (Compatible)";
            }
        } catch (ClassNotFoundException e) {
            software = "Bukkit";
        }

        try {
            if (Class.forName("com.destroystokyo.paper.PaperConfig") != null) {
                software = "PaperMC (Compatible)";
                runningPaperSpigot = true;
            }
        } catch (ClassNotFoundException e) {
        }

        String worldState = setupWorld();

        Bukkit.getConsoleSender().sendMessage(format("&f&m------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(format("         &c&lPERMADEATH&4&lCORE"));
        Bukkit.getConsoleSender().sendMessage(format("     &7- Versión: &e" + this.getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(format("     &7- Versión del Servidor: &e" + VersionManager.getFormatedVersion()));
        Bukkit.getConsoleSender().sendMessage(format("&f&m------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(format(" &7>> &e&lINFORME DE ESTADO"));
        Bukkit.getConsoleSender().sendMessage(format("&7> &bEstado de mundos: " + worldState));
        Bukkit.getConsoleSender().sendMessage(format("    &7> &eEnd&7: &8" + this.endWorld.getName()));
        Bukkit.getConsoleSender().sendMessage(format("    &7> &aOverworld&7: &8" + this.world.getName()));
        Bukkit.getConsoleSender().sendMessage(format("&7> &bEstado de Compatibilidad: " + compatibleVersion));
        Bukkit.getConsoleSender().sendMessage(format("&7> &bSoftware: " + software));
        Bukkit.getConsoleSender().sendMessage(format("&7> &b&lCambios:"));
        Bukkit.getConsoleSender().sendMessage(format("&7>   &aDías: &71-60"));

        if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
            Bukkit.getConsoleSender().sendMessage(format("&7> &4&lADVERTENCIA: &7No se ha encontrado el Plugin &7World Edit"));
            Bukkit.getConsoleSender().sendMessage(format("&7> &7Si deseas usar PermadeathCore instala &e&lWorldEdit&7."));
            PDCLog.getInstance().log("No se encontró WorldEdit");
        }

        if (software.contains("Bukkit")) {
            Bukkit.broadcastMessage(format(tag +"&7> &4&lADVERTENCIA&7: &eEl plugin NO es compatible con CraftBukkit, cambia a SpigotMC o PaperSpigot"));
            PDCLog.getInstance().disable("No es compatible con Bukkit.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        new UpdateChecker(this).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getConsoleSender().sendMessage(format("&7> &bEstado de Actualización: &aVersión más reciente."));
            } else {
                Bukkit.getConsoleSender().sendMessage(format("&7> &eNueva versión detectada."));
                Bukkit.getConsoleSender().sendMessage(format("&7> &aDescarga: &7http://permadeathcore.com/"));
            }
        });

        registerChanges();
        generateOfflinePlayerData();

        PDCLog.getInstance().log("Se ha activado el plugin.");
    }

    protected void registerListeners() {

        String prefix = "&e[PermaDeathCore] &7> ";

        if (!registeredDays.get(1)) {
            registeredDays.replace(1, true);

            this.getServer().getPluginManager().registerEvents(new AnvilListener(this), this);
        }

        if (DateManager.getInstance().getDays() >= 20 && !registeredDays.get(20)) {
            registeredDays.replace(20, true);

            this.hostile = new PeaceToHostileManager(this);
            getServer().getPluginManager().registerEvents(hostile, instance);
            Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado los cambios de Mobs pacíficos hostiles."));
        }

        if (DateManager.getInstance().getDays() >= 30 && endManager == null && endData == null && !registeredDays.get(30)) {
            registeredDays.replace(30, true);

            this.endManager = new EndManager(instance);
            getServer().getPluginManager().registerEvents(endManager, instance);

            this.endData = new EndDataManager(instance);

            if (runningPaperSpigot) {

                getServer().getPluginManager().registerEvents(new PaperListeners(instance), instance);
                Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado cambios especiales para &c&lPaperMC&e."));
            }
        }

        if (DateManager.getInstance().getDays() >= 40 && !registeredDays.get(40)) {

            registeredDays.replace(40, true);
            if (this.recipes == null) this.recipes = new RecipeManager(this);
            this.recipes.registerRecipes();
            this.getNmsHandler().addMushrooms();
            getServer().getPluginManager().registerEvents(new VoidListeners(instance), instance);
            Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado cambios para el día &b40"));

            if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
                Bukkit.broadcastMessage(format(prefix + "&4&lNo se pudo registrar TheBeginning ya que no se ha encontrado el plugin &7WorldEdit"));
                Bukkit.broadcastMessage(format(prefix + "&7Si necesitas soporte entra a este discord: &ehttps://discord.gg/peM8c6y"));
                return;
            }
            this.beData = new BeginningDataManager(this);
            this.begginingManager = new BeginningManager(this);
            Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado cambios de TheBeginning"));
        }

        if (DateManager.getInstance().getDays() >= 50 && !registeredDays.get(50)) {

            if (this.recipes == null) {
                this.recipes = new RecipeManager(this);
                this.recipes.registerRecipes();
            }

            Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado cambios para el día &b50"));
            this.recipes.registerD50Recipes();
            registeredDays.replace(50, true);
        }

        if (DateManager.getInstance().getDays() >= 60 && !registeredDays.get(60)) {

            if (this.recipes == null) {
                this.recipes = new RecipeManager(this);
                this.recipes.registerRecipes();
                this.recipes.registerD50Recipes();
            }

            this.recipes.registerD60Recipes();
            Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado cambios para el día &b60"));
            registeredDays.replace(60, true);
        }
    }

    protected void registerChanges() {

        if (alreadyRegisteredChanges) return;
        alreadyRegisteredChanges = true;
    }

    public void generateOfflinePlayerData() {

        for (OfflinePlayer off : Bukkit.getOfflinePlayers()) {

            if (off == null) return;

            PlayerDataManager manager = new PlayerDataManager(off.getName(), this);
            manager.generateDayData();
        }
    }

    protected String setupWorld() {

        if (Bukkit.getWorld(Objects.requireNonNull(instance.getConfig().getString("Worlds.MainWorld"))) == null) {

            for (World w : Bukkit.getWorlds()) {
                if (w.getEnvironment() == World.Environment.NORMAL) {
                    this.world = w;
                    break;
                }
            }

            System.out.println("[ERROR] Error al cargar el mundo principal, esto hará que los Death Train no se presenten.");
            System.out.println("[ERROR] Tan solo ve a config.yml y establece el mundo principal en la opción: MainWorld");
            System.out.println("[INFO] El plugin utilizará el mundo " + world.getName() + " como mundo principal.");
            System.out.println("[INFO] Si deseas utilizar otro mundo, configura en el archivo config.yml.");

        } else {
            world = Bukkit.getWorld(Objects.requireNonNull(instance.getConfig().getString("Worlds.MainWorld")));
        }

        if (Bukkit.getWorld(Objects.requireNonNull(instance.getConfig().getString("Worlds.EndWorld"))) == null) {

            System.out.println("[ERROR] Error al cargar el mundo del end, esto hará que el end no funcione como debe.");
            System.out.println("[ERROR] Tan solo ve a config.yml y establece el mundo del end en la opción: EndWorld");

            for (World w : Bukkit.getWorlds()) {
                if (w.getEnvironment() == World.Environment.THE_END) {
                    this.endWorld = world;
                    System.out.println("[INFO] El plugin utilizará el mundo " + w.getName() + " como mundo del End.");
                    break;
                }
            }

        } else {
            endWorld = Bukkit.getWorld(Objects.requireNonNull(instance.getConfig().getString("Worlds.EndWorld")));
        }

        for (World w : Bukkit.getWorlds()) {
            if (Objects.requireNonNull(getConfig().getBoolean("Toggles.Doble-Mob-Cap")) && getDays() >= 10) {
                Bukkit.getConsoleSender().sendMessage(tag + "&eDoblando la mob-cap en todos los mundos.");
                w.setMonsterSpawnLimit(140);
            }

            if (this.isRunningPaperSpigot()) {
                try {
                    Object nmsW = w.getClass().getDeclaredMethod("getHandle").invoke(w);

                    //Object nmsW = w.getClass().getDeclaredMethod("getHandle").invoke(w);
                    //CraftWorld test;
                    //test.getHandle().paperConfig.disableCreeperLingeringEffect;

                    final Field f = nmsW.getClass().getDeclaredField("paperConfig");
                    f.setAccessible(true);
                    Object paperConfig = f.get(nmsW);

                    final Field creepers = paperConfig.getClass().getDeclaredField("disableCreeperLingeringEffect");
                    creepers.setAccessible(true);
                    creepers.set(paperConfig, true);

                    Bukkit.getConsoleSender().sendMessage(tag + "&eDeshabilitando Creeper-Lingering-Effect...");

                } catch (Exception x) {
                }
            }
        }

        return "&aOverworld: &b" + this.world.getName() + " &eEnd: &b" + this.endWorld.getName();
    }

    protected void reload(CommandSender sender) {

        this.setupConfig();
        reloadConfig();
        this.messages.reloadFiles();
        DateManager.getInstance().reloadDate();
        setupWorld();

        sender.sendMessage(instance.format("&aSe ha recargado el archivo de configuración y los mensajes."));
        sender.sendMessage(instance.format("&eAlgunos cambios pueden requerir un reinicio para funcionar correctamente."));
        sender.sendMessage(format("&c&lNota importante: &7Algunos cambios pueden requerir un reinicio y la fecha puede no ser exacta."));
        this.tag = format((getConfig().contains("Prefix") ? getConfig().getString("Prefix") : "&c&lPERMADEATH&4&lCORE &7➤ &f"));

        PDCLog.getInstance().log("Se ha recargado el plugin");
        DiscordManager.getInstance();
    }

    protected void setupListeners() {
        getServer().getPluginManager().registerEvents(this, this);

        this.spawnListener = new SpawnListener(this);
        getServer().getPluginManager().registerEvents(spawnListener, instance);
        getServer().getPluginManager().registerEvents(new SkeletonClasses(instance), instance);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), instance);
        getServer().getPluginManager().registerEvents(new BlockEvents(), instance);
        getServer().getPluginManager().registerEvents(new EntityEvents(), instance);
        getServer().getPluginManager().registerEvents(new TotemConsumeEvent(), instance);
        getServer().getPluginManager().registerEvents(new RaidEvents(), instance);
        getServer().getPluginManager().registerEvents(new WorldEvents(), instance);
        registeredDays.put(1, false);
        registeredDays.put(20, false);
        registeredDays.put(30, false);
        registeredDays.put(40, false);
        registeredDays.put(50, false);
        registeredDays.put(60, false);
    }

    protected void setupConsoleFilter() {
        try {
            Class.forName("org.apache.logging.log4j.core.filter.AbstractFilter");
            org.apache.logging.log4j.core.Logger logger;
            logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
            logger.addFilter(new Log4JFilter());
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            Log4JFilter filter = new Log4JFilter();
            Filter f = (Filter) new Log4JFilter();
            Bukkit.getLogger().setFilter(f);
            Logger.getLogger("Minecraft").setFilter(f);
        }
    }

    protected void setupCommands() {
        getCommand("pdc").setExecutor(new PDCCommand(instance));
        getCommand("pdc").setTabCompleter(new PDCCommandCompleter());
    }

    protected void setupConfig() {

        File f = new File(getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);

        FileAPI.UtilFile c = FileAPI.select(instance, f, config);

        c.set("config-version", 2);
        c.set("Prefix", "&c&lPERMADEATH&4&lCORE &7➤ &f");
        c.set("ban-enabled", true);
        c.set("anti-afk-enabled", false);
        c.set("AntiAFK.DaysForBan", 7);
        c.set("Toggles.OptifineItems", false);
        c.set("Toggles.DefaultDeathSoundsEnabled", true);
        c.set("Toggles.Netherite.Helmet", 10);
        c.set("Toggles.Netherite.Chestplate", 10);
        c.set("Toggles.Netherite.Leggings", 10);
        c.set("Toggles.Netherite.Boots", 10);
        c.set("Toggles.End.Mob-Spawn-Limit", 70);
        c.set("Toggles.End.Ender-Ghast-Count", 170);
        c.set("Toggles.End.Ender-Creeper-Count", 20);
        c.set("Toggles.End.Protect-End-Spawn", false);
        c.set("Toggles.End.Protect-Radius", 10);
        c.set("Toggles.End.PermadeathDemon.DisplayName", "&6&lPERMADEATH DEMON");
        c.set("Toggles.End.PermadeathDemon.DisplayNameEnraged", "&6&lENRAGED PERMADEATH DEMON");
        c.set("Toggles.End.PermadeathDemon.Health", 1350);
        c.set("Toggles.End.PermadeathDemon.EnragedHealth", 1350);
        c.set("Toggles.End.PermadeathDemon.Optimizar-TNT", false);
        c.set("Toggles.TheBeginning.YticGenerateChance", 100000);
        c.set("Toggles.Spider-Effect", true);
        c.set("Toggles.OP-Ban", true);
        c.set("Toggles.Doble-Mob-Cap", false);
        c.set("Toggles.Replace-Mobs-On-Chunk-Load", true);
        c.set("Toggles.Quantum-Explosion-Power", 60);
        c.set("Toggles.Mike-Creeper-Spawn", true);
        c.set("Toggles.Optimizar-Mob-Spawns", false);
        c.set("Toggles.Gatos-Supernova.Destruir-Bloques", true);
        c.set("Toggles.Gatos-Supernova.Fuego", true);
        c.set("Toggles.Gatos-Supernova.Explosion-Power", 200);
        c.set("Server-Messages.coords-msg-enable", true);
        c.set("TotemFail.Enable", true);
        c.set("TotemFail.Medalla", "&7¡El jugador %player% ha usado su medalla de superviviente!");
        c.set("TotemFail.ChatMessage", "&7¡El tótem de &c%player% &7ha fallado!");
        c.set("TotemFail.ChatMessageTotems", "&7¡Los tótems de &c%player% &7han fallado!");
        c.set("TotemFail.NotEnoughTotems", "&7¡%player% no tenía suficientes tótems en el inventario!");
        c.set("TotemFail.PlayerUsedTotemMessage", "&7El jugador %player% ha consumido un tótem (Probabilidad: %totem_fail% %porcent% %number%)");
        c.set("TotemFail.PlayerUsedTotemsMessage", "&7El jugador %player% ha consumido {ammount} tótems (Probabilidad: %totem_fail% %porcent% %number%)");
        c.set("Worlds.MainWorld", "world");
        c.set("Worlds.EndWorld", "world_the_end");
        c.set("DontTouch.PlayTime", 0);

        c.save();
        c.load();
    }

    public static boolean isOptifineEnabled() {
        if (instance == null) return false;
        return instance.getConfig().getBoolean("Toggles.OptifineItems");
    }

    public MobFactory getFactory() {
        return factory;
    }

    public static String format(String texto) {

        return ChatColor.translateAlternateColorCodes('&', texto);
    }

    public Messages getMessages() {
        return messages;
    }

    public NMSHandler getNmsHandler() {
        return nmsHandler;
    }

    public BeginningManager getBeginningManager() {
        return begginingManager;
    }

    public BeginningDataManager getBeData() {
        return beData;
    }

    public EndDataManager getEndData() {
        return endData;
    }

    public EndTask getTask() {
        return task;
    }

    public void setTask(EndTask task) {
        this.task = task;
    }

    public NMSAccesor getNmsAccesor() {
        return nmsAccesor;
    }

    public long getDays() {
        return DateManager.getInstance().getDays();
    }

    public static Main getInstance() {
        return instance;
    }

    public PeaceToHostileManager getHostile() {
        return hostile;
    }

    public ArrayList<Player> getDoneEffectPlayers() {
        return doneEffectPlayers;
    }

    public ShellEvent getShulkerEvent() {
        return shulkerEvent;
    }

    public LifeOrbEvent getOrbEvent() {
        return orbEvent;
    }

    public static boolean isRunningPaperSpigot() {
        return runningPaperSpigot;
    }

    public CustomBlock getNetheriteBlock() {
        return this.netheriteBlock;
    }

    public boolean isSmallIslandsEnabled() {
        return true;
    }

    public void deathTrainEffects(LivingEntity entity) {

        if (entity instanceof Player) return;

        if (getDays() >= 25) {

            int lvl = (getDays() >= 50 ? 1 : 0);

            entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, lvl));
            entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, lvl));
            entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, lvl));

            if (getDays() >= 50 && getDays() < 60) {
                entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
            }
        }
    }

    public SpawnListener getSpawnListener() {
        return spawnListener;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public String formatInterval(int segundos) {

        int hrs = segundos / 3600;
        int minAndSec = segundos % 3600;
        int min = minAndSec / 60;
        int sec = minAndSec % 60;

        return String.format("%02d:%02d:%02d", hrs, min, sec);
    }
}