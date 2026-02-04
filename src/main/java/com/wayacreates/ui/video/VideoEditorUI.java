package com.wayacreates.ui.video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static com.wayacreates.ui.video.VideoEditorUIComponents.*;

/**
 * Professional Video Editor UI
 * After Effects and CapCut inspired interface with timeline, effects, and compositing
 * Main UI class - Part 1 of 2
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
        
        // Layout effects panel (left sidebar)
        effectsPanel.setBounds(0, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, panelHeight - TOOLBAR_HEIGHT - TIMELINE_HEIGHT);
        
        // Layout media library (right sidebar)
        mediaLibrary.setBounds(panelWidth - SIDEBAR_WIDTH, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, 
                              panelHeight - TOOLBAR_HEIGHT - TIMELINE_HEIGHT);
        
        // Layout properties panel (bottom-right)
        propertiesPanel.setBounds(panelWidth - SIDEBAR_WIDTH, timelineY - 100, SIDEBAR_WIDTH, 100);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background
        renderBackground(context);
        
        // Draw toolbar
        toolBar.render(context, mouseX, mouseY, delta);
        
        // Draw main components based on selected tab
        switch (selectedTab) {
            case 0: // Timeline
                timeline.render(context, mouseX, mouseY, delta);
                break;
            case 1: // Effects
                effectsPanel.render(context, mouseX, mouseY, delta);
                break;
            case 2: // Media
                mediaLibrary.render(context, mouseX, mouseY, delta);
                break;
            case 3: // Preview
                previewWindow.render(context, mouseX, mouseY, delta);
                break;
        }
        
        // Draw properties panel
        propertiesPanel.render(context, mouseX, mouseY, delta);
        
        // Draw tab selector
        renderTabSelector(context, mouseX, mouseY);
        
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
        
        for (int y = 0; y < bgHeight; y++) {
            float alpha = (float) y / bgHeight;
            int color = interpolateColor(0xFF1a1a1a, 0xFF0a0a0a, alpha);
            context.fill(0, y, bgWidth, y + 1, color);
        }
    }
    
    private void renderTabSelector(DrawContext context, int mouseX, int mouseY) {
        int tabY = TOOLBAR_HEIGHT;
        context.fill(0, tabY, this.width, tabY + 30, 0xFF2a2a2a);
        
        String[] tabNames = {"Timeline", "Effects", "Media", "Preview"};
        int tabWidth = this.width / tabNames.length;
        
        var textRenderer = this.textRenderer;
        for (int i = 0; i < tabNames.length; i++) {
            int tabX = i * tabWidth;
            boolean isSelected = selectedTab == i;
            int color = isSelected ? 0xFF4a4a4a : 0xFF3a3a3a;
            
            context.fill(tabX, tabY, tabX + tabWidth, tabY + 30, color);
            context.drawBorder(tabX, tabY, tabWidth, 30, 0xFF6a6a6a);
            
            context.drawText(textRenderer, tabNames[i], tabX + 10, tabY + 8, 0xFFFFFF, false);
        }
    }
    
    private void renderPlaybackControls(DrawContext context, int mouseX, int mouseY) {
        int controlsY = this.height - TIMELINE_HEIGHT - 50;
        int controlsX = this.width / 2 - 150;
        
        // Play/Pause button
        int playButtonX = controlsX;
        int playButtonY = controlsY;
        int buttonSize = 30;
        
        String playIcon = isPlaying ? "⏸️" : "▶️";
        drawButton(context, playButtonX, playButtonY, buttonSize, buttonSize, playIcon, mouseX, mouseY);
        
        // Time display
        String timeText = formatTime(currentTime);
        context.drawText(this.textRenderer, timeText, playButtonX + buttonSize + 10, playButtonY + 5, 0xFFFFFF, false);
        
        // Zoom controls
        int zoomX = controlsX + 200;
        drawButton(context, zoomX, playButtonY, 30, buttonSize, "🔍+", mouseX, mouseY);
        drawButton(context, zoomX + 35, playButtonY, 30, buttonSize, "🔍-", mouseX, mouseY);
        context.drawText(this.textRenderer, String.format("%.1fx", zoomLevel), zoomX + 70, playButtonY + 5, 0xFFFFFF, false);
    }
    
    private void renderStatusBar(DrawContext context) {
        int statusY = this.height - 25;
        context.fill(0, statusY, this.width, statusY + 25, 0xFF2a2a2a);
        
        String statusText = isPlaying ? "Playing" : "Ready";
        context.drawText(this.textRenderer, statusText, 10, statusY + 5, 0xFFFFFF, false);
        
        String fpsText = String.format("FPS: %d", MinecraftClient.getInstance().getCurrentFps());
        context.drawText(this.textRenderer, fpsText, this.width - 100, statusY + 5, 0xFFFFFF, false);
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
            int textWidth = this.textRenderer.getWidth(text);
            int textX = x + (width - textWidth) / 2;
            int textY = y + (height - 8) / 2;
            context.drawText(this.textRenderer, text, textX, textY, 0xFFFFFF, false);
        }
    }
    
    private String formatTime(float time) {
        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);
        int frames = (int) ((time % 1) * 30);
        return String.format("%02d:%02d:%02d", minutes, seconds, frames);
    }
}
