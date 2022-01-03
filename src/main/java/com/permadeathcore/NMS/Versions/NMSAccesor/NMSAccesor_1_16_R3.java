package com.permadeathcore.NMS.Versions.NMSAccesor;


import com.permadeathcore.NMS.NMSAccesor;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

public class NMSAccesor_1_16_R3 implements NMSAccesor {

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

        net.minecraft.server.v1_16_R3.Entity entity = ((CraftEntity) en).getHandle();

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

        if (entity instanceof LivingEntity) {

            ((LivingEntity) entity).getAttribute((Attribute) at).setBaseValue(5.0D);
        }
    }

    @Override
    public void setAttributeValue(Object attribute, Double value, Entity entity) {

        if (entity instanceof LivingEntity) {

            ((LivingEntity) entity).getAttribute((Attribute) attribute).setBaseValue(value);
        }
    }

    @Override
    public void registerAttribute(Attribute a, Double value, Entity who) {

        EntityLiving creature = (EntityLiving) ((CraftEntity)who).getHandle();

        try {

            final Field map = EntityLiving.class.getDeclaredField("attributeMap");
            map.setAccessible(true);

            AttributeProvider provider;

            if (who.getType() == EntityType.valueOf("BEE") || who.getType() == EntityType.PARROT) {

                provider = EntityInsentient.p().
                        a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue())
                        .a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue())
                        .a(GenericAttributes.FLYING_SPEED, creature.getAttributeInstance(GenericAttributes.FLYING_SPEED).getValue())
                        .a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue())
                        .a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue())
                        .a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue())
                        .a(bukkitToNMSAttribute(a), value)
                        .a();

            } else {

                provider = EntityInsentient.p().
                        a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue())
                        .a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue())
                        .a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue())
                        .a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue())
                        .a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue())
                        .a(bukkitToNMSAttribute(a), value)
                        .a();
            }

            map.set(creature, new AttributeMapBase(provider));
        } catch (Throwable reason) {}
    }

    @Override
    public void unregisterAttributes(Entity who) {

        EntityLiving creature = (EntityLiving) ((CraftEntity)who).getHandle();

        try {

            final Field map = EntityLiving.class.getDeclaredField("attributeMap");
            map.setAccessible(true);

            AttributeProvider provider;

            if (who.getType() == EntityType.valueOf("BEE") || who.getType() == EntityType.PARROT) {

                provider = EntityInsentient.p().
                        a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue())
                        .a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue())
                        .a(GenericAttributes.FLYING_SPEED, creature.getAttributeInstance(GenericAttributes.FLYING_SPEED).getValue())
                        .a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue())
                        .a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue())
                        .a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue())
                        .a();

            } else {

                provider = EntityInsentient.p().
                        a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue())
                        .a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue())
                        .a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue())
                        .a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue())
                        .a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue())
                        .a();
            }

            map.set(creature, new AttributeMapBase(provider));

        } catch (Throwable t) {

            t.printStackTrace();
        }
    }

    @Override
    public void registerKnockback(Double value, Double attackValue, Entity who) {

        EntityLiving creature = (EntityLiving) ((CraftEntity)who).getHandle();

        try {

            final Field map = EntityLiving.class.getDeclaredField("attributeMap");
            map.setAccessible(true);

            AttributeProvider provider;

            if (who.getType() == EntityType.valueOf("BEE") || who.getType() == EntityType.PARROT) {

                provider = EntityInsentient.p().
                        a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue())
                        .a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue())
                        .a(GenericAttributes.FLYING_SPEED, creature.getAttributeInstance(GenericAttributes.FLYING_SPEED).getValue())
                        .a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue())
                        .a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue())
                        .a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue())
                        .a(GenericAttributes.ATTACK_KNOCKBACK, value)
                        .a(GenericAttributes.ATTACK_DAMAGE, attackValue)
                        .a();

            } else {

                provider = EntityInsentient.p().
                        a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue())
                        .a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue())
                        .a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue())
                        .a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue())
                        .a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue())
                        .a(GenericAttributes.ATTACK_KNOCKBACK, value)
                        .a(GenericAttributes.ATTACK_DAMAGE, attackValue)
                        .a();
            }

            map.set(creature, new AttributeMapBase(provider));
        } catch (Throwable reason) {
            throw new RuntimeException(reason);
        }
    }

    @Override
    public void registerAttribute(HashMap<Attribute, Double> a, Entity who) {

        EntityLiving creature = (EntityLiving) ((CraftEntity)who).getHandle();

        try {

            final Field map = EntityLiving.class.getDeclaredField("attributeMap");
            map.setAccessible(true);

            AttributeMapBase defaultMap = (AttributeMapBase) map.get(creature);

            final Field aP = AttributeMapBase.class.getDeclaredField("d");
            aP.setAccessible(true);

            AttributeProvider provider = (AttributeProvider) aP.get(defaultMap);
            AttributeProvider.Builder c = provider.a();

            for (Attribute l : a.keySet()) {

                AttributeBase b = bukkitToNMSAttribute(l);

                c = c.a(b, a.get(b));
            }

            AttributeProvider p = c.a();
            aP.set(defaultMap, p);
            map.set(creature, defaultMap);

            final Field craftF = EntityLiving.class.getDeclaredField("craftAttributes");
            map.setAccessible(true);

            CraftAttributeMap cam = new CraftAttributeMap(defaultMap);
            craftF.set(creature, cam);

            /**
            AttributeProvider provider = EntityInsentient.p().a();

            for (AttributeModifiable m : defaultMap.getAttributes()) {

                provider = provider.a().a(m.getAttribute(), m.getValue()).a();
            }

            for (Attribute l : a.keySet()) {

                AttributeBase b = bukkitToNMSAttribute(l);

                provider = provider.a().a(b, a.get(b)).a();
            }

            AttributeMapBase attributeMapBase = new AttributeMapBase(provider);

            map.set(creature, attributeMapBase);
             */
        } catch (Throwable reason) {
            throw new RuntimeException(reason);
        }
    }

    @Override
    public void registerHostileMobs() {
    }


    @Override
    public void injectHostilePathfinders(Entity entity) {

        net.minecraft.server.v1_16_R3.Entity nms = ((CraftEntity)entity).getHandle();
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

        net.minecraft.server.v1_16_R3.Entity nms = ((CraftEntity)entity).getHandle();
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

                for (PathfinderGoalWrapped w : set) {

                    final Field f = w.getClass().getDeclaredField("a");
                    f.setAccessible(true);

                    PathfinderGoal goal = (PathfinderGoal) f.get(w);

                    if (goal.getClass().getName().equalsIgnoreCase(PathfinderGoalNearestAttackableTarget.class.getName())) {

                        set.remove(w);
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

        net.minecraft.server.v1_16_R3.Entity en = ((CraftEntity)entity).getHandle();
        NBTTagCompound t = new NBTTagCompound();
        t.setInt("Invul", 100);
        en.save(t);
        en.load(t);
    }

    @Override
    public AttributeBase bukkitToNMSAttribute(Attribute attribute) {

        AttributeBase ia = null;

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

        if (attribute == Attribute.HORSE_JUMP_STRENGTH) {

            ia = GenericAttributes.JUMP_STRENGTH;
        }

        if (attribute == Attribute.ZOMBIE_SPAWN_REINFORCEMENTS) {

            ia = GenericAttributes.SPAWN_REINFORCEMENTS;
        }

        return ia;
    }

    @Override
    public Class getNMSEntityClass(String name) {

        try {
            return Class.forName("net.minecraft.server.v1_16_R3.Entity" + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void drown(org.bukkit.entity.Player p, double ammount) {

        net.minecraft.server.v1_16_R3.Entity en = ((CraftEntity)p).getHandle();
        en.damageEntity(DamageSource.DROWN, (float) ammount);
    }

    @Override
    public AttributeModifiable getAtribute(Entity en, Object at) {

        return getEntityInsentiment(en) == null ? null : getEntityInsentiment(en).getAttributeInstance((AttributeBase) at);
    }
}
