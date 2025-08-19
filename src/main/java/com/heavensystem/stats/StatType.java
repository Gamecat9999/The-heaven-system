package com.heavensystem.stats;

public enum StatType {
    STRENGTH("Strength", "STR", "§c"),
    AGILITY("Agility", "AGI", "§a"),
    INTELLIGENCE("Intelligence", "INT", "§9"),
    CHARISMA("Charisma", "CHA", "§d");
    
    private final String displayName;
    private final String shortName;
    private final String color;
    
    StatType(String displayName, String shortName, String color) {
        this.displayName = displayName;
        this.shortName = shortName;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getColoredName() {
        return color + displayName;
    }
    
    public String getColoredShortName() {
        return color + shortName;
    }
}