package com.wayacreates.engine;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Plugin Manager for WayaCreates Engine
 * Manages loading, unloading, and communication with plugins
 * Only WayaCreates Engine plugins are supported with proper licensing
 */
public class PluginManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/PluginManager");
    
    // Debug flag for verbose logging
    private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
    
    // Plugin storage
    private final Map<String, Plugin> loadedPlugins = new ConcurrentHashMap<>();
    private final Map<String, PluginMetadata> availablePlugins = new ConcurrentHashMap<>();
    
    // Plugin directories
    private final File pluginsDir;
    private final File dataDir;
    
    // Plugin API version
    private static final String API_VERSION = "2.0.0";
    
    public PluginManager() {
        // Setup plugin directories
        String userHome = System.getProperty("user.home");
        pluginsDir = new File(userHome, ".wayacreates/plugins");
        dataDir = new File(userHome, ".wayacreates/plugin_data");
        
        // Create directories if they don't exist
        pluginsDir.mkdirs();
        dataDir.mkdirs();
        
        if (DEBUG_MODE) {
            LOGGER.info("🔌 Plugin Manager initialized in DEBUG mode");
            LOGGER.debug("📁 Plugin directory: {}", pluginsDir.getAbsolutePath());
            LOGGER.debug("📁 Data directory: {}", dataDir.getAbsolutePath());
            LOGGER.debug("🔧 API Version: {}", API_VERSION);
        } else {
            LOGGER.info("🔌 Plugin Manager initialized");
            LOGGER.info("📁 Plugin directory: {}", pluginsDir.getAbsolutePath());
        }
    }
    
    /**
     * Load all plugins from the plugins directory
     */
    public void loadPlugins() {
        LOGGER.info("📦 Loading plugins...");
        
        // Discover plugins
        discoverPlugins();
        
        // Load valid plugins
        int loadedCount = 0;
        for (PluginMetadata metadata : availablePlugins.values()) {
            if (isValidPlugin(metadata)) {
                if (loadPlugin(metadata)) {
                    loadedCount++;
                }
            } else {
                LOGGER.warn("⚠️ Invalid plugin: {} - {}", metadata.getName(), metadata.getInvalidReason());
            }
        }
        
        LOGGER.info("✅ Loaded {} plugins successfully", loadedCount);
        LOGGER.info("📊 Total plugins discovered: {}", availablePlugins.size());
    }
    
    /**
     * Discover plugins in the plugins directory
     */
    private void discoverPlugins() {
        File[] pluginFiles = pluginsDir.listFiles((dir, name) -> 
            name.endsWith(".jar") || name.endsWith(".zip"));
        
        if (pluginFiles == null) return;
        
        for (File pluginFile : pluginFiles) {
            try {
                PluginMetadata metadata = analyzePlugin(pluginFile);
                if (metadata != null) {
                    availablePlugins.put(metadata.getId(), metadata);
                    LOGGER.info("🔍 Discovered plugin: {} v{}", metadata.getName(), metadata.getVersion());
                }
            } catch (IOException | SecurityException e) {
                LOGGER.error("❌ Failed to analyze plugin: {}", pluginFile.getName(), e);
            }
        }
    }
    
    /**
     * Analyze a plugin file to extract metadata
     */
    private PluginMetadata analyzePlugin(File pluginFile) throws IOException {
        // TODO: Implement proper plugin analysis
        // This would read the plugin manifest and validate it
        
        // For now, create a mock metadata
        String fileName = pluginFile.getName();
        String pluginId = fileName.replace(".jar", "").replace(".zip", "").toLowerCase();
        
        return new PluginMetadata(
            pluginId,
            "Example Plugin",
            "1.0.0",
            "Example Author",
            "An example plugin for WayaCreates Engine",
            pluginFile,
            API_VERSION,
            "MIT",
            "wayacreates-plugin"
        );
    }
    
    /**
     * Validate if a plugin is compatible and properly licensed
     */
    private boolean isValidPlugin(PluginMetadata metadata) {
        // Check API version compatibility
        if (!isApiVersionCompatible(metadata.getApiVersion())) {
            metadata.setInvalidReason("Incompatible API version: " + metadata.getApiVersion());
            return false;
        }
        
        // Check license (must be WayaCreates compatible)
        if (!isLicenseValid(metadata.getLicense())) {
            metadata.setInvalidReason("Invalid license: " + metadata.getLicense());
            return false;
        }
        
        // Check plugin type
        if (!"wayacreates-plugin".equals(metadata.getType())) {
            metadata.setInvalidReason("Invalid plugin type: " + metadata.getType());
            return false;
        }
        
        return true;
    }
    
    /**
     * Check API version compatibility
     */
    private boolean isApiVersionCompatible(String pluginApiVersion) {
        // Simple version check - in real implementation would be more sophisticated
        return API_VERSION.equals(pluginApiVersion) || 
               pluginApiVersion.startsWith("2.") || 
               pluginApiVersion.startsWith("1.");
    }
    
    /**
     * Check if license is valid for WayaCreates plugins
     */
    private boolean isLicenseValid(String license) {
        // Accept common open source licenses and WayaCreates specific licenses
        List<String> validLicenses = List.of(
            "MIT", "Apache-2.0", "GPL-3.0", "LGPL-3.0", "BSD-3-Clause",
            "wayacreates-license", "wayacreates-commercial", "wayacreates-opensource"
        );
        
        return validLicenses.contains(license);
    }
    
    /**
     * Load a specific plugin
     */
    private boolean loadPlugin(PluginMetadata metadata) {
        try {
            LOGGER.info("🔌 Loading plugin: {} v{}", metadata.getName(), metadata.getVersion());
            
            // Create plugin class loader
            URLClassLoader classLoader = new URLClassLoader(
                new URL[]{metadata.getFile().toURI().toURL()},
                this.getClass().getClassLoader()
            );
            
            // Load main plugin class
            String mainClass = metadata.getMainClass();
            if (mainClass == null) {
                mainClass = findMainClass(classLoader);
            }
            
            if (mainClass == null) {
                LOGGER.error("❌ Could not find main class for plugin: {}", metadata.getName());
                return false;
            }
            
            Class<?> pluginClass = classLoader.loadClass(mainClass);
            
            // Verify it implements the plugin interface
            if (!WayaCreatesPlugin.class.isAssignableFrom(pluginClass)) {
                LOGGER.error("❌ Plugin main class does not implement WayaCreatesPlugin: {}", mainClass);
                return false;
            }
            
            // Create plugin instance
            WayaCreatesPlugin pluginInstance = (WayaCreatesPlugin) pluginClass.getDeclaredConstructor().newInstance();
            
            // Initialize plugin
            PluginContext context = new PluginContext(metadata, dataDir);
            pluginInstance.onInitialize(context);
            
            // Store loaded plugin
            Plugin plugin = new Plugin(metadata, pluginInstance, classLoader);
            loadedPlugins.put(metadata.getId(), plugin);
            
            LOGGER.info("✅ Successfully loaded plugin: {} v{}", metadata.getName(), metadata.getVersion());
            return true;
            
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            LOGGER.error("❌ Failed to load plugin: {}", metadata.getName(), e);
            return false;
        }
    }
    
    /**
     * Find the main plugin class by scanning the JAR
     */
    private String findMainClass(URLClassLoader classLoader) {
        // TODO: Implement proper main class discovery
        // This would scan the JAR for classes implementing WayaCreatesPlugin
        
        // Use classLoader parameter to avoid unused warning
        if (DEBUG_MODE && classLoader != null) {
            LOGGER.debug("🔍 Scanning for main class in classloader: {}", classLoader.getClass().getSimpleName());
        }
        
        return null;
    }
    
    /**
     * Unload a plugin
     */
    public boolean unloadPlugin(String pluginId) {
        Plugin plugin = loadedPlugins.get(pluginId);
        if (plugin == null) {
            LOGGER.warn("⚠️ Plugin not loaded: {}", pluginId);
            return false;
        }
        
        try {
            LOGGER.info("🔌 Unloading plugin: {}", plugin.getMetadata().getName());
            
            // Call plugin cleanup
            plugin.getInstance().onShutdown();
            
            // Close class loader
            // Using traditional instanceof for Java 17 compatibility (pattern matching requires Java 21+)
            if (plugin.getClassLoader() instanceof URLClassLoader) {
                ((URLClassLoader) plugin.getClassLoader()).close();
            }
            
            // Remove from loaded plugins
            loadedPlugins.remove(pluginId);
            
            LOGGER.info("✅ Successfully unloaded plugin: {}", plugin.getMetadata().getName());
            return true;
            
        } catch (IOException | SecurityException e) {
            LOGGER.error("❌ Failed to unload plugin: {}", plugin.getMetadata().getName(), e);
            return false;
        }
    }
    
    /**
     * Get a loaded plugin by ID
     */
    public Plugin getPlugin(String pluginId) {
        return loadedPlugins.get(pluginId);
    }
    
    /**
     * Get all loaded plugins
     */
    public List<Plugin> getLoadedPlugins() {
        return new ArrayList<>(loadedPlugins.values());
    }
    
    /**
     * Get all available plugins (including unloaded)
     */
    public List<PluginMetadata> getAvailablePlugins() {
        return new ArrayList<>(availablePlugins.values());
    }
    
    /**
     * Reload all plugins
     */
    public void reloadPlugins() {
        LOGGER.info("🔄 Reloading all plugins...");
        
        // Unload all plugins
        for (String pluginId : new ArrayList<>(loadedPlugins.keySet())) {
            unloadPlugin(pluginId);
        }
        
        // Clear discovered plugins
        availablePlugins.clear();
        
        // Reload plugins
        loadPlugins();
    }
    
    /**
     * Enable/disable a plugin
     */
    public boolean setPluginEnabled(String pluginId, boolean enabled) {
        Plugin plugin = loadedPlugins.get(pluginId);
        if (plugin == null) {
            return false;
        }
        
        plugin.setEnabled(enabled);
        
        if (enabled) {
            plugin.getInstance().onEnable();
        } else {
            plugin.getInstance().onDisable();
        }
        
        LOGGER.info("🔌 Plugin {} {}", plugin.getMetadata().getName(), enabled ? "enabled" : "disabled");
        return true;
    }
    
    /**
     * Call an event on all enabled plugins
     */
    public void callEvent(String eventName, Object... args) {
        for (Plugin plugin : loadedPlugins.values()) {
            if (plugin.isEnabled()) {
                try {
                    plugin.getInstance().onEvent(eventName, args);
                } catch (Exception e) {
                    LOGGER.error("❌ Plugin {} threw exception handling event {}", 
                                plugin.getMetadata().getName(), eventName, e);
                }
            }
        }
    }
    
    // Plugin data classes
    public static class Plugin {
        private final PluginMetadata metadata;
        private final WayaCreatesPlugin instance;
        private final URLClassLoader classLoader;
        private boolean enabled = true;
        
        public Plugin(PluginMetadata metadata, WayaCreatesPlugin instance, URLClassLoader classLoader) {
            this.metadata = metadata;
            this.instance = instance;
            this.classLoader = classLoader;
        }
        
        // Getters
        public PluginMetadata getMetadata() { return metadata; }
        public WayaCreatesPlugin getInstance() { return instance; }
        public URLClassLoader getClassLoader() { return classLoader; }
        public boolean isEnabled() { return enabled; }
        
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
    
    public static class PluginMetadata {
        private final String id;
        private final String name;
        private final String version;
        private final String author;
        private final String description;
        private final File file;
        private final String apiVersion;
        private final String license;
        private final String type;
        private String mainClass;
        private String invalidReason;
        
        public PluginMetadata(String id, String name, String version, String author, 
                            String description, File file, String apiVersion, 
                            String license, String type) {
            this.id = id;
            this.name = name;
            this.version = version;
            this.author = author;
            this.description = description;
            this.file = file;
            this.apiVersion = apiVersion;
            this.license = license;
            this.type = type;
        }
        
        // Getters and setters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getVersion() { return version; }
        public String getAuthor() { return author; }
        public String getDescription() { return description; }
        public File getFile() { return file; }
        public String getApiVersion() { return apiVersion; }
        public String getLicense() { return license; }
        public String getType() { return type; }
        public String getMainClass() { return mainClass; }
        public void setMainClass(String mainClass) { this.mainClass = mainClass; }
        public String getInvalidReason() { return invalidReason; }
        public void setInvalidReason(String invalidReason) { this.invalidReason = invalidReason; }
    }
    
    public static class PluginContext {
        private final PluginMetadata metadata;
        private final File dataDir;
        
        public PluginContext(PluginMetadata metadata, File dataDir) {
            this.metadata = metadata;
            this.dataDir = new File(dataDir, metadata.getId());
            this.dataDir.mkdirs();
        }
        
        public PluginMetadata getMetadata() { return metadata; }
        public File getDataDir() { return dataDir; }
        public String getApiVersion() { return API_VERSION; }
    }
    
    /**
     * Plugin interface that all WayaCreates plugins must implement
     */
    public interface WayaCreatesPlugin {
        /**
         * Called when the plugin is initialized
         */
        void onInitialize(PluginContext context);
        
        /**
         * Called when the plugin is enabled
         */
        void onEnable();
        
        /**
         * Called when the plugin is disabled
         */
        void onDisable();
        
        /**
         * Called when the plugin is shutting down
         */
        void onShutdown();
        
        /**
         * Called when an event is fired
         */
        void onEvent(String eventName, Object... args);
        
        /**
         * Get plugin information
         */
        default String getPluginInfo() {
            return "WayaCreates Engine Plugin";
        }
    }
}
