package com.heavensystem.titles;

import com.heavensystem.stats.StatType;
import org.bukkit.ChatColor;

import java.util.Map;
import java.util.Objects;

public class Title {
    
    private final String id;
    private final String name;
    private final String description;
    private final int tier;
    private final Map<StatType, Integer> statBonuses;
    private final double potentialGrant;
    private final String prefix;
    private final String suffix;
    private final ChatColor color;
    private final boolean hasParticleEffect;
    
    public Title(String id, String name, String description, int tier, 
                Map<StatType, Integer> statBonuses, double potentialGrant, 
                String prefix, String suffix, ChatColor color, boolean hasParticleEffect) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.statBonuses = statBonuses;
        this.potentialGrant = potentialGrant;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
        this.hasParticleEffect = hasParticleEffect;
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getTier() {
        return tier;
    }
    
    public Map<StatType, Integer> getStatBonuses() {
        return statBonuses;
    }
    
    public double getPotentialGrant() {
        return potentialGrant;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
    public ChatColor getColor() {
        return color;
    }
    
    public boolean hasParticleEffect() {
        return hasParticleEffect;
    }
    
    // Utility methods
    public String getColoredName() {
        return color + name;
    }
    
    public String getFullDisplayName() {
        StringBuilder sb = new StringBuilder();
        if (prefix != null && !prefix.isEmpty()) {
            sb.append(color).append(prefix).append(" ");
        }
        sb.append(color).append(name);
        if (suffix != null && !suffix.isEmpty()) {
            sb.append(" ").append(color).append(suffix);
        }
        return sb.toString();
    }
    
    public String getTierDisplay() {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < tier; i++) {
            stars.append("★");
        }
        return color + stars.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title = (Title) o;
        return Objects.equals(id, title.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Title{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", tier=" + tier +
                '}';
    }
}