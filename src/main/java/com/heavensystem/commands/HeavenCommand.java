package com.heavensystem.commands;

import com.heavensystem.HeavenSystemPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HeavenCommand implements CommandExecutor {
    
    private final HeavenSystemPlugin plugin;
    
    public HeavenCommand(HeavenSystemPlugin plugin) {
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
            showHelpMenu(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "help":
                showHelpMenu(player);
                break;
                
            case "gui":
            case "stats":
                plugin.getGuiManager().openStatsGui(player);
                break;
                
            case "titles":
                plugin.getGuiManager().openTitlesGui(player);
                break;
                
            case "info":
                showSystemInfo(player);
                break;
                
            case "reload":
                if (player.hasPermission("heavensystem.admin")) {
                    plugin.getConfigManager().reloadConfigs();
                    player.sendMessage("§aHeaven System configs reloaded!");
                } else {
                    player.sendMessage("§cYou don't have permission to use this command.");
                }
                break;
                
            default:
                player.sendMessage("§cUnknown subcommand. Use '/heaven help' for help.");
                break;
        }
        
        return true;
    }
    
    private void showHelpMenu(Player player) {
        player.sendMessage("§8§l═══════════════════════════════════════════════════");
        player.sendMessage("§6§l                 Heaven System Help");
        player.sendMessage("§8§l═══════════════════════════════════════════════════");
        player.sendMessage("");
        player.sendMessage("§e/heaven gui §7- Open your stats and progression GUI");
        player.sendMessage("§e/heaven titles §7- View and manage your titles");
        player.sendMessage("§e/heaven info §7- View system information");
        player.sendMessage("§e/stats [player] §7- View player statistics");
        player.sendMessage("§e/titles [player] §7- View player titles");
        
        if (player.hasPermission("heavensystem.admin")) {
            player.sendMessage("");
            player.sendMessage("§c§lAdmin Commands:");
            player.sendMessage("§c/heaven reload §7- Reload configurations");
            player.sendMessage("§c/ascend <player> <role> §7- Force ascend a player");
        }
        
        player.sendMessage("");
        player.sendMessage("§8§l═══════════════════════════════════════════════════");
    }
    
    private void showSystemInfo(Player player) {
        player.sendMessage("§8§l═══════════════════════════════════════════════════");
        player.sendMessage("§6§l               Heaven System Info");
        player.sendMessage("§8§l═══════════════════════════════════════════════════");
        player.sendMessage("");
        player.sendMessage("§7The Heaven System is a mythic progression framework");
        player.sendMessage("§7that transforms Minecraft into a realm of growth,");
        player.sendMessage("§7mastery, and divine recognition.");
        player.sendMessage("");
        player.sendMessage("§e§lCore Features:");
        player.sendMessage("§7• Stat-based progression system");
        player.sendMessage("§7• Mythic titles with unique rewards");
        player.sendMessage("§7• Class evolution trees");
        player.sendMessage("§7• Ascension to divine roles");
        player.sendMessage("§7• Dynamic system events");
        player.sendMessage("");
        player.sendMessage("§d§lYour Journey:");
        player.sendMessage("§7Gain stats through actions, earn titles through feats,");
        player.sendMessage("§7accumulate potential, and ascend to reshape reality.");
        player.sendMessage("");
        player.sendMessage("§8§l═══════════════════════════════════════════════════");
    }
}