package com.heavensystem.events;

import com.heavensystem.HeavenSystemPlugin;
import com.heavensystem.data.HeavenPlayer;
import com.heavensystem.stats.StatType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEventListener implements Listener {
    
    private final HeavenSystemPlugin plugin;
    private final Map<UUID, Long> sessionStartTime;
    private final Map<UUID, Integer> actionCounters;
    
    public PlayerEventListener(HeavenSystemPlugin plugin) {
        this.plugin = plugin;
        this.sessionStartTime = new HashMap<>();
        this.actionCounters = new HashMap<>();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Record session start
        sessionStartTime.put(playerId, System.currentTimeMillis());
        actionCounters.put(playerId, 0);
        
        // Load player data
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
        heavenPlayer.setLastLogin(System.currentTimeMillis());
        
        // Show welcome message with stats
        player.sendMessage("");
        player.sendMessage("§8§l⚡ §6Welcome to the Heaven System §8§l⚡");
        player.sendMessage("§7Your journey of ascension continues...");
        player.sendMessage("");
        player.sendMessage("§8Potential: §d" + String.format("%.1f", heavenPlayer.getPotential()));
        
        if (heavenPlayer.getActiveTitle() != null) {
            player.sendMessage("§8Active Title: " + heavenPlayer.getActiveTitle().getFullDisplayName());
        }
        
        if (heavenPlayer.getAscensionRole() != null) {
            player.sendMessage("§8Ascension Role: " + heavenPlayer.getAscensionRole().getFullDisplayName());
        }
        
        player.sendMessage("");
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Calculate and add session playtime
        Long startTime = sessionStartTime.remove(playerId);
        if (startTime != null) {
            long sessionTime = System.currentTimeMillis() - startTime;
            HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
            heavenPlayer.addPlaytime(sessionTime);
        }
        
        // Clean up counters
        actionCounters.remove(playerId);
        
        // Save and unload player data
        plugin.getPlayerDataManager().unloadPlayer(playerId);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        
        // Track action
        incrementActionCounter(player);
        
        // Apply stat surge bonus if active
        int statGain = 1;
        if (plugin.getSystemEventManager().isStatSurgeActive(player)) {
            statGain = 2; // 50% bonus rounded up
        }
        
        // Apply scarcity effect to drops
        if (plugin.getSystemEventManager().isScarcityActive(player)) {
            if (Math.random() < 0.5) { // 50% chance to remove drops
                event.setDropItems(false);
            }
        }
        
        // Grant stats based on block type and check for achievements
        switch (event.getBlock().getType()) {
            case DIAMOND_ORE:
            case DEEPSLATE_DIAMOND_ORE:
                plugin.getStatManager().grantStat(player, StatType.INTELLIGENCE, statGain * 3, "Diamond Mining");
                checkMiningAchievements(player, "diamond");
                break;
                
            case EMERALD_ORE:
            case DEEPSLATE_EMERALD_ORE:
                plugin.getStatManager().grantStat(player, StatType.CHARISMA, statGain * 3, "Emerald Mining");
                checkMiningAchievements(player, "emerald");
                break;
                
            case GOLD_ORE:
            case DEEPSLATE_GOLD_ORE:
            case NETHER_GOLD_ORE:
                plugin.getStatManager().grantStat(player, StatType.INTELLIGENCE, statGain * 2, "Gold Mining");
                break;
                
            case IRON_ORE:
            case DEEPSLATE_IRON_ORE:
                plugin.getStatManager().grantStat(player, StatType.STRENGTH, statGain, "Iron Mining");
                break;
                
            case ANCIENT_DEBRIS:
                plugin.getStatManager().grantStat(player, StatType.STRENGTH, statGain * 5, "Ancient Debris");
                checkMiningAchievements(player, "netherite");
                break;
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        
        // Track building for achievements
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
        int blocksPlaced = (Integer) heavenPlayer.getCustomData().getOrDefault("blocksPlaced", 0);
        blocksPlaced++;
        heavenPlayer.setCustomData("blocksPlaced", blocksPlaced);
        
        // Check building achievements
        if (blocksPlaced == 10000 && !heavenPlayer.hasTitle("master_builder_1")) {
            plugin.getTitleManager().grantTitle(player, "master_builder_1");
        }
        
        if (blocksPlaced == 50000 && !heavenPlayer.hasTitle("architect_1")) {
            plugin.getTitleManager().grantTitle(player, "architect_1");
        }
        
        // Occasionally grant intelligence for building
        if (Math.random() < 0.01) { // 1% chance
            int statGain = plugin.getSystemEventManager().isStatSurgeActive(player) ? 2 : 1;
            plugin.getStatManager().grantStat(player, StatType.INTELLIGENCE, statGain, "Building");
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player killer = event.getEntity().getKiller();
            
            // Track kills for achievements
            HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(killer);
            int kills = (Integer) heavenPlayer.getCustomData().getOrDefault("mobKills", 0);
            kills++;
            heavenPlayer.setCustomData("mobKills", kills);
            
            // Check combat achievements
            if (kills == 100 && !heavenPlayer.hasTitle("beast_slayer_1")) {
                plugin.getTitleManager().grantTitle(killer, "beast_slayer_1");
            }
            
            if (kills == 500 && !heavenPlayer.hasTitle("apex_predator_1")) {
                plugin.getTitleManager().grantTitle(killer, "apex_predator_1");
            }
            
            // Bonus drops during stat surge
            if (plugin.getSystemEventManager().isStatSurgeActive(killer)) {
                // Double drops
                for (ItemStack drop : event.getDrops()) {
                    drop.setAmount(drop.getAmount() * 2);
                }
            }
        }
    }
    
    private void incrementActionCounter(Player player) {
        UUID playerId = player.getUniqueId();
        int count = actionCounters.getOrDefault(playerId, 0) + 1;
        actionCounters.put(playerId, count);
        
        // Award potential for consistent activity
        if (count % 1000 == 0) {
            HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
            heavenPlayer.addPotential(1.0);
            player.sendMessage("§8[§6Heaven System§8] §d+1.0 Potential §7(Consistent Activity)");
        }
    }
    
    private void checkMiningAchievements(Player player, String oreType) {
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
        int oreCount = (Integer) heavenPlayer.getCustomData().getOrDefault(oreType + "Mined", 0);
        oreCount++;
        heavenPlayer.setCustomData(oreType + "Mined", oreCount);
        
        // Grant titles based on rare ore mining
        switch (oreType) {
            case "diamond":
                if (oreCount == 100) {
                    plugin.getTitleManager().grantTitle(player, "diamond_seeker_1");
                }
                break;
            case "emerald":
                if (oreCount == 50) {
                    plugin.getTitleManager().grantTitle(player, "emerald_hunter_1");
                }
                break;
            case "netherite":
                if (oreCount == 10) {
                    plugin.getTitleManager().grantTitle(player, "netherite_forger_1");
                }
                break;
        }
    }
}