package com.heavensystem.commands;

import com.heavensystem.HeavenSystemPlugin;
import com.heavensystem.data.HeavenPlayer;
import com.heavensystem.titles.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TitlesCommand implements CommandExecutor {
    
    private final HeavenSystemPlugin plugin;
    
    public TitlesCommand(HeavenSystemPlugin plugin) {
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
            
            // Open GUI for the player
            plugin.getGuiManager().openTitlesGui(target);
            return true;
            
        } else {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found: " + args[0]);
                return true;
            }
        }
        
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(target);
        showPlayerTitles(sender, target, heavenPlayer);
        
        return true;
    }
    
    private void showPlayerTitles(CommandSender viewer, Player target, HeavenPlayer heavenPlayer) {
        viewer.sendMessage("§8§l═══════════════════════════════════════════════════");
        viewer.sendMessage("§6§l              " + target.getName() + "'s Titles");
        viewer.sendMessage("§8§l═══════════════════════════════════════════════════");
        viewer.sendMessage("");
        
        if (heavenPlayer.getUnlockedTitles().isEmpty()) {
            viewer.sendMessage("§7    No titles unlocked yet.");
            viewer.sendMessage("§7    Complete feats to earn mythic titles!");
        } else {
            viewer.sendMessage("§e§lUnlocked Titles (" + heavenPlayer.getUnlockedTitles().size() + "):");
            viewer.sendMessage("");
            
            for (Title title : heavenPlayer.getUnlockedTitles()) {
                String status = "";
                if (title.equals(heavenPlayer.getActiveTitle())) {
                    status = " §a§l(ACTIVE)";
                }
                
                viewer.sendMessage("  " + title.getFullDisplayName() + status);
                viewer.sendMessage("    §8" + title.getDescription());
                viewer.sendMessage("    §8Tier: " + title.getTierDisplay() + " §8| Potential: §d+" + title.getPotentialGrant());
                viewer.sendMessage("");
            }
            
            viewer.sendMessage("§8Total Title Tier: §f" + heavenPlayer.getTotalTitleTier());
        }
        
        viewer.sendMessage("§8§l═══════════════════════════════════════════════════");
    }
}