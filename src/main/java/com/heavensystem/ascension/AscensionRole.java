package com.heavensystem.ascension;

import org.bukkit.ChatColor;

import java.util.List;

public class AscensionRole {
    
    private final String id;
    private final String name;
    private final String description;
    private final double requiredPotential;
    private final ChatColor color;
    private final List<String> permissions;
    private final List<String> abilities;
    private final String prefix;
    private final String suffix;
    
    public AscensionRole(String id, String name, String description, double requiredPotential,
                        ChatColor color, List<String> permissions, List<String> abilities,
                        String prefix, String suffix) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.requiredPotential = requiredPotential;
        this.color = color;
        this.permissions = permissions;
        this.abilities = abilities;
        this.prefix = prefix;
        this.suffix = suffix;
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
    
    public double getRequiredPotential() {
        return requiredPotential;
    }
    
    public ChatColor getColor() {
        return color;
    }
    
    public List<String> getPermissions() {
        return permissions;
    }
    
    public List<String> getAbilities() {
        return abilities;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public String getSuffix() {
        return suffix;
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
    
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
    
    public boolean hasAbility(String ability) {
        return abilities.contains(ability);
    }
}