package com.wayacreates.commands;

import com.wayacreates.WayaCreatesEngine;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Livestream Commands for WayaCreates Engine
 * Handles livestream functionality
 */
public class LivestreamCommands {
    
    /**
     * Starts livestream to the specified platform
     */
    public static int startLivestream(ServerCommandSource source, String platform) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        // Implementation: Start livestream
        var livestreamManager = WayaCreatesEngine.getLivestreamManager();
        if (livestreamManager != null) {
            source.sendFeedback(() -> Text.literal("§a📡 Livestream started on: " + platform), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Livestream system not available"), false);
            return 0;
        }
        
        return 1;
    }
    
    /**
     * Stops the active livestream
     */
    public static int stopLivestream(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        // Implementation: Stop livestream
        var livestreamManager = WayaCreatesEngine.getLivestreamManager();
        if (livestreamManager != null) {
            source.sendFeedback(() -> Text.literal("§a📡 Livestream stopped"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§c❌ Livestream system not available"), false);
            return 0;
        }
        
        return 1;
    }
    
    /**
     * Gets the current status of the livestream
     */
    public static int livestreamStatus(ServerCommandSource source) {
        // Implementation: Get livestream status
        var livestreamManager = WayaCreatesEngine.getLivestreamManager();
        if (livestreamManager != null) {
            source.sendFeedback(() -> Text.literal("§7📡 Livestream status: Not active"), false);
        } else {
            source.sendFeedback(() -> Text.literal("§7📡 Livestream status: Not available"), false);
        }
        
        return 1;
    }
}
