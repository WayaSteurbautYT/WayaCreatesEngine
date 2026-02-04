package com.wayacreates.ui.enhanced;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wayacreates.WayaCreatesEngine;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Configuration class for UI settings and preferences
 * Handles loading and saving of UI configuration using JSON format
 */
public class UIConfiguration {
    private static final String CONFIG_FILE = "config/wayacreates-ui.json";
    private static UIConfiguration instance;
    private final Gson gson = new Gson();
    
    // UI Settings
    private boolean darkMode = true;
    private boolean showTooltips = true;
    private boolean enableAnimations = true;
    private int uiScale = 100;
    private String theme = "dark";
    
    // Layout Settings
    private boolean showToolbar = true;
    private boolean showTimeline = true;
    private boolean showProperties = true;
    private boolean showEffects = true;
    private boolean showAudio = true;
    
    // Performance Settings
    private boolean enableVSync = true;
    private int maxFPS = 60;
    private boolean enableHardwareAcceleration = true;
    
    public UIConfiguration() {
        loadConfiguration();
    }
    
    public static UIConfiguration getInstance() {
        if (instance == null) {
            instance = new UIConfiguration();
        }
        return instance;
    }
    
    /**
     * Load default settings
     */
    public void loadDefaultSettings() {
        darkMode = true;
        showTooltips = true;
        enableAnimations = true;
        uiScale = 100;
        theme = "dark";
        
        showToolbar = true;
        showTimeline = true;
        showProperties = true;
        showEffects = true;
        showAudio = true;
        
        enableVSync = true;
        maxFPS = 60;
        enableHardwareAcceleration = true;
    }
    
    /**
     * Check if debug mode is enabled
     */
    public boolean isDebugMode() {
        return false; // Could be loaded from config
    }
    
    /**
     * Load configuration from JSON object
     */
    public void loadFromJson(com.google.gson.JsonObject config) {
        if (config.has("darkMode")) darkMode = config.get("darkMode").getAsBoolean();
        if (config.has("showTooltips")) showTooltips = config.get("showTooltips").getAsBoolean();
        if (config.has("enableAnimations")) enableAnimations = config.get("enableAnimations").getAsBoolean();
        if (config.has("uiScale")) uiScale = config.get("uiScale").getAsInt();
        if (config.has("theme")) theme = config.get("theme").getAsString();
        
        if (config.has("showToolbar")) showToolbar = config.get("showToolbar").getAsBoolean();
        if (config.has("showTimeline")) showTimeline = config.get("showTimeline").getAsBoolean();
        if (config.has("showProperties")) showProperties = config.get("showProperties").getAsBoolean();
        if (config.has("showEffects")) showEffects = config.get("showEffects").getAsBoolean();
        if (config.has("showAudio")) showAudio = config.get("showAudio").getAsBoolean();
        
        if (config.has("enableVSync")) enableVSync = config.get("enableVSync").getAsBoolean();
        if (config.has("maxFPS")) maxFPS = config.get("maxFPS").getAsInt();
        if (config.has("enableHardwareAcceleration")) enableHardwareAcceleration = config.get("enableHardwareAcceleration").getAsBoolean();
    }
    
    /**
     * Save configuration to JSON object
     */
    public com.google.gson.JsonObject saveToJson() {
        com.google.gson.JsonObject config = new com.google.gson.JsonObject();
        
        config.addProperty("darkMode", darkMode);
        config.addProperty("showTooltips", showTooltips);
        config.addProperty("enableAnimations", enableAnimations);
        config.addProperty("uiScale", uiScale);
        config.addProperty("theme", theme);
        
        config.addProperty("showToolbar", showToolbar);
        config.addProperty("showTimeline", showTimeline);
        config.addProperty("showProperties", showProperties);
        config.addProperty("showEffects", showEffects);
        config.addProperty("showAudio", showAudio);
        
        config.addProperty("enableVSync", enableVSync);
        config.addProperty("maxFPS", maxFPS);
        config.addProperty("enableHardwareAcceleration", enableHardwareAcceleration);
        
        return config;
    }
    
    /**
     * Load configuration from JSON file
     */
    private void loadConfiguration() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                FileReader reader = new FileReader(configFile);
                JsonObject config = gson.fromJson(reader, JsonObject.class);
                reader.close();
                
