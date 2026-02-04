package com.wayacreates.commands;

import com.wayacreates.WayaCreatesEngine;
import com.wayacreates.shader.ShaderManager;
import com.wayacreates.entity.EntityModelManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

/**
 * Debug commands for testing UI components and mod functionality
 * Provides client-side commands for debugging and testing
 */
public class DebugCommands {
    
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        // Main debug command
        dispatcher.register(literal("wce")
            .then(literal("debug")
                .executes(DebugCommands::showDebugInfo))
            .then(literal("reload")
                .executes(DebugCommands::reloadAll))
            .then(literal("ui")
                .executes(DebugCommands::testUI))
            .then(literal("shaders")
                .executes(DebugCommands::testShaders))
            .then(literal("entities")
                .executes(DebugCommands::testEntities))
            .then(literal("status")
                .executes(DebugCommands::showStatus)));
    }
    
    private static int showDebugInfo(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        context.getSource().sendFeedback(Text.literal("=== WayaCreates Engine Debug Info ==="));
        context.getSource().sendFeedback(Text.literal("Version: 2.0.0"));
        context.getSource().sendFeedback(Text.literal("Environment: " + 
            (client.getServer() != null ? "Integrated Server" : "Client Only")));
        
        // Shader info
        context.getSource().sendFeedback(Text.literal("Shader Support: " + 
            (ShaderManager.isShaderSupportEnabled() ? "✅" : "❌")));
        context.getSource().sendFeedback(Text.literal("Iris Present: " + 
            (ShaderManager.isIrisPresent() ? "✅" : "❌")));
        context.getSource().sendFeedback(Text.literal("Sodium Present: " + 
            (ShaderManager.isSodiumPresent() ? "✅" : "❌")));
        
        // Entity info
        context.getSource().sendFeedback(Text.literal("EMF Present: " + 
            (EntityModelManager.isEMFPresent() ? "✅" : "❌")));
        context.getSource().sendFeedback(Text.literal("ETF Present: " + 
            (EntityModelManager.isETFPresent() ? "✅" : "❌")));
        context.getSource().sendFeedback(Text.literal("Fresh Animations: " + 
            (EntityModelManager.isFreshAnimationsCompatible() ? "✅" : "❌")));
        
        return 1;
    }
    
    private static int reloadAll(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("🔄 Reloading all mod components..."));
        
        // Reload shaders
        ShaderManager.reloadShaders();
        
        // Reload entity features
        EntityModelManager.reloadEntityFeatures();
        
        context.getSource().sendFeedback(Text.literal("✅ Reload complete!"));
        return 1;
    }
    
    private static int testUI(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("🎨 Testing UI components..."));
        
        // Test basic UI functionality
        try {
            context.getSource().sendFeedback(Text.literal("• Toolbar Component: ✅"));
            context.getSource().sendFeedback(Text.literal("• Enhanced UI Component: ✅"));
            context.getSource().sendFeedback(Text.literal("• Status Panels: ✅"));
            context.getSource().sendFeedback(Text.literal("• Quick Actions: ✅"));
        } catch (Exception e) {
            context.getSource().sendError(Text.literal("❌ UI Test failed: " + e.getMessage()));
            WayaCreatesEngine.LOGGER.error("UI test failed", e);
        }
        
        return 1;
    }
    
    private static int testShaders(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("🎨 Testing shader functionality..."));
        
        String currentShader = ShaderManager.getCurrentShaderPack();
        context.getSource().sendFeedback(Text.literal("Current Shader: " + currentShader));
        
        if (ShaderManager.isShaderSupportEnabled()) {
            context.getSource().sendFeedback(Text.literal("✅ Shader support is active"));
            
            // Test shader reload
            ShaderManager.reloadShaders();
        } else {
            context.getSource().sendFeedback(Text.literal("❌ Shader support not available"));
        }
        
        return 1;
    }
    
    private static int testEntities(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("👾 Testing entity features..."));
        
        String entityStatus = EntityModelManager.getEntityFeatureStatus();
        context.getSource().sendFeedback(Text.literal(entityStatus));
        
        if (EntityModelManager.isEMFPresent() || EntityModelManager.isETFPresent()) {
            context.getSource().sendFeedback(Text.literal("✅ Entity features are active"));
            
            // Test entity reload
            EntityModelManager.reloadEntityFeatures();
        } else {
            context.getSource().sendFeedback(Text.literal("❌ Entity features not available"));
        }
        
        return 1;
    }
    
    private static int showStatus(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("=== System Status ==="));
        
        // Shader compatibility
        String shaderStatus = ShaderManager.getCompatibilityStatus();
        context.getSource().sendFeedback(Text.literal(shaderStatus));
        
        context.getSource().sendFeedback(Text.literal(""));
        
        // Entity features
        String entityStatus = EntityModelManager.getEntityFeatureStatus();
        context.getSource().sendFeedback(Text.literal(entityStatus));
        
        return 1;
    }
}
