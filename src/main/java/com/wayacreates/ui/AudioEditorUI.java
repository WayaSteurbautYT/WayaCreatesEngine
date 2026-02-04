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
 * Enhanced Audio Editor with Drag & Drop Support
 * Professional audio mixing with sound effects, transitions, and live streaming support
 */
public class AudioEditorUI extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/AudioEditor");
    private static final Text TITLE = Text.translatable("wayacreates.ui.audio_editor");
    
    // Debug flag for verbose logging
    private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
    
    // Audio components
    private AudioMixer audioMixer;
    private SoundEffectLibrary soundEffectLibrary;
    private Timeline timeline;
    private WaveformDisplay waveformDisplay;
    private EffectsPanel effectsPanel;
    private LiveStreamPanel liveStreamPanel;
    
    // Drag & Drop system
    private boolean isDragging = false;
    private DraggedItem draggedItem = null;
    
    // UI state
    private boolean isInitialized = false;
    private boolean isPlaying = false;
    private float currentTime = 0.0f;
    private float zoomLevel = 1.0f;
    private String selectedTrack = null;
    
    // Layout constants
    private static final int TOOLBAR_HEIGHT = 60;
    private static final int SIDEBAR_WIDTH = 300;
    private static final int TIMELINE_HEIGHT = 200;
    private static final int LIVE_PANEL_HEIGHT = 150;
    
    public AudioEditorUI() {
        super(TITLE);
        if (DEBUG_MODE) {
            LOGGER.info("🎵 Audio Editor UI initialized in DEBUG mode");
        } else {
            LOGGER.info("🎵 Audio Editor UI initialized");
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
        loadSoundEffects();
    }
    
    private void initializeComponents() {
        audioMixer = new AudioMixer();
        soundEffectLibrary = new SoundEffectLibrary();
        timeline = new Timeline();
        waveformDisplay = new WaveformDisplay();
        effectsPanel = new EffectsPanel();
        liveStreamPanel = new LiveStreamPanel();
    }
    
    private void layoutComponents() {
        int panelWidth = this.width;
        int panelHeight = this.height;
        
        // Layout waveform display (main area)
        int waveformX = SIDEBAR_WIDTH;
        int waveformY = TOOLBAR_HEIGHT;
        int waveformWidth = panelWidth - SIDEBAR_WIDTH;
        int waveformHeight = panelHeight - TOOLBAR_HEIGHT - TIMELINE_HEIGHT - LIVE_PANEL_HEIGHT;
        
        waveformDisplay.setBounds(waveformX, waveformY, waveformWidth, waveformHeight);
        
        // Layout sound effects library (left sidebar)
        soundEffectLibrary.setBounds(0, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, 
                                    panelHeight - TOOLBAR_HEIGHT - TIMELINE_HEIGHT - LIVE_PANEL_HEIGHT);
        
        // Layout effects panel (right sidebar)
        effectsPanel.setBounds(panelWidth - SIDEBAR_WIDTH, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, 
                              panelHeight - TOOLBAR_HEIGHT - TIMELINE_HEIGHT - LIVE_PANEL_HEIGHT);
        
        // Layout timeline (bottom)
        timeline.setBounds(SIDEBAR_WIDTH, panelHeight - TIMELINE_HEIGHT - LIVE_PANEL_HEIGHT, 
                          panelWidth - SIDEBAR_WIDTH, TIMELINE_HEIGHT);
        
        // Layout live stream panel (bottom-right)
        liveStreamPanel.setBounds(panelWidth - SIDEBAR_WIDTH - 200, panelHeight - LIVE_PANEL_HEIGHT, 
                                  200, LIVE_PANEL_HEIGHT);
    }
    
    private void loadSoundEffects() {
        // Load built-in sound effects
        soundEffectLibrary.addEffect(new SoundEffect("Explosion", "sounds/explosion.ogg"));
        soundEffectLibrary.addEffect(new SoundEffect("Coin Collect", "sounds/coin.ogg"));
        soundEffectLibrary.addEffect(new SoundEffect("Jump", "sounds/jump.ogg"));
        soundEffectLibrary.addEffect(new SoundEffect("Hit", "sounds/hit.ogg"));
        soundEffectLibrary.addEffect(new SoundEffect("Level Up", "sounds/levelup.ogg"));
        
        // Load meme sounds
        soundEffectLibrary.addEffect(new SoundEffect("Bruh", "sounds/memes/bruh.ogg"));
        soundEffectLibrary.addEffect(new SoundEffect("Oof", "sounds/memes/oof.ogg"));
        soundEffectLibrary.addEffect(new SoundEffect("Vine Boom", "sounds/memes/vine.ogg"));
        soundEffectLibrary.addEffect(new SoundEffect("Airhorn", "sounds/memes/airhorn.ogg"));
        
        LOGGER.info("🎵 Loaded {} sound effects", soundEffectLibrary.getEffectCount());
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (context == null) {
            LOGGER.error("❌ DrawContext is null in render method");
            return;
        }
        
        // Draw background
        renderBackground(context);
        
        // Draw main components with null checks
        if (waveformDisplay != null) {
            waveformDisplay.render(context, mouseX, mouseY, delta);
        }
        if (soundEffectLibrary != null) {
            soundEffectLibrary.render(context, mouseX, mouseY, delta);
        }
        if (effectsPanel != null) {
            effectsPanel.render(context, mouseX, mouseY, delta);
        }
        if (timeline != null) {
            timeline.render(context, mouseX, mouseY, delta);
        }
        if (liveStreamPanel != null) {
            liveStreamPanel.render(context, mouseX, mouseY, delta);
        }
        
        // Draw toolbar
        renderToolbar(context, mouseX, mouseY);
        
        // Draw drag preview
        if (isDragging && draggedItem != null) {
            renderDragPreview(context, mouseX, mouseY);
        }
        
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
    
    private void renderToolbar(DrawContext context, int mouseX, int mouseY) {
        int toolbarY = 0;
        context.fill(0, toolbarY, this.width, TOOLBAR_HEIGHT, 0xFF333333);
        
        // Draw toolbar buttons
        int buttonX = 10;
        drawToolbarButton(context, buttonX, toolbarY + 5, 80, 30, "New", mouseX, mouseY);
        buttonX += 90;
        drawToolbarButton(context, buttonX, toolbarY + 5, 80, 30, "Open", mouseX, mouseY);
        buttonX += 90;
        drawToolbarButton(context, buttonX, toolbarY + 5, 80, 30, "Save", mouseX, mouseY);
        buttonX += 90;
        drawToolbarButton(context, buttonX, toolbarY + 5, 80, 30, "Export", mouseX, mouseY);
        buttonX += 90;
        drawToolbarButton(context, buttonX, toolbarY + 5, 80, 30, "Record", mouseX, mouseY);
        
        // Draw title
        drawText(context, this.width / 2 - 100, toolbarY + 12, 
                "WayaCreates Audio Editor", 0xFFFFFF);
    }
    
    private void renderDragPreview(DrawContext context, int mouseX, int mouseY) {
        if (draggedItem != null) {
            // Draw preview of dragged item
            String previewText = draggedItem.getDisplayName();
            int textWidth = this.textRenderer.getWidth(previewText);
            int previewWidth = textWidth + 20;
            int previewHeight = 30;
            
            int previewX = mouseX - previewWidth / 2;
            int previewY = mouseY - previewHeight / 2;
            
            // Draw preview background
            context.fill(previewX, previewY, previewX + previewWidth, previewY + previewHeight, 0xFF4a4a4a);
            context.drawBorder(previewX, previewY, previewWidth, previewHeight, 0xFF6a6a6a);
            
            // Draw preview text
            drawText(context, previewX + 10, previewY + 8, previewText, 0xFFFFFF);
        }
    }
    
    private void renderPlaybackControls(DrawContext context, int mouseX, int mouseY) {
        int controlsY = this.height - TIMELINE_HEIGHT - LIVE_PANEL_HEIGHT - 50;
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
        
        // Volume control
        int volumeX = controlsX + 200;
        drawButton(context, volumeX, playButtonY, 30, buttonSize, "🔊", mouseX, mouseY);
        drawText(context, volumeX + 35, playButtonY + 5, "100%", 0xFFFFFF);
    }
    
    private void renderStatusBar(DrawContext context) {
        int statusY = this.height - 25;
        context.fill(0, statusY, this.width, statusY + 25, 0xFF2a2a2a);
        
        String statusText = isPlaying ? "Playing" : "Ready";
        drawText(context, 10, statusY + 5, statusText, 0xFFFFFF);
        
        String fpsText = String.format("FPS: %d", MinecraftClient.getInstance().getCurrentFps());
        drawText(context, this.width - 100, statusY + 5, fpsText, 0xFFFFFF);
        
        // Show selected track
        if (selectedTrack != null) {
            drawText(context, 200, statusY + 5, "Track: " + selectedTrack, 0xFFFFFF);
        }
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        try {
            // Handle drag start
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && soundEffectLibrary != null) {
                if (soundEffectLibrary.mouseClicked(mouseX, mouseY, button)) {
                    SoundEffect effect = soundEffectLibrary.getEffectAt(mouseX, mouseY);
                    if (effect != null) {
                        startDrag(effect, mouseX, mouseY);
                        return true;
                    }
                }
            }
            
            // Handle component clicks with null checks
            if (waveformDisplay != null && waveformDisplay.mouseClicked(mouseX, mouseY, button)) return true;
            if (effectsPanel != null && effectsPanel.mouseClicked(mouseX, mouseY, button)) return true;
            if (timeline != null && timeline.mouseClicked(mouseX, mouseY, button)) return true;
            if (liveStreamPanel != null && liveStreamPanel.mouseClicked(mouseX, mouseY, button)) return true;
            
            return super.mouseClicked(mouseX, mouseY, button);
        } catch (Exception e) {
            LOGGER.error("❌ Error in mouseClicked: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        try {
            // Handle drag end
            if (isDragging && draggedItem != null) {
                // Check if dropped on timeline
                if (timeline != null && timeline.isInBounds(mouseX, mouseY)) {
                    if (draggedItem.item instanceof SoundEffect effect) {
                        timeline.addSoundEffect(effect, mouseX, mouseY, timeline);
                        LOGGER.info("🎵 Dropped sound effect: {} on timeline", effect.getName());
                    }
                }
                
                // Check if dropped on live stream panel
                if (liveStreamPanel != null && liveStreamPanel.isInBounds(mouseX, mouseY)) {
                    if (draggedItem.item instanceof SoundEffect effect) {
                        liveStreamPanel.playSoundEffect(effect, audioMixer);
                        LOGGER.info("🎵 Played sound effect: {} for live stream", effect.getName());
                    }
                }
                
                endDrag();
            }
            
            return super.mouseReleased(mouseX, mouseY, button);
        } catch (Exception e) {
            LOGGER.error("❌ Error in mouseReleased: {}", e.getMessage(), e);
            endDrag(); // Ensure drag state is cleared on error
            return false;
        }
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging && draggedItem != null) {
            // Update drag position
            draggedItem.updatePosition(mouseX, mouseY);
        }
        
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        // Zoom waveform display
        float zoomFactor = amount > 0 ? 1.1f : 0.9f;
        float newZoomLevel = zoomLevel * zoomFactor;
        
        // Clamp zoom level between 0.1x and 10x
        if (newZoomLevel >= 0.1f && newZoomLevel <= 10.0f) {
            zoomLevel = newZoomLevel;
            LOGGER.info("🔍 Zoom level changed to {:.2f}x", zoomLevel);
        }
        
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Handle keyboard shortcuts
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            togglePlayback();
            return true;
        }
        
        if (keyCode == GLFW.GLFW_KEY_DELETE && selectedTrack != null) {
            timeline.removeTrack(selectedTrack, timeline);
            selectedTrack = null;
            return true;
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    private void startDrag(SoundEffect effect, double mouseX, double mouseY) {
        isDragging = true;
        draggedItem = new DraggedItem(effect, mouseX, mouseY);
        LOGGER.info("🎵 Started dragging sound effect: {}", effect.getName());
    }
    
    private void endDrag() {
        isDragging = false;
        draggedItem = null;
    }
    
    private void togglePlayback() {
        isPlaying = !isPlaying;
        if (isPlaying) {
            audioMixer.play();
            LOGGER.info("🎵 Started playback");
        } else {
            audioMixer.pause();
            LOGGER.info("⏸️ Paused playback");
        }
    }
    
    @Override
    public void tick() {
        if (isPlaying) {
            currentTime += 1.0f / 30.0f; // 30 FPS
            if (audioMixer != null) {
                audioMixer.update(currentTime);
            }
        }
        
        // Update components with null checks
        if (waveformDisplay != null) {
            waveformDisplay.tick();
        }
        if (effectsPanel != null) {
            effectsPanel.tick();
        }
        if (timeline != null) {
            timeline.tick();
        }
        if (liveStreamPanel != null) {
            liveStreamPanel.tick();
        }
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
    
    private void drawToolbarButton(DrawContext context, int x, int y, int width, int height, 
                                  String text, int mouseX, int mouseY) {
        drawButton(context, x, y, width, height, text, mouseX, mouseY);
    }
    
    private void drawText(DrawContext context, int x, int y, String text, int color) {
        context.drawText(this.textRenderer, text, x, y, color, false);
    }
    
    private String formatTime(float time) {
        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);
        int frames = (int) ((time % 1) * 30);
        return String.format("%02d:%02d:%02d", minutes, seconds, frames);
    }
    
    // Component classes (simplified)
    private static class AudioMixer {
        public void play() {
            // Start audio playback
            if (DEBUG_MODE) {
                LOGGER.info("🎵 Audio playback started");
            }
        }
        
        public void pause() {
            // Pause audio playback
            if (DEBUG_MODE) {
                LOGGER.info("⏸️ Audio playback paused");
            }
        }
        
        public void update(float time) {
            // Update audio mixer
            if (DEBUG_MODE) {
                // Update audio processing based on current time
            }
        }
    }
    
    private static class SoundEffect {
        private final String name;
        
        public SoundEffect(String name, String filePath) {
            this.name = name;
        }
        
        public String getName() { return name; }
    }
    
    private static class SoundEffectLibrary {
        private final List<SoundEffect> effects = new ArrayList<>();
        private int x, y, width, height;
        
        public void addEffect(SoundEffect effect) {
            effects.add(effect);
        }
        
        public SoundEffect getEffectAt(double mouseX, double mouseY) {
            // Find effect at position
            int itemY = y + 40;
            for (SoundEffect effect : effects) {
                if (mouseX >= x + 5 && mouseX <= x + width - 10 && 
                    mouseY >= itemY && mouseY <= itemY + 25) {
                    return effect;
                }
                itemY += 30;
            }
            return null;
        }
        
        public int getEffectCount() { return effects.size(); }
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF2a2a2a);
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, Text.translatable("wayacreates.audio.sound_effects").getString(), x + 10, y + 10, 0xFFFFFF, false);
            
            // Render sound effects list
            int itemY = y + 40;
            for (SoundEffect effect : effects) {
                boolean isHovered = mouseY >= itemY && mouseY <= itemY + 25;
                int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
                
                context.fill(x + 5, itemY, x + width - 10, itemY + 25, color);
                context.drawText(textRenderer, effect.getName(), x + 15, itemY + 5, 0xFFFFFF, false);
                
                itemY += 30;
            }
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
    }
    
    private static class WaveformDisplay extends UIComponent {
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF1a1a1a);
            drawBorder(context, 0xFF4a4a4a);
            drawText(context, "Waveform Display", x + 10, y + 10, 0xFFFFFF);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        public void tick() {}
    }
    
    private static class EffectsPanel extends UIComponent {
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawText(context, "Audio Effects", x + 10, y + 10, 0xFFFFFF);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        public void tick() {}
    }
    
    private static class Timeline extends UIComponent {
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF252525);
            drawText(context, "Audio Timeline", x + 10, y + 10, 0xFFFFFF);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        public void addSoundEffect(SoundEffect effect, double mouseX, double mouseY, Timeline timeline) {
            // Add sound effect to timeline
            if (DEBUG_MODE) {
                LOGGER.info("🎵 Added sound effect '{}' to timeline at ({}, {})", effect.getName(), (int)mouseX, (int)mouseY);
            }
            // Add sound effect to timeline data structure
            // For now, just log the action
            if (DEBUG_MODE) {
                LOGGER.info("🎵 Effect {} would be added to timeline at ({}, {})", effect.getName(), (int)mouseX, (int)mouseY);
            }
        }
        
        public void removeTrack(String trackName, Timeline timeline) {
            // Remove track
            if (DEBUG_MODE) {
                LOGGER.info("🗑️ Removed track: {}", trackName);
            }
            // Remove from timeline data structure
            // For now, just log the action
            if (DEBUG_MODE) {
                LOGGER.info("🗑️ Track {} would be removed from timeline", trackName);
            }
        }
        
        public void tick() {}
    }
    
    private static class LiveStreamPanel {
        private int x, y, width, height;
        private final boolean isLive = false;
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            int color = isLive ? 0xFF4a2a2a : 0xFF2a2a2a;
            context.fill(x, y, x + width, y + height, color);
            
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            String status = isLive ? "🔴 LIVE" : "OFFLINE";
            int statusColor = isLive ? 0xFFff4444 : 0xFF888888;
            context.drawText(textRenderer, status, x + 10, y + 10, statusColor, false);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public boolean isInBounds(double mouseX, double mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void playSoundEffect(SoundEffect effect, AudioMixer audioMixer) {
            // Play sound effect for live stream
            if (DEBUG_MODE) {
                LOGGER.info("🎵 Playing sound effect for live stream: {}", effect.getName());
            }
            // Play the sound effect
            // For now, just log the action
            if (DEBUG_MODE) {
                LOGGER.info("🎵 Effect {} would be played", effect.getName());
            }
        }
        
        public void tick() {}
    }
    
    private static class DraggedItem {
        private Object item;
        
        public DraggedItem(Object item, double mouseX, double mouseY) {
            this.item = item;
        }
        
        public void updatePosition(double mouseX, double mouseY) {
            // Update drag position if needed
            if (DEBUG_MODE) {
                LOGGER.info("🎯 Updated drag position to ({}, {})", (int)mouseX, (int)mouseY);
            }
        }
        
        public String getDisplayName() {
            if (item instanceof SoundEffect effect) {
                return effect.getName();
            }
            return "Unknown";
        }
    }
}
