package com.permadeathcore.Util.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
  protected ItemStack is;
  
  protected ItemMeta im;
  
  public ItemBuilder() {}
  
  public ItemBuilder(ItemStack itemStack) {
    this.is = new ItemStack(itemStack);
  }
  
  public ItemBuilder(Material material) {
    this.is = new ItemStack(material);
  }
  
  public ItemBuilder(Material material, int amount) {
    this.is = new ItemStack(material, amount);
  }
  
  public ItemBuilder(Material material, int amount, int subid) {
    this.is = new ItemStack(material, amount, (short)subid);
  }

  public static ItemStack createItem(String var0) {
    var0 = var0.replace("\\n", "\n");
    String[] var1 = var0.split(" : ");
    String var2 = var1[0].split(":")[0];
    ItemStack var3 = new ItemStack(Material.matchMaterial(var2.toUpperCase()));
    if ((var1[0].split(":")).length > 1)
      var3.setDurability((short)Integer.parseInt(var1[0].split(":")[1]));
    ItemMeta var4 = var3.getItemMeta();
    if (var1.length > 1)
      var3.setAmount((Integer.parseInt(var1[1]) > 64) ? 64 : Integer.parseInt(var1[1]));
    ArrayList<String> var5 = new ArrayList();
    for (int var6 = 2; var6 < var1.length; var6++) {
      String var7 = var1[var6];
      if (var7.startsWith("name="))
        var4.setDisplayName(format(var7.split("=")[1]));
      if (var7.startsWith("lore=")) {
        String[] var11;
        for (String var8 : var11 = var7.split("=")[1].split("\\n"))
          var5.add(format(var8));
      }
      if (var7.startsWith("enchant=")) {
        String[] var16;
        for (String var13 : var16 = var7.split("=")[1].split("\\n"))
          var4.addEnchant(Enchantment.getByName(var13.split(":")[0]), Integer.parseInt(var13.split(":")[1]), true);
      }
    }
    if (!var5.isEmpty())
      var4.setLore(var5);
    var3.setItemMeta(var4);
    return var3;
  }

  public static ItemStack createSkull(String var0) {
    var0 = var0.replace("\\n", "\n");
    String[] var1 = var0.split(" : ");
    ItemStack var2 = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short)Integer.parseInt(var1[0]));
    SkullMeta var3 = (SkullMeta)var2.getItemMeta();
    ArrayList<String> var4 = new ArrayList();
    for (int var5 = 1; var5 < var1.length; var5++) {
      String var6 = var1[var5];
      if (var6.startsWith("owner="))
        var3.setOwner(var6.split("=")[1]);
      if (var6.startsWith("name="))
        var3.setDisplayName(format(var6.split("=")[1]));
      if (var6.startsWith("lore=")) {
        String[] var10;
        for (String var7 : var10 = var6.split("=")[1].split("\\n"))
          var4.add(format(var7));
      }
      if (var6.startsWith("enchant=")) {
        String[] var15;
        for (String var12 : var15 = var6.split("=")[1].split("\\n"))
          var3.addEnchant(Enchantment.getByName(var12.split(":")[0]), Integer.parseInt(var12.split(":")[1]), true);
      }
    }
    if (!var4.isEmpty())
      var3.setLore(var4);
    var2.setItemMeta((ItemMeta)var3);
    return var2;
  }
  
  public ItemBuilder setDurability(int durability) {
    this.is.setDurability((short)durability);
    return this;
  }

  public ItemBuilder setUnbrekeable(boolean b) {

    this.im = this.is.getItemMeta();
    this.im.setUnbreakable(b);
    this.is.setItemMeta(this.im);
    return this;
  }
  
  public ItemBuilder setDisplayName(String name) {
    this.im = this.is.getItemMeta();
    this.im.setDisplayName(name);
    this.is.setItemMeta(this.im);
    return this;
  }
  
  public ItemBuilder addEnchant(Enchantment enchantment, int level) {
    this.im = this.is.getItemMeta();
    this.im.addEnchant(enchantment, level, true);
    this.is.setItemMeta(this.im);
    return this;
  }
  
  public ItemBuilder addEnchants(Map<Enchantment, Integer> enchantments) {
    this.im = this.is.getItemMeta();
    if (!enchantments.isEmpty())
      for (Enchantment ench : enchantments.keySet())
        this.im.addEnchant(ench, ((Integer)enchantments.get(ench)).intValue(), true);  
    this.is.setItemMeta(this.im);
    return this;
  }
  
  public ItemBuilder addItemFlag(ItemFlag itemflag) {
    this.im = this.is.getItemMeta();
    this.im.addItemFlags(new ItemFlag[] { itemflag });
    this.is.setItemMeta(this.im);
    return this;
  }
  
  public ItemBuilder setLore(List<String> lore) {
    this.im = this.is.getItemMeta();
    this.im.setLore(lore);
    this.is.setItemMeta(this.im);
    return this;
  }

  public static String format(String s) {

    return ChatColor.translateAlternateColorCodes('&', s);
  }
  
  public ItemStack build() {
    return this.is;
  }
}
