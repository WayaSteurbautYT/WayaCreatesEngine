package com.wayacreates.ui.audio;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.gui.DrawContext;

import static com.wayacreates.ui.audio.AudioEditorUIComponents.*;

/**
 * Audio Editor UI - Mouse Event Handling
 * Part 2 of 3 - Mouse interactions and drag & drop functionality
 */
public class AudioEditorUIEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/AudioEditor");
    private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
    
    // Reference to main UI
    private final AudioEditorUI mainUI;
    
    public AudioEditorUIEvents(AudioEditorUI mainUI) {
        this.mainUI = mainUI;
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        try {
            // Handle drag start
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && mainUI.getSoundEffectLibrary() != null) {
                if (mainUI.getSoundEffectLibrary().mouseClicked(mouseX, mouseY, button)) {
                    Object effectObj = mainUI.getSoundEffectLibrary().getEffectAt(mouseX, mouseY);
                    if (effectObj != null) {
                        mainUI.startDrag((SoundEffect) effectObj, mouseX, mouseY);
                        return true;
                    }
                }
            }
            
            // Handle component clicks with null checks
            if (mainUI.getWaveformDisplay() != null && mainUI.getWaveformDisplay().mouseClicked(mouseX, mouseY, button)) return true;
            if (mainUI.getEffectsPanel() != null && mainUI.getEffectsPanel().mouseClicked(mouseX, mouseY, button)) return true;
            if (mainUI.getTimeline() != null && mainUI.getTimeline().mouseClicked(mouseX, mouseY, button)) return true;
            if (mainUI.getLiveStreamPanel() != null && mainUI.getLiveStreamPanel().mouseClicked(mouseX, mouseY, button)) return true;
            
            return false;
        } catch (Exception e) {
            LOGGER.error("❌ Error in mouseClicked: {}", e.getMessage(), e);
            return false;
        }
    }
    
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        try {
            // Handle drag end
            if (mainUI.isDragging() && mainUI.getDraggedItem() != null) {
                // Check if dropped on timeline
                if (mainUI.getTimeline() != null && mainUI.getTimeline().isInBounds(mouseX, mouseY)) {
                    if (mainUI.getDraggedItem().getItem() instanceof SoundEffect effect) {
                        mainUI.getTimeline().addSoundEffect(effect, mouseX, mouseY);
                        LOGGER.info("🎵 Dropped sound effect: {} on timeline", effect.getName());
                    }
                }
                
                // Check if dropped on live stream panel
                if (mainUI.getLiveStreamPanel() != null && mainUI.getLiveStreamPanel().isInBounds(mouseX, mouseY)) {
                    if (mainUI.getDraggedItem().getItem() instanceof SoundEffect effect) {
                        mainUI.getLiveStreamPanel().playSoundEffect(effect);
                        LOGGER.info("🎵 Played sound effect: {} for live stream", effect.getName());
                    }
                }
                
                mainUI.endDrag();
            }
            
            return false;
        } catch (Exception e) {
            LOGGER.error("❌ Error in mouseReleased: {}", e.getMessage(), e);
            mainUI.endDrag(); // Ensure drag state is cleared on error
            return false;
        }
    }
    
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (mainUI.isDragging() && mainUI.getDraggedItem() != null) {
            // Update drag position
            mainUI.getDraggedItem().updatePosition(mouseX, mouseY);
        }
        
        return false;
    }
    
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        // Zoom waveform display
        float zoomFactor = amount > 0 ? 1.1f : 0.9f;
        float newZoomLevel = mainUI.getZoomLevel() * zoomFactor;
        
        // Clamp zoom level between 0.1x and 10x
        if (newZoomLevel >= 0.1f && newZoomLevel <= 10.0f) {
            mainUI.setZoomLevel(newZoomLevel);
            LOGGER.info("🔍 Zoom level changed to {:.2f}x", newZoomLevel);
        }
        
        return false;
    }
    
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Handle keyboard shortcuts
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            mainUI.togglePlayback();
            return true;
        }
        
        if (keyCode == GLFW.GLFW_KEY_DELETE && mainUI.getSelectedTrack() != null) {
            mainUI.getTimeline().removeTrack(mainUI.getSelectedTrack());
            mainUI.setSelectedTrack(null);
            return true;
        }
        
        return false;
    }
    
    public void tick() {
        if (mainUI.isPlaying()) {
            float currentTime = mainUI.getCurrentTime() + 1.0f / 30.0f; // 30 FPS
            mainUI.setCurrentTime(currentTime);
            if (mainUI.getAudioMixer() != null) {
                mainUI.getAudioMixer().update(currentTime);
            }
        }
        
        // Update components
        if (mainUI.getWaveformDisplay() != null) mainUI.getWaveformDisplay().tick();
        if (mainUI.getEffectsPanel() != null) mainUI.getEffectsPanel().tick();
        if (mainUI.getTimeline() != null) mainUI.getTimeline().tick();
        if (mainUI.getLiveStreamPanel() != null) mainUI.getLiveStreamPanel().tick();
    }
}
