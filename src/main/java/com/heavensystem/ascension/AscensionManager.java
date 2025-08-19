package com.heavensystem.ascension;

import com.heavensystem.HeavenSystemPlugin;
import com.heavensystem.data.HeavenPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class AscensionManager {
    
    private final HeavenSystemPlugin plugin;
    private final Map<String, AscensionRole> roles;
    
    public AscensionManager(HeavenSystemPlugin plugin) {
        this.plugin = plugin;
        this.roles = new HashMap<>();
        
        initializeAscensionRoles();
    }
    
    private void initializeAscensionRoles() {
        // System Mediator
        List<String> mediatorPerms = Arrays.asList(
            "heavensystem.mediator",
            "minecraft.command.tp",
            "minecraft.command.give"
        );
        List<String> mediatorAbilities = Arrays.asList(
            "divine_sight", "entity_inspection", "temporary_buffs", "minor_teleportation"
        );
        
        roles.put("mediator", new AscensionRole("mediator", "System Mediator",
            "A semi-divine guardian who maintains balance and guides mortals",
            250.0, ChatColor.LIGHT_PURPLE, mediatorPerms, mediatorAbilities,
            "◊", "◊"));
        
        // System Architect
        List<String> architectPerms = Arrays.asList(
            "heavensystem.architect",
            "minecraft.command.*",
            "worldedit.*"
        );
        List<String> architectAbilities = Arrays.asList(
            "world_shaping", "event_creation", "system_modification", "reality_bending"
        );
        
        roles.put("architect", new AscensionRole("architect", "System Architect",
            "Master of reality itself, capable of reshaping the world",
            1000.0, ChatColor.GOLD, architectPerms, architectAbilities,
            "⬢", "⬢"));
    }
    
    public void checkAscensionEligibility(Player player, HeavenPlayer heavenPlayer) {
        double potential = heavenPlayer.getPotential();
        
        // Check if player qualifies for any ascension role
        for (AscensionRole role : roles.values()) {
            if (potential >= role.getRequiredPotential() && 
                (heavenPlayer.getAscensionRole() == null || 
                 heavenPlayer.getAscensionRole().getRequiredPotential() < role.getRequiredPotential())) {
                
                offerAscension(player, heavenPlayer, role);
                return; // Only offer one ascension at a time
            }
        }
    }
    
    private void offerAscension(Player player, HeavenPlayer heavenPlayer, AscensionRole role) {
        // Clear chat and show dramatic ascension offer
        for (int i = 0; i < 30; i++) {
            player.sendMessage("");
        }
        
        player.sendMessage("§8§l═══════════════════════════════════════════════════");
        player.sendMessage("");
        player.sendMessage("§6§l              ⚡ ASCENSION AWAITS ⚡");
        player.sendMessage("");
        player.sendMessage("§f         The Heaven System recognizes your worth.");
        player.sendMessage("§f            You are eligible for ascension to:");
        player.sendMessage("");
        player.sendMessage("              " + role.getFullDisplayName());
        player.sendMessage("");
        player.sendMessage("§7         " + role.getDescription());
        player.sendMessage("");
        player.sendMessage("§8         Required Potential: §d" + role.getRequiredPotential());
        player.sendMessage("§8         Your Potential: §d" + heavenPlayer.getPotential());
        player.sendMessage("");
        player.sendMessage("§a§l         Type '/ascend accept' to ascend");
        player.sendMessage("§c§l         Type '/ascend decline' to remain mortal");
        player.sendMessage("");
        player.sendMessage("§8§l═══════════════════════════════════════════════════");
        
        // Store pending ascension
        heavenPlayer.setCustomData("pendingAscension", role.getId());
        
        // Play dramatic sound
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.7f);
    }
    
    public void processAscension(Player player, boolean accept) {
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
        String pendingRoleId = (String) heavenPlayer.getCustomData("pendingAscension");
        
        if (pendingRoleId == null) {
            player.sendMessage("§cYou have no pending ascension.");
            return;
        }
        
        AscensionRole role = roles.get(pendingRoleId);
        if (role == null) {
            player.sendMessage("§cInvalid ascension role.");
            heavenPlayer.setCustomData("pendingAscension", null);
            return;
        }
        
        if (accept) {
            performAscension(player, heavenPlayer, role);
        } else {
            player.sendMessage("§7You have chosen to remain among mortals... for now.");
            heavenPlayer.setCustomData("pendingAscension", null);
        }
    }
    
    private void performAscension(Player player, HeavenPlayer heavenPlayer, AscensionRole role) {
        // Set the ascension role
        heavenPlayer.setAscensionRole(role);
        heavenPlayer.setCustomData("pendingAscension", null);
        
        // Grant permissions
        for (String permission : role.getPermissions()) {
            player.addAttachment(plugin, permission, true);
        }
        
        // Show epic ascension sequence
        showAscensionSequence(player, role);
        
        // Announce to server
        plugin.getServer().broadcastMessage("");
        plugin.getServer().broadcastMessage("§6§l⚡ A mortal has ascended! ⚡");
        plugin.getServer().broadcastMessage("§f    " + player.getName() + " §7is now a " + role.getFullDisplayName());
        plugin.getServer().broadcastMessage("");
    }
    
    private void showAscensionSequence(Player player, AscensionRole role) {
        // Clear chat
        for (int i = 0; i < 50; i++) {
            player.sendMessage("");
        }
        
        player.sendMessage("§8§l╔═══════════════════════════════════════════════════╗");
        player.sendMessage("§8§l║                                                   ║");
        player.sendMessage("§8§l║  §6§l               ASCENSION COMPLETE              §8§l║");
        player.sendMessage("§8§l║                                                   ║");
        player.sendMessage("§8§l║      §fYou have transcended mortal limitations      §8§l║");
        player.sendMessage("§8§l║                                                   ║");
        player.sendMessage("§8§l║              " + role.getFullDisplayName() + "               §8§l║");
        player.sendMessage("§8§l║                                                   ║");
        player.sendMessage("§8§l║         §7New abilities have been granted           §8§l║");
        player.sendMessage("§8§l║         §7Reality bends to your will               §8§l║");
        player.sendMessage("§8§l║                                                   ║");
        player.sendMessage("§8§l╚═══════════════════════════════════════════════════╝");
        
        // Play epic effects
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 0.5f);
        
        // Spawn particle effects
        for (int i = 0; i < 20; i++) {
            player.getWorld().playEffect(
                player.getLocation().add(Math.random() * 6 - 3, Math.random() * 3, Math.random() * 6 - 3),
                Effect.ENDERDRAGON_SHOOT, 1
            );
        }
    }
    
    public AscensionRole getRole(String id) {
        return roles.get(id);
    }
    
    public Map<String, AscensionRole> getAllRoles() {
        return roles;
    }
    
    public void registerRole(AscensionRole role) {
        roles.put(role.getId(), role);
    }
}