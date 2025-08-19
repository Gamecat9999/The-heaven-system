package com.heavensystem.stats;

import java.util.HashMap;
import java.util.Map;

public class PlayerStats {
    
    private int strength;
    private int agility;
    private int intelligence;
    private int charisma;
    private Map<String, Integer> customStats;
    
    public PlayerStats() {
        this.strength = 0;
        this.agility = 0;
        this.intelligence = 0;
        this.charisma = 0;
        this.customStats = new HashMap<>();
    }
    
    // Core stat getters and setters
    public int getStrength() {
        return strength;
    }
    
    public void setStrength(int strength) {
        this.strength = Math.max(0, strength);
    }
    
    public void addStrength(int amount) {
        this.strength = Math.max(0, this.strength + amount);
    }
    
    public int getAgility() {
        return agility;
    }
    
    public void setAgility(int agility) {
        this.agility = Math.max(0, agility);
    }
    
    public void addAgility(int amount) {
        this.agility = Math.max(0, this.agility + amount);
    }
    
    public int getIntelligence() {
        return intelligence;
    }
    
    public void setIntelligence(int intelligence) {
        this.intelligence = Math.max(0, intelligence);
    }
    
    public void addIntelligence(int amount) {
        this.intelligence = Math.max(0, this.intelligence + amount);
    }
    
    public int getCharisma() {
        return charisma;
    }
    
    public void setCharisma(int charisma) {
        this.charisma = Math.max(0, charisma);
    }
    
    public void addCharisma(int amount) {
        this.charisma = Math.max(0, this.charisma + amount);
    }
    
    // Custom stats
    public Map<String, Integer> getCustomStats() {
        return customStats;
    }
    
    public int getCustomStat(String statName) {
        return customStats.getOrDefault(statName, 0);
    }
    
    public void setCustomStat(String statName, int value) {
        customStats.put(statName, Math.max(0, value));
    }
    
    public void addCustomStat(String statName, int amount) {
        int current = customStats.getOrDefault(statName, 0);
        customStats.put(statName, Math.max(0, current + amount));
    }
    
    // Utility methods
    public int getTotalStats() {
        int total = strength + agility + intelligence + charisma;
        for (int value : customStats.values()) {
            total += value;
        }
        return total;
    }
    
    public double getStatMultiplier(StatType statType) {
        int statValue = getStat(statType);
        return 1.0 + (statValue * 0.01); // 1% per stat point
    }
    
    public int getStat(StatType statType) {
        switch (statType) {
            case STRENGTH:
                return strength;
            case AGILITY:
                return agility;
            case INTELLIGENCE:
                return intelligence;
            case CHARISMA:
                return charisma;
            default:
                return 0;
        }
    }
    
    public void addStat(StatType statType, int amount) {
        switch (statType) {
            case STRENGTH:
                addStrength(amount);
                break;
            case AGILITY:
                addAgility(amount);
                break;
            case INTELLIGENCE:
                addIntelligence(amount);
                break;
            case CHARISMA:
                addCharisma(amount);
                break;
        }
    }
}