package com.wayacreates.commands;

import com.wayacreates.WayaCreatesEngine;
import com.wayacreates.shader.ShaderManager;
import com.wayacreates.entity.EntityModelManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

/**
 * Debug commands for testing mod functionality
 * Provides server-side commands for debugging and testing
 */
public class DebugCommands {
    
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        // Main debug command
        dispatcher.register(literal("wce")
            .then(literal("debug")
                .executes(DebugCommands::showDebugInfo))
            .then(literal("reload")
                .executes(DebugCommands::reloadAll))
            .then(literal("status")
                .executes(DebugCommands::showStatus))
            .then(literal("shaders")
                .executes(DebugCommands::testShaders))
            .then(literal("entities")
                .executes(DebugCommands::testEntities)));
    }
    
    private static int showDebugInfo(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("=== WayaCreates Engine Debug Info ==="), false);
        context.getSource().sendFeedback(() -> Text.literal("Version: 2.1.0"), false);
        context.getSource().sendFeedback(() -> Text.literal("Environment: Server"), false);
        
        // Engine status
        context.getSource().sendFeedback(() -> Text.literal("Video Engine: " + 
            (WayaCreatesEngine.getVideoEngine() != null ? "✅" : "❌")), false);
        context.getSource().sendFeedback(() -> Text.literal("Render Engine: " + 
            (WayaCreatesEngine.getRenderEngine() != null ? "✅" : "❌")), false);
        context.getSource().sendFeedback(() -> Text.literal("Animation Engine: " + 
            (WayaCreatesEngine.getAnimationEngine() != null ? "✅" : "❌")), false);
        context.getSource().sendFeedback(() -> Text.literal("Audio Engine: " + 
            (WayaCreatesEngine.getAudioEngine() != null ? "✅" : "❌")), false);
        
        return 1;
    }
    
    private static int reloadAll(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("Reloading WayaCreates Engine components..."), false);
        
        try {
            // Reload engines if they exist
            if (WayaCreatesEngine.getVideoEngine() != null) {
                WayaCreatesEngine.getVideoEngine().initialize();
                context.getSource().sendFeedback(() -> Text.literal("✅ Video Engine reloaded"), false);
            }
            
            if (WayaCreatesEngine.getRenderEngine() != null) {
                WayaCreatesEngine.getRenderEngine().initialize();
                context.getSource().sendFeedback(() -> Text.literal("✅ Render Engine reloaded"), false);
            }
            
            if (WayaCreatesEngine.getAnimationEngine() != null) {
                WayaCreatesEngine.getAnimationEngine().initialize();
                context.getSource().sendFeedback(() -> Text.literal("✅ Animation Engine reloaded"), false);
            }
            
            if (WayaCreatesEngine.getAudioEngine() != null) {
                WayaCreatesEngine.getAudioEngine().initialize();
                context.getSource().sendFeedback(() -> Text.literal("✅ Audio Engine reloaded"), false);
            }
            
            context.getSource().sendFeedback(() -> Text.literal("🔄 All components reloaded successfully"), false);
        } catch (Exception e) {
            context.getSource().sendError(Text.literal("❌ Error during reload: " + e.getMessage()));
        }
        
        return 1;
    }
    
    private static int showStatus(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("=== WayaCreates Engine Status ==="), false);
        
        // Check mod dependencies
        final boolean sodiumPresent;
        final boolean irisPresent;
        final boolean etfPresent;
        final boolean emfPresent;
        
        boolean sodiumFound = false;
        boolean irisFound = false;
        boolean etfFound = false;
        boolean emfFound = false;
        
        try {
            Class.forName("net.caffeinemc.sodium.SodiumMod");
            sodiumFound = true;
        } catch (ClassNotFoundException e) {
            // Sodium not present
        }
        sodiumPresent = sodiumFound;
        
        try {
            Class.forName("net.irisshaders.iris.Iris");
            irisFound = true;
        } catch (ClassNotFoundException e) {
            // Iris not present
        }
        irisPresent = irisFound;
        
        try {
            Class.forName("traben.entity_texture_features.EntityTextureFeatures");
            etfFound = true;
        } catch (ClassNotFoundException e) {
            // ETF not present
        }
        etfPresent = etfFound;
        
        try {
            Class.forName("traben.entity_model_features.EntityModelFeatures");
            emfFound = true;
        } catch (ClassNotFoundException e) {
            // EMF not present
        }
        emfPresent = emfFound;
        
        context.getSource().sendFeedback(() -> Text.literal("Sodium: " + (sodiumPresent ? "✅" : "❌")), false);
        context.getSource().sendFeedback(() -> Text.literal("Iris: " + (irisPresent ? "✅" : "❌")), false);
        context.getSource().sendFeedback(() -> Text.literal("Entity Texture Features: " + (etfPresent ? "✅" : "❌")), false);
        context.getSource().sendFeedback(() -> Text.literal("Entity Model Features: " + (emfPresent ? "✅" : "❌")), false);
        
        return 1;
    }
    
    private static int testShaders(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("Testing shader functionality..."), false);
        
        try {
            boolean shaderSupport = ShaderManager.isShaderSupportEnabled();
            boolean irisPresent = ShaderManager.isIrisPresent();
            boolean sodiumPresent = ShaderManager.isSodiumPresent();
            
            context.getSource().sendFeedback(() -> Text.literal("Shader Support: " + (shaderSupport ? "✅" : "❌")), false);
            context.getSource().sendFeedback(() -> Text.literal("Iris Integration: " + (irisPresent ? "✅" : "❌")), false);
            context.getSource().sendFeedback(() -> Text.literal("Sodium Compatibility: " + (sodiumPresent ? "✅" : "❌")), false);
            
            if (shaderSupport) {
                context.getSource().sendFeedback(() -> Text.literal("🎨 Shader system is operational"), false);
            } else {
                context.getSource().sendError(Text.literal("❌ Shader system is not available"));
            }
        } catch (Exception e) {
            context.getSource().sendError(Text.literal("❌ Error testing shaders: " + e.getMessage()));
        }
        
        return 1;
    }
    
    private static int testEntities(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("Testing entity model features..."), false);
        
        try {
            boolean entityFeatures = EntityModelManager.isEntityFeaturesEnabled();
            boolean freshAnimations = EntityModelManager.isFreshAnimationsEnabled();
            
            context.getSource().sendFeedback(() -> Text.literal("Entity Features: " + (entityFeatures ? "✅" : "❌")), false);
            context.getSource().sendFeedback(() -> Text.literal("Fresh Animations: " + (freshAnimations ? "✅" : "❌")), false);
            
            if (entityFeatures) {
                context.getSource().sendFeedback(() -> Text.literal("🎭 Entity model system is operational"), false);
            } else {
                context.getSource().sendError(Text.literal("❌ Entity model system is not available"));
            }
        } catch (Exception e) {
            context.getSource().sendError(Text.literal("❌ Error testing entity features: " + e.getMessage()));
        }
        
        return 1;
    }
}
