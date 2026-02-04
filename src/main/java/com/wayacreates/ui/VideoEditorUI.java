package com.wayacreates.ui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Professional Video Editor UI
 * After Effects and CapCut inspired interface with timeline, effects, and compositing
 */
public class VideoEditorUI extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/VideoEditor");
    private static final Text TITLE = Text.translatable("wayacreates.video.editor_title");
    
    // Debug flag for verbose logging
    private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
    
    // UI Components
    private Timeline timeline;
    private EffectsPanel effectsPanel;
    private MediaLibrary mediaLibrary;
    private PreviewWindow previewWindow;
    private ToolBar toolBar;
    private PropertiesPanel propertiesPanel;
    
    // UI State
    private boolean isInitialized = false;
    private int selectedTab = 0; // 0: Timeline, 1: Effects, 2: Media, 3: Preview
    private boolean isPlaying = false;
    private float currentTime = 0.0f;
    private float zoomLevel = 1.0f;
    
    // Layout constants
    private static final int TOOLBAR_HEIGHT = 60;
    private static final int SIDEBAR_WIDTH = 300;
    private static final int TIMELINE_HEIGHT = 200;
    private static final int PREVIEW_MIN_HEIGHT = 300;
    
    public VideoEditorUI() {
        super(TITLE);
        if (DEBUG_MODE) {
            LOGGER.info("🎬 Video Editor UI initialized in DEBUG mode");
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
    }
    
    private void initializeComponents() {
        // Create UI components with proper bounds
        timeline = new Timeline();
        effectsPanel = new EffectsPanel();
        
        // Example usage of Track factory methods
        Track videoTrack = Track.createVideoTrack("Main Video");
        Track audioTrack = Track.createAudioTrack("Background Music");
        
        if (DEBUG_MODE) {
            LOGGER.info("🎬 Video Editor UI initialized with tracks: {} , {}", 
                       videoTrack.getName(), audioTrack.getName());
        }
        
        mediaLibrary = new MediaLibrary();
        previewWindow = new PreviewWindow();
        toolBar = new ToolBar();
        propertiesPanel = new PropertiesPanel();
    }
    
    private void layoutComponents() {
        int panelWidth = this.width;
        int panelHeight = this.height;
        
        // Layout toolbar (top)
        toolBar.setBounds(0, 0, panelWidth, TOOLBAR_HEIGHT);
        
        // Layout preview window (center-left)
        int previewWidth = (panelWidth - SIDEBAR_WIDTH * 2) / 2;
        int previewHeight = Math.max(PREVIEW_MIN_HEIGHT, panelHeight - TOOLBAR_HEIGHT - TIMELINE_HEIGHT);
        int previewX = SIDEBAR_WIDTH;
        int previewY = TOOLBAR_HEIGHT;
        previewWindow.setBounds(previewX, previewY, previewWidth, previewHeight);
        
        // Layout timeline (bottom)
        int timelineY = panelHeight - TIMELINE_HEIGHT;
        timeline.setBounds(0, timelineY, panelWidth, TIMELINE_HEIGHT);
        
        // Layout effects panel (right)
        effectsPanel.setBounds(panelWidth - SIDEBAR_WIDTH, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, 
                              panelHeight - TOOLBAR_HEIGHT - TIMELINE_HEIGHT);
        
        // Layout media library (left)
        mediaLibrary.setBounds(0, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, 
                              panelHeight - TOOLBAR_HEIGHT - TIMELINE_HEIGHT);
        
        // Layout properties panel (center-right)
        int propsX = previewX + previewWidth;
        propertiesPanel.setBounds(propsX, previewY, panelWidth - propsX - SIDEBAR_WIDTH, previewHeight);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background
        renderBackground(context);
        
        // Draw components based on selected tab
        switch (selectedTab) {
            case 0: // Timeline view
                renderTimelineView(context, mouseX, mouseY);
                break;
            case 1: // Effects view
                renderEffectsView(context, mouseX, mouseY);
                break;
            case 2: // Media view
                renderMediaView(context, mouseX, mouseY);
                break;
            case 3: // Preview view
                renderPreviewView(context, mouseX, mouseY);
                break;
        }
        
        // Draw toolbar (always visible)
        toolBar.render(context, mouseX, mouseY, delta);
        
        // Draw timeline (always visible at bottom)
        timeline.render(context, mouseX, mouseY, delta);
        
        // Draw playback controls
        renderPlaybackControls(context, mouseX, mouseY);
        
        // Draw status bar
        renderStatusBar(context);
    }
    
    @Override
    public void renderBackground(DrawContext context) {
        // Dark gradient background
        int bgWidth = this.width;
        int bgHeight = this.height;
        
        // Draw gradient
        for (int y = 0; y < bgHeight; y++) {
            float alpha = (float) y / bgHeight;
            int color = interpolateColor(0xFF1a1a1a, 0xFF0a0a0a, alpha);
            context.fill(0, y, bgWidth, y + 1, color);
        }
    }
    
    private void renderTimelineView(DrawContext context, int mouseX, int mouseY) {
        previewWindow.render(context, mouseX, mouseY, 0);
        mediaLibrary.render(context, mouseX, mouseY, 0);
        propertiesPanel.render(context, mouseX, mouseY, 0);
    }
    
    private void renderEffectsView(DrawContext context, int mouseX, int mouseY) {
        effectsPanel.render(context, mouseX, mouseY, 0);
        previewWindow.render(context, mouseX, mouseY, 0);
    }
    
    private void renderMediaView(DrawContext context, int mouseX, int mouseY) {
        mediaLibrary.render(context, mouseX, mouseY, 0);
        previewWindow.render(context, mouseX, mouseY, 0);
    }
    
    private void renderPreviewView(DrawContext context, int mouseX, int mouseY) {
        previewWindow.render(context, mouseX, mouseY, 0);
        propertiesPanel.render(context, mouseX, mouseY, 0);
    }
    
    private void renderPlaybackControls(DrawContext context, int mouseX, int mouseY) {
        int controlsY = this.height - TIMELINE_HEIGHT - 50;
        int controlsX = this.width / 2 - 150;
        
        // Play/Pause button
        int playButtonX = controlsX;
        int playButtonY = controlsY;
        int buttonSize = 30;
        
        String playIcon = isPlaying ? "⏸️" : "▶️";
        drawButton(context, playButtonX, playButtonY, buttonSize, buttonSize, playIcon, 
                   mouseX, mouseY);
        
        // Time display
        String timeText = formatTime(currentTime);
        drawText(context, playButtonX + buttonSize + 10, playButtonY + 5, timeText, 0xFFFFFF);
        
        // Zoom controls
        int zoomX = controlsX + 200;
        drawButton(context, zoomX, playButtonY, 30, buttonSize, "🔍", mouseX, mouseY);
        drawText(context, zoomX + 35, playButtonY + 5, String.format("%.0fx", zoomLevel), 0xFFFFFF);
    }
    
    private void renderStatusBar(DrawContext context) {
        int statusY = this.height - 25;
        context.fill(0, statusY, this.width, statusY + 25, 0xFF2a2a2a);
        
        String statusText = isPlaying ? "Playing" : "Ready";
        drawText(context, 10, statusY + 5, statusText, 0xFFFFFF);
        
        String fpsText = String.format("FPS: %d", MinecraftClient.getInstance().getCurrentFps());
        drawText(context, this.width - 100, statusY + 5, fpsText, 0xFFFFFF);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Handle toolbar clicks
        if (toolBar.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        
        // Handle timeline clicks
        if (timeline.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        
        // Handle component clicks based on current view
        switch (selectedTab) {
            case 0:
                if (previewWindow.mouseClicked(mouseX, mouseY, button)) return true;
                if (mediaLibrary.mouseClicked(mouseX, mouseY, button)) return true;
                if (propertiesPanel.mouseClicked(mouseX, mouseY, button)) return true;
                break;
            case 1:
                if (effectsPanel.mouseClicked(mouseX, mouseY, button)) return true;
                if (previewWindow.mouseClicked(mouseX, mouseY, button)) return true;
                break;
            case 2:
                if (mediaLibrary.mouseClicked(mouseX, mouseY, button)) return true;
                if (previewWindow.mouseClicked(mouseX, mouseY, button)) return true;
                break;
            case 3:
                if (previewWindow.mouseClicked(mouseX, mouseY, button)) return true;
                if (propertiesPanel.mouseClicked(mouseX, mouseY, button)) return true;
                break;
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Handle keyboard shortcuts
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            togglePlayback();
            return true;
        }
        
        if (keyCode == GLFW.GLFW_KEY_TAB) {
            switchTab((modifiers & GLFW.GLFW_MOD_SHIFT) != 0);
            return true;
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    private void togglePlayback() {
        isPlaying = !isPlaying;
        if (isPlaying) {
            // Start playback
        } else {
            // Pause playback
        }
    }
    
    private void switchTab(boolean reverse) {
        if (reverse) {
            selectedTab = (selectedTab - 1 + 4) % 4;
        } else {
            selectedTab = (selectedTab + 1) % 4;
        }
    }
    
    public void tick() {
        if (isPlaying) {
            currentTime += 1.0f / 30.0f; // Assuming 30 FPS
        }
        
        // Update components
        timeline.tick();
        previewWindow.tick();
        effectsPanel.tick();
        mediaLibrary.tick();
        propertiesPanel.tick();
    }
    
    // Utility methods
    private int interpolateColor(int color1, int color2, float alpha) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;
        
        int r = (int) (r1 + (r2 - r1) * alpha);
        int g = (int) (g1 + (g2 - g1) * alpha);
        int b = (int) (b1 + (b2 - b1) * alpha);
        
        return 0xFF << 24 | r << 16 | g << 8 | b;
    }
    
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
    
    private void drawText(DrawContext context, int x, int y, String text, int color) {
        var textRenderer = this.textRenderer;
        context.drawText(textRenderer, text, x, y, color, false);
    }
    
    private String formatTime(float time) {
        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);
        int frames = (int) ((time % 1) * 30);
        return String.format("%02d:%02d:%02d", minutes, seconds, frames);
    }
    
    // Component classes (simplified for brevity)
    private static class Timeline {
        private int x, y, width, height;
        private List<Track> tracks = new ArrayList<>();
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            // Draw timeline background
            context.fill(x, y, x + width, y + height, 0xFF252525);
            
            // Draw track headers
            for (int i = 0; i < tracks.size(); i++) {
                int trackY = y + i * 30;
                context.fill(x, trackY, x + 100, trackY + 30, 0xFF353535);
                var client = MinecraftClient.getInstance();
                if (client != null && client.textRenderer != null) {
                    var textRenderer = client.textRenderer;
                    context.drawText(textRenderer, 
                                   tracks.get(i).getName(), x + 5, trackY + 8, 0xFFFFFF, false);
                }
            }
            
            // Draw time ruler
            context.fill(x, y, x + width, y + 20, 0xFF454545);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void tick() {
            // Update timeline state
        }
    }
    
    private static class Track {
        private String name;
        
        public Track(String name) {
            this.name = name;
        }
        
        public String getName() { 
            return name; 
        }
        
        // Add usage of constructor in a factory method
        public static Track createVideoTrack(String name) {
            return new Track("Video: " + name);
        }
        
        public static Track createAudioTrack(String name) {
            return new Track("Audio: " + name);
        }
    }
    
    private static class EffectsPanel {
        private int x, y, width, height;
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF2a2a2a);
            var client = MinecraftClient.getInstance();
            if (client != null && client.textRenderer != null) {
                var textRenderer = client.textRenderer;
                context.drawText(textRenderer, 
                               "Effects", x + 10, y + 10, 0xFFFFFF, false);
            }
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void tick() {}
    }
    
    private static class MediaLibrary {
        private int x, y, width, height;
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF2a2a2a);
            var client = MinecraftClient.getInstance();
            if (client != null && client.textRenderer != null) {
                var textRenderer = client.textRenderer;
                context.drawText(textRenderer, 
                               "Media Library", x + 10, y + 10, 0xFFFFFF, false);
            }
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void tick() {}
    }
    
    private static class PreviewWindow {
        private int x, y, width, height;
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF1a1a1a);
            context.drawBorder(x, y, width, height, 0xFF4a4a4a);
            var client = MinecraftClient.getInstance();
            if (client != null && client.textRenderer != null) {
                var textRenderer = client.textRenderer;
                context.drawText(textRenderer, 
                               "Preview", x + 10, y + 10, 0xFFFFFF, false);
            }
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void tick() {}
    }
    
    private static class ToolBar {
        private int x, y, width, height;
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF333333);
            var client = MinecraftClient.getInstance();
            if (client != null && client.textRenderer != null) {
                var textRenderer = client.textRenderer;
                context.drawText(textRenderer, 
                               "WayaCreates Video Editor", x + 10, y + 20, 0xFFFFFF, false);
            }
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
    }
    
    private static class PropertiesPanel {
        private int x, y, width, height;
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF2a2a2a);
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, 
                           "Properties", x + 10, y + 10, 0xFFFFFF, false);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void tick() {}
    }
}
