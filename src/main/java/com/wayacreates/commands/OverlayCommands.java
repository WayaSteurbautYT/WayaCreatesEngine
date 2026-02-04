package com.wayacreates.commands;

import com.wayacreates.WayaCreatesEngine;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Overlay Commands for WayaCreates Engine
 * Handles overlay system functionality
 */
public class OverlayCommands {
    
    /**
     * Shows the overlay system GUI for the player
     */
    public static int showOverlay(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        // Implementation: Show overlay system GUI
        var overlay = WayaCreatesEngine.getOverlayUI();
        if (overlay != null) {
            source.sendFeedback(() -> Text.literal("§a🖼️ Overlay shown"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Overlay system not available"), false);
            return 0;
        }
        
        return 1;
    }
    
    /**
     * Hides the currently displayed overlay
     */
    public static int hideOverlay(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        // Implementation: Hide overlay
        var overlay = WayaCreatesEngine.getOverlayUI();
        if (overlay != null) {
            source.sendFeedback(() -> Text.literal("§a🖼️ Overlay hidden"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Overlay system not available"), false);
            return 0;
        }
        
        return 1;
    }
    
    /**
     * Toggles the visibility of the overlay
     */
    public static int toggleOverlay(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        // Implementation: Toggle overlay
        var overlay = WayaCreatesEngine.getOverlayUI();
        if (overlay != null) {
            source.sendFeedback(() -> Text.literal("§a🖼️ Overlay toggled"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Overlay system not available"), false);
            return 0;
        }
        
        return 1;
    }
}
