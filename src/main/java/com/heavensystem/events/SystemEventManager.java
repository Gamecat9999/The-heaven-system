package com.heavensystem.events;

import com.heavensystem.HeavenSystemPlugin;
import com.heavensystem.data.HeavenPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SystemEventManager {
    
    private final HeavenSystemPlugin plugin;
    private final Random random;
    private final List<SystemEvent> availableEvents;
    
    public SystemEventManager(HeavenSystemPlugin plugin) {
        this.plugin = plugin;
        this.random = new Random();
        this.availableEvents = new ArrayList<>();
        
        initializeSystemEvents();
    }
    
    private void initializeSystemEvents() {
        // Blessing events
        availableEvents.add(new SystemEvent("divine_blessing", "Divine Blessing", 
            "The heavens smile upon the worthy", EventType.BLESSING, 0.05));
        
        availableEvents.add(new SystemEvent("stat_surge", "Stat Surge",
            "Mystical energy empowers those who strive", EventType.BLESSING, 0.08));
        
        // Challenge events
        availableEvents.add(new SystemEvent("mob_invasion", "Mob Invasion",
            "Hostile forces gather in the shadows", EventType.CHALLENGE, 0.03));
        
        availableEvents.add(new SystemEvent("resource_scarcity", "Resource Scarcity",
            "The world's bounty grows scarce", EventType.CHALLENGE, 0.04));
        
        // Neutral events
        availableEvents.add(new SystemEvent("mystical_phenomenon", "Mystical Phenomenon",
            "Strange energies ripple through reality", EventType.NEUTRAL, 0.06));
    }
    
    public void processRandomEvents() {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return; // No players online
        }
        
        for (SystemEvent event : availableEvents) {
            if (random.nextDouble() < event.getProbability()) {
                executeEvent(event);
                return; // Only one event per cycle
            }
        }
    }
    
    private void executeEvent(SystemEvent event) {
        switch (event.getId()) {
            case "divine_blessing":
                executeDivineBlessing();
                break;
            case "stat_surge":
                executeStatSurge();
                break;
            case "mob_invasion":
                executeMobInvasion();
                break;
            case "resource_scarcity":
                executeResourceScarcity();
                break;
            case "mystical_phenomenon":
                executeMysticalPhenomenon();
                break;
        }
    }
    
    private void executeDivineBlessing() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (players.isEmpty()) return;
        
        Player blessed = players.get(random.nextInt(players.size()));
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(blessed);
        
        // Grant potential
        double potentialGain = 5.0 + random.nextDouble() * 10.0;
        heavenPlayer.addPotential(potentialGain);
        
        // Show blessing message
        Bukkit.broadcastMessage("§8§l⚡ §6DIVINE BLESSING §8§l⚡");
        Bukkit.broadcastMessage("§f" + blessed.getName() + " §7has been blessed by the heavens!");
        Bukkit.broadcastMessage("§d+{" + String.format("%.1f", potentialGain) + "} Potential");
        
        // Add potion effect
        blessed.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 2));
        blessed.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1));
    }
    
    private void executeStatSurge() {
        Bukkit.broadcastMessage("§8§l⚡ §a STAT SURGE §8§l⚡");
        Bukkit.broadcastMessage("§7Mystical energy flows through the realm!");
        Bukkit.broadcastMessage("§7All actions grant §a+50% §7stats for the next 5 minutes!");
        
        // Apply temporary stat multiplier to all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
            heavenPlayer.setCustomData("statSurgeEnd", System.currentTimeMillis() + 300000); // 5 minutes
            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 6000, 0));
        }
    }
    
    private void executeMobInvasion() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (players.isEmpty()) return;
        
        Player target = players.get(random.nextInt(players.size()));
        Location location = target.getLocation();
        
        Bukkit.broadcastMessage("§8§l⚡ §c MOB INVASION §8§l⚡");
        Bukkit.broadcastMessage("§7Hostile forces converge near §c" + target.getName() + "§7!");
        Bukkit.broadcastMessage("§7Defeat them for bonus rewards!");
        
        // Spawn mobs around the player
        EntityType[] hostileMobs = {EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER};
        
        for (int i = 0; i < 5 + random.nextInt(5); i++) {
            EntityType mobType = hostileMobs[random.nextInt(hostileMobs.length)];
            Location spawnLoc = location.clone().add(
                random.nextInt(20) - 10,
                0,
                random.nextInt(20) - 10
            );
            spawnLoc.setY(location.getWorld().getHighestBlockYAt(spawnLoc) + 1);
            
            location.getWorld().spawnEntity(spawnLoc, mobType);
        }
    }
    
    private void executeResourceScarcity() {
        Bukkit.broadcastMessage("§8§l⚡ §6 RESOURCE SCARCITY §8§l⚡");
        Bukkit.broadcastMessage("§7The world's bounty grows scarce...");
        Bukkit.broadcastMessage("§7Block drops reduced by §c50% §7for 10 minutes!");
        
        // Set scarcity end time for all players
        long scarcityEnd = System.currentTimeMillis() + 600000; // 10 minutes
        for (Player player : Bukkit.getOnlinePlayers()) {
            HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
            heavenPlayer.setCustomData("scarcityEnd", scarcityEnd);
            player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 12000, 0));
        }
    }
    
    private void executeMysticalPhenomenon() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (players.isEmpty()) return;
        
        Bukkit.broadcastMessage("§8§l⚡ §d MYSTICAL PHENOMENON §8§l⚡");
        Bukkit.broadcastMessage("§7Reality shimmers with otherworldly energy...");
        
        // Random effects for all players
        String[] phenomena = {"teleport", "items", "effects", "weather"};
        String chosen = phenomena[random.nextInt(phenomena.length)];
        
        switch (chosen) {
            case "teleport":
                Bukkit.broadcastMessage("§7Space bends and warps around you!");
                for (Player player : players) {
                    if (random.nextDouble() < 0.3) { // 30% chance per player
                        Location randomLoc = getRandomSafeLocation(player);
                        if (randomLoc != null) {
                            player.teleport(randomLoc);
                            player.sendMessage("§dYou have been mystically relocated!");
                        }
                    }
                }
                break;
                
            case "effects":
                Bukkit.broadcastMessage("§7Magical auras envelop all beings!");
                PotionEffectType[] effects = {PotionEffectType.SPEED, PotionEffectType.JUMP, 
                                            PotionEffectType.NIGHT_VISION, PotionEffectType.WATER_BREATHING};
                PotionEffectType chosenEffect = effects[random.nextInt(effects.length)];
                
                for (Player player : players) {
                    player.addPotionEffect(new PotionEffect(chosenEffect, 3600, 1));
                }
                break;
        }
    }
    
    private Location getRandomSafeLocation(Player player) {
        Location center = player.getLocation();
        
        for (int i = 0; i < 10; i++) {
            int x = center.getBlockX() + random.nextInt(200) - 100;
            int z = center.getBlockZ() + random.nextInt(200) - 100;
            int y = center.getWorld().getHighestBlockYAt(x, z);
            
            Location loc = new Location(center.getWorld(), x + 0.5, y + 1, z + 0.5);
            if (loc.getBlock().getType() == Material.AIR && 
                loc.clone().add(0, 1, 0).getBlock().getType() == Material.AIR) {
                return loc;
            }
        }
        
        return null; // Couldn't find safe location
    }
    
    public boolean isStatSurgeActive(Player player) {
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
        Long surgeEnd = (Long) heavenPlayer.getCustomData("statSurgeEnd");
        return surgeEnd != null && System.currentTimeMillis() < surgeEnd;
    }
    
    public boolean isScarcityActive(Player player) {
        HeavenPlayer heavenPlayer = plugin.getPlayerDataManager().getPlayer(player);
        Long scarcityEnd = (Long) heavenPlayer.getCustomData("scarcityEnd");
        return scarcityEnd != null && System.currentTimeMillis() < scarcityEnd;
    }
    
    // Inner class for system events
    private static class SystemEvent {
        private final String id;
        private final String name;
        private final String description;
        private final EventType type;
        private final double probability;
        
        public SystemEvent(String id, String name, String description, EventType type, double probability) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.type = type;
            this.probability = probability;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public EventType getType() { return type; }
        public double getProbability() { return probability; }
    }
    
    private enum EventType {
        BLESSING, CHALLENGE, NEUTRAL
    }
}