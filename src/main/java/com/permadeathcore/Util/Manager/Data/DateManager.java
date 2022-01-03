package com.permadeathcore.Util.Manager.Data;

import com.permadeathcore.Discord.DiscordManager;
import com.permadeathcore.Main;
import com.permadeathcore.Util.Manager.Log.PDCLog;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateManager {

    private Main instance;
    private static DateManager dai;

    private File f;
    private FileConfiguration c;

    public String fecha;
    public LocalDate fechaInicio;
    public LocalDate fechaActual;

    public DateManager() {
        this.instance = Main.getInstance();

        this.fechaActual = LocalDate.now();
        this.prepareFile();
        this.fecha = c.getString("Fecha");

        //LocalDate act = LocalDate.now();
        //fechaActual = act.minusDays(1);

        try {
            this.fechaInicio = LocalDate.parse(fecha);
        } catch (DateTimeParseException ex) {

            Bukkit.getConsoleSender().sendMessage(instance.format(instance.tag + "&4&lERROR: &eLa fecha en config.yml estaba mal configurada &7(" + c.getString("Fecha") + ")&e."));
            Bukkit.getConsoleSender().sendMessage(instance.format(instance.tag + "&eSe ha establecido el día: &b1"));
            this.fechaInicio = LocalDate.parse(getDateForDayOne());

            c.set("Fecha", getDateForDayOne());
            saveFile();
            reloadFile();
        }
    }

    public void tick() {

        LocalDate now = LocalDate.now();

        if (this.fechaActual.isBefore(now)) {
            this.fechaActual = now;
            DiscordManager.getInstance().onDayChange();
        }
    }

    public void reloadDate() {
        this.fecha = this.c.getString("Fecha");
        this.fechaInicio = LocalDate.parse(this.fecha);
        this.fechaActual = LocalDate.now();
    }

    public void setDay(CommandSender sender, String args1) {
        int nD;

        try {
            int d = Integer.parseInt(args1);
            if (d > 120 || d < 0) {
                nD = 0;
            } else {
                nD = d;
            }
        } catch (NumberFormatException ex) {
            sender.sendMessage(instance.format("&cNecesitas ingresar un número válido."));
            return;
        }
        if (nD == 0) {
            sender.sendMessage(instance.format("&cHas ingresado un número no válido, o ni siquiera un número."));
            return;
        }

        LocalDate add = fechaActual.minusDays(nD);
        DateManager.getInstance().setNewDate(String.format(add.getYear() + "-%02d-%02d", add.getMonthValue(), add.getDayOfMonth()));

        sender.sendMessage(instance.format("&eSe han actualizado los días a: &7" + nD));
        sender.sendMessage(instance.format("&c&lNota importante: &7Algunos cambios pueden requerir un reinicio y la fecha puede no ser exacta."));

        PDCLog.getInstance().log("Día cambiado a: " + nD);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pdc reload");
        if (Bukkit.getOnlinePlayers() != null && Bukkit.getOnlinePlayers().size() >= 1) {
            for (OfflinePlayer off : Bukkit.getOfflinePlayers()) {

                if (off == null) return;

                if (off.isBanned()) return;

                PlayerDataManager manager = new PlayerDataManager(off.getName(), instance);
                manager.setLastDay(getDays());
            }
        }
    }

    public long getDays() {
        if (Main.SPEED_RUN_MODE) {
            return instance.getPlayTime() / 3600;
        } else {
            return fechaInicio.until(fechaActual, ChronoUnit.DAYS);
        }
    }

    public void setNewDate(String value) {
        this.c.set("Fecha", value);
        saveFile();
        reloadFile();
    }

    public String getDateForDayOne() {
        LocalDate w = fechaActual.minusDays(1);

        return String.format(w.getYear() + "-%02d-%02d", w.getMonthValue(), w.getDayOfMonth());
    }

    private void prepareFile() {
        this.f = new File(this.instance.getDataFolder(), "fecha.yml");
        this.c = YamlConfiguration.loadConfiguration(f);

        if (!f.exists()) {

            this.instance.saveResource("fecha.yml", false);

            c.set("Fecha", getDateForDayOne());

            saveFile();
            reloadFile();
        }

        if (c.getString("Fecha").isEmpty()) {

            c.set("Fecha", getDateForDayOne());
            saveFile();
            reloadFile();
        }
    }

    private void saveFile() {

        try {
            this.c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reloadFile() {

        try {
            this.c.load(f);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static DateManager getInstance() {
        if (dai == null) dai = new DateManager();

        return dai;
    }
}
