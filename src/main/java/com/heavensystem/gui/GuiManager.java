package com.heavensystem.gui;

import com.heavensystem.HeavenSystemPlugin;
import com.heavensystem.data.HeavenPlayer;
import com.heavensystem.stats.StatType;
import com.heavensystem.titles.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiManager {
    
    private final HeavenSystemPlugin plugin;
    
    public GuiManager(HeavenSystemPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void openStatsGui(Player player) {
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
        
        Inventory gui = Bukkit.createInventory(null, 27, "§8§l⚡ Heaven System Stats ⚡");
        
        // Stat items
        gui.setItem(10, createStatItem(StatType.STRENGTH, heavenPlayer));
        gui.setItem(11, createStatItem(StatType.AGILITY, heavenPlayer));
        gui.setItem(15, createStatItem(StatType.INTELLIGENCE, heavenPlayer));
        gui.setItem(16, createStatItem(StatType.CHARISMA, heavenPlayer));
        
        // Potential item
        ItemStack potential = new ItemStack(Material.NETHER_STAR);
        ItemMeta potentialMeta = potential.getItemMeta();
        potentialMeta.setDisplayName("§d§lPotential");
        
        List<String> potentialLore = new ArrayList<>();
        potentialLore.add("§7Current: §d" + String.format("%.1f", heavenPlayer.getPotential()));
        potentialLore.add("");
        potentialLore.add("§7Potential reflects how deeply the");
        potentialLore.add("§7Heaven System recognizes your impact.");
        potentialLore.add("§7High potential unlocks ascension roles.");
        
        potentialMeta.setLore(potentialLore);
        potential.setItemMeta(potentialMeta);
        gui.setItem(13, potential);
        
        // Class item
        if (heavenPlayer.getPlayerClass() != null) {
            ItemStack classItem = new ItemStack(heavenPlayer.getPlayerClass().getIcon());
            ItemMeta classMeta = classItem.getItemMeta();
            classMeta.setDisplayName(heavenPlayer.getPlayerClass().getColoredName());
            
            List<String> classLore = new ArrayList<>();
            classLore.add("§7" + heavenPlayer.getPlayerClass().getDescription());
            classLore.add("");
            classLore.add("§8Abilities: " + heavenPlayer.getPlayerClass().getAbilities().size());
            
            classMeta.setLore(classLore);
            classItem.setItemMeta(classMeta);
            gui.setItem(22, classItem);
        }
        
        // Ascension role item
        if (heavenPlayer.getAscensionRole() != null) {
            ItemStack roleItem = new ItemStack(Material.BEACON);
            ItemMeta roleMeta = roleItem.getItemMeta();
            roleMeta.setDisplayName(heavenPlayer.getAscensionRole().getFullDisplayName());
            
            List<String> roleLore = new ArrayList<>();
            roleLore.add("§7" + heavenPlayer.getAscensionRole().getDescription());
            roleLore.add("");
            roleLore.add("§8Abilities: " + heavenPlayer.getAscensionRole().getAbilities().size());
            
            roleMeta.setLore(roleLore);
            roleItem.setItemMeta(roleMeta);
            gui.setItem(4, roleItem);
        }
        
        player.openInventory(gui);
    }
    
    public void openTitlesGui(Player player) {
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
        
        Inventory gui = Bukkit.createInventory(null, 54, "§8§l⚡ Heaven System Titles ⚡");
        
        int slot = 0;
        for (Title title : heavenPlayer.getUnlockedTitles()) {
            if (slot >= 54) break;
            
            ItemStack titleItem = new ItemStack(Material.NAME_TAG);
            ItemMeta titleMeta = titleItem.getItemMeta();
            
            String displayName = title.getFullDisplayName();
            if (title.equals(heavenPlayer.getActiveTitle())) {
                displayName += " §a§l(ACTIVE)";
            }
            titleMeta.setDisplayName(displayName);
            
            List<String> titleLore = new ArrayList<>();
            titleLore.add("§7" + title.getDescription());
            titleLore.add("");
            titleLore.add("§8Tier: " + title.getTierDisplay());
            titleLore.add("§8Potential Granted: §d" + title.getPotentialGrant());
            
            if (!title.getStatBonuses().isEmpty()) {
                titleLore.add("");
                titleLore.add("§8Stat Bonuses:");
                for (java.util.Map.Entry<StatType, Integer> bonus : title.getStatBonuses().entrySet()) {
                    titleLore.add("§8  " + bonus.getKey().getColoredName() + " §7+" + bonus.getValue());
                }
            }
            
            if (!title.equals(heavenPlayer.getActiveTitle())) {
                titleLore.add("");
                titleLore.add("§eClick to set as active title!");
            }
            
            titleMeta.setLore(titleLore);
            titleItem.setItemMeta(titleMeta);
            
            gui.setItem(slot, titleItem);
            slot++;
        }
        
        player.openInventory(gui);
    }
    
    private ItemStack createStatItem(StatType statType, HeavenPlayer heavenPlayer) {
        Material material;
        switch (statType) {
            case STRENGTH:
                material = Material.IRON_SWORD;
                break;
            case AGILITY:
                material = Material.FEATHER;
                break;
            case INTELLIGENCE:
                material = Material.BOOK;
                break;
            case CHARISMA:
                material = Material.GOLD_INGOT;
                break;
            default:
                material = Material.STONE;
        }
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(statType.getColoredName() + " §f" + heavenPlayer.getStats().getStat(statType));
        
        List<String> lore = new ArrayList<>();
        lore.add("§7Base value of your " + statType.getDisplayName().toLowerCase());
        lore.add("§7Multiplier: §a" + String.format("%.0f%%", heavenPlayer.getStats().getStatMultiplier(statType) * 100));
        lore.add("");
        
        switch (statType) {
            case STRENGTH:
                lore.add("§7Affects combat damage and mining");
                break;
            case AGILITY:
                lore.add("§7Affects movement and dodge chance");
                break;
            case INTELLIGENCE:
                lore.add("§7Affects crafting and experience");
                break;
            case CHARISMA:
                lore.add("§7Affects trading and social interactions");
                break;
        }
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
}