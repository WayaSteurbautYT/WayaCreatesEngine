package com.wayacreates.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WayaCreates Engine Configuration
 * Manages all mod settings and preferences
 */
public class WayaCreatesConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/Config");
    
    // Configuration file
    private static final String CONFIG_FILE = "config/wayacreates-engine.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    // Configuration data
    private final Map<String, Object> configData = new ConcurrentHashMap<>();
    
    // Default configuration
    private final WayaCreatesSettings defaultSettings = new WayaCreatesSettings();
    
    public WayaCreatesConfig() {
        LOGGER.info("⚙️ WayaCreates Config initialized");
    }
    
    /**
     * Load configuration from file
     */
    public void loadConfig() {
        File configFile = new File(CONFIG_FILE);
        
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                WayaCreatesSettings loaded = gson.fromJson(reader, WayaCreatesSettings.class);
                if (loaded != null) {
                    // Convert loaded settings to map
                    convertToMap(loaded);
                    LOGGER.info("✅ Configuration loaded from: {}", CONFIG_FILE);
                }
            } catch (IOException e) {
                LOGGER.error("❌ Failed to load configuration", e);
                loadDefaults();
            }
        } else {
            LOGGER.info("📄 Configuration file not found, using defaults");
            loadDefaults();
        }
    }
    
    /**
     * Save configuration to file
     */
    public void saveConfig() {
        try {
            // Convert map to settings object
            WayaCreatesSettings settings = convertFromMap();
            
            // Create config directory if it doesn't exist
            File configFile = new File(CONFIG_FILE);
            configFile.getParentFile().mkdirs();
            
            // Save to file
            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(settings, writer);
                LOGGER.info("💾 Configuration saved to: {}", CONFIG_FILE);
            }
        } catch (IOException e) {
            LOGGER.error("❌ Failed to save configuration", e);
        }
    }
    
    /**
     * Load default configuration
     */
    private void loadDefaults() {
        convertToMap(defaultSettings);
        saveConfig(); // Save defaults for next time
    }
    
    /**
     * Convert settings object to map
     */
    private void convertToMap(WayaCreatesSettings settings) {
        configData.clear();
        
        // Video settings
        configData.put("video.defaultResolution", settings.video.defaultResolution);
        configData.put("video.defaultFrameRate", settings.video.defaultFrameRate);
        configData.put("video.defaultBitrate", settings.video.defaultBitrate);
        configData.put("video.hardwareAcceleration", settings.video.hardwareAcceleration);
        
        // Audio settings
        configData.put("audio.sampleRate", settings.audio.sampleRate);
        configData.put("audio.bitRate", settings.audio.bitRate);
        configData.put("audio.channels", settings.audio.channels);
        
        // Render settings
        configData.put("render.defaultViewMode", settings.render.defaultViewMode);
        configData.put("render.showGrid", settings.render.showGrid);
        configData.put("render.showGizmo", settings.render.showGizmo);
        configData.put("render.useFreshAnimations", settings.render.useFreshAnimations);
        configData.put("render.useCharacterRigs", settings.render.useCharacterRigs);
        configData.put("render.useFaceRigs", settings.render.useFaceRigs);
        
        // Animation settings
        configData.put("animation.frameRate", settings.animation.frameRate);
        configData.put("animation.enableKeyframes", settings.animation.enableKeyframes);
        configData.put("animation.enableBlending", settings.animation.enableBlending);
        configData.put("animation.autoKeyframe", settings.animation.autoKeyframe);
        
        // Overlay settings
        configData.put("overlay.showPlayerTags", settings.overlay.showPlayerTags);
        configData.put("overlay.showParticles", settings.overlay.showParticles);
        configData.put("overlay.enableVoiceChat", settings.overlay.enableVoiceChat);
        configData.put("overlay.enableMocap", settings.overlay.enableMocap);
        configData.put("overlay.enableBaritone", settings.overlay.enableBaritone);
        
        // Livestream settings
        configData.put("livestream.defaultPlatform", settings.livestream.defaultPlatform);
        configData.put("livestream.defaultQuality", settings.livestream.defaultQuality);
        configData.put("livestream.enableBackgroundRemoval", settings.livestream.enableBackgroundRemoval);
        configData.put("livestream.enableOverlays", settings.livestream.enableOverlays);
        
        // AI Assistant settings
        configData.put("ai.enabled", settings.ai.enabled);
        configData.put("ai.useFreeAPI", settings.ai.useFreeAPI);
        configData.put("ai.apiKey", settings.ai.apiKey);
        configData.put("ai.model", settings.ai.model);
        
        // Plugin settings
        configData.put("plugins.autoLoad", settings.plugins.autoLoad);
        configData.put("plugins.allowedLicenses", settings.plugins.allowedLicenses);
        
        // UI settings
        configData.put("ui.showTutorial", settings.ui.showTutorial);
        configData.put("ui.firstTimeUser", settings.ui.firstTimeUser);
        configData.put("ui.theme", settings.ui.theme);
        configData.put("ui.language", settings.ui.language);
        
        // Performance settings
        configData.put("performance.maxMemoryUsage", settings.performance.maxMemoryUsage);
        configData.put("performance.enableResourceMonitoring", settings.performance.enableResourceMonitoring);
        configData.put("performance.autoOptimize", settings.performance.autoOptimize);
    }
    
    /**
     * Convert map to settings object
     */
    private WayaCreatesSettings convertFromMap() {
        WayaCreatesSettings settings = new WayaCreatesSettings();
        
        // Video settings
        settings.video.defaultResolution = getString("video.defaultResolution", "1920x1080");
        settings.video.defaultFrameRate = getInt("video.defaultFrameRate", 30);
        settings.video.defaultBitrate = getLong("video.defaultBitrate", 8000000L);
        settings.video.hardwareAcceleration = getBoolean("video.hardwareAcceleration", true);
        
        // Audio settings
        settings.audio.sampleRate = getInt("audio.sampleRate", 48000);
        settings.audio.bitRate = getInt("audio.bitRate", 320000);
        settings.audio.channels = getInt("audio.channels", 2);
        
        // Render settings
        settings.render.defaultViewMode = getString("render.defaultViewMode", "SOLID");
        settings.render.showGrid = getBoolean("render.showGrid", true);
        settings.render.showGizmo = getBoolean("render.showGizmo", true);
        settings.render.useFreshAnimations = getBoolean("render.useFreshAnimations", true);
        settings.render.useCharacterRigs = getBoolean("render.useCharacterRigs", true);
        settings.render.useFaceRigs = getBoolean("render.useFaceRigs", true);
        
        // Animation settings
        settings.animation.frameRate = getInt("animation.frameRate", 30);
        settings.animation.enableKeyframes = getBoolean("animation.enableKeyframes", true);
        settings.animation.enableBlending = getBoolean("animation.enableBlending", true);
        settings.animation.autoKeyframe = getBoolean("animation.autoKeyframe", false);
        
        // Overlay settings
        settings.overlay.showPlayerTags = getBoolean("overlay.showPlayerTags", true);
        settings.overlay.showParticles = getBoolean("overlay.showParticles", true);
        settings.overlay.enableVoiceChat = getBoolean("overlay.enableVoiceChat", false);
        settings.overlay.enableMocap = getBoolean("overlay.enableMocap", false);
        settings.overlay.enableBaritone = getBoolean("overlay.enableBaritone", false);
        
        // Livestream settings
        settings.livestream.defaultPlatform = getString("livestream.defaultPlatform", "twitch");
        settings.livestream.defaultQuality = getString("livestream.defaultQuality", "HIGH");
        settings.livestream.enableBackgroundRemoval = getBoolean("livestream.enableBackgroundRemoval", false);
        settings.livestream.enableOverlays = getBoolean("livestream.enableOverlays", true);
        
        // AI Assistant settings
        settings.ai.enabled = getBoolean("ai.enabled", true);
        settings.ai.useFreeAPI = getBoolean("ai.useFreeAPI", true);
        settings.ai.apiKey = getString("ai.apiKey", "");
        settings.ai.model = getString("ai.model", "gpt-4");
        
        // Plugin settings
        settings.plugins.autoLoad = getBoolean("plugins.autoLoad", true);
        settings.plugins.allowedLicenses = getStringList("plugins.allowedLicenses", 
            "MIT,Apache-2.0,GPL-3.0,LGPL-3.0,BSD-3-Clause,wayacreates-license");
        
        // UI settings
        settings.ui.showTutorial = getBoolean("ui.showTutorial", true);
        settings.ui.firstTimeUser = getBoolean("ui.firstTimeUser", true);
        settings.ui.theme = getString("ui.theme", "dark");
        settings.ui.language = getString("ui.language", "en");
        
        // Performance settings
        settings.performance.maxMemoryUsage = getInt("performance.maxMemoryUsage", 2048);
        settings.performance.enableResourceMonitoring = getBoolean("performance.enableResourceMonitoring", true);
        settings.performance.autoOptimize = getBoolean("performance.autoOptimize", true);
        
        return settings;
    }
    
    // Configuration getters and setters
    public String getString(String key, String defaultValue) {
        Object value = configData.get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    public int getInt(String key, int defaultValue) {
        Object value = configData.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public long getLong(String key, long defaultValue) {
        Object value = configData.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = configData.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        try {
            return Boolean.parseBoolean(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public String getStringList(String key, String defaultValue) {
        Object value = configData.get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    public void set(String key, Object value) {
        configData.put(key, value);
    }
    
    // Specific getters for common settings
    public boolean isFirstTimeUser() {
        return getBoolean("ui.firstTimeUser", true);
    }
    
    public void setFirstTimeUser(boolean firstTime) {
        set("ui.firstTimeUser", firstTime);
    }
    
    public boolean isAIEnabled() {
        return getBoolean("ai.enabled", true);
    }
    
    public void setAIEnabled(boolean enabled) {
        set("ai.enabled", enabled);
    }
    
    public boolean isFreshAnimationsEnabled() {
        return getBoolean("render.useFreshAnimations", true);
    }
    
    public void setFreshAnimationsEnabled(boolean enabled) {
        set("render.useFreshAnimations", enabled);
    }
    
    // Settings class
    public static class WayaCreatesSettings {
        public VideoSettings video = new VideoSettings();
        public AudioSettings audio = new AudioSettings();
        public RenderSettings render = new RenderSettings();
        public AnimationSettings animation = new AnimationSettings();
        public OverlaySettings overlay = new OverlaySettings();
        public LivestreamSettings livestream = new LivestreamSettings();
        public AISettings ai = new AISettings();
        public PluginSettings plugins = new PluginSettings();
        public UISettings ui = new UISettings();
        public PerformanceSettings performance = new PerformanceSettings();
        
        // Nested settings classes
        public static class VideoSettings {
            public String defaultResolution = "1920x1080";
            public int defaultFrameRate = 30;
            public long defaultBitrate = 8000000L;
            public boolean hardwareAcceleration = true;
        }
        
        public static class AudioSettings {
            public int sampleRate = 48000;
            public int bitRate = 320000;
            public int channels = 2;
        }
        
        public static class RenderSettings {
            public String defaultViewMode = "SOLID";
            public boolean showGrid = true;
            public boolean showGizmo = true;
            public boolean useFreshAnimations = true;
            public boolean useCharacterRigs = true;
            public boolean useFaceRigs = true;
        }
        
        public static class AnimationSettings {
            public int frameRate = 30;
            public boolean enableKeyframes = true;
            public boolean enableBlending = true;
            public boolean autoKeyframe = false;
        }
        
        public static class OverlaySettings {
            public boolean showPlayerTags = true;
            public boolean showParticles = true;
            public boolean enableVoiceChat = false;
            public boolean enableMocap = false;
            public boolean enableBaritone = false;
        }
        
        public static class LivestreamSettings {
            public String defaultPlatform = "twitch";
            public String defaultQuality = "HIGH";
            public boolean enableBackgroundRemoval = false;
            public boolean enableOverlays = true;
        }
        
        public static class AISettings {
            public boolean enabled = true;
            public boolean useFreeAPI = true;
            public String apiKey = "";
            public String model = "gpt-4";
        }
        
        public static class PluginSettings {
            public boolean autoLoad = true;
            public String allowedLicenses = "MIT,Apache-2.0,GPL-3.0,LGPL-3.0,BSD-3-Clause,wayacreates-license";
        }
        
        public static class UISettings {
            public boolean showTutorial = true;
            public boolean firstTimeUser = true;
            public String theme = "dark";
            public String language = "en";
        }
        
        public static class PerformanceSettings {
            public int maxMemoryUsage = 2048;
            public boolean enableResourceMonitoring = true;
            public boolean autoOptimize = true;
        }
    }
}
