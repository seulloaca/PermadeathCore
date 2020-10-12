package com.permadeathcore.TheBeginning.Loot;

import com.permadeathcore.TheBeginning.BeginningManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BeginningLootTable {

    private List<Integer> randomLoc = new ArrayList();
    private List<String> probs;
    private List<Material> alreadyRolled;
    private BeginningManager manager;
    private SplittableRandom random;

    public BeginningLootTable(BeginningManager man) {
        this.manager = man;

        for (int i = 0; i < 27; ++i) {
            this.randomLoc.add(i);
        }

        this.probs = new ArrayList<>();
        this.alreadyRolled = new ArrayList<>();
        this.random = new SplittableRandom();

        addItem(probs, Material.GOLD_INGOT, 5, 50, 60);
        addItem(probs, Material.GOLDEN_APPLE, 60, 1, 8);
        addItem(probs, Material.DIAMOND, 60, 16, 24);
        addItem(probs, Material.ARROW, 10, 10, 16);
        addItem(probs, Material.FIREWORK_ROCKET, 20, 55, 64);
        addItem(probs, Material.TOTEM_OF_UNDYING, 5, 1, 2);
        addItem(probs, Material.STRUCTURE_VOID, 1, 1, 1);

    }

    public void populateChest(Chest chest) {

        World w = chest.getWorld();
        Inventory inv = chest.getBlockInventory();
        if (!w.getName().equalsIgnoreCase("pdc_the_beginning")) return;
        if (inv.contains(Material.DIAMOND_PICKAXE)) {
            return;
        }
        roll(chest);
    }

    private List<String> addItem(List<String> list, Material mat, int chance, int min, int max) {
        list.add(mat.toString() + ";" + chance + ";" + min + ";" + max);
        return list;
    }

    private void roll(Chest c) {
        int rollTimes = random.nextInt(3) + 1;
        for (int i = 0; i < rollTimes; i++) {
            generate(c);
        }
    }

    private void generate(Chest chest) {
        Iterator t = probs.iterator();
        while (t.hasNext()) {
            String[] split = String.valueOf(t.next()).split(";");
            Inventory inventory = chest.getBlockInventory();

            Collections.shuffle(this.randomLoc);

            int added = 0;
            if (random.nextInt(100) + 1 <= getChance(split) && !alreadyRolled.contains(getMaterial(split))) {
                if (getMaterial(split) == Material.TOTEM_OF_UNDYING || getMaterial(split) == Material.STRUCTURE_VOID) {
                    inventory.setItem(this.randomLoc.get(added), new ItemStack(getMaterial(split)));
                    return;
                }
                int ammount = generateValue(getMin(split), getMax(split));
                ItemStack s = new ItemStack(getMaterial(split), ammount);
                inventory.setItem(this.randomLoc.get(added), s);

                try {
                    int x = ammount + getMin(split) / 2;
                    ItemStack s2 = new ItemStack(s.getType(), x);

                    int r = random.nextInt(5)+1;
                    int slot = (random.nextBoolean() ? -1 : 1) * r;

                    inventory.setItem(this.randomLoc.get(added + slot), s2);
                } catch (Exception x) {}

                if (added++ >= inventory.getSize() - 1) {
                    break;
                }
                alreadyRolled.add(s.getType());
            }
        }
    }

    private boolean hasSlot(Inventory inventory) {
        boolean b = false;
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                b = true;
            }
        }

        return b;
    }

    private int getMin(String[] s) {
        return Integer.valueOf(s[2]);
    }

    private int getMax(String[] s) {
        return Integer.valueOf(s[3]);
    }

    private int getChance(String[] s) {
        return Integer.valueOf(s[1]);
    }

    private Material getMaterial(String[] s) {
        return Material.valueOf(s[0]);
    }

    private int generateValue(int min, int max) {
        return random.nextInt(max - min) + random.nextInt(min) + 1;
    }
}
