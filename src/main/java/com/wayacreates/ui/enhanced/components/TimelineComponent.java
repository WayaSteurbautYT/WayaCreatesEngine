package com.wayacreates.ui.enhanced.components;

import java.awt.Color;

import com.wayacreates.ui.UIComponent;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Timeline component for video editing
 * Shows video timeline with keyframes and markers
 */
public class TimelineComponent extends UIComponent {
    private int currentTime = 0;
    private int totalTime = 10000; // 10 seconds in milliseconds
    private boolean isDragging = false;
    private boolean showKeyframes = true;
    private boolean showMarkers = true;
    
    public TimelineComponent(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background
        context.fill(x, y, x + width, y + height, Color.DARK_GRAY.getRGB());
        
        // Draw border
        drawBorder(context, Color.GRAY.getRGB());
        
        // Draw timeline track
        int trackY = y + height / 2 - 2;
        context.fill(x + 10, trackY, x + width - 10, trackY + 4, Color.BLUE.getRGB());
        
        // Draw current time indicator
        int timeX = x + 10 + (int) ((currentTime * (width - 20)) / totalTime);
        context.fill(timeX - 1, trackY - 10, timeX + 1, trackY + 14, Color.RED.getRGB());
        
        // Draw time markers
        drawTimeMarkers(context);
        
        // Draw keyframes if enabled
        if (showKeyframes) {
            drawKeyframes(context);
        }
        
        // Draw markers if enabled
        if (showMarkers) {
            drawMarkers(context);
        }
        
        // Draw current time text
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        String timeText = formatTime(currentTime) + " / " + formatTime(totalTime);
        context.drawText(textRenderer, Text.literal(timeText).formatted(Formatting.WHITE),
            x + 5, y + 5, Color.WHITE.getRGB(), false);
    }
    
    private void drawTimeMarkers(DrawContext context) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        
        // Draw time markers every second
        for (int i = 0; i <= totalTime; i += 1000) {
            int markerX = x + 10 + (int) ((i * (width - 20)) / totalTime);
            
            // Draw tick mark
            context.fill(markerX, y + height - 15, markerX, y + height - 10, Color.GRAY.getRGB());
            
            // Draw time label
            if (i % 5000 == 0) { // Every 5 seconds
                String label = formatTime(i);
                context.drawText(textRenderer, Text.literal(label).formatted(Formatting.GRAY),
                    markerX - 15, y + height - 25, Color.GRAY.getRGB(), false);
            }
        }
    }
    
    private void drawKeyframes(DrawContext context) {
        // Draw some example keyframes
        int[] keyframeTimes = {1000, 3000, 5000, 7000, 9000};
        
        for (int time : keyframeTimes) {
            if (time <= totalTime) {
                int keyframeX = x + 10 + (int) ((time * (width - 20)) / totalTime);
                context.fill(keyframeX - 2, y + height / 2 - 8, keyframeX + 2, y + height / 2 - 4,
                    Color.YELLOW.getRGB());
            }
        }
    }
    
    private void drawMarkers(DrawContext context) {
        // Draw some example markers
        int[] markerTimes = {2000, 6000, 8000};
        
        for (int time : markerTimes) {
            if (time <= totalTime) {
                int markerX = x + 10 + (int) ((time * (width - 20)) / totalTime);
                context.fill(markerX - 1, y + 5, markerX + 1, y + height / 2 - 10,
                    Color.GREEN.getRGB());
            }
        }
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isInBounds(mouseX, mouseY)) {
            if (button == 0) { // Left click
                isDragging = true;
                updateTimeFromMouse((int) mouseX);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void tick() {
        // Update timeline state
    }
    
    private void updateTimeFromMouse(int mouseX) {
        int relativeX = mouseX - x - 10;
        if (relativeX >= 0 && relativeX <= width - 20) {
            currentTime = (relativeX * totalTime) / (width - 20);
            currentTime = Math.max(0, Math.min(totalTime, currentTime));
        }
    }
    
    private String formatTime(int milliseconds) {
        int seconds = milliseconds / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    // Getters and setters
    public int getCurrentTime() { return currentTime; }
    public void setCurrentTime(int currentTime) { this.currentTime = currentTime; }
    
    public int getTotalTime() { return totalTime; }
    public void setTotalTime(int totalTime) { this.totalTime = totalTime; }
    
    public boolean isShowKeyframes() { return showKeyframes; }
    public void setShowKeyframes(boolean showKeyframes) { this.showKeyframes = showKeyframes; }
    
    public boolean isShowMarkers() { return showMarkers; }
    public void setShowMarkers(boolean showMarkers) { this.showMarkers = showMarkers; }
}
