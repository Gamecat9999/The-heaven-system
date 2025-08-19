package com.heavensystem.commands;

import com.heavensystem.HeavenSystemPlugin;
import com.heavensystem.data.HeavenPlayer;
import com.heavensystem.stats.StatType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    
    private final HeavenSystemPlugin plugin;
    
    public StatsCommand(HeavenSystemPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cConsole must specify a player name.");
                return true;
            }
            target = (Player) sender;
        } else {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found: " + args[0]);
                return true;
            }
        }
        
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(target);
        showPlayerStats(sender, target, heavenPlayer);
        
        return true;
    }
    
    private void showPlayerStats(CommandSender viewer, Player target, HeavenPlayer heavenPlayer) {
        viewer.sendMessage("§8§l═══════════════════════════════════════════════════");
        viewer.sendMessage("§6§l            " + target.getName() + "'s Statistics");
        viewer.sendMessage("§8§l═══════════════════════════════════════════════════");
        viewer.sendMessage("");
        
        // Core stats
        viewer.sendMessage("§e§lCore Statistics:");
        for (StatType statType : StatType.values()) {
            int statValue = heavenPlayer.getStats().getStat(statType);
            double multiplier = heavenPlayer.getStats().getStatMultiplier(statType);
            
            viewer.sendMessage(String.format("§7  %s §f%d §8(%.0f%% effectiveness)",
                statType.getColoredShortName(), statValue, multiplier * 100));
        }
        
        viewer.sendMessage("");
        viewer.sendMessage("§e§lProgression:");
        viewer.sendMessage("§8  Total Stats: §f" + heavenPlayer.getStats().getTotalStats());
        viewer.sendMessage("§8  Potential: §d" + String.format("%.1f", heavenPlayer.getPotential()));
        viewer.sendMessage("§8  Titles Unlocked: §6" + heavenPlayer.getUnlockedTitles().size());
        
        if (heavenPlayer.getActiveTitle() != null) {
            viewer.sendMessage("§8  Active Title: " + heavenPlayer.getActiveTitle().getFullDisplayName());
        }
        
        if (heavenPlayer.getPlayerClass() != null) {
            viewer.sendMessage("§8  Class: " + heavenPlayer.getPlayerClass().getColoredName());
        }
        
        if (heavenPlayer.getAscensionRole() != null) {
            viewer.sendMessage("§8  Ascension Role: " + heavenPlayer.getAscensionRole().getFullDisplayName());
        }
        
        viewer.sendMessage("");
        viewer.sendMessage("§e§lActivity:");
        viewer.sendMessage("§8  Total Playtime: §f" + formatTime(heavenPlayer.getTotalPlaytime()));
        viewer.sendMessage("§8  Last Login: §f" + formatLastLogin(heavenPlayer.getLastLogin()));
        
        viewer.sendMessage("");
        viewer.sendMessage("§8§l═══════════════════════════════════════════════════");
    }
    
    private String formatTime(long milliseconds) {
        long hours = milliseconds / (1000 * 60 * 60);
        long minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
    
    private String formatLastLogin(long timestamp) {
        long diff = System.currentTimeMillis() - timestamp;
        long hours = diff / (1000 * 60 * 60);
        long minutes = (diff % (1000 * 60 * 60)) / (1000 * 60);
        
        if (hours > 24) {
            return (hours / 24) + " days ago";
        } else if (hours > 0) {
            return hours + " hours ago";
        } else if (minutes > 0) {
            return minutes + " minutes ago";
        } else {
            return "Just now";
        }
    }
}