                // Load settings from JSON
                if (config.has("darkMode")) darkMode = config.get("darkMode").getAsBoolean();
                if (config.has("showTooltips")) showTooltips = config.get("showTooltips").getAsBoolean();
                if (config.has("enableAnimations")) enableAnimations = config.get("enableAnimations").getAsBoolean();
                if (config.has("uiScale")) uiScale = config.get("uiScale").getAsInt();
                if (config.has("theme")) theme = config.get("theme").getAsString();
                
                if (config.has("showToolbar")) showToolbar = config.get("showToolbar").getAsBoolean();
                if (config.has("showTimeline")) showTimeline = config.get("showTimeline").getAsBoolean();
                if (config.has("showProperties")) showProperties = config.get("showProperties").getAsBoolean();
                if (config.has("showEffects")) showEffects = config.get("showEffects").getAsBoolean();
                if (config.has("showAudio")) showAudio = config.get("showAudio").getAsBoolean();
                
                if (config.has("enableVSync")) enableVSync = config.get("enableVSync").getAsBoolean();
                if (config.has("maxFPS")) maxFPS = config.get("maxFPS").getAsInt();
                if (config.has("enableHardwareAcceleration")) enableHardwareAcceleration = config.get("enableHardwareAcceleration").getAsBoolean();
                
                WayaCreatesEngine.LOGGER.info("UI configuration loaded successfully");
            }
        } catch (IOException e) {
            WayaCreatesEngine.LOGGER.warn("Failed to load UI configuration, using defaults: " + e.getMessage());
        }
    }
    
    /**
     * Save configuration to JSON file
     */
    public void saveConfiguration() {
        try {
            JsonObject config = new JsonObject();
            
            // Save settings to JSON
            config.addProperty("darkMode", darkMode);
            config.addProperty("showTooltips", showTooltips);
            config.addProperty("enableAnimations", enableAnimations);
            config.addProperty("uiScale", uiScale);
            config.addProperty("theme", theme);
            
            config.addProperty("showToolbar", showToolbar);
            config.addProperty("showTimeline", showTimeline);
            config.addProperty("showProperties", showProperties);
            config.addProperty("showEffects", showEffects);
            config.addProperty("showAudio", showAudio);
            
            config.addProperty("enableVSync", enableVSync);
            config.addProperty("maxFPS", maxFPS);
            config.addProperty("enableHardwareAcceleration", enableHardwareAcceleration);
            
            File configFile = new File(CONFIG_FILE);
            configFile.getParentFile().mkdirs();
            
            FileWriter writer = new FileWriter(configFile);
            gson.toJson(config, writer);
            writer.close();
            
            WayaCreatesEngine.LOGGER.info("UI configuration saved successfully");
        } catch (IOException e) {
            WayaCreatesEngine.LOGGER.error("Failed to save UI configuration: " + e.getMessage());
        }
    }
    
    // Getters and setters
    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }
    
    public boolean isShowTooltips() { return showTooltips; }
    public void setShowTooltips(boolean showTooltips) { this.showTooltips = showTooltips; }
    
    public boolean isEnableAnimations() { return enableAnimations; }
    public void setEnableAnimations(boolean enableAnimations) { this.enableAnimations = enableAnimations; }
    
    public int getUiScale() { return uiScale; }
    public void setUiScale(int uiScale) { this.uiScale = uiScale; }
    
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    
    public boolean isShowToolbar() { return showToolbar; }
    public void setShowToolbar(boolean showToolbar) { this.showToolbar = showToolbar; }
    
    public boolean isShowTimeline() { return showTimeline; }
    public void setShowTimeline(boolean showTimeline) { this.showTimeline = showTimeline; }
    
    public boolean isShowProperties() { return showProperties; }
    public void setShowProperties(boolean showProperties) { this.showProperties = showProperties; }
    
    public boolean isShowEffects() { return showEffects; }
    public void setShowEffects(boolean showEffects) { this.showEffects = showEffects; }
    
    public boolean isShowAudio() { return showAudio; }
    public void setShowAudio(boolean showAudio) { this.showAudio = showAudio; }
    
    public boolean isEnableVSync() { return enableVSync; }
    public void setEnableVSync(boolean enableVSync) { this.enableVSync = enableVSync; }
    
    public int getMaxFPS() { return maxFPS; }
    public void setMaxFPS(int maxFPS) { this.maxFPS = maxFPS; }
    
    public boolean isEnableHardwareAcceleration() { return enableHardwareAcceleration; }
    public void setEnableHardwareAcceleration(boolean enableHardwareAcceleration) { this.enableHardwareAcceleration = enableHardwareAcceleration; }
}
