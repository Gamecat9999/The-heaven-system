package com.heavensystem.commands;

import com.heavensystem.HeavenSystemPlugin;
import com.heavensystem.ascension.AscensionRole;
import com.heavensystem.data.HeavenPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AscendCommand implements CommandExecutor {
    
    private final HeavenSystemPlugin plugin;
    
    public AscendCommand(HeavenSystemPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            player.sendMessage("§cUsage: /ascend <accept|decline> or /ascend <player> <role> (admin)");
            return true;
        }
        
        // Player accepting/declining ascension
        if (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("decline")) {
            boolean accept = args[0].equalsIgnoreCase("accept");
            plugin.getAscensionManager().processAscension(player, accept);
            return true;
        }
        
        // Admin command to force ascension
        if (args.length >= 2 && player.hasPermission("heavensystem.admin")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cPlayer not found: " + args[0]);
                return true;
            }
            
            String roleId = args[1].toLowerCase();
            AscensionRole role = plugin.getAscensionManager().getRole(roleId);
            if (role == null) {
                player.sendMessage("§cAscension role not found: " + roleId);
                player.sendMessage("§7Available roles: mediator, architect");
                return true;
            }
            
            HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(target);
            
            // Force ascension
            heavenPlayer.setAscensionRole(role);
            
            // Grant permissions
            for (String permission : role.getPermissions()) {
                target.addAttachment(plugin, permission, true);
            }
            
            player.sendMessage("§aForced ascension of " + target.getName() + " to " + role.getColoredName());
            target.sendMessage("§6You have been ascended to " + role.getFullDisplayName() + " §6by an administrator!");
            
            // Announce to server
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§6§l⚡ Administrative Ascension ⚡");
            Bukkit.broadcastMessage("§f    " + target.getName() + " §7has been elevated to " + role.getFullDisplayName());
            Bukkit.broadcastMessage("");
            
            return true;
        }
        
        player.sendMessage("§cUsage: /ascend <accept|decline> or /ascend <player> <role> (admin)");
        return true;
    }
}