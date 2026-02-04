package com.wayacreates;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wayacreates.engine.VideoEngine;
import com.wayacreates.engine.RenderEngine;
import com.wayacreates.engine.AnimationEngine;
import com.wayacreates.engine.AudioEngine;
import com.wayacreates.engine.TutorialSystem;
import com.wayacreates.engine.PluginManager;
// import com.wayacreates.ui.VideoEditorUI; // Temporarily disabled
import com.wayacreates.ui.ThreeDViewport;
import com.wayacreates.ui.NodeCompositorUI;
import com.wayacreates.ui.AudioEditorUI;
// import com.wayacreates.ui.OverlayUI; // Temporarily disabled
import com.wayacreates.commands.WayaCreatesCommands;
import com.wayacreates.config.WayaCreatesConfig;
import com.wayacreates.recording.RecordingManager;
import com.wayacreates.livestream.LivestreamManager;
import com.wayacreates.integrations.ModIntegrationManager;

/**
 * WayaCreates Engine - Professional Minecraft Animation & Video Editing System
 * 
 * Features:
 * - 3D Viewport with Blender-style rig integration
 * - Professional video editor with After Effects-style compositing
 * - Node-based compositor with color grading
 * - Audio editing and mixing capabilities
 * - Livestream recording and broadcasting
 * - Fresh Animations integration
 * - Plugin system for extensibility
 * - Beginner-friendly tutorial system
 * 
 * License: MIT - WayaCreatesYT & Minecraft Community
 * Open Source with attribution requirement
 */
public class WayaCreatesEngine implements ModInitializer {
    public static final String MOD_ID = "wayacreates-engine";
    public static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/Engine");
    
    // Debug flag for verbose logging
    private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
    
    // Core Engine Components
    private static VideoEngine videoEngine;
    private static RenderEngine renderEngine;
    private static AnimationEngine animationEngine;
    private static AudioEngine audioEngine;
    private static TutorialSystem tutorialSystem;
    private static PluginManager pluginManager;
    
    // UI Components
    // private static VideoEditorUI videoEditorUI; // Temporarily disabled
    private static ThreeDViewport threeDViewport;
    private static NodeCompositorUI nodeCompositorUI;
    private static AudioEditorUI audioEditorUI;
    // private static OverlayUI overlayUI; // Temporarily disabled
    
    // Managers
    private static RecordingManager recordingManager;
    private static LivestreamManager livestreamManager;
    private static WayaCreatesConfig config;
    private static ModIntegrationManager modIntegrationManager;
    
    @Override
    public void onInitialize() {
        if (DEBUG_MODE) {
            LOGGER.info("🎬 Initializing WayaCreates Engine v2.0.0 (DEBUG MODE)");
            LOGGER.debug("🔧 Debug information:");
            LOGGER.debug("- Java Version: {}", System.getProperty("java.version"));
            LOGGER.debug("- OS: {}", System.getProperty("os.name"));
            LOGGER.debug("- Available Processors: {}", Runtime.getRuntime().availableProcessors());
            LOGGER.debug("- Max Memory: {} MB", Runtime.getRuntime().maxMemory() / 1024 / 1024);
        } else {
            LOGGER.info("🎬 Initializing WayaCreates Engine v2.0.0");
        }
        LOGGER.info("🎮 Professional Minecraft Animation & Video Editing System");
        
        // Initialize configuration
        config = new WayaCreatesConfig();
        config.loadConfig();
        
        // Initialize core engines
        initializeEngines();
        
        // Initialize UI components
        initializeUI();
        
        // Initialize managers
        initializeManagers();
        
        // Register event handlers
        registerEventHandlers();
        
        // Register commands
        registerCommands();
        
        // Initialize tutorial system
        initializeTutorialSystem();
        
        // Load plugins
        loadPlugins();
        
        LOGGER.info("✅ WayaCreates Engine initialized successfully!");
        LOGGER.info("🎯 Ready for professional animation and video editing!");
    }
    
    private void initializeEngines() {
        if (DEBUG_MODE) {
            LOGGER.debug("🔧 Initializing core engines...");
            LOGGER.debug("- Video Engine: Starting up");
            LOGGER.debug("- Render Engine: Initializing OpenGL context");
            LOGGER.debug("- Animation Engine: Loading rigging system");
            LOGGER.debug("- Audio Engine: Setting up audio buffers");
        } else {
            LOGGER.info("🔧 Initializing core engines...");
        }
        
        videoEngine = new VideoEngine();
        renderEngine = new RenderEngine();
        renderEngine.initialize();
        animationEngine = new AnimationEngine();
        audioEngine = new AudioEngine();
        audioEngine.initialize();
        
        LOGGER.info("✅ Core engines initialized");
    }
    
    private void initializeUI() {
        if (DEBUG_MODE) {
            LOGGER.debug("🖥️ Initializing UI components...");
            LOGGER.debug("- Video Editor: Setting up timeline");
            LOGGER.debug("- 3D Viewport: Initializing renderer");
            LOGGER.debug("- Node Compositor: Loading node system");
            LOGGER.debug("- Audio Editor: Setting up waveforms");
            LOGGER.debug("- Overlay System: Initializing HUD");
        } else {
            LOGGER.info("🖥️ Initializing UI components...");
        }
        
        // videoEditorUI = new VideoEditorUI(); // Temporarily disabled
        threeDViewport = new ThreeDViewport();
        nodeCompositorUI = new NodeCompositorUI();
        audioEditorUI = new AudioEditorUI();
        // overlayUI = new OverlayUI(); // Temporarily disabled
        
        LOGGER.info("✅ UI components initialized");
    }
    
