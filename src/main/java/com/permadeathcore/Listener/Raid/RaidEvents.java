package com.permadeathcore.Listener.Raid;

import com.permadeathcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.permadeathcore.Main.instance;

public class RaidEvents implements Listener {

    @EventHandler
    public void onRaidFinish(RaidFinishEvent e) {

        if (Main.getInstance().getDays() < 50) return;
        if (e.getWinners().isEmpty()) return;

        Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
            @Override
            public void run() {

                for (Player player : e.getWinners()) {
                    if (player.hasPotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE)) {

                        PotionEffect effect = player.getPotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE);
                        player.removePotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE);

                        int min = 5*60;
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, min, effect.getAmplifier()));
                    }
                }
            }
        }, 10L);
    }
}
