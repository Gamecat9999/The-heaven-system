package com.heavensystem.config;

import com.heavensystem.HeavenSystemPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigManager {
    
    private final HeavenSystemPlugin plugin;
    private FileConfiguration config;
    private FileConfiguration titlesConfig;
    private FileConfiguration classesConfig;
    
    public ConfigManager(HeavenSystemPlugin plugin) {
        this.plugin = plugin;
        
        loadConfigs();
        setupDefaultConfigs();
    }
    
    private void loadConfigs() {
        // Main config
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        
        // Titles config
        File titlesFile = new File(plugin.getDataFolder(), "titles.yml");
        if (!titlesFile.exists()) {
            plugin.saveResource("titles.yml", false);
        }
        titlesConfig = YamlConfiguration.loadConfiguration(titlesFile);
        
        // Classes config
        File classesFile = new File(plugin.getDataFolder(), "classes.yml");
        if (!classesFile.exists()) {
            plugin.saveResource("classes.yml", false);
        }
        classesConfig = YamlConfiguration.loadConfiguration(classesFile);
    }
    
    private void setupDefaultConfigs() {
        // Main config defaults
        config.addDefault("system.stat-gain-cooldown", 5000);
        config.addDefault("system.auto-save-interval", 300000);
        config.addDefault("system.events.enabled", true);
        config.addDefault("system.events.frequency", 60000);
        
        // Ascension requirements
        config.addDefault("ascension.mediator.potential-required", 250.0);
        config.addDefault("ascension.architect.potential-required", 1000.0);
        
        // Stat multipliers
        config.addDefault("stats.strength.combat-multiplier", 0.01);
        config.addDefault("stats.agility.movement-multiplier", 0.005);
        config.addDefault("stats.intelligence.exp-multiplier", 0.01);
        config.addDefault("stats.charisma.trade-multiplier", 0.02);
        
        config.options().copyDefaults(true);
        
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config.yml", e);
        }
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
    
    public FileConfiguration getTitlesConfig() {
        return titlesConfig;
    }
    
    public FileConfiguration getClassesConfig() {
        return classesConfig;
    }
    
    public void reloadConfigs() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        
        File titlesFile = new File(plugin.getDataFolder(), "titles.yml");
        titlesConfig = YamlConfiguration.loadConfiguration(titlesFile);
        
        File classesFile = new File(plugin.getDataFolder(), "classes.yml");
        classesConfig = YamlConfiguration.loadConfiguration(classesFile);
    }
    
    public void saveConfig(String configName) {
        try {
            switch (configName.toLowerCase()) {
                case "main":
                    config.save(new File(plugin.getDataFolder(), "config.yml"));
                    break;
                case "titles":
                    titlesConfig.save(new File(plugin.getDataFolder(), "titles.yml"));
                    break;
                case "classes":
                    classesConfig.save(new File(plugin.getDataFolder(), "classes.yml"));
                    break;
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save " + configName + " config", e);
        }
    }
}