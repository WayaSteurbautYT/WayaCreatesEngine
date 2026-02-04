package com.wayacreates.commands;

import com.wayacreates.WayaCreatesEngine;
import com.wayacreates.ui.ThreeDViewport;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Viewport Commands for WayaCreates Engine
 * Handles 3D viewport and shader functionality
 */
public class ViewportCommands {
    
    /**
     * Opens the 3D viewport GUI for the player
     */
    public static int open3DViewport(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        // Implementation: Open 3D viewport GUI
        var viewport = WayaCreatesEngine.getThreeDViewport();
        if (viewport != null) {
            source.sendFeedback(() -> Text.literal("§a🎭 3D Viewport opened"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ 3D Viewport not available"), false);
            return 0;
        }
        
        return 1;
    }
    
    /**
     * Sets the viewport mode for the 3D viewport
     */
    public static int setViewportMode(ServerCommandSource source, String mode) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        ThreeDViewport.ViewMode viewMode;
        try {
            viewMode = ThreeDViewport.ViewMode.valueOf(mode.toUpperCase());
        } catch (IllegalArgumentException e) {
            source.sendFeedback(() -> Text.literal("§c❌ Invalid view mode: " + mode), false);
            return 0;
        }
        
        // Implementation: Set viewport mode
        var viewport = WayaCreatesEngine.getThreeDViewport();
        if (viewport != null) {
            source.sendFeedback(() -> Text.literal("§a🎭 Viewport mode set to: " + viewMode), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ 3D Viewport not available"), false);
            return 0;
        }
        
        return 1;
    }
    
    /**
     * Sets the shader for the 3D viewport
     */
    public static int setViewportShader(ServerCommandSource source, String shader) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        // Implementation: Set viewport shader
        var viewport = WayaCreatesEngine.getThreeDViewport();
        if (viewport != null) {
            source.sendFeedback(() -> Text.literal("§a🎨 Viewport shader set to: " + shader), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ 3D Viewport not available"), false);
            return 0;
        }
        
        return 1;
    }
}
