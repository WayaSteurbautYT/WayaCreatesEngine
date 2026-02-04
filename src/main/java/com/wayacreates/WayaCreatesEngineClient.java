package com.wayacreates;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

// import com.wayacreates.ui.VideoEditorUI; // Temporarily disabled
import com.wayacreates.ui.ThreeDViewport;
import com.wayacreates.ui.NodeCompositorUI;
import com.wayacreates.ui.AudioEditorUI;
// import com.wayacreates.ui.OverlayUI; // Temporarily disabled

/**
 * WayaCreates Engine Client Entry Point
 * Handles client-side initialization and key bindings
 */
public class WayaCreatesEngineClient implements ClientModInitializer {
    public static final String MOD_ID = "wayacreates-engine";
    
    // Key bindings
    public static KeyBinding openEditorKey;
    public static KeyBinding toggleOverlayKey;
    public static KeyBinding startRecordingKey;
    public static KeyBinding toggleViewportKey;
    public static KeyBinding openCompositorKey;
    public static KeyBinding openAudioEditorKey;
    
    @Override
    public void onInitializeClient() {
        WayaCreatesEngine.LOGGER.info("🎬 WayaCreates Engine Client initialized");
        
        // Register key bindings
        registerKeyBindings();
        
        // Register client events
        registerClientEvents();
        
        WayaCreatesEngine.LOGGER.info("✅ Client-side features ready");
    }
    
    private void registerKeyBindings() {
        // Main editor key binding (F1)
        openEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.wayacreates.open_editor",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F1,
            "category.wayacreates"
        ));
        
        // Toggle overlay (F2)
        toggleOverlayKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.wayacreates.toggle_overlay",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F2,
            "category.wayacreates"
        ));
        
        // Start/stop recording (F3)
        startRecordingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.wayacreates.toggle_recording",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F3,
            "category.wayacreates"
        ));
        
        // Toggle 3D viewport (F4)
        toggleViewportKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.wayacreates.toggle_viewport",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F4,
            "category.wayacreates"
        ));
        
        // Open node compositor (F5)
        openCompositorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.wayacreates.open_compositor",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F5,
            "category.wayacreates"
        ));
        
        // Open audio editor (F6)
        openAudioEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.wayacreates.open_audio_editor",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F6,
            "category.wayacreates"
        ));
        
        WayaCreatesEngine.LOGGER.info("⌨️ Key bindings registered");
    }
    
    private void registerClientEvents() {
        // Client tick event for handling key presses
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                handleKeyBindings(client);
            }
        });
        
        // HUD render callback for overlay rendering
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (MinecraftClient.getInstance().player != null) {
                renderOverlay(drawContext, tickDelta);
            }
        });
        
        WayaCreatesEngine.LOGGER.info("📡 Client events registered");
    }
    
    private void handleKeyBindings(MinecraftClient client) {
        // Open main editor - temporarily disabled
        if (openEditorKey.wasPressed()) {
            // client.setScreen(new VideoEditorUI()); // Temporarily disabled
            client.player.sendMessage(Text.literal("🎬 Video editor temporarily disabled"), true);
            WayaCreatesEngine.LOGGER.info("🎬 Video editor opened via key binding (temporarily disabled)");
        }
        
        // Toggle overlay - temporarily disabled
        if (toggleOverlayKey.wasPressed()) {
            // OverlayUI overlay = WayaCreatesEngine.getOverlayUI(); // Temporarily disabled
            client.player.sendMessage(Text.literal("🖼️ Overlay toggled (temporarily disabled)"), true);
            WayaCreatesEngine.LOGGER.info("🖼️ Overlay toggled via key binding (temporarily disabled)");
        }
        
        // Start/stop recording
        if (startRecordingKey.wasPressed()) {
            var videoEngine = WayaCreatesEngine.getVideoEngine();
            var session = videoEngine.getActiveSession(client.player.getUuid());
            
            if (session != null && session.isRecording()) {
                videoEngine.stopRecording(client.player.getUuid());
                client.player.sendMessage(Text.literal("⏹️ Recording stopped"), true);
                WayaCreatesEngine.LOGGER.info("⏹️ Recording stopped via key binding");
            } else {
                var newSession = videoEngine.startRecording(client.player.getUuid(), 
                    new com.wayacreates.engine.VideoEngine.VideoSettings());
                client.player.sendMessage(Text.literal("🎥 Recording started"), true);
                WayaCreatesEngine.LOGGER.info("🎥 Recording started via key binding");
            }
        }
        
        // Toggle 3D viewport
        if (toggleViewportKey.wasPressed()) {
            client.setScreen(new ThreeDViewport());
            WayaCreatesEngine.LOGGER.info("🎭 3D viewport opened via key binding");
        }
        
        // Open node compositor
        if (openCompositorKey.wasPressed()) {
            client.setScreen(new NodeCompositorUI());
            WayaCreatesEngine.LOGGER.info("🎨 Node compositor opened via key binding");
        }
        
        // Open audio editor
        if (openAudioEditorKey.wasPressed()) {
            client.setScreen(new AudioEditorUI());
            WayaCreatesEngine.LOGGER.info("🎵 Audio editor opened via key binding");
        }
    }
    
    private void renderOverlay(net.minecraft.client.gui.DrawContext drawContext, float tickDelta) {
        // OverlayUI overlay = WayaCreatesEngine.getOverlayUI(); // Temporarily disabled
        // TODO: Render overlay elements when re-enabled
        // This would render things like recording status, resource usage, etc.
    }
}
