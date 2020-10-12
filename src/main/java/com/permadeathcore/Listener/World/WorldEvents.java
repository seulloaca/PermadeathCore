package com.permadeathcore.Listener.World;

import com.permadeathcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import static com.permadeathcore.Main.instance;
import static com.permadeathcore.Main.tag;

public class WorldEvents implements Listener {

    @EventHandler
    public void onWeatherStorm(WeatherChangeEvent event) {

        if (!event.toWeatherState()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                String msg = tag + Main.getInstance().getMessages().getMessage("StormEnd", p);
                p.sendMessage(msg);
            }
            Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("StormEnd"));
            if (instance.getDays() >= 50) {
                if (instance.getBeginningManager() != null) {
                    instance.getBeginningManager().setClosed(false);
                }
                for (World w : Bukkit.getWorlds()) {
                    w.setGameRule(GameRule.NATURAL_REGENERATION, true);
                }
            }
        } else {
            if (event.getWorld().getEnvironment() == World.Environment.NORMAL && Main.getInstance().getDays() >= 25) {
                for (World w : Bukkit.getWorlds()) {
                    for (LivingEntity l : w.getLivingEntities()) {
                        Main.getInstance().deathTrainEffects(l);
                    }
                }
            }
        }
    }
}
