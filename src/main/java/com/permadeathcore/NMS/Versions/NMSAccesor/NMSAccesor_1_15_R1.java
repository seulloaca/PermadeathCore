package com.permadeathcore.NMS.Versions.NMSAccesor;

import com.permadeathcore.Main;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NMSAccesor_1_15_R1 implements com.permadeathcore.NMS.NMSAccesor {

    @Override
    public void setMaxHealth(org.bukkit.entity.Entity entity, Double d, boolean setHealth) {

        getAtribute(entity, GenericAttributes.MAX_HEALTH).setValue(d);

        if (setHealth && entity instanceof LivingEntity) {

            ((LivingEntity) entity).setHealth(d);
        }
    }

    @Override
    public Double getMaxHealth(org.bukkit.entity.Entity entity) {

        return getAtribute(entity, GenericAttributes.MAX_HEALTH).getValue();
    }

    @Override
    public EntityInsentient getEntityInsentiment(org.bukkit.entity.Entity en) {

        if (en == null) return null;

        net.minecraft.server.v1_15_R1.Entity entity = ((CraftEntity) en).getHandle();

        if (entity instanceof EntityInsentient) {

            EntityInsentient insentient = (EntityInsentient) entity;
            return insentient;
        }

        return null;
    }

    @Override
    public void moveEntityTo(org.bukkit.entity.Entity entity, Location location, Double speed) {

        EntityInsentient insentient = getEntityInsentiment(entity);

        if (insentient == null) return;

        PathEntity path = insentient.getNavigation().a(location.getX(), location.getY(), location.getZ(), 2);

        if (path != null) {

            insentient.getNavigation().a(path, speed);
        }
    }

    @Override
    public void registerNewAttribute(org.bukkit.entity.Entity entity, Object at) {

        EntityInsentient insentient = getEntityInsentiment(entity);

        if (insentient == null) return;

        IAttribute attribute = (IAttribute) at;
        insentient.getAttributeMap().b(attribute);
    }

    @Override
    public void setAttributeValue(Object attribute, Double value, org.bukkit.entity.Entity entity) {

        EntityInsentient insentient = getEntityInsentiment(entity);

        if (insentient == null) return;

        IAttribute at = (IAttribute) attribute;
        insentient.getAttributeInstance(at).setValue(value);
    }

    @Override
    public void registerAttribute(Attribute a, Double value, org.bukkit.entity.Entity who) {

        if (who instanceof LivingEntity) {

            net.minecraft.server.v1_15_R1.Entity e = ((CraftEntity) who).getHandle();

            if (!(e instanceof EntityInsentient)) return;

            EntityInsentient insentient = (EntityInsentient) e;


            try {
                insentient.getAttributeMap().b(bukkitToNMSAttribute(a));
            } catch (IllegalArgumentException x) {
            }
            insentient.getAttributeInstance(bukkitToNMSAttribute(a)).setValue(value);
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

        try {

            final Field fieldMap = EntityLiving.class.getDeclaredField("attributeMap");
            fieldMap.setAccessible(true);

            AttributeMapBase map = (AttributeMapBase) fieldMap.get(null);
            map.b(GenericAttributes.ATTACK_DAMAGE);

            fieldMap.set(null, map);

        } catch (Throwable t) {
        }
    }

    @Override
    public void unregisterAttributes(Entity who) {

        if (!(who instanceof LivingEntity)) return;

        if (who instanceof org.bukkit.entity.Mob) {
            ((org.bukkit.entity.Mob) who).setTarget(null);
        }

        ((LivingEntity)who).setAI(false);

        EntityLiving insentient = (EntityLiving) ((CraftEntity)who).getHandle();

        try {

            final Field mapField = EntityLiving.class.getDeclaredField("attributeMap");
            mapField.setAccessible(true);

            AttributeMapServer s = new AttributeMapServer();

            s.b(GenericAttributes.MAX_HEALTH);
            s.b(GenericAttributes.KNOCKBACK_RESISTANCE);
            s.b(GenericAttributes.MOVEMENT_SPEED);
            s.b(GenericAttributes.ARMOR);
            s.b(GenericAttributes.ARMOR_TOUGHNESS);

            if (insentient instanceof EntityInsentient) {

                s.b(GenericAttributes.FOLLOW_RANGE).setValue(15.0D);
                s.b(GenericAttributes.ATTACK_KNOCKBACK);
            }

            s.a(GenericAttributes.MAX_HEALTH).setValue(((LivingEntity) who).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

            if (((LivingEntity) who).getAttribute(Attribute.GENERIC_ARMOR) != null) {

                s.a(GenericAttributes.ARMOR).setValue(((LivingEntity) who).getAttribute(Attribute.GENERIC_ARMOR).getValue());
            }
            if (((LivingEntity) who).getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS) != null) {

                s.a(GenericAttributes.ARMOR_TOUGHNESS).setValue(((LivingEntity) who).getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue());
            }

            s.a(GenericAttributes.MOVEMENT_SPEED).setValue(((LivingEntity) who).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue());

            s.a(GenericAttributes.FOLLOW_RANGE).setValue(((LivingEntity) who).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).getValue());

            if (((LivingEntity) who).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null) {
                s.a(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(((LivingEntity) who).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) == null ? 1.0D : ((LivingEntity) who).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getValue());
            }
            //insentient.getAttributeInstance(GenericAttributes.).setValue(((LivingEntity) who).getAttribute(Attribute.).getValue());

            if (((LivingEntity) who).getAttribute(Attribute.GENERIC_FLYING_SPEED) != null) {

                s.b(GenericAttributes.FLYING_SPEED);
                s.a(GenericAttributes.FLYING_SPEED).setValue(((LivingEntity) who).getAttribute(Attribute.GENERIC_FLYING_SPEED).getValue());
            }

            CraftAttributeMap m = new CraftAttributeMap(s);

            mapField.set(insentient, s);

            final Field craftAttributesField = EntityLiving.class.getDeclaredField("craftAttributes");
            craftAttributesField.setAccessible(true);

            craftAttributesField.set(insentient, m);

        } catch (Throwable t) {

            t.printStackTrace();
        }

        ((LivingEntity)who).setAI(true);
    }

    @Override
    public void injectHostilePathfinders(Entity entity) {

        net.minecraft.server.v1_15_R1.Entity nms = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity)entity).getHandle();
        EntityType type = entity.getType();

        if (nms instanceof net.minecraft.server.v1_15_R1.EntityCreature) {

            net.minecraft.server.v1_15_R1.EntityCreature insentient = (net.minecraft.server.v1_15_R1.EntityCreature) ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity) entity).getHandle();

            net.minecraft.server.v1_15_R1.PathfinderGoalSelector goalSelector = insentient.goalSelector;

            try {

                if (type == EntityType.LLAMA || type == EntityType.PANDA) return;

                Field dField = net.minecraft.server.v1_15_R1.PathfinderGoalSelector.class.getDeclaredField("d");
                dField.setAccessible(true);
                Set<net.minecraft.server.v1_15_R1.PathfinderGoalWrapped> set = (Set<net.minecraft.server.v1_15_R1.PathfinderGoalWrapped>) dField.get(goalSelector);

                boolean containsMelee = false;

                for (net.minecraft.server.v1_15_R1.PathfinderGoalWrapped w : set) {

                    final Field f = w.getClass().getDeclaredField("a");
                    f.setAccessible(true);

                    PathfinderGoal goal = (PathfinderGoal) f.get(w);

                    if (goal.getClass().getName().equalsIgnoreCase(net.minecraft.server.v1_15_R1.PathfinderGoalAvoidTarget.class.getName())) {

                        set.remove(goal);
                    }

                    if (goal.getClass().getName().equalsIgnoreCase(net.minecraft.server.v1_15_R1.PathfinderGoalPanic.class.getName())) {

                        set.remove(goal);
                    }

                    if (goal.getClass().getName().equalsIgnoreCase(net.minecraft.server.v1_15_R1.PathfinderGoalMeleeAttack.class.getName())) {

                        containsMelee = true;
                    }
                }

                if (!containsMelee) {

                    set.add(new net.minecraft.server.v1_15_R1.PathfinderGoalWrapped(0, new net.minecraft.server.v1_15_R1.PathfinderGoalMeleeAttack(insentient, 1.0F, true)));


                }

                dField.set(goalSelector, set);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (nms instanceof net.minecraft.server.v1_15_R1.EntityInsentient) {

            net.minecraft.server.v1_15_R1.EntityInsentient insentient = (net.minecraft.server.v1_15_R1.EntityInsentient) ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity) entity).getHandle();

            net.minecraft.server.v1_15_R1.PathfinderGoalSelector targetSelector = insentient.targetSelector;

            try {
                Field dField;
                dField = net.minecraft.server.v1_15_R1.PathfinderGoalSelector.class.getDeclaredField("d");
                dField.setAccessible(true);

                Set<net.minecraft.server.v1_15_R1.PathfinderGoalWrapped> set = (Set<net.minecraft.server.v1_15_R1.PathfinderGoalWrapped>) dField.get(targetSelector);
                set.add(new net.minecraft.server.v1_15_R1.PathfinderGoalWrapped(0, new net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget(insentient, net.minecraft.server.v1_15_R1.EntityHuman.class, true)));

                dField.set(targetSelector, set);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unregisterHostilePathfinders(Entity entity) {

        net.minecraft.server.v1_15_R1.Entity nms = ((CraftEntity)entity).getHandle();
        EntityType type = entity.getType();

        if (nms instanceof EntityCreature) {

            EntityCreature insentient = (EntityCreature) ((CraftEntity) entity).getHandle();

            PathfinderGoalSelector goalSelector = insentient.goalSelector;

            try {
                Field dField = PathfinderGoalSelector.class.getDeclaredField("d");
                dField.setAccessible(true);
                Set<PathfinderGoalWrapped> set = (Set<PathfinderGoalWrapped>) dField.get(goalSelector);

                for (PathfinderGoalWrapped w : set) {

                    final Field f = w.getClass().getDeclaredField("a");
                    f.setAccessible(true);

                    PathfinderGoal goal = (PathfinderGoal) f.get(w);

                    if (goal.getClass().getName().equalsIgnoreCase(PathfinderGoalNearestAttackableTarget.class.getName())) {

                        set.remove(w);
                    }
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

                if (set != null) {

                    try {

                        for (PathfinderGoalWrapped w : set) {

                            final Field f = w.getClass().getDeclaredField("a");
                            f.setAccessible(true);

                            PathfinderGoal goal = (PathfinderGoal) f.get(w);

                            if (goal.getClass().getName().equalsIgnoreCase(PathfinderGoalNearestAttackableTarget.class.getName())) {

                                set.remove(w);
                            }
                        }
                    } catch (Throwable t) {
                    }
                }

                dField.set(targetSelector, set);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addNMSTag(Entity entity) {
        net.minecraft.server.v1_15_R1.Entity en = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity)entity).getHandle();
        net.minecraft.server.v1_15_R1.NBTTagCompound t = new net.minecraft.server.v1_15_R1.NBTTagCompound();

        t.setInt("Invul", 100);

        en.save(t);
    }

    @Override
    public IAttribute bukkitToNMSAttribute(Attribute attribute) {

        IAttribute ia = null;

        if (attribute == Attribute.GENERIC_ATTACK_DAMAGE) {

            ia = net.minecraft.server.v1_15_R1.GenericAttributes.ATTACK_DAMAGE;
        }

        if (attribute == Attribute.GENERIC_ARMOR_TOUGHNESS) {

            ia = net.minecraft.server.v1_15_R1.GenericAttributes.ARMOR_TOUGHNESS;
        }

        if (attribute == Attribute.GENERIC_ARMOR) {

            ia = net.minecraft.server.v1_15_R1.GenericAttributes.ARMOR;
        }

        if (attribute == Attribute.GENERIC_MAX_HEALTH) {

            ia = net.minecraft.server.v1_15_R1.GenericAttributes.MAX_HEALTH;
        }

        if (attribute == Attribute.GENERIC_KNOCKBACK_RESISTANCE) {

            ia = net.minecraft.server.v1_15_R1.GenericAttributes.KNOCKBACK_RESISTANCE;
        }

        if (attribute == Attribute.GENERIC_ATTACK_SPEED) {

            ia = net.minecraft.server.v1_15_R1.GenericAttributes.ATTACK_SPEED;
        }

        if (attribute == Attribute.GENERIC_FLYING_SPEED) {

            ia = net.minecraft.server.v1_15_R1.GenericAttributes.FLYING_SPEED;
        }

        if (attribute == Attribute.GENERIC_FOLLOW_RANGE) {

            ia = net.minecraft.server.v1_15_R1.GenericAttributes.FOLLOW_RANGE;
        }

        if (attribute == Attribute.GENERIC_MOVEMENT_SPEED) {

            ia = net.minecraft.server.v1_15_R1.GenericAttributes.MOVEMENT_SPEED;
        }

        if (attribute == Attribute.GENERIC_LUCK) {

            ia = net.minecraft.server.v1_15_R1.GenericAttributes.LUCK;
        }

        return ia;
    }

    @Override
    public Class getNMSEntityClass(String name) {
        try {
            return Class.forName("net.minecraft.server.v1_15_R1.Entity" + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void drown(org.bukkit.entity.Player p, double ammount) {
        net.minecraft.server.v1_15_R1.Entity en = ((CraftEntity)p).getHandle();
        en.damageEntity(net.minecraft.server.v1_15_R1.DamageSource.DROWN, (float) ammount);
    }

    @Override
    public AttributeInstance getAtribute(Entity en, Object at) {

        return getEntityInsentiment(en) == null ? null : getEntityInsentiment(en).getAttributeInstance((IAttribute) at);
    }
}
