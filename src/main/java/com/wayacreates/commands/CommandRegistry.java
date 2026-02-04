package com.wayacreates.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.CommandRegistryAccess;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

/**
 * Command Registry for WayaCreates Engine
 * Registers all commands and provides help functionality
 */
public class CommandRegistry {
    
    /**
     * Registers all WayaCreates Engine commands
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        // Main command
        dispatcher.register(literal("wayacreates")
            .executes(context -> {
                context.getSource().sendFeedback(() -> Text.literal("§6🎬 WayaCreates Engine v2.0.0"), false);
                context.getSource().sendFeedback(() -> Text.literal("§7Professional Minecraft Animation & Video Editing System"), false);
                context.getSource().sendFeedback(() -> Text.literal("§7Use /wayacreates help for commands"), false);
                return 1;
            })
            
            // Tutorial commands
            .then(literal("tutorial")
                .then(literal("start")
                    .executes(context -> TutorialCommands.startTutorial(context.getSource()))
                )
                .then(literal("skip")
                    .executes(context -> TutorialCommands.skipTutorial(context.getSource()))
                )
                .then(literal("next")
                    .executes(context -> TutorialCommands.nextTutorialStep(context.getSource()))
                )
                .then(literal("previous")
                    .executes(context -> TutorialCommands.previousTutorialStep(context.getSource()))
                )
            )
            
            // Video editor commands
            .then(literal("video")
                .then(literal("editor")
                    .executes(context -> VideoCommands.openVideoEditor(context.getSource()))
                )
                .then(literal("record")
                    .executes(context -> VideoCommands.startVideoRecording(context.getSource()))
                )
                .then(literal("stop")
                    .executes(context -> VideoCommands.stopVideoRecording(context.getSource()))
                )
                .then(literal("project")
                    .then(argument("name", StringArgumentType.string())
                        .executes(context -> VideoCommands.createVideoProject(context.getSource(), 
                            StringArgumentType.getString(context, "name")))
                    )
                )
            )
            
            // 3D Viewport commands
            .then(literal("viewport")
                .then(literal("open")
                    .executes(context -> ViewportCommands.open3DViewport(context.getSource()))
                )
                .then(literal("mode")
                    .then(argument("mode", StringArgumentType.string())
                        .executes(context -> ViewportCommands.setViewportMode(context.getSource(), 
                            StringArgumentType.getString(context, "mode")))
                    )
                )
                .then(literal("shader")
                    .then(argument("shader", StringArgumentType.string())
                        .executes(context -> ViewportCommands.setViewportShader(context.getSource(), 
                            StringArgumentType.getString(context, "shader")))
                    )
                )
            )
            
            // Node compositor commands
            .then(literal("compositor")
                .then(literal("open")
                    .executes(context -> CompositorCommands.openNodeCompositor(context.getSource()))
                )
                .then(literal("node")
                    .then(argument("type", StringArgumentType.string())
                        .executes(context -> CompositorCommands.addNode(context.getSource(), 
                            StringArgumentType.getString(context, "type")))
                    )
                )
                .then(literal("clear")
                    .executes(context -> CompositorCommands.clearCompositor(context.getSource()))
                )
            )
            
            // Audio editor commands
            .then(literal("audio")
                .then(literal("editor")
                    .executes(context -> AudioCommands.openAudioEditor(context.getSource()))
                )
                .then(literal("record")
                    .executes(context -> AudioCommands.startAudioRecording(context.getSource()))
                )
                .then(literal("import")
                    .then(argument("file", StringArgumentType.string())
                        .executes(context -> AudioCommands.importAudio(context.getSource(), 
                            StringArgumentType.getString(context, "file")))
                    )
                )
            )
            
            // Overlay commands
            .then(literal("overlay")
                .then(literal("show")
                    .executes(context -> OverlayCommands.showOverlay(context.getSource()))
                )
                .then(literal("hide")
                    .executes(context -> OverlayCommands.hideOverlay(context.getSource()))
                )
                .then(literal("toggle")
                    .executes(context -> OverlayCommands.toggleOverlay(context.getSource()))
                )
            )
            
            // Livestream commands
            .then(literal("livestream")
                .then(literal("start")
                    .then(argument("platform", StringArgumentType.string())
                        .executes(context -> LivestreamCommands.startLivestream(context.getSource(), 
                            StringArgumentType.getString(context, "platform")))
                    )
                )
                .then(literal("stop")
                    .executes(context -> LivestreamCommands.stopLivestream(context.getSource()))
                )
                .then(literal("status")
                    .executes(context -> LivestreamCommands.livestreamStatus(context.getSource()))
                )
            )
            
            // Plugin commands
            .then(literal("plugin")
                .then(literal("list")
                    .executes(context -> SystemCommands.listPlugins(context.getSource()))
                )
                .then(literal("reload")
                    .executes(context -> SystemCommands.reloadPlugins(context.getSource()))
                )
                .then(literal("enable")
                    .then(argument("plugin", StringArgumentType.string())
                        .executes(context -> SystemCommands.enablePlugin(context.getSource(), 
                            StringArgumentType.getString(context, "plugin")))
                    )
                )
                .then(literal("disable")
                    .then(argument("plugin", StringArgumentType.string())
                        .executes(context -> SystemCommands.disablePlugin(context.getSource(), 
                            StringArgumentType.getString(context, "plugin")))
                    )
                )
            )
            
            // Settings commands
            .then(literal("settings")
                .then(literal("show")
                    .executes(context -> SystemCommands.showSettings(context.getSource()))
                )
                .then(literal("set")
                    .then(argument("key", StringArgumentType.string())
                        .then(argument("value", StringArgumentType.string())
                            .executes(context -> SystemCommands.setSetting(context.getSource(), 
                                StringArgumentType.getString(context, "key"),
                                StringArgumentType.getString(context, "value")))
                        )
                    )
                )
                .then(literal("reset")
                    .executes(context -> SystemCommands.resetSettings(context.getSource()))
                )
            )
            
            // Help command
            .then(literal("help")
                .executes(context -> showHelp(context.getSource()))
            )
        );
    }
    
    /**
     * Shows comprehensive help for all commands
     */
    private static int showHelp(ServerCommandSource source) {
        source.sendFeedback(() -> Text.translatable("wayacreates.command.help.header"), false);
        
        source.sendFeedback(() -> Text.translatable("wayacreates.command.help.tutorial"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates tutorial start §f- Start interactive tutorial"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates tutorial skip §f- Skip tutorial"), false);
        
        source.sendFeedback(() -> Text.translatable("wayacreates.command.help.video"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates video editor §f- Open video editor"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates video record §f- Start recording"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates video stop §f- Stop recording"), false);
        
        source.sendFeedback(() -> Text.translatable("wayacreates.command.help.viewport"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates viewport open §f- Open 3D viewport"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates viewport mode <mode> §f- Set view mode"), false);
        
        source.sendFeedback(() -> Text.translatable("wayacreates.command.help.compositor"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates compositor open §f- Open node compositor"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates compositor node <type> §f- Add node"), false);
        
        source.sendFeedback(() -> Text.translatable("wayacreates.command.help.audio"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates audio editor §f- Open audio editor"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates audio record §f- Start audio recording"), false);
        
        source.sendFeedback(() -> Text.translatable("wayacreates.command.help.overlay"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates overlay show §f- Show overlay"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates overlay hide §f- Hide overlay"), false);
        
        source.sendFeedback(() -> Text.translatable("wayacreates.command.help.livestream"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates livestream start <platform> §f- Start livestream"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates livestream stop §f- Stop livestream"), false);
        
        source.sendFeedback(() -> Text.translatable("wayacreates.command.help.plugins"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates plugin list §f- List plugins"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates plugin reload §f- Reload plugins"), false);
        
        source.sendFeedback(() -> Text.translatable("wayacreates.command.help.settings"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates settings show §f- Show settings"), false);
        source.sendFeedback(() -> Text.literal("§7/wayacreates settings set <key> <value> §f- Set setting"), false);
        
        return 1;
    }
}
