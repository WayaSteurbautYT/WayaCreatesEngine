package com.wayacreates.commands;

import com.wayacreates.WayaCreatesEngine;
import com.wayacreates.engine.VideoEngine;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Video Commands for WayaCreates Engine
 * Handles all video recording and editing functionality
 */
public class VideoCommands {
    
    /**
     * Opens the video editor GUI for the player
     */
    public static int openVideoEditor(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        // Implementation: Open video editor GUI
        var videoEditor = WayaCreatesEngine.getVideoEditorUI();
        if (videoEditor != null) {
            source.sendFeedback(() -> Text.translatable("wayacreates.video.editor_opened"), true);
        } else {
            source.sendFeedback(() -> Text.translatable("wayacreates.video.not_available"), false);
            return 0;
        }
        
        return 1;
    }
    
    /**
     * Starts video recording with current settings
     */
    public static int startVideoRecording(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        if (Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"))) {
            System.out.println("[DEBUG] Video recording started by player: " + player.getName().getString());
        }
        
        VideoEngine videoEngine = WayaCreatesEngine.getVideoEngine();
        VideoEngine.VideoSettings settings = new VideoEngine.VideoSettings();
        
        VideoEngine.VideoSession session = videoEngine.startRecording(player.getUuid(), settings);
        
        source.sendFeedback(() -> Text.translatable("wayacreates.video.recording_started", session.getSessionId()), true);
        return 1;
    }
    
    /**
     * Stops the current video recording session
     */
    public static int stopVideoRecording(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        VideoEngine videoEngine = WayaCreatesEngine.getVideoEngine();
        boolean stopped = videoEngine.stopRecording(player.getUuid());
        
        if (stopped) {
            source.sendFeedback(() -> Text.translatable("wayacreates.video.recording_stopped"), true);
        } else {
            source.sendFeedback(() -> Text.translatable("wayacreates.video.no_active_recording"), false);
        }
        
        return stopped ? 1 : 0;
    }
    
    /**
     * Creates a new video project with the specified name
     */
    public static int createVideoProject(ServerCommandSource source, String name) {
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        VideoEngine videoEngine = WayaCreatesEngine.getVideoEngine();
        VideoEngine.VideoProject project = videoEngine.createProject(name, player.getUuid());
        
        source.sendFeedback(() -> Text.translatable("wayacreates.video.project_created", project.getName()), true);
        return 1;
    }
}
