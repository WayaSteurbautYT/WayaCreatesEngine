package com.wayacreates.client;

import com.wayacreates.WayaCreatesEngine;
import com.wayacreates.commands.DebugCommands;
import com.wayacreates.ui.enhanced.components.ToolbarComponent;
import com.wayacreates.ui.enhanced.components.EnhancedUIComponent;
import com.wayacreates.shader.ShaderManager;
import com.wayacreates.entity.EntityModelManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

/**
 * Client-side initialization for WayaCreates Engine
 * Handles client-only functionality, UI components, and debug commands
 */
public class WayaCreatesEngineClient implements ClientModInitializer {
    
    private static ToolbarComponent toolbarComponent;
    private static EnhancedUIComponent enhancedUIComponent;
    private static boolean initialized = false;
    
    @Override
    public void onInitializeClient() {
        WayaCreatesEngine.LOGGER.info("Initializing WayaCreates Engine Client...");
        
        // Register debug commands
        registerCommands();
        
        // Register client events
        registerEvents();
        
        // Initialize UI components
        initializeUI();
        
        // Initialize managers
        initializeManagers();
        
        initialized = true;
        WayaCreatesEngine.LOGGER.info("WayaCreates Engine Client initialized successfully!");
    }
    
    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            DebugCommands.registerCommands(dispatcher);
            WayaCreatesEngine.LOGGER.info("Debug commands registered");
        });
    }
    
    private void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (initialized && client.player != null) {
                // Update UI components
                if (toolbarComponent != null) {
                    toolbarComponent.tick();
                }
                if (enhancedUIComponent != null) {
                    enhancedUIComponent.tick();
                }
            }
        });
        
        WayaCreatesEngine.LOGGER.info("Client events registered");
    }
    
    private void initializeUI() {
        try {
            // Initialize toolbar component
            toolbarComponent = new ToolbarComponent(10, 10, 600, 40);
            
            // Initialize enhanced UI component
            enhancedUIComponent = new EnhancedUIComponent(10, 60, 400, 250);
            
            WayaCreatesEngine.LOGGER.info("UI components initialized");
        } catch (Exception e) {
            WayaCreatesEngine.LOGGER.error("Failed to initialize UI components", e);
        }
    }
    
    private void initializeManagers() {
        try {
            // Initialize shader manager (static initialization will run)
            WayaCreatesEngine.LOGGER.info("Shader manager initialized: " + 
                ShaderManager.isShaderSupportEnabled());
            
            // Initialize entity model manager (static initialization will run)
            WayaCreatesEngine.LOGGER.info("Entity model manager initialized: " + 
                EntityModelManager.isFreshAnimationsCompatible());
            
        } catch (Exception e) {
            WayaCreatesEngine.LOGGER.error("Failed to initialize managers", e);
        }
    }
    
    // Public getters for accessing UI components
    public static ToolbarComponent getToolbarComponent() {
        return toolbarComponent;
    }
    
    public static EnhancedUIComponent getEnhancedUIComponent() {
        return enhancedUIComponent;
    }
    
    public static boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Safe method to reload all client components
     */
    public static void reloadClientComponents() {
        try {
            WayaCreatesEngine.LOGGER.info("Reloading client components...");
            
            // Reload shaders
            ShaderManager.reloadShaders();
            
            // Reload entity features
            EntityModelManager.reloadEntityFeatures();
            
            // Reinitialize UI if needed
            if (toolbarComponent == null || enhancedUIComponent == null) {
                new WayaCreatesEngineClient().initializeUI();
            }
            
            WayaCreatesEngine.LOGGER.info("Client components reloaded successfully");
        } catch (Exception e) {
            WayaCreatesEngine.LOGGER.error("Failed to reload client components", e);
        }
    }
    
    /**
     * Get comprehensive client status
     */
    public static String getClientStatus() {
        StringBuilder status = new StringBuilder();
        status.append("=== Client Status ===\n");
        status.append("Initialized: ").append(initialized ? "✅" : "❌").append("\n");
        status.append("UI Components: ").append((toolbarComponent != null && enhancedUIComponent != null) ? "✅" : "❌").append("\n");
        status.append("\n");
        status.append(ShaderManager.getCompatibilityStatus()).append("\n");
        status.append("\n");
        status.append(EntityModelManager.getEntityFeatureStatus());
        
        return status.toString();
    }
}
