package com.wayacreates.ui.overlay;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.wayacreates.ui.overlay.OverlayUIComponents.*;

/**
 * Overlay UI - Mouse Event Handling
 * Part 2 of 3 - Mouse interactions and drag & drop functionality
 */
public class OverlayUIEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/OverlayUI");
    private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
    
    // Reference to main UI
    private final OverlayUI mainUI;
    
    public OverlayUIEvents(OverlayUI mainUI) {
        this.mainUI = mainUI;
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        try {
            if (!mainUI.isVisible()) return false;
            
            // Handle toolbar clicks
            if (mouseY <= 40) { // TOOLBAR_HEIGHT
                handleToolbarClick(mouseX, mouseY);
                return true;
            }
            
            // Handle drag start
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                if (mainUI.getOverlayManager().mouseClicked(mouseX, mouseY, button)) {
                    Overlay overlay = mainUI.getOverlayManager().getOverlayAt(mouseX, mouseY);
                    if (overlay != null) {
                        mainUI.startDrag(overlay, mouseX, mouseY);
                        return true;
                    }
                }
            }
            
            // Handle component clicks
            if (mainUI.getPlayerTagManager().mouseClicked(mouseX, mouseY, button)) return true;
            if (mainUI.getParticleManager().mouseClicked(mouseX, mouseY, button)) return true;
            if (mainUI.getLivestreamOverlay().mouseClicked(mouseX, mouseY, button)) return true;
            if (mainUI.getDragDropOverlay().mouseClicked(mouseX, mouseY, button)) return true;
            
            return false;
        } catch (Exception e) {
            LOGGER.error("❌ Error in mouseClicked: {}", e.getMessage(), e);
            return false;
        }
    }
    
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        try {
            if (!mainUI.isVisible()) return false;
            
            // Handle drag end
            if (mainUI.isDragging() && mainUI.getDraggedOverlay() != null) {
                // Update overlay position
                mainUI.getDraggedOverlay().updatePosition(mouseX, mouseY);
                mainUI.getOverlayManager().updateOverlay(mainUI.getDraggedOverlay());
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
        if (!mainUI.isVisible()) return false;
        
        if (mainUI.isDragging() && mainUI.getDraggedOverlay() != null) {
            mainUI.getDraggedOverlay().updatePosition(mouseX, mouseY);
        }
        
        return false;
    }
    
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!mainUI.isVisible()) return false;
        
        // Handle keyboard shortcuts
        if (keyCode == 290) { // F1 key
            mainUI.toggleVisibility();
            return true;
        }
        
        if (keyCode == 259 && mainUI.getSelectedOverlay() != null) { // Delete key
            mainUI.getOverlayManager().removeOverlay(mainUI.getSelectedOverlay());
            mainUI.setSelectedOverlay(null);
            return true;
        }
        
        return false;
    }
    
    public void tick() {
        if (!mainUI.isVisible()) return;
        
        // Update components
        mainUI.getOverlayManager().tick();
        mainUI.getPlayerTagManager().tick();
        mainUI.getParticleManager().tick();
        mainUI.getLivestreamOverlay().tick();
        mainUI.getDragDropOverlay().tick();
    }
    
    private void handleToolbarClick(double mouseX, double mouseY) {
        // Check mode buttons
        int buttonX = 10;
        for (OverlayUI.OverlayMode mode : OverlayUI.OverlayMode.values()) {
            if (mouseX >= buttonX && mouseX <= buttonX + 80 && 
                mouseY >= 5 && mouseY <= 35) {
                mainUI.setCurrentMode(mode);
                return;
            }
            buttonX += 90;
        }
        
        // Check toggle button
        if (mouseX >= 800 && mouseX <= 880 && 
            mouseY >= 5 && mouseY <= 35) {
            mainUI.toggleVisibility();
        }
    }
}
