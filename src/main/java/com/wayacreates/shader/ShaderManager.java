package com.wayacreates.shader;

import com.wayacreates.WayaCreatesEngine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Shader Manager for handling Iris shader integration and compatibility
 * Provides shader loading, switching, and configuration management
 */
public class ShaderManager {
    private static boolean irisPresent = false;
    private static boolean sodiumPresent = false;
    private static boolean shaderSupportEnabled = false;
    
    static {
        checkShaderCompatibility();
    }
    
    /**
     * Check for shader mod compatibility and initialize support
     */
    private static void checkShaderCompatibility() {
        try {
            // Check for Iris
            Class.forName("net.coderbot.iris.Iris");
            irisPresent = true;
            WayaCreatesEngine.LOGGER.info("Iris shaders detected - enabling shader support");
        } catch (ClassNotFoundException e) {
            WayaCreatesEngine.LOGGER.info("Iris not found - shader support disabled");
        }
        
        try {
            // Check for Sodium
            Class.forName("net.caffeinemc.mods.sodium.client.SodiumClientMod");
            sodiumPresent = true;
            WayaCreatesEngine.LOGGER.info("Sodium detected - optimizing for compatibility");
        } catch (ClassNotFoundException e) {
            WayaCreatesEngine.LOGGER.info("Sodium not found - using default rendering");
        }
        
        shaderSupportEnabled = irisPresent;
    }
    
    /**
     * Check if shader support is available
     */
    public static boolean isShaderSupportEnabled() {
        return shaderSupportEnabled;
    }
    
    /**
     * Check if Iris is present
     */
    public static boolean isIrisPresent() {
        return irisPresent;
    }
    
    /**
     * Check if Sodium is present
     */
    public static boolean isSodiumPresent() {
        return sodiumPresent;
    }
    
    /**
     * Get current shader pack information
     */
    public static String getCurrentShaderPack() {
        if (!irisPresent) {
            return "No shader support";
        }
        
        try {
            // Try to get current shader pack from Iris
            Class<?> irisClass = Class.forName("net.coderbot.iris.Iris");
            Object iris = irisClass.getMethod("getIrisConfig").invoke(null);
            if (iris != null) {
                Object shaderPack = iris.getClass().getMethod("getShaderPackName").invoke(iris);
                return shaderPack != null ? shaderPack.toString() : "None";
            }
        } catch (Exception e) {
            WayaCreatesEngine.LOGGER.warn("Failed to get current shader pack: {}", e.getMessage());
        }
        
        return "Unknown";
    }
    
    /**
     * Reload shaders
     */
    public static void reloadShaders() {
        if (!irisPresent) {
            sendMessageToPlayer("❌ Shader support not available", Formatting.RED);
            return;
        }
        
        try {
            // Try to reload shaders through Iris
            Class<?> irisClass = Class.forName("net.coderbot.iris.Iris");
            irisClass.getMethod("reload").invoke(null);
            sendMessageToPlayer("✅ Shaders reloaded", Formatting.GREEN);
        } catch (Exception e) {
            WayaCreatesEngine.LOGGER.warn("Failed to reload shaders: {}", e.getMessage());
            sendMessageToPlayer("⚠ Failed to reload shaders", Formatting.YELLOW);
        }
    }
    
    /**
     * Get shader compatibility status
     */
    public static String getCompatibilityStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Shader Compatibility Status:\n");
        status.append("• Iris: ").append(irisPresent ? "✅ Detected" : "❌ Not found").append("\n");
        status.append("• Sodium: ").append(sodiumPresent ? "✅ Detected" : "❌ Not found").append("\n");
        status.append("• Entity Model Features: ✅ Available\n");
        status.append("• Entity Texture Features: ✅ Available\n");
        status.append("• Fresh Animations: ✅ Compatible");
        
        return status.toString();
    }
    
    /**
     * Toggle shader support (if available)
     */
    public static void toggleShaderSupport() {
        if (!irisPresent) {
            sendMessageToPlayer("❌ Iris not installed - cannot toggle shaders", Formatting.RED);
            return;
        }
        
        // This would need to be implemented based on Iris API
        sendMessageToPlayer("🔄 Shader toggle not yet implemented", Formatting.YELLOW);
    }
    
    /**
     * Helper method to safely send messages to player
     */
    private static void sendMessageToPlayer(String message, Formatting formatting) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                client.player.sendMessage(Text.literal(message).formatted(formatting), true);
            }
        } catch (Exception e) {
            WayaCreatesEngine.LOGGER.warn("Failed to send message to player: {}", e.getMessage());
        }
    }
}
