package com.heavensystem.classes;

import com.heavensystem.HeavenSystemPlugin;
import com.heavensystem.data.HeavenPlayer;
import com.heavensystem.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class ClassManager {
    
    private final HeavenSystemPlugin plugin;
    private final Map<String, PlayerClass> classes;
    
    public ClassManager(HeavenSystemPlugin plugin) {
        this.plugin = plugin;
        this.classes = new HashMap<>();
        
        initializeDefaultClasses();
    }
    
    private void initializeDefaultClasses() {
        // Duelist - Combat focused
        Map<StatType, Integer> duelistStats = new HashMap<>();
        duelistStats.put(StatType.STRENGTH, 3);
        duelistStats.put(StatType.AGILITY, 2);
        
        classes.put("duelist", new PlayerClass("duelist", "Duelist", 
            "Master of blade and bow, excelling in combat prowess",
            ChatColor.RED, Material.IRON_SWORD, duelistStats,
            Arrays.asList("combat_mastery", "weapon_expertise", "battle_rage"),
            new HashMap<>()));
        
        // Arcanist - Magic focused
        Map<StatType, Integer> arcanistStats = new HashMap<>();
        arcanistStats.put(StatType.INTELLIGENCE, 3);
        arcanistStats.put(StatType.CHARISMA, 2);
        
        classes.put("arcanist", new PlayerClass("arcanist", "Arcanist",
            "Wielder of mystical forces and ancient knowledge",
            ChatColor.BLUE, Material.ENCHANTED_BOOK, arcanistStats,
            Arrays.asList("spell_casting", "mana_mastery", "arcane_shield"),
            new HashMap<>()));
        
        // Artisan - Crafting focused
        Map<StatType, Integer> artisanStats = new HashMap<>();
        artisanStats.put(StatType.INTELLIGENCE, 2);
        artisanStats.put(StatType.CHARISMA, 3);
        
        classes.put("artisan", new PlayerClass("artisan", "Artisan",
            "Master craftsman and builder of wondrous creations",
            ChatColor.YELLOW, Material.CRAFTING_TABLE, artisanStats,
            Arrays.asList("master_crafting", "resource_efficiency", "blueprint_mastery"),
            new HashMap<>()));
        
        // Trickster - Stealth and utility
        Map<StatType, Integer> tricksterStats = new HashMap<>();
        tricksterStats.put(StatType.AGILITY, 3);
        tricksterStats.put(StatType.INTELLIGENCE, 2);
        
        classes.put("trickster", new PlayerClass("trickster", "Trickster",
            "Swift and cunning, master of stealth and deception",
            ChatColor.GREEN, Material.ENDER_PEARL, tricksterStats,
            Arrays.asList("stealth_mastery", "trap_setting", "quick_escape"),
            new HashMap<>()));
    }
    
    public void assignClass(Player player, String classId) {
        PlayerClass playerClass = classes.get(classId);
        if (playerClass == null) {
            player.sendMessage("§cClass not found: " + classId);
            return;
        }
        
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
        heavenPlayer.setPlayerClass(playerClass);
        
        // Apply class stat bonuses
        for (Map.Entry<StatType, Integer> bonus : playerClass.getStatPreferences().entrySet()) {
            heavenPlayer.getStats().addStat(bonus.getKey(), bonus.getValue());
        }
        
        // Show class assignment message
        player.sendMessage("§8§m                                                    ");
        player.sendMessage("");
        player.sendMessage("§6§l                ⚡ CLASS CHOSEN ⚡");
        player.sendMessage("");
        player.sendMessage("              " + playerClass.getColoredName());
        player.sendMessage("");
        player.sendMessage("§7         " + playerClass.getDescription());
        player.sendMessage("");
        player.sendMessage("§8         Abilities Unlocked: " + playerClass.getAbilities().size());
        player.sendMessage("§8§m                                                    ");
    }
    
    public PlayerClass getClass(String id) {
        return classes.get(id);
    }
    
    public Map<String, PlayerClass> getAllClasses() {
        return classes;
    }
    
    public void registerClass(PlayerClass playerClass) {
        classes.put(playerClass.getId(), playerClass);
    }
}