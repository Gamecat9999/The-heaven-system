package com.heavensystem;

import com.heavensystem.commands.HeavenCommand;
import com.heavensystem.commands.StatsCommand;
import com.heavensystem.commands.TitlesCommand;
import com.heavensystem.commands.AscendCommand;
import com.heavensystem.config.ConfigManager;
import com.heavensystem.data.PlayerDataManager;
import com.heavensystem.events.PlayerEventListener;
import com.heavensystem.events.SystemEventManager;
import com.heavensystem.gui.GuiManager;
import com.heavensystem.stats.StatManager;
import com.heavensystem.titles.TitleManager;
import com.heavensystem.classes.ClassManager;
import com.heavensystem.ascension.AscensionManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HeavenSystemPlugin extends JavaPlugin {
    
    private static HeavenSystemPlugin instance;
    private ConfigManager configManager;
    private PlayerDataManager playerDataManager;
    private StatManager statManager;
    private TitleManager titleManager;
    private ClassManager classManager;
    private AscensionManager ascensionManager;
    private SystemEventManager systemEventManager;
    private GuiManager guiManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        initializeManagers();
        
        // Register events
        registerEvents();
        
        // Register commands
        registerCommands();
        
        // Start periodic tasks
        startPeriodicTasks();
        
        getLogger().info("Heaven System has been enabled! The divine realm awaits...");
    }
    
    @Override
    public void onDisable() {
        // Save all player data
        if (playerDataManager != null) {
            playerDataManager.saveAllPlayerData();
        }
        
        // Cancel all tasks
        getServer().getScheduler().cancelTasks(this);
        
        getLogger().info("Heaven System has been disabled. Until we meet again...");
    }
    
    private void initializeManagers() {
        configManager = new ConfigManager(this);
        playerDataManager = new PlayerDataManager(this);
        statManager = new StatManager(this);
        titleManager = new TitleManager(this);
        classManager = new ClassManager(this);
        ascensionManager = new AscensionManager(this);
        systemEventManager = new SystemEventManager(this);
        guiManager = new GuiManager(this);
    }
    
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
    }
    
    private void registerCommands() {
        getCommand("heaven").setExecutor(new HeavenCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("titles").setExecutor(new TitlesCommand(this));
        getCommand("ascend").setExecutor(new AscendCommand(this));
    }
    
    private void startPeriodicTasks() {
        // Auto-save task every 5 minutes
        new BukkitRunnable() {
            @Override
            public void run() {
                playerDataManager.saveAllPlayerData();
            }
        }.runTaskTimerAsynchronously(this, 6000L, 6000L);
        
        // System events task
        new BukkitRunnable() {
            @Override
            public void run() {
                systemEventManager.processRandomEvents();
            }
        }.runTaskTimer(this, 1200L, 1200L); // Every minute
    }
    
    // Getters
    public static HeavenSystemPlugin getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
    
    public StatManager getStatManager() {
        return statManager;
    }
    
    public TitleManager getTitleManager() {
        return titleManager;
    }
    
    public ClassManager getClassManager() {
        return classManager;
    }
    
    public AscensionManager getAscensionManager() {
        return ascensionManager;
    }
    
    public SystemEventManager getSystemEventManager() {
        return systemEventManager;
    }
    
    public GuiManager getGuiManager() {
        return guiManager;
    }
}