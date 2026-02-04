package com.wayacreates.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Overlay UI System with Drag & Drop Support
 * Manages in-game overlays, player tags, particles, and livestream elements
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
    
    // UI state
    private boolean isInitialized = false;
    private boolean isVisible = true;
    private OverlayMode currentMode = OverlayMode.EDIT;
    private String selectedOverlay = null;
    
    // Drag & Drop state
    private boolean isDragging = false;
    private DraggableOverlay draggedOverlay = null;
    
    // Layout constants
    private static final int TOOLBAR_HEIGHT = 40;
    private static final int SIDEBAR_WIDTH = 250;
    
    public OverlayUI() {
        super(TITLE);
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
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isVisible) return false;
        
        // Handle toolbar clicks
        if (mouseY <= TOOLBAR_HEIGHT) {
            handleToolbarClick(mouseX, mouseY);
            return true;
        }
        
        // Handle drag start
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (overlayManager.mouseClicked(mouseX, mouseY, button)) {
                Overlay overlay = overlayManager.getOverlayAt(mouseX, mouseY);
                if (overlay != null) {
                    startDrag(overlay, mouseX, mouseY);
                    return true;
                }
            }
        }
        
        // Handle component clicks
        if (playerTagManager.mouseClicked(mouseX, mouseY, button)) return true;
        if (particleManager.mouseClicked(mouseX, mouseY, button)) return true;
        if (livestreamOverlay.mouseClicked(mouseX, mouseY, button)) return true;
        if (dragDropOverlay.mouseClicked(mouseX, mouseY, button)) return true;
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!isVisible) return false;
        
        // Handle drag end
        if (isDragging && draggedOverlay != null) {
            // Update overlay position
            draggedOverlay.updatePosition(mouseX, mouseY);
            overlayManager.updateOverlay(draggedOverlay);
            endDrag();
        }
        
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!isVisible) return false;
        
        if (isDragging && draggedOverlay != null) {
            draggedOverlay.updatePosition(mouseX, mouseY);
        }
        
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isVisible) return false;
        
        // Handle keyboard shortcuts
        if (keyCode == GLFW.GLFW_KEY_F1) {
            toggleVisibility();
            return true;
        }
        
        if (keyCode == GLFW.GLFW_KEY_DELETE && selectedOverlay != null) {
            overlayManager.removeOverlay(selectedOverlay);
            selectedOverlay = null;
            return true;
        }
        
        // Mode switching
        if (keyCode >= GLFW.GLFW_KEY_1 && keyCode <= GLFW.GLFW_KEY_4) {
            int modeIndex = keyCode - GLFW.GLFW_KEY_1;
            OverlayMode[] modes = OverlayMode.values();
            if (modeIndex < modes.length) {
                currentMode = modes[modeIndex];
                return true;
            }
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
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
        if (mouseX >= this.width - 100 && mouseX <= this.width - 20 && 
            mouseY >= 5 && mouseY <= 35) {
            toggleVisibility();
        }
    }
    
    private void startDrag(Overlay overlay, double mouseX, double mouseY) {
        isDragging = true;
        draggedOverlay = new DraggableOverlay(overlay, mouseX, mouseY);
        selectedOverlay = overlay.getId();
        LOGGER.info("🖼️ Started dragging overlay: {}", overlay.getName());
    }
    
    private void endDrag() {
        isDragging = false;
        draggedOverlay = null;
    }
    
    private void toggleVisibility() {
        isVisible = !isVisible;
        LOGGER.info("🖼️ Overlay system {}", isVisible ? "shown" : "hidden");
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
    
    // Overlay enum
    public enum OverlayMode {
        EDIT, PREVIEW, LIVESTREAM, RECORDING
    }
    
    // Component classes (simplified)
    private static class OverlayManager {
        private final Map<String, Overlay> overlays = new ConcurrentHashMap<>();
        private int x, y, width, height;
        
        public void addOverlay(Overlay overlay) {
            overlays.put(overlay.getId(), overlay);
        }
        
        public void removeOverlay(String overlayId) {
            overlays.remove(overlayId);
        }
        
        public Overlay getOverlay(String overlayId) {
            return overlays.get(overlayId);
        }
        
        public Overlay getOverlayAt(double mouseX, double mouseY) {
            for (Overlay overlay : overlays.values()) {
                if (overlay.isPointInside(mouseX, mouseY)) {
                    return overlay;
                }
            }
            return null;
        }
        
        public void updateOverlay(DraggableOverlay draggedOverlay) {
            Overlay overlay = overlays.get(draggedOverlay.getId());
            if (overlay != null) {
                overlay.setPosition(draggedOverlay.getIntX(), draggedOverlay.getIntY());
            }
        }
        
        public int getOverlayCount() { return overlays.size(); }
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF1a1a1a);
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Overlay Editor", x + 10, y + 10, 0xFFFFFF, false);
            
            // Render overlays
            for (Overlay overlay : overlays.values()) {
                if (overlay.isEnabled()) {
                    overlay.render(context, mouseX, mouseY, delta);
                }
            }
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void tick() {}
    }
    
    private static class Overlay {
        private final String id;
        private final String name;
        private int x, y;
        private boolean enabled;
        
        public Overlay(String id, String name, int x, int y, boolean enabled) {
            this.id = id;
            this.name = name;
            this.x = x;
            this.y = y;
            this.enabled = enabled;
        }
        
        public boolean isPointInside(double mouseX, double mouseY) {
            return mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 30;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            boolean isHovered = isPointInside(mouseX, mouseY);
            int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
            
            context.fill(x, y, x + 100, y + 30, color);
            context.drawBorder(x, y, 100, 30, 0xFF6a6a6a);
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, name, x + 5, y + 8, 0xFFFFFF, false);
        }
        
        // Getters and setters
        public String getId() { return id; }
        public String getName() { return name; }
        public boolean isEnabled() { return enabled; }
        
        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
    
    private static class DraggableOverlay {
        private final String id;
        private final String name;
        private double x, y;
        
        public DraggableOverlay(Overlay overlay, double x, double y) {
            this.id = overlay.getId();
            this.name = overlay.getName();
            this.x = x;
            this.y = y;
        }
        
        public void updatePosition(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public int getIntX() { return (int) x; }
        public int getIntY() { return (int) y; }
    }
    
    private static class PlayerTagManager {
        private int x, y, width, height;
        private final boolean showPlayerTags = true;
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF2a2a2a);
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Player Tags", x + 10, y + 10, 0xFFFFFF, false);
            
            // Draw toggle
            String toggleText = showPlayerTags ? "ON" : "OFF";
            int toggleColor = showPlayerTags ? 0xFF4a7a4a : 0xFF7a4a4a;
            context.drawText(textRenderer, toggleText, x + 10, y + 30, toggleColor, false);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void tick() {}
    }
    
    private static class ParticleManager {
        private int x, y, width, height;
        private final boolean showParticles = true;
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF2a2a2a);
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Particles", x + 10, y + 10, 0xFFFFFF, false);
            
            // Draw toggle
            String toggleText = showParticles ? "ON" : "OFF";
            int toggleColor = showParticles ? 0xFF4a7a4a : 0xFF7a4a4a;
            context.drawText(textRenderer, toggleText, x + 10, y + 30, toggleColor, false);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void tick() {}
    }
    
    private static class LivestreamOverlay {
        private final List<LivestreamElement> elements = new ArrayList<>();
        
        public void addOverlay(String content, int x, int y) {
            elements.add(new LivestreamElement(content, x, y));
        }
        
        public void removeOverlay(String content) {
            elements.removeIf(element -> element.getContent().equals(content));
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            for (LivestreamElement element : elements) {
                element.render(context, mouseX, mouseY, delta);
            }
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return false;
        }
        
        public void tick() {}
    }
    
    private static class LivestreamElement {
        private final String content;
        private final int x, y;
        
        public LivestreamElement(String content, int x, int y) {
            this.content = content;
            this.x = x;
            this.y = y;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + 150, y + 30, 0xFF4a2a2a);
            context.drawBorder(x, y, 150, 30, 0xFF6a4a4a);
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, content, x + 5, y + 8, 0xFFFFFF, false);
        }
        
        public String getContent() { return content; }
    }
    
    private static class DragDropOverlay {
        private int x, y, width, height;
        private boolean isActive = false;
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            if (!isActive) return;
            
            // Draw drag & drop zone
            context.fill(x, y, x + width, y + height, 0xFF3a4a3a);
            context.drawBorder(x, y, width, height, 0xFF5a6a6a);
            
            context.drawText(MinecraftClient.getInstance().textRenderer, "Drag & Drop Zone", x + 10, y + 10, 0xFFFFFF, false);
            
            // Draw drop hint
            String hint = "Drop overlays here";
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            int hintWidth = textRenderer.getWidth(hint);
            int hintX = x + (width - hintWidth) / 2;
            context.drawText(textRenderer, hint, hintX, y + height / 2, 0xAAAAAA, false);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                isActive = !isActive;
                return true;
            }
            return false;
        }
        
        public void tick() {
            // Update drag & drop state if needed
        }
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public boolean isActive() { return isActive; }
    }
}
