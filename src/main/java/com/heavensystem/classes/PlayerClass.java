package com.heavensystem.classes;

import com.heavensystem.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class PlayerClass {
    
    private final String id;
    private final String name;
    private final String description;
    private final ChatColor color;
    private final Material icon;
    private final Map<StatType, Integer> statPreferences;
    private final List<String> abilities;
    private final Map<String, Object> classData;
    
    public PlayerClass(String id, String name, String description, ChatColor color, 
                      Material icon, Map<StatType, Integer> statPreferences, 
                      List<String> abilities, Map<String, Object> classData) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.icon = icon;
        this.statPreferences = statPreferences;
        this.abilities = abilities;
        this.classData = classData;
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
    
    public ChatColor getColor() {
        return color;
    }
    
    public Material getIcon() {
        return icon;
    }
    
    public Map<StatType, Integer> getStatPreferences() {
        return statPreferences;
    }
    
    public List<String> getAbilities() {
        return abilities;
    }
    
    public Map<String, Object> getClassData() {
        return classData;
    }
    
    // Utility methods
    public String getColoredName() {
        return color + name;
    }
    
    public boolean hasAbility(String abilityId) {
        return abilities.contains(abilityId);
    }
    
    public Object getClassDataValue(String key) {
        return classData.get(key);
    }
    
    public void setClassDataValue(String key, Object value) {
        classData.put(key, value);
    }
}