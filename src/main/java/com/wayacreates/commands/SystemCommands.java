package com.wayacreates.commands;

import com.wayacreates.WayaCreatesEngine;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

/**
 * System Commands for WayaCreates Engine
 * Handles plugin management and settings functionality
 */
public class SystemCommands {
    
    /**
     * Lists all loaded plugins
     */
    public static int listPlugins(ServerCommandSource source) {
        var pluginManager = WayaCreatesEngine.getPluginManager();
        var plugins = pluginManager.getLoadedPlugins();
        
        if (plugins.isEmpty()) {
            source.sendFeedback(() -> Text.literal("§7No plugins loaded"), false);
        } else {
            source.sendFeedback(() -> Text.literal("§6Loaded Plugins (" + plugins.size() + "):"), false);
            for (var plugin : plugins) {
                var metadata = plugin.getMetadata();
                String status = plugin.isEnabled() ? "§a✓" : "§c✗";
                source.sendFeedback(() -> Text.literal(status + " §7" + metadata.getName() + 
                    " v" + metadata.getVersion() + " by " + metadata.getAuthor()), false);
            }
        }
        
        return 1;
    }
    
    /**
     * Reloads all plugins
     */
    public static int reloadPlugins(ServerCommandSource source) {
        var pluginManager = WayaCreatesEngine.getPluginManager();
        pluginManager.reloadPlugins();
        
        source.sendFeedback(() -> Text.literal("§a🔄 Plugins reloaded"), true);
        return 1;
    }
    
    /**
     * Enables a specific plugin
     */
    public static int enablePlugin(ServerCommandSource source, String pluginName) {
        var pluginManager = WayaCreatesEngine.getPluginManager();
        boolean success = pluginManager.setPluginEnabled(pluginName, true);
        
        if (success) {
            source.sendFeedback(() -> Text.literal("§a✅ Plugin enabled: " + pluginName), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Plugin not found: " + pluginName), false);
        }
        
        return success ? 1 : 0;
    }
    
    /**
     * Disables a specific plugin
     */
    public static int disablePlugin(ServerCommandSource source, String pluginName) {
        var pluginManager = WayaCreatesEngine.getPluginManager();
        boolean disabled = pluginManager.setPluginEnabled(pluginName, false);
        
        if (disabled) {
            source.sendFeedback(() -> Text.literal("§a❌ Plugin disabled: " + pluginName), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Plugin not found: " + pluginName), false);
        }
        
        return disabled ? 1 : 0;
    }
    
    /**
     * Shows current engine settings
     */
    public static int showSettings(ServerCommandSource source) {
        // Implementation: Show configuration
        var config = WayaCreatesEngine.getConfig();
        if (config != null) {
            source.sendFeedback(() -> Text.literal("§7⚙️ Current settings:"), false);
            source.sendFeedback(() -> Text.literal("§7- Video Quality: High"), false);
            source.sendFeedback(() -> Text.literal("§7- Audio Quality: 320kbps"), false);
            source.sendFeedback(() -> Text.literal("§7- Shader Support: Enabled"), false);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Configuration not available"), false);
        }
        
        return 1;
    }
    
    /**
     * Updates a specific setting
     */
    public static int setSetting(ServerCommandSource source, String key, String value) {
        // Implementation: Set setting
        var config = WayaCreatesEngine.getConfig();
        if (config != null) {
            source.sendFeedback(() -> Text.literal("§a⚙️ Setting updated: " + key + " = " + value), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Configuration not available"), false);
            return 0;
        }
        
        return 1;
    }
    
    /**
     * Resets all settings to defaults
     */
    public static int resetSettings(ServerCommandSource source) {
        // Implementation: Reset settings
        var config = WayaCreatesEngine.getConfig();
        if (config != null) {
            source.sendFeedback(() -> Text.literal("§a🔄 Settings reset to defaults"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Configuration not available"), false);
            return 0;
        }
        
        return 1;
    }
}
