package com.heavensystem.data;

import com.heavensystem.stats.PlayerStats;
import com.heavensystem.titles.Title;
import com.heavensystem.classes.PlayerClass;
import com.heavensystem.ascension.AscensionRole;
import org.bukkit.entity.Player;

import java.util.*;

public class HeavenPlayer {
    
    private final UUID playerId;
    private String playerName;
    private PlayerStats stats;
    private Set<Title> unlockedTitles;
    private Title activeTitle;
    private PlayerClass playerClass;
    private AscensionRole ascensionRole;
    private double potential;
    private Map<String, Object> customData;
    private long lastLogin;
    private long totalPlaytime;
    
    public HeavenPlayer(UUID playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.stats = new PlayerStats();
        this.unlockedTitles = new HashSet<>();
        this.customData = new HashMap<>();
        this.potential = 0.0;
        this.lastLogin = System.currentTimeMillis();
        this.totalPlaytime = 0L;
    }
    
    // Getters and Setters
    public UUID getPlayerId() {
        return playerId;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public PlayerStats getStats() {
        return stats;
    }
    
    public void setStats(PlayerStats stats) {
        this.stats = stats;
    }
    
    public Set<Title> getUnlockedTitles() {
        return unlockedTitles;
    }
    
    public void addTitle(Title title) {
        unlockedTitles.add(title);
    }
    
    public Title getActiveTitle() {
        return activeTitle;
    }
    
    public void setActiveTitle(Title activeTitle) {
        this.activeTitle = activeTitle;
    }
    
    public PlayerClass getPlayerClass() {
        return playerClass;
    }
    
    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }
    
    public AscensionRole getAscensionRole() {
        return ascensionRole;
    }
    
    public void setAscensionRole(AscensionRole ascensionRole) {
        this.ascensionRole = ascensionRole;
    }
    
    public double getPotential() {
        return potential;
    }
    
    public void addPotential(double amount) {
        this.potential += amount;
    }
    
    public void setPotential(double potential) {
        this.potential = potential;
    }
    
    public Map<String, Object> getCustomData() {
        return customData;
    }
    
    public void setCustomData(String key, Object value) {
        customData.put(key, value);
    }
    
    public Object getCustomData(String key) {
        return customData.get(key);
    }
    
    public long getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public long getTotalPlaytime() {
        return totalPlaytime;
    }
    
    public void addPlaytime(long playtime) {
        this.totalPlaytime += playtime;
    }
    
    // Utility methods
    public boolean hasTitle(String titleId) {
        return unlockedTitles.stream().anyMatch(title -> title.getId().equals(titleId));
    }
    
    public boolean isAscended() {
        return ascensionRole != null;
    }
    
    public int getTotalTitleTier() {
        return unlockedTitles.stream().mapToInt(Title::getTier).sum();
    }
}