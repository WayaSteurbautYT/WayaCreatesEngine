package com.wayacreates.commands;

import com.wayacreates.WayaCreatesEngine;
import com.wayacreates.engine.TutorialSystem;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Tutorial Commands for WayaCreates Engine
 * Handles all tutorial-related functionality
 */
public class TutorialCommands {
    
    /**
     * Starts the interactive tutorial for the player
     */
    public static int startTutorial(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        if (Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"))) {
            System.out.println("[DEBUG] Tutorial command executed by player: " + player.getName().getString());
        }
        
        TutorialSystem tutorialSystem = WayaCreatesEngine.getTutorialSystem();
        if (tutorialSystem != null) {
            tutorialSystem.startTutorial(player.getUuid());
            source.sendFeedback(() -> Text.literal("§a📚 Tutorial started! Follow the on-screen instructions."), true);
        } else {
            source.sendFeedback(() -> Text.literal("§cTutorial system is not available"), false);
        }
        return 1;
    }
    
    /**
     * Skips the current tutorial for the player
     */
    public static int skipTutorial(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        TutorialSystem tutorialSystem = WayaCreatesEngine.getTutorialSystem();
        if (tutorialSystem != null) {
            tutorialSystem.skipTutorial(player.getUuid());
            source.sendFeedback(() -> Text.literal("§7⏭️ Tutorial skipped"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§cTutorial system is not available"), false);
        }
        return 1;
    }
    
    /**
     * Advances to the next tutorial step
     */
    public static int nextTutorialStep(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        TutorialSystem tutorialSystem = WayaCreatesEngine.getTutorialSystem();
        if (tutorialSystem != null) {
            tutorialSystem.nextStep(player.getUuid());
            source.sendFeedback(() -> Text.literal("§7➡️ Next tutorial step"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§cTutorial system is not available"), false);
        }
        return 1;
    }
    
    /**
     * Goes back to the previous tutorial step
     */
    public static int previousTutorialStep(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendFeedback(() -> Text.literal("§cThis command can only be used by players"), false);
            return 0;
        }
        
        TutorialSystem tutorialSystem = WayaCreatesEngine.getTutorialSystem();
        if (tutorialSystem != null) {
            tutorialSystem.previousStep(player.getUuid());
            source.sendFeedback(() -> Text.literal("§7⬅️ Previous tutorial step"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§cTutorial system is not available"), false);
        }
        return 1;
    }
}
