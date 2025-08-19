package com.heavensystem.titles;

import com.heavensystem.HeavenSystemPlugin;
import com.heavensystem.data.HeavenPlayer;
import com.heavensystem.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.Particle;
import java.util.HashMap;
import java.util.Map;

public class TitleManager {
    
    private final HeavenSystemPlugin plugin;
    private final Map<String, Title> titles;
    
    public TitleManager(HeavenSystemPlugin plugin) {
        this.plugin = plugin;
        this.titles = new HashMap<>();
        
        initializeDefaultTitles();
    }
    
    private void initializeDefaultTitles() {
        // Stat-based titles
        registerTitle(createStatTitle("strength_specialist_1", "Warrior", StatType.STRENGTH, 5, 1, ChatColor.RED));
        registerTitle(createStatTitle("strength_specialist_2", "Berserker", StatType.STRENGTH, 10, 2, ChatColor.DARK_RED));
        
        registerTitle(createStatTitle("agility_specialist_1", "Scout", StatType.AGILITY, 5, 1, ChatColor.GREEN));
        registerTitle(createStatTitle("agility_specialist_2", "Shadow", StatType.AGILITY, 10, 2, ChatColor.DARK_GREEN));
        
        registerTitle(createStatTitle("intelligence_specialist_1", "Scholar", StatType.INTELLIGENCE, 5, 1, ChatColor.BLUE));
        registerTitle(createStatTitle("intelligence_specialist_2", "Sage", StatType.INTELLIGENCE, 10, 2, ChatColor.DARK_BLUE));
        
        registerTitle(createStatTitle("charisma_specialist_1", "Diplomat", StatType.CHARISMA, 5, 1, ChatColor.LIGHT_PURPLE));
        registerTitle(createStatTitle("charisma_specialist_2", "Leader", StatType.CHARISMA, 10, 2, ChatColor.DARK_PURPLE));
        
        // General progression titles
        registerTitle(new Title("stat_master_1", "Apprentice", "Achieved 100 total stats", 1,
                createStatMap(StatType.STRENGTH, 2, StatType.AGILITY, 2, StatType.INTELLIGENCE, 2, StatType.CHARISMA, 2),
                5.0, "[", "]", ChatColor.YELLOW, false));
        
        registerTitle(new Title("stat_master_2", "Master", "Achieved 500 total stats", 2,
                createStatMap(StatType.STRENGTH, 5, StatType.AGILITY, 5, StatType.INTELLIGENCE, 5, StatType.CHARISMA, 5),
                15.0, "≪", "≫", ChatColor.GOLD, true));
        
        // Combat titles
        registerTitle(new Title("beast_slayer_1", "Beast Slayer", "Slay 100 hostile mobs", 1,
                createStatMap(StatType.STRENGTH, 10), 8.0, "", "", ChatColor.DARK_RED, false));
        
        registerTitle(new Title("apex_predator_1", "Apex Predator", "Slay 500 hostile mobs", 2,
                createStatMap(StatType.STRENGTH, 20, StatType.AGILITY, 10), 20.0, "⚔", "⚔", ChatColor.DARK_RED, true));
        
        // Building titles
        registerTitle(new Title("master_builder_1", "Master Builder", "Place 10,000 blocks", 1,
                createStatMap(StatType.INTELLIGENCE, 15), 10.0, "🏗", "", ChatColor.AQUA, false));
        
        registerTitle(new Title("architect_1", "Architect", "Place 50,000 blocks", 2,
                createStatMap(StatType.INTELLIGENCE, 30, StatType.CHARISMA, 15), 25.0, "⛏", "⛏", ChatColor.DARK_AQUA, true));
    }
    
    private Title createStatTitle(String id, String name, StatType statType, int bonus, int tier, ChatColor color) {
        Map<StatType, Integer> bonuses = new HashMap<>();
        bonuses.put(statType, bonus);
        
        return new Title(id, name, "Specialize in " + statType.getDisplayName(), tier,
                bonuses, tier * 2.5, "", "", color, tier > 1);
    }
    
    private Map<StatType, Integer> createStatMap(Object... args) {
        Map<StatType, Integer> map = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            if (args[i] instanceof StatType && args[i + 1] instanceof Integer) {
                map.put((StatType) args[i], (Integer) args[i + 1]);
            }
        }
        return map;
    }
    
    public void registerTitle(Title title) {
        titles.put(title.getId(), title);
    }
    
    public Title getTitle(String id) {
        return titles.get(id);
    }
    
    public Map<String, Title> getAllTitles() {
        return titles;
    }
    
    public void grantTitle(Player player, String titleId) {
        Title title = titles.get(titleId);
        if (title == null) {
            plugin.getLogger().warning("Attempted to grant unknown title: " + titleId);
            return;
        }
        
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
        
        if (heavenPlayer.hasTitle(titleId)) {
            return; // Player already has this title
        }
        
        // Grant the title
        heavenPlayer.addTitle(title);
        
        // Apply stat bonuses
        for (Map.Entry<StatType, Integer> bonus : title.getStatBonuses().entrySet()) {
            heavenPlayer.getStats().addStat(bonus.getKey(), bonus.getValue());
        }
        
        // Grant potential
        heavenPlayer.addPotential(title.getPotentialGrant());
        
        // Show dramatic title acquisition
        showTitleAcquisition(player, title);
        
        // Check for ascension eligibility
        plugin.getAscensionManager().checkAscensionEligibility(player, heavenPlayer);
    }
    
    private void showTitleAcquisition(Player player, Title title) {
    // Clear chat and show cinematic message
    for (int i = 0; i < 20; i++) {
        player.sendMessage("");
    }

    player.sendMessage("§8§m                                                    ");
    player.sendMessage("");
    player.sendMessage("§6§l                 ✦ TITLE UNLOCKED ✦");
    player.sendMessage("");
    player.sendMessage("              " + title.getFullDisplayName());
    player.sendMessage("");
    player.sendMessage("§7         " + title.getDescription());
    player.sendMessage("");
    player.sendMessage("§8         Potential Gained: §d+" + title.getPotentialGrant());

    if (!title.getStatBonuses().isEmpty()) {
        player.sendMessage("");
        player.sendMessage("§8         Stat Bonuses:");
        for (Map.Entry<StatType, Integer> bonus : title.getStatBonuses().entrySet()) {
            player.sendMessage("§8           " + bonus.getKey().getColoredName() + " §7+" + bonus.getValue());
        }
    }

    player.sendMessage("");
    player.sendMessage("§8§m                                                    ");

    // Play sound
    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);

    // Spawn central particle burst
    player.spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 20, 0.5, 1.0, 0.5, 0.01);

    if (title.hasParticleEffect()) {
        // Spawn surrounding particles
        for (int i = 0; i < 10; i++) {
            player.spawnParticle(
                Particle.VILLAGER_HAPPY,
                player.getLocation().add(Math.random() * 4 - 2, Math.random() * 2, Math.random() * 4 - 2),
                5,
                0.2, 0.5, 0.2,
                0.01
            );
        }
    }
}

}