package com.wayacreates.ui.enhanced.components;

import java.awt.Color;

import com.wayacreates.ui.UIComponent;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Viewport component for video preview
 * Shows the current video frame with zoom and pan controls
 */
public class ViewportComponent extends UIComponent {
    private float zoom = 1.0f;
    private int panX = 0;
    private int panY = 0;
    private boolean isDragging = false;
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private boolean showGrid = false;
    private boolean showSafeArea = true;
    
    public ViewportComponent(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background
        context.fill(x, y, x + width, y + height, Color.DARK_GRAY.getRGB());
        
        // Draw border
        drawBorder(context, Color.GRAY.getRGB());
        
        // Draw video area (placeholder)
        int videoWidth = (int) (width * 0.8f * zoom);
        int videoHeight = (int) (height * 0.8f * zoom);
        int videoX = x + (width - videoWidth) / 2 + panX;
        int videoY = y + (height - videoHeight) / 2 + panY;
        
        // Draw video background
        context.fill(videoX, videoY, videoX + videoWidth, videoY + videoHeight, Color.BLUE.getRGB());
        
        // Draw grid if enabled
        if (showGrid) {
            drawGrid(context, videoX, videoY, videoWidth, videoHeight);
        }
        
        // Draw safe area if enabled
        if (showSafeArea) {
            drawSafeArea(context, videoX, videoY, videoWidth, videoHeight);
        }
        
        // Draw placeholder text
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        String placeholder = "Video Preview";
        int textWidth = textRenderer.getWidth(placeholder);
        context.drawText(textRenderer, Text.literal(placeholder).formatted(Formatting.WHITE),
            videoX + (videoWidth - textWidth) / 2, videoY + videoHeight / 2 - 5, 
            Color.WHITE.getRGB(), false);
        
        // Draw zoom indicator
        String zoomText = String.format("Zoom: %.1f%%", zoom * 100);
        context.drawText(textRenderer, Text.literal(zoomText).formatted(Formatting.GRAY),
            x + 5, y + 5, Color.GRAY.getRGB(), false);
        
        // Draw dimensions
        String dimText = String.format("%dx%d", videoWidth, videoHeight);
        context.drawText(textRenderer, Text.literal(dimText).formatted(Formatting.GRAY),
            x + width - 60, y + 5, Color.GRAY.getRGB(), false);
    }
    
    private void drawGrid(DrawContext context, int videoX, int videoY, int videoWidth, int videoHeight) {
        int gridSize = 50;
        
        // Draw vertical lines
        for (int gx = videoX; gx < videoX + videoWidth; gx += gridSize) {
            context.fill(gx, videoY, gx + 1, videoY + videoHeight, Color.GRAY.getRGB());
        }
        
        // Draw horizontal lines
        for (int gy = videoY; gy < videoY + videoHeight; gy += gridSize) {
            context.fill(videoX, gy, videoX + videoWidth, gy + 1, Color.GRAY.getRGB());
        }
    }
    
    private void drawSafeArea(DrawContext context, int videoX, int videoY, int videoWidth, int videoHeight) {
        // Draw action safe area (90%)
        int actionSafeX = videoX + (int) (videoWidth * 0.05);
        int actionSafeY = videoY + (int) (videoHeight * 0.05);
        int actionSafeWidth = (int) (videoWidth * 0.9);
        int actionSafeHeight = (int) (videoHeight * 0.9);
        
        context.drawBorder(actionSafeX, actionSafeY, actionSafeWidth, actionSafeHeight, Color.YELLOW.getRGB());
        
        // Draw title safe area (80%)
        int titleSafeX = videoX + (int) (videoWidth * 0.1);
        int titleSafeY = videoY + (int) (videoHeight * 0.1);
        int titleSafeWidth = (int) (videoWidth * 0.8);
        int titleSafeHeight = (int) (videoHeight * 0.8);
        
        context.drawBorder(titleSafeX, titleSafeY, titleSafeWidth, titleSafeHeight, Color.GREEN.getRGB());
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isInBounds(mouseX, mouseY)) {
            if (button == 0) { // Left click
                isDragging = true;
                lastMouseX = (int) mouseX;
                lastMouseY = (int) mouseY;
                return true;
            } else if (button == 1) { // Right click
                // Reset zoom and pan
                zoom = 1.0f;
                panX = 0;
                panY = 0;
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void tick() {
        // Update viewport state
    }
    
    // Getters and setters
    public float getZoom() { return zoom; }
    public void setZoom(float zoom) { this.zoom = zoom; }
    
    public int getPanX() { return panX; }
    public void setPanX(int panX) { this.panX = panX; }
    
    public int getPanY() { return panY; }
    public void setPanY(int panY) { this.panY = panY; }
    
    public boolean isShowGrid() { return showGrid; }
    public void setShowGrid(boolean showGrid) { this.showGrid = showGrid; }
    
    public boolean isShowSafeArea() { return showSafeArea; }
    public void setShowSafeArea(boolean showSafeArea) { this.showSafeArea = showSafeArea; }
}
