package com.heavensystem.data;

import com.heavensystem.HeavenSystemPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerDataManager {
    
    private final HeavenSystemPlugin plugin;
    private final File dataFolder;
    private final Map<UUID, HeavenPlayer> loadedPlayers;
    
    public PlayerDataManager(HeavenSystemPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        this.loadedPlayers = new HashMap<>();
        
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }
    
    public HeavenPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId(), player.getName());
    }
    
    public HeavenPlayer getPlayer(UUID playerId, String playerName) {
        HeavenPlayer heavenPlayer = loadedPlayers.get(playerId);
        if (heavenPlayer == null) {
            heavenPlayer = loadPlayerData(playerId, playerName);
            loadedPlayers.put(playerId, heavenPlayer);
        }
        return heavenPlayer;
    }
    
    public HeavenPlayer loadPlayerData(UUID playerId, String playerName) {
        File playerFile = new File(dataFolder, playerId.toString() + ".yml");
        
        if (!playerFile.exists()) {
            return new HeavenPlayer(playerId, playerName);
        }
        
        try {
            FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
            HeavenPlayer heavenPlayer = new HeavenPlayer(playerId, playerName);
            
            // Load basic data
            heavenPlayer.setPlayerName(config.getString("name", playerName));
            heavenPlayer.setPotential(config.getDouble("potential", 0.0));
            heavenPlayer.setLastLogin(config.getLong("lastLogin", System.currentTimeMillis()));
            heavenPlayer.addPlaytime(config.getLong("totalPlaytime", 0L));
            
            // Load stats
            if (config.contains("stats")) {
                heavenPlayer.getStats().setStrength(config.getInt("stats.strength", 0));
                heavenPlayer.getStats().setAgility(config.getInt("stats.agility", 0));
                heavenPlayer.getStats().setIntelligence(config.getInt("stats.intelligence", 0));
                heavenPlayer.getStats().setCharisma(config.getInt("stats.charisma", 0));
            }
            
            // Load titles
            if (config.contains("titles")) {
                for (String titleId : config.getStringList("titles")) {
                    // Title loading will be handled by TitleManager
                    heavenPlayer.setCustomData("titleIds", config.getStringList("titles"));
                }
            }
            
            // Load class
            if (config.contains("class")) {
                heavenPlayer.setCustomData("classId", config.getString("class"));
            }
            
            // Load ascension role
            if (config.contains("ascensionRole")) {
                heavenPlayer.setCustomData("ascensionRoleId", config.getString("ascensionRole"));
            }
            
            return heavenPlayer;
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load player data for " + playerId, e);
            return new HeavenPlayer(playerId, playerName);
        }
    }
    
    public void savePlayerData(HeavenPlayer heavenPlayer) {
        File playerFile = new File(dataFolder, heavenPlayer.getPlayerId().toString() + ".yml");
        FileConfiguration config = new YamlConfiguration();
        
        try {
            // Save basic data
            config.set("name", heavenPlayer.getPlayerName());
            config.set("potential", heavenPlayer.getPotential());
            config.set("lastLogin", heavenPlayer.getLastLogin());
            config.set("totalPlaytime", heavenPlayer.getTotalPlaytime());
            
            // Save stats
            config.set("stats.strength", heavenPlayer.getStats().getStrength());
            config.set("stats.agility", heavenPlayer.getStats().getAgility());
            config.set("stats.intelligence", heavenPlayer.getStats().getIntelligence());
            config.set("stats.charisma", heavenPlayer.getStats().getCharisma());
            
            // Save titles
            java.util.List<String> titleIds = heavenPlayer.getUnlockedTitles().stream()
                    .map(title -> title.getId())
                    .collect(java.util.stream.Collectors.toList());
            config.set("titles", titleIds);
            
            if (heavenPlayer.getActiveTitle() != null) {
                config.set("activeTitle", heavenPlayer.getActiveTitle().getId());
            }
            
            // Save class
            if (heavenPlayer.getPlayerClass() != null) {
                config.set("class", heavenPlayer.getPlayerClass().getId());
            }
            
            // Save ascension role
            if (heavenPlayer.getAscensionRole() != null) {
                config.set("ascensionRole", heavenPlayer.getAscensionRole().getId());
            }
            
            config.save(playerFile);
            
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save player data for " + heavenPlayer.getPlayerId(), e);
        }
    }
    
    public void saveAllPlayerData() {
        for (HeavenPlayer heavenPlayer : loadedPlayers.values()) {
            savePlayerData(heavenPlayer);
        }
    }
    
    public void unloadPlayer(UUID playerId) {
        HeavenPlayer heavenPlayer = loadedPlayers.remove(playerId);
        if (heavenPlayer != null) {
            savePlayerData(heavenPlayer);
        }
    }
    
    public Map<UUID, HeavenPlayer> getLoadedPlayers() {
        return loadedPlayers;
    }
}