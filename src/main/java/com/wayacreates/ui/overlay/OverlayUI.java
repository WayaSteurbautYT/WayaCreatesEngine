package com.wayacreates.ui.overlay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static com.wayacreates.ui.overlay.OverlayUIComponents.*;

/**
 * Overlay UI System with Drag & Drop Support
 * Manages in-game overlays, player tags, particles, and livestream elements
 * Main UI class - Part 1 of 3
 */
public class OverlayUI extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/OverlayUI");
    private static final Text TITLE = Text.translatable("wayacreates.ui.overlay");
    
    // Debug flag for verbose logging
    private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
    
    // Overlay components
    private OverlayManager overlayManager;
    private PlayerTagManager playerTagManager;
    private ParticleManager particleManager;
    private LivestreamOverlay livestreamOverlay;
    private DragDropOverlay dragDropOverlay;
    
    // Drag & Drop state
    private boolean isDragging = false;
    private DraggableOverlay draggedOverlay = null;
    
    // Event handling
    private OverlayUIEvents eventHandler;
    
    // UI state
    private boolean isInitialized = false;
    private boolean isVisible = true;
    private OverlayMode currentMode = OverlayMode.EDIT;
    private String selectedOverlay = null;
    
    // Layout constants
    private static final int TOOLBAR_HEIGHT = 40;
    private static final int SIDEBAR_WIDTH = 250;
    
    public OverlayUI() {
        super(TITLE);
        this.eventHandler = new OverlayUIEvents(this);
        if (DEBUG_MODE) {
            LOGGER.info("🖼️ Overlay UI initialized in DEBUG mode");
        } else {
            LOGGER.info("🖼️ Overlay UI initialized");
        }
    }
    
    @Override
    protected void init() {
        super.init();
        
        if (!isInitialized) {
            initializeComponents();
            isInitialized = true;
        }
        
        layoutComponents();
        loadDefaultOverlays();
    }
    
    private void initializeComponents() {
        overlayManager = new OverlayManager();
        playerTagManager = new PlayerTagManager();
        particleManager = new ParticleManager();
        livestreamOverlay = new LivestreamOverlay();
        dragDropOverlay = new DragDropOverlay();
    }
    
    private void layoutComponents() {
        int panelWidth = this.width;
        int panelHeight = this.height;
        
        // Layout overlay editor (main area)
        int editorX = SIDEBAR_WIDTH;
        int editorY = TOOLBAR_HEIGHT;
        int editorWidth = panelWidth - SIDEBAR_WIDTH;
        int editorHeight = panelHeight - TOOLBAR_HEIGHT;
        
        overlayManager.setBounds(editorX, editorY, editorWidth, editorHeight);
        
        // Layout sidebar
        playerTagManager.setBounds(0, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, panelHeight - TOOLBAR_HEIGHT);
        
        // Layout particle manager (bottom of sidebar)
        particleManager.setBounds(0, panelHeight - 100, SIDEBAR_WIDTH, 100);
        
        // Layout drag & drop overlay (bottom area)
        dragDropOverlay.setBounds(SIDEBAR_WIDTH, panelHeight - 100, panelWidth - SIDEBAR_WIDTH, 100);
    }
    
    private void loadDefaultOverlays() {
        // Load default overlays
        overlayManager.addOverlay(new Overlay("player_tags", "Player Tags", 10, 10, true));
        overlayManager.addOverlay(new Overlay("particles", "Particles", 10, 50, true));
        overlayManager.addOverlay(new Overlay("voice_chat", "Voice Chat", 10, 90, false));
        overlayManager.addOverlay(new Overlay("mocap", "Motion Capture", 10, 130, false));
        overlayManager.addOverlay(new Overlay("baritone", "Baritone", 10, 170, false));
        overlayManager.addOverlay(new Overlay("livestream", "Livestream", width - 200, 10, false));
        
        LOGGER.info("🖼️ Loaded {} default overlays", overlayManager.getOverlayCount());
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!isVisible) return;
        
        // Draw semi-transparent background
        renderBackground(context);
        
        // Draw toolbar
        renderToolbar(context, mouseX, mouseY);
        
        // Draw main components
        overlayManager.render(context, mouseX, mouseY, delta);
        playerTagManager.render(context, mouseX, mouseY, delta);
        particleManager.render(context, mouseX, mouseY, delta);
        livestreamOverlay.render(context, mouseX, mouseY, delta);
        dragDropOverlay.render(context, mouseX, mouseY, delta);
        
        // Draw drag preview
        if (isDragging && draggedOverlay != null) {
            renderDragPreview(context, mouseX, mouseY);
        }
        
        // Draw status bar
        renderStatusBar(context);
    }
    
    @Override
    public void renderBackground(DrawContext context) {
        int bgWidth = this.width;
        int bgHeight = this.height;
        
        // Semi-transparent dark background
        for (int y = 0; y < bgHeight; y++) {
            float alpha = 0.3f; // 30% opacity
            int color = (int) (0xFF * alpha) << 24 | 0x1a1a1a;
            context.fill(0, y, bgWidth, y + 1, color);
        }
    }
    
    private void renderToolbar(DrawContext context, int mouseX, int mouseY) {
        int toolbarY = 0;
        context.fill(0, toolbarY, this.width, TOOLBAR_HEIGHT, 0xFF333333);
        
        // Draw mode buttons
        int buttonX = 10;
        var textRenderer = this.textRenderer;
        for (OverlayMode mode : OverlayMode.values()) {
            boolean isSelected = currentMode == mode;
            int color = isSelected ? 0xFF4a6a4a : 0xFF3a4a3a;
            
            context.fill(buttonX, toolbarY + 5, buttonX + 80, toolbarY + 35, color);
            context.drawBorder(buttonX, toolbarY + 5, 80, 30, 0xFF6a6a6a);
            
            String modeText = mode.toString().substring(0, 1) + mode.toString().substring(1).toLowerCase();
            context.drawText(textRenderer, modeText, buttonX + 10, toolbarY + 12, 0xFFFFFF, false);
            
            buttonX += 90;
        }
        
        // Draw toggle button
        String toggleText = isVisible ? "Hide" : "Show";
        drawButton(context, this.width - 100, toolbarY + 5, 80, 30, toggleText, mouseX, mouseY);
        
        // Draw title
        context.drawText(textRenderer, "Overlay System", this.width / 2 - 60, toolbarY + 12, 0xFFFFFF, false);
    }
    
    private void renderDragPreview(DrawContext context, int mouseX, int mouseY) {
        if (draggedOverlay != null) {
            // Draw preview of dragged overlay
            String previewText = draggedOverlay.getName();
            var textRenderer = this.textRenderer;
            int textWidth = textRenderer.getWidth(previewText);
            int previewWidth = textWidth + 20;
            int previewHeight = 30;
            
            int previewX = mouseX - previewWidth / 2;
            int previewY = mouseY - previewHeight / 2;
            
            // Draw preview background
            context.fill(previewX, previewY, previewX + previewWidth, previewY + previewHeight, 0xFF4a4a4a);
            context.drawBorder(previewX, previewY, previewWidth, previewHeight, 0xFF6a6a6a);
            
            // Draw preview text
            context.drawText(textRenderer, previewText, previewX + 10, previewY + 8, 0xFFFFFF, false);
        }
    }
    
    private void renderStatusBar(DrawContext context) {
        int statusY = this.height - 25;
        context.fill(0, statusY, this.width, statusY + 25, 0xFF2a2a2a);
        
        String statusText = currentMode.toString() + " Mode";
        var textRenderer = this.textRenderer;
        context.drawText(textRenderer, statusText, 10, statusY + 5, 0xFFFFFF, false);
        
        String overlayCount = String.format("Overlays: %d", overlayManager.getOverlayCount());
        context.drawText(textRenderer, overlayCount, this.width - 100, statusY + 5, 0xFFFFFF, false);
    }
    
    // Utility methods
    private void drawButton(DrawContext context, int x, int y, int width, int height, 
                           String text, int mouseX, int mouseY) {
        boolean isHovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
        
        context.fill(x, y, x + width, y + height, color);
        context.drawBorder(x, y, width, height, 0xFF6a6a6a);
        
        if (text != null) {
            var textRenderer = this.textRenderer;
            int textWidth = textRenderer.getWidth(text);
            int textX = x + (width - textWidth) / 2;
            int textY = y + (height - 8) / 2;
            context.drawText(textRenderer, text, textX, textY, 0xFFFFFF, false);
        }
    }
    
    // Public API methods
    public void toggleOverlay(String overlayId, boolean enabled) {
        Overlay overlay = overlayManager.getOverlay(overlayId);
        if (overlay != null) {
            overlay.setEnabled(enabled);
            LOGGER.info("🖼️ {} overlay: {}", enabled ? "Enabled" : "Disabled", overlay.getName());
        }
    }
    
    public void addLivestreamOverlay(String content, int x, int y) {
        livestreamOverlay.addOverlay(content, x, y);
    }
    
    public void removeLivestreamOverlay(String content) {
        livestreamOverlay.removeOverlay(content);
    }
    
    public void tick() {
        if (!isVisible) return;
        
        // Update components
        overlayManager.tick();
        playerTagManager.tick();
        particleManager.tick();
        livestreamOverlay.tick();
        dragDropOverlay.tick();
        eventHandler.tick();
    }
    
    // Public getters for event handler access
    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { this.isVisible = visible; }
    public OverlayMode getCurrentMode() { return currentMode; }
    public void setCurrentMode(OverlayMode mode) { this.currentMode = mode; }
    public String getSelectedOverlay() { return selectedOverlay; }
    public void setSelectedOverlay(String overlay) { this.selectedOverlay = overlay; }
    public boolean isDraggingItem() { return isDragging; }
    public DraggableOverlay getDraggedOverlay() { return draggedOverlay; }
    public OverlayManager getOverlayManager() { return overlayManager; }
    public PlayerTagManager getPlayerTagManager() { return playerTagManager; }
    public ParticleManager getParticleManager() { return particleManager; }
    public LivestreamOverlay getLivestreamOverlay() { return livestreamOverlay; }
    public DragDropOverlay getDragDropOverlay() { return dragDropOverlay; }
    
    // Private helper methods for event handler
    public void startDrag(Overlay overlay, double mouseX, double mouseY) {
        isDragging = true;
        draggedOverlay = new DraggableOverlay(overlay, mouseX, mouseY);
        selectedOverlay = overlay.getId();
        LOGGER.info("🖼️ Started dragging overlay: {}", overlay.getName());
    }
    
    public void endDrag() {
        isDragging = false;
        draggedOverlay = null;
    }
    
    public void toggleVisibility() {
        isVisible = !isVisible;
        LOGGER.info("🖼️ Overlay system {}", isVisible ? "shown" : "hidden");
    }
    
    private void handleToolbarClick(double mouseX, double mouseY) {
        // Check mode buttons
        int buttonX = 10;
        for (OverlayMode mode : OverlayMode.values()) {
            if (mouseX >= buttonX && mouseX <= buttonX + 80 && 
                mouseY >= 5 && mouseY <= 35) {
                currentMode = mode;
                return;
            }
            buttonX += 90;
        }
        
        // Check toggle button
        if (mouseX >= width - 100 && mouseX <= width - 20 && 
            mouseY >= 5 && mouseY <= 35) {
            toggleVisibility();
        }
    }
    
    // Overlay enum
    public enum OverlayMode {
        EDIT, PREVIEW, LIVESTREAM, RECORDING
    }
}
