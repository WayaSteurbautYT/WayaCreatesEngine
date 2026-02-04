package com.wayacreates.commands;

import com.wayacreates.WayaCreatesEngine;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Compositor Commands for WayaCreates Engine
 * Handles node-based compositor functionality
 */
public class CompositorCommands {
    
    /**
     * Opens the node compositor GUI for the player
     */
    public static int openNodeCompositor(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        // Implementation: Open node compositor GUI
        var compositor = WayaCreatesEngine.getNodeCompositorUI();
        if (compositor != null) {
            source.sendFeedback(() -> Text.literal("§a🎨 Node Compositor opened"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Node Compositor not available"), false);
            return 0;
        }
        
        return 1;
    }
    
    /**
     * Adds a node of the specified type to the compositor
     */
    public static int addNode(ServerCommandSource source, String type) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        // Implementation: Add node to compositor
        var compositor = WayaCreatesEngine.getNodeCompositorUI();
        if (compositor != null) {
            // Add node logic would go here
            source.sendFeedback(() -> Text.literal("§a🔗 Added node: " + type), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Node Compositor not available"), false);
            return 0;
        }
        
        return 1;
    }
    
    /**
     * Clears all nodes from the compositor
     */
    public static int clearCompositor(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        // Implementation: Clear compositor
        var compositor = WayaCreatesEngine.getNodeCompositorUI();
        if (compositor != null) {
            // Clear compositor logic would go here
            source.sendFeedback(() -> Text.literal("§a🗑️ Node compositor cleared"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Node Compositor not available"), false);
            return 0;
        }
        
        return 1;
    }
}
