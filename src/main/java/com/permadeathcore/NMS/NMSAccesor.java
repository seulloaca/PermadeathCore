package com.permadeathcore.NMS;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;

public interface NMSAccesor {

    void setMaxHealth(Entity entity, Double d, boolean setHealth);
    Double getMaxHealth(Entity entity);
    Object getAtribute(Entity en, Object at);
    Object getEntityInsentiment(Entity entity);
    void moveEntityTo(Entity entity, org.bukkit.Location where, Double speed);
    void registerNewAttribute(Entity entity, Object attribute);
    void setAttributeValue(Object at, Double value, Entity entity);
    void registerAttribute(Attribute a, Double value, Entity who);
    void unregisterAttributes(Entity who);
    void registerKnockback(Double value, Double attackValue, Entity who);
    void registerAttribute(HashMap<Attribute, Double> a, Entity who);
    void registerHostileMobs();
    void injectHostilePathfinders(Entity entity);
    void unregisterHostilePathfinders(Entity entity);
    void addNMSTag(Entity entity);
    Object bukkitToNMSAttribute(Attribute attribute);
    Class getNMSEntityClass(String name);
    void drown(Player p, double ammount);
}