    private void initializeManagers() {
        LOGGER.info("📋 Initializing managers...");
        
        recordingManager = new RecordingManager();
        livestreamManager = new LivestreamManager();
        modIntegrationManager = new ModIntegrationManager();
        
        LOGGER.info("✅ Managers initialized");
    }
    
    private void registerEventHandlers() {
        LOGGER.info("📡 Registering event handlers...");
        
        ServerTickEvents.END_SERVER_TICK.register(this::onServerTick);
        ServerWorldEvents.LOAD.register(this::onWorldLoad);
        ServerWorldEvents.UNLOAD.register(this::onWorldUnload);
        
        LOGGER.info("✅ Event handlers registered");
    }
    
    private void registerCommands() {
        LOGGER.info("⌨️ Registering commands...");
        
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            WayaCreatesCommands.register(dispatcher, registryAccess);
        });
        
        LOGGER.info("✅ Commands registered");
    }
    
    private void initializeTutorialSystem() {
        LOGGER.info("📚 Initializing tutorial system...");
        
        tutorialSystem = new TutorialSystem();
        tutorialSystem.initialize();
        
        // Show welcome message for new users
        if (config.isFirstTimeUser()) {
            tutorialSystem.showWelcomeMessage();
            config.setFirstTimeUser(false);
            config.saveConfig();
        }
        
        LOGGER.info("✅ Tutorial system initialized");
    }
    
    private void loadPlugins() {
        LOGGER.info("🔌 Loading plugins...");
        
        pluginManager = new PluginManager();
        pluginManager.loadPlugins();
        
        LOGGER.info("✅ Plugins loaded");
    }
    
    private void onServerTick(MinecraftServer server) {
        // Update all engines
        if (videoEngine != null) videoEngine.tick();
        if (renderEngine != null) renderEngine.tick();
        if (animationEngine != null) animationEngine.tick();
        if (audioEngine != null) audioEngine.tick();
        
        // Update managers
        if (recordingManager != null) recordingManager.tick();
        if (livestreamManager != null) livestreamManager.tick();
        if (modIntegrationManager != null) modIntegrationManager.tick();
        
        // Update UI
        // if (videoEditorUI != null) videoEditorUI.tick(); // Temporarily disabled
        if (threeDViewport != null) threeDViewport.tick();
        if (nodeCompositorUI != null) nodeCompositorUI.tick();
        if (audioEditorUI != null) audioEditorUI.tick();
        // if (overlayUI != null) overlayUI.tick(); // Temporarily disabled
        
        // Update tutorial system
        if (tutorialSystem != null) tutorialSystem.tick();
    }
    
    private void onWorldLoad(MinecraftServer server, ServerWorld world) {
        LOGGER.info("🌍 World loaded: {}", world.getRegistryKey().getValue());
        
        // Initialize engines for new world
        if (renderEngine != null) renderEngine.onWorldLoad(world);
        if (animationEngine != null) animationEngine.onWorldLoad(world);
        if (recordingManager != null) recordingManager.onWorldLoad(world);
        if (livestreamManager != null) livestreamManager.onWorldLoad(world);
    }
    
    private void onWorldUnload(MinecraftServer server, ServerWorld world) {
        LOGGER.info("🌍 World unloaded: {}", world.getRegistryKey().getValue());
        
        // Cleanup engines for unloaded world
        if (renderEngine != null) renderEngine.onWorldUnload(world);
        if (animationEngine != null) animationEngine.onWorldUnload(world);
        if (recordingManager != null) recordingManager.onWorldUnload(world);
        if (livestreamManager != null) livestreamManager.onWorldUnload(world);
    }
    
    // Static getters for accessing engine components
    public static VideoEngine getVideoEngine() { return videoEngine; }
    public static RenderEngine getRenderEngine() { return renderEngine; }
    public static AnimationEngine getAnimationEngine() { return animationEngine; }
    public static AudioEngine getAudioEngine() { return audioEngine; }
    public static TutorialSystem getTutorialSystem() { return tutorialSystem; }
    public static PluginManager getPluginManager() { return pluginManager; }
    
    // public static VideoEditorUI getVideoEditorUI() { return videoEditorUI; } // Temporarily disabled
    public static ThreeDViewport getThreeDViewport() { return threeDViewport; }
    public static NodeCompositorUI getNodeCompositorUI() { return nodeCompositorUI; }
    public static AudioEditorUI getAudioEditorUI() { return audioEditorUI; }
    // public static OverlayUI getOverlayUI() { return overlayUI; } // Temporarily disabled
    
    public static RecordingManager getRecordingManager() { return recordingManager; }
    public static LivestreamManager getLivestreamManager() { return livestreamManager; }
    public static WayaCreatesConfig getConfig() { return config; }
    public static ModIntegrationManager getModIntegrationManager() { return modIntegrationManager; }
}
