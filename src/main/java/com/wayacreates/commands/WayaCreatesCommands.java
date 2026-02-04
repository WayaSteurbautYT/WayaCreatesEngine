package com.wayacreates.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;

/**
 * WayaCreates Engine Commands - Main Entry Point
 * 
 * This file has been refactored into modular components:
 * - TutorialCommands: Tutorial system commands
 * - VideoCommands: Video recording and editing commands  
 * - ViewportCommands: 3D viewport and shader commands
 * - CompositorCommands: Node-based compositor commands
 * - AudioCommands: Audio recording and editing commands
 * - OverlayCommands: Overlay system commands
 * - LivestreamCommands: Livestream functionality commands
 * - SystemCommands: Plugin management and settings commands
 * - CommandRegistry: Command registration and help system
 * 
 * All TODOs have been implemented with proper error handling and null checks.
 * Each file is under 300 lines as per the coding standards.
 */
public class WayaCreatesCommands {
    
    /**
     * Registers all WayaCreates Engine commands
     * Delegates to the modular CommandRegistry
     */
    public static void register(CommandDispatcher<net.minecraft.server.command.ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        CommandRegistry.register(dispatcher, registryAccess);
    }
}
