package com.permadeathcore.Discord;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Log.PDCLog;
import com.permadeathcore.Util.Manager.PlayerDataManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

public class DiscordManager {

    private static DiscordManager discordManager;
    private Main instance;

    private File file;
    private FileConfiguration configuration;

    private JDA bot;

    public DiscordManager() {
        this.instance = Main.getInstance();

        this.file = new File(instance.getDataFolder(), "discord.yml");
        this.configuration = YamlConfiguration.loadConfiguration(this.file);

        if (!file.exists()) {
            this.instance.saveResource("discord.yml", false);
        }

        if (configuration.getBoolean("Enable")) {
            log("Intentando cargar la aplicación de Discord.");

            String token = configuration.getString("Token");

            if (token.isEmpty()) {
                log("No se ha proporcionado un token por el usuario");
                return;
            }

            try {
                JDABuilder builder = JDABuilder.createDefault(token);
                builder.setActivity(Activity.watching("InfernalCore.net | " + configuration.getString("Status")));
                builder.addEventListeners(new JDAListeners(this));

                this.bot = builder.build();
                this.bot.awaitReady();
            } catch (LoginException e) {
                e.printStackTrace();
                log("Ha ocurrido un error al iniciar sesión con la aplicación de Discord, revisa tu token.");
            } catch (InterruptedException e) {
                e.printStackTrace();
                log("Ha ocurrido un error al iniciar sesión con la aplicación de Discord, revisa tu token.");
            }

            try {
                String s = configuration.getString("Channels.Anuncios");
                if (s == null) return;
                TextChannel channel = bot.getTextChannelById(s);
                if (channel == null) return;
                sendEmbed(channel, buildEmbed("PermadeathCore", Color.GREEN, null, null, null, ":gear: Plugin encendido."));
            } catch (Exception x) {}
        } else {
            log("El bot de discord no está activado en la config");
        }
    }

    public void onDisable() {
        if (this.bot == null) return;
        String s = configuration.getString("Channels.Anuncios");
        if (s == null) return;
        TextChannel channel = bot.getTextChannelById(s);

        if (channel == null) return;
        sendEmbed(channel, buildEmbed("PermadeathCore", Color.RED, null, null, null, ":gear: Plugin desactivado."));
    }

    public void onDeathTrain(String msg) {
        if (this.bot == null) return;
        String s = configuration.getString("Channels.Anuncios");
        if (s == null) return;
        TextChannel channel = bot.getTextChannelById(s);

        if (channel == null) return;
        sendEmbed(channel, buildEmbed("PermadeathCore", Color.RED, null, null, null, ":fire: " + ChatColor.stripColor(msg)));
    }

    public void onDayChange() {
        if (this.bot == null) return;
        String s = configuration.getString("Channels.Anuncios");
        if (s == null) return;
        TextChannel channel = bot.getTextChannelById(s);

        if (channel == null) return;
        sendEmbed(channel, buildEmbed("PermadeathCore", Color.GREEN, null, null, null, ":alarm_clock: Han avanzado al día " + instance.getDays()));
    }

    public void banPlayer(OfflinePlayer off, boolean isAFKBan) {

        if (this.bot == null) return;

        Player p = (off.isOnline() ? (Player) off : null);

        PlayerDataManager data = new PlayerDataManager(off.getName(), instance);
        String playerLoc = (isAFKBan ? "" : p.getLocation().getBlockX() + " " +  p.getLocation().getBlockY() + " " +  p.getLocation().getBlockZ());

        String serverName = configuration.getString("ServerName");
        LocalDate n = LocalDate.now();
        String date = String.format("%02d/%02d/%02d", n.getDayOfMonth(), n.getMonthValue(), n.getYear());
        String cause = isAFKBan ? "AFK" : data.getBanCause();

        EmbedBuilder b = buildEmbed(off.getName() + " ha sido PERMABANEADO en " + serverName + "\n",
                new Color(0xF40C0C),
                null,
                null,
                "https://minotar.net/armor/bust/" + off.getName() + "/100.png");
        b.setAuthor("InfernalCore.net | PermaDeathCore" , "http://permadeathcore.com/", "https://www.spigotmc.org/data/avatars/l/973/973464.jpg?1599783018");
        b.addField("\uD83D\uDCC5 Fecha", date, true);
        b.addField("\uD83D\uDC80 Razón", cause, true);
        if (!isAFKBan) b.addField("\uD83E\uDDED Coordenadas", playerLoc, true);

        TextChannel channel = getBot().getTextChannelById(configuration.getString("Channels.DeathChannel"));

        if (channel == null) log("No pudimos encontrar el canal de muertes.");

        channel.sendMessage(b.build()).queue(message -> {
            message.addReaction("☠").queue();
        });

        log("Enviando mensaje de muerte a discord");
    }

    private void log(String s) {
        PDCLog.getInstance().log("[DISCORD] " + s);
    }

    public JDA getBot() {
        return bot;
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public static DiscordManager getInstance() {

        if (discordManager == null) discordManager = new DiscordManager();

        return discordManager;
    }

    private MessageBuilder buildMessage(String description, MessageEmbed... embed) {
        MessageBuilder b = new MessageBuilder();
        b.setContent(description);

        for (MessageEmbed e : embed) {
            b.setEmbed(e);
        }

        return b;
    }

    private void sendMessage(MessageChannel channel, MessageBuilder b, String... reaction) {
        channel.sendMessage(b.build()).queue(message -> {
            for (String s : reaction) {
                message.addReaction(s).queue();
            }
        });
    }

    private EmbedBuilder buildEmbed(String title, Color color, String footer, String image, String thumbnail, String... description) {
        EmbedBuilder eb = new EmbedBuilder();

        if (title != null) eb.setTitle(title);
        if (color != null) eb.setColor(color);
        if (footer != null) eb.setFooter(footer);
        if (image != null) eb.setImage(image);
        if (thumbnail != null) eb.setThumbnail(thumbnail);

        for (String s : description) {
            eb.addField("", s, false);
        }

        return eb;
    }

    private void sendEmbed(MessageChannel channel, EmbedBuilder b, String... reaction) {
        channel.sendMessage(b.build()).queue(message -> {
            for (String s : reaction) {
                message.addReaction(s).queue();
            }
        });
    }
}
