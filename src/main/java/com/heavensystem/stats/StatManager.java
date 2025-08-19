package com.heavensystem.stats;

import com.heavensystem.HeavenSystemPlugin;
import com.heavensystem.data.HeavenPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatManager implements Listener {
    
    private final HeavenSystemPlugin plugin;
    private final Map<UUID, Long> lastStatGain;
    private final Map<UUID, Integer> actionCounter;
    
    public StatManager(HeavenSystemPlugin plugin) {
        this.plugin = plugin;
        this.lastStatGain = new HashMap<>();
        this.actionCounter = new HashMap<>();
        
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public void grantStat(Player player, StatType statType, int amount, String reason) {
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
        heavenPlayer.getStats().addStat(statType, amount);
        
        // Show stat gain message
        if (amount > 0) {
            player.sendMessage("§8[§6Heaven System§8] §7+" + amount + " " + 
                              statType.getColoredName() + " §7(" + reason + ")");
        }
        
        // Check for stat-based achievements
        checkStatAchievements(player, heavenPlayer);
    }
    
    public void grantStatWithCooldown(Player player, StatType statType, int amount, String reason, long cooldownMs) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long lastGain = lastStatGain.getOrDefault(playerId, 0L);
        
        if (currentTime - lastGain >= cooldownMs) {
            grantStat(player, statType, amount, reason);
            lastStatGain.put(playerId, currentTime);
        }
    }
    
    private void checkStatAchievements(Player player, HeavenPlayer heavenPlayer) {
        PlayerStats stats = heavenPlayer.getStats();
        
        // Check total stat milestones
        int totalStats = stats.getTotalStats();
        
        if (totalStats >= 100 && !heavenPlayer.hasTitle("stat_master_1")) {
            plugin.getTitleManager().grantTitle(player, "stat_master_1");
        }
        
        if (totalStats >= 500 && !heavenPlayer.hasTitle("stat_master_2")) {
            plugin.getTitleManager().grantTitle(player, "stat_master_2");
        }
        
        // Check individual stat milestones
        checkIndividualStatMilestones(player, heavenPlayer, StatType.STRENGTH);
        checkIndividualStatMilestones(player, heavenPlayer, StatType.AGILITY);
        checkIndividualStatMilestones(player, heavenPlayer, StatType.INTELLIGENCE);
        checkIndividualStatMilestones(player, heavenPlayer, StatType.CHARISMA);
    }
    
    private void checkIndividualStatMilestones(Player player, HeavenPlayer heavenPlayer, StatType statType) {
        int statValue = heavenPlayer.getStats().getStat(statType);
        String baseTitleId = statType.name().toLowerCase() + "_specialist";
        
        if (statValue >= 50 && !heavenPlayer.hasTitle(baseTitleId + "_1")) {
            plugin.getTitleManager().grantTitle(player, baseTitleId + "_1");
        }
        
        if (statValue >= 100 && !heavenPlayer.hasTitle(baseTitleId + "_2")) {
            plugin.getTitleManager().grantTitle(player, baseTitleId + "_2");
        }
    }
    
    // Event handlers for automatic stat gains
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            grantStatWithCooldown(player, StatType.STRENGTH, 1, "Combat", 5000L);
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().distanceSquared(event.getTo()) > 0.1) {
            Player player = event.getPlayer();
            
            // Increment action counter
            UUID playerId = player.getUniqueId();
            int count = actionCounter.getOrDefault(playerId, 0) + 1;
            actionCounter.put(playerId, count);
            
            // Grant agility every 1000 movements
            if (count % 1000 == 0) {
                grantStat(player, StatType.AGILITY, 1, "Movement");
            }
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        
        // Different blocks give different stats
        switch (event.getBlock().getType()) {
            case DIAMOND_ORE:
            case EMERALD_ORE:
                grantStatWithCooldown(player, StatType.INTELLIGENCE, 2, "Rare Mining", 10000L);
                break;
            case STONE:
            case COBBLESTONE:
                grantStatWithCooldown(player, StatType.STRENGTH, 1, "Mining", 30000L);
                break;
            default:
                break;
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Grant charisma for certain interactions
        if (event.getClickedBlock() != null) {
            switch (event.getClickedBlock().getType()) {
                case CRAFTING_TABLE:
                case ENCHANTING_TABLE:
                    grantStatWithCooldown(event.getPlayer(), StatType.INTELLIGENCE, 1, "Crafting", 60000L);
                    break;
                default:
                    break;
            }
        }
    }
}