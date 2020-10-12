package com.permadeathcore.NMS.Versions.NMSAccesor;

import com.permadeathcore.Main;
import com.permadeathcore.NMS.NMSAccesor;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NMSAccesor_1_14_R1 implements NMSAccesor {

    @Override
    public void setMaxHealth(Entity entity, Double d, boolean setHealth) {

        getAtribute(entity, GenericAttributes.MAX_HEALTH).setValue(d);

        if (setHealth && entity instanceof LivingEntity) {

            ((LivingEntity) entity).setHealth(d);
        }
    }

    @Override
    public Double getMaxHealth(Entity entity) {

        return getAtribute(entity, GenericAttributes.MAX_HEALTH).getValue();
    }

    @Override
    public EntityInsentient getEntityInsentiment(Entity en) {

        if (en == null) return null;

        net.minecraft.server.v1_14_R1.Entity entity = ((CraftEntity) en).getHandle();

        if (entity instanceof EntityInsentient) {

            EntityInsentient insentient = (EntityInsentient) entity;
            return insentient;
        }

        return null;
    }

    @Override
    public void moveEntityTo(Entity entity, Location location, Double speed) {

        EntityInsentient insentient = getEntityInsentiment(entity);

        if (insentient == null) return;

        PathEntity path = insentient.getNavigation().a(location.getX(), location.getY(), location.getZ(), 2);

        if (path != null) {

            insentient.getNavigation().a(path, speed);
        }
    }

    @Override
    public void registerNewAttribute(Entity entity, Object at) {

        EntityInsentient insentient = getEntityInsentiment(entity);

        if (insentient == null) return;

        IAttribute attribute = (IAttribute) at;
        insentient.getAttributeMap().b(attribute);
    }

    @Override
    public void setAttributeValue(Object attribute, Double value, Entity entity) {

        EntityInsentient insentient = getEntityInsentiment(entity);

        if (insentient == null) return;

        IAttribute at = (IAttribute) attribute;
        insentient.getAttributeInstance(at).setValue(value);
    }

    @Override
    public void registerAttribute(Attribute a, Double value, Entity who) {

        EntityInsentient insentient = (EntityInsentient) ((CraftEntity)who).getHandle();

        try {
            insentient.getAttributeMap().b(bukkitToNMSAttribute(a));
        } catch (IllegalArgumentException e) {
        }

        insentient.getAttributeInstance(bukkitToNMSAttribute(a)).setValue(value);
    }

    @Override
    public void unregisterAttributes(Entity who) {

        net.minecraft.server.v1_14_R1.EntityLiving insentient = (net.minecraft.server.v1_14_R1.EntityLiving) ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity)who).getHandle();

        try {

            final Field f = net.minecraft.server.v1_14_R1.EntityLiving.class.getDeclaredField("attributeMap");
            f.setAccessible(true);

            f.set(insentient, null);

            Class c = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity)who).getHandle().getClass();

            Method m = c.getDeclaredMethod("initAttributes");
            m.setAccessible(true);

            m.invoke(((org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity)who).getHandle());

        } catch (Throwable t) {

            t.printStackTrace();
        }
    }

    @Override
    public void registerKnockback(Double value, Double attackValue, Entity who) {

    }

    @Override
    public void registerAttribute(HashMap<Attribute, Double> a, Entity who) {

        EntityInsentient insentient = (EntityInsentient) ((CraftEntity)who).getHandle();

        for (Attribute attribute : a.keySet()) {

            Double value = a.get(attribute);

            try {
                insentient.getAttributeMap().b(bukkitToNMSAttribute(attribute));
            } catch (IllegalArgumentException e) {
            }
            insentient.getAttributeInstance(bukkitToNMSAttribute(attribute)).setValue(value);
        }
    }

    @Override
    public void registerHostileMobs() {

        for (EntityType p : EntityType.values()) {

            if (Main.getInstance().getHostile().isHostileMob(p)) return;

            try {

                final Field fieldMap = net.minecraft.server.v1_14_R1.EntityLiving.class.getDeclaredField("attributeMap");
                fieldMap.setAccessible(true);

                net.minecraft.server.v1_14_R1.AttributeMapBase map = (net.minecraft.server.v1_14_R1.AttributeMapBase) fieldMap.get(net.minecraft.server.v1_14_R1.EntityLiving.class);

                map.b(net.minecraft.server.v1_14_R1.GenericAttributes.ATTACK_DAMAGE);
                map.a(net.minecraft.server.v1_14_R1.GenericAttributes.ATTACK_DAMAGE).setValue(8.0D);

                fieldMap.set(net.minecraft.server.v1_14_R1.EntityLiving.class, map);

            } catch (Throwable t) {
            }
        }
    }

    @Override
    public void injectHostilePathfinders(Entity entity) {

        net.minecraft.server.v1_14_R1.Entity nms = ((CraftEntity)entity).getHandle();
        EntityType type = entity.getType();

        if (nms instanceof EntityCreature) {

            EntityCreature insentient = (EntityCreature) ((CraftEntity) entity).getHandle();

            PathfinderGoalSelector goalSelector = insentient.goalSelector;

            try {
                Field dField = PathfinderGoalSelector.class.getDeclaredField("d");
                dField.setAccessible(true);
                Set<PathfinderGoalWrapped> set = (Set<PathfinderGoalWrapped>) dField.get(goalSelector);

                boolean containsMelee = false;

                for (PathfinderGoalWrapped goals : set) {

                    if (goals.getClass().getName().equalsIgnoreCase(PathfinderGoalAvoidTarget.class.getName())) {

                        set.remove(goals);
                    }

                    if (goals.getClass().getName().equalsIgnoreCase(PathfinderGoalPanic.class.getName())) {

                        set.remove(goals);
                    }

                    if (goals.getClass().getName().equalsIgnoreCase(PathfinderGoalMeleeAttack.class.getName())) {

                        containsMelee = true;
                    }
                }

                if (!containsMelee) {

                    set.add(new PathfinderGoalWrapped(0, new PathfinderGoalMeleeAttack(insentient, 1.0F, true)));
                }

                dField.set(goalSelector, set);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (nms instanceof EntityInsentient) {

            EntityInsentient insentient = (EntityInsentient) ((CraftEntity) entity).getHandle();

            PathfinderGoalSelector targetSelector = insentient.targetSelector;

            try {
                Field dField;
                dField = PathfinderGoalSelector.class.getDeclaredField("d");
                dField.setAccessible(true);

                Set<PathfinderGoalWrapped> set = (Set<PathfinderGoalWrapped>) dField.get(targetSelector);
                set.add(new PathfinderGoalWrapped(0, new PathfinderGoalNearestAttackableTarget(insentient, EntityHuman.class, true)));

                dField.set(targetSelector, set);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unregisterHostilePathfinders(Entity entity) {

    }

    @Override
    public void addNMSTag(Entity entity) {
        net.minecraft.server.v1_14_R1.Entity en = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity)entity).getHandle();
        net.minecraft.server.v1_14_R1.NBTTagCompound t = new net.minecraft.server.v1_14_R1.NBTTagCompound();

        t.setInt("Invul", 100);

        en.save(t);
    }

    @Override
    public IAttribute bukkitToNMSAttribute(Attribute attribute) {

        IAttribute ia = null;

        if (attribute == Attribute.GENERIC_ATTACK_DAMAGE) {

            ia = GenericAttributes.ATTACK_DAMAGE;
        }

        if (attribute == Attribute.GENERIC_ARMOR_TOUGHNESS) {

            ia = GenericAttributes.ARMOR_TOUGHNESS;
        }

        if (attribute == Attribute.GENERIC_ARMOR) {

            ia = GenericAttributes.ARMOR;
        }

        if (attribute == Attribute.GENERIC_MAX_HEALTH) {

            ia = GenericAttributes.MAX_HEALTH;
        }

        if (attribute == Attribute.GENERIC_KNOCKBACK_RESISTANCE) {

            ia = GenericAttributes.KNOCKBACK_RESISTANCE;
        }

        if (attribute == Attribute.GENERIC_ATTACK_SPEED) {

            ia = GenericAttributes.ATTACK_SPEED;
        }

        if (attribute == Attribute.GENERIC_FLYING_SPEED) {

            ia = GenericAttributes.FLYING_SPEED;
        }

        if (attribute == Attribute.GENERIC_FOLLOW_RANGE) {

            ia = GenericAttributes.FOLLOW_RANGE;
        }

        if (attribute == Attribute.GENERIC_MOVEMENT_SPEED) {

            ia = GenericAttributes.MOVEMENT_SPEED;
        }

        if (attribute == Attribute.GENERIC_LUCK) {

            ia = GenericAttributes.LUCK;
        }
        
        return ia;
    }

    @Override
    public Class getNMSEntityClass(String name) {
        try {
            return Class.forName("net.minecraft.server.v1_14_R1.Entity" + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void drown(Player p, double ammount) {

        net.minecraft.server.v1_14_R1.Entity en = ((CraftEntity)p).getHandle();
        en.damageEntity(DamageSource.DROWN, (float) ammount);
    }

    @Override
    public AttributeInstance getAtribute(Entity en, Object at) {

        return getEntityInsentiment(en) == null ? null : getEntityInsentiment(en).getAttributeInstance((IAttribute) at);
    }
}
