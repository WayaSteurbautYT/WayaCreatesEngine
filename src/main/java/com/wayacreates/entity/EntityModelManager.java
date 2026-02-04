package com.wayacreates.entity;

import com.wayacreates.WayaCreatesEngine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Entity Model Manager for handling EMF and ETF integration
 * Provides entity animation, texture features, and model management
 */
public class EntityModelManager {
    private static boolean emfPresent = false;
    private static boolean etfPresent = false;
    private static boolean entityCullingPresent = false;
    private static boolean freshAnimationsCompatible = false;
    
    static {
        checkEntityFeatures();
    }
    
    /**
     * Check for entity feature mods and initialize compatibility
     */
    private static void checkEntityFeatures() {
        try {
            // Check for Entity Model Features (EMF)
            Class.forName("dev.traben.entitymodelfeatures.EMF");
            emfPresent = true;
            WayaCreatesEngine.LOGGER.info("Entity Model Features detected - enabling custom entity models");
        } catch (ClassNotFoundException e) {
            WayaCreatesEngine.LOGGER.info("Entity Model Features not found");
        }
        
        try {
            // Check for Entity Texture Features (ETF)
            Class.forName("dev.traben.entitytexturefeatures.ETF");
            etfPresent = true;
            WayaCreatesEngine.LOGGER.info("Entity Texture Features detected - enabling texture features");
        } catch (ClassNotFoundException e) {
            WayaCreatesEngine.LOGGER.info("Entity Texture Features not found");
        }
        
        try {
            // Check for Entity Culling
            Class.forName("net.alexwils.ketherculling.EntityCulling");
            entityCullingPresent = true;
            WayaCreatesEngine.LOGGER.info("Entity Culling detected - performance optimization available");
        } catch (ClassNotFoundException e) {
            WayaCreatesEngine.LOGGER.info("Entity Culling not found");
        }
        
        // Fresh Animations compatibility check
        freshAnimationsCompatible = emfPresent && etfPresent;
        if (freshAnimationsCompatible) {
            WayaCreatesEngine.LOGGER.info("Fresh Animations compatibility enabled");
        }
    }
    
    /**
     * Check if Entity Model Features is present
     */
    public static boolean isEMFPresent() {
        return emfPresent;
    }
    
    /**
     * Check if Entity Texture Features is present
     */
    public static boolean isETFPresent() {
        return etfPresent;
    }
    
    /**
     * Check if Entity Culling is present
     */
    public static boolean isEntityCullingPresent() {
        return entityCullingPresent;
    }
    
    /**
     * Check if Fresh Animations is compatible
     */
    public static boolean isFreshAnimationsCompatible() {
        return freshAnimationsCompatible;
    }
    
    /**
     * Get entity feature status
     */
    public static String getEntityFeatureStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Entity Features Status:\n");
        status.append("• Entity Model Features (EMF): ").append(emfPresent ? "✅ Active" : "❌ Not found").append("\n");
        status.append("• Entity Texture Features (ETF): ").append(etfPresent ? "✅ Active" : "❌ Not found").append("\n");
        status.append("• Entity Culling: ").append(entityCullingPresent ? "✅ Active" : "❌ Not found").append("\n");
        status.append("• Fresh Animations: ").append(freshAnimationsCompatible ? "✅ Compatible" : "⚠ Requires EMF+ETF");
        
        return status.toString();
    }
    
    /**
     * Reload entity models and textures
     */
    public static void reloadEntityFeatures() {
        if (!emfPresent && !etfPresent) {
            sendMessageToPlayer("❌ No entity feature mods installed", Formatting.RED);
            return;
        }
        
        try {
            // Try to reload EMF
            if (emfPresent) {
                Class<?> emfClass = Class.forName("dev.traben.entitymodelfeatures.EMF");
                emfClass.getMethod("reload").invoke(null);
            }
            
            // Try to reload ETF
            if (etfPresent) {
                Class<?> etfClass = Class.forName("dev.traben.entitytexturefeatures.ETF");
                etfClass.getMethod("reload").invoke(null);
            }
            
            sendMessageToPlayer("✅ Entity features reloaded", Formatting.GREEN);
        } catch (Exception e) {
            WayaCreatesEngine.LOGGER.warn("Failed to reload entity features: {}", e.getMessage());
            sendMessageToPlayer("⚠ Failed to reload entity features", Formatting.YELLOW);
        }
    }
    
    /**
     * Toggle entity culling (if available)
     */
    public static void toggleEntityCulling() {
        if (!entityCullingPresent) {
            sendMessageToPlayer("❌ Entity Culling not installed", Formatting.RED);
            return;
        }
        
        try {
            // This would need to be implemented based on Entity Culling API
            sendMessageToPlayer("🔄 Entity culling toggle not yet implemented", Formatting.YELLOW);
        } catch (Exception e) {
            WayaCreatesEngine.LOGGER.warn("Failed to toggle entity culling: {}", e.getMessage());
        }
    }
    
    /**
     * Get entity animation info for debugging
     */
    public static String getEntityInfo(Entity entity) {
        if (entity == null) {
            return "No entity selected";
        }
        
        EntityType<?> type = entity.getType();
        StringBuilder info = new StringBuilder();
        info.append("Entity: ").append(type.getTranslationKey()).append("\n");
        info.append("• Custom Models: ").append(emfPresent ? "Available" : "Not supported").append("\n");
        info.append("• Texture Features: ").append(etfPresent ? "Available" : "Not supported").append("\n");
        info.append("• Animation Support: ").append(freshAnimationsCompatible ? "Available" : "Limited");
        
        return info.toString();
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
