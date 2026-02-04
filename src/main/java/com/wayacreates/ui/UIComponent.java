package com.wayacreates.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * Base class for UI components to reduce code duplication
 * Provides common functionality for bounds management, rendering, and mouse interaction
 * Includes dirty flag system for performance optimization
 */
public abstract class UIComponent {
    protected int x, y, width, height;
    protected boolean isVisible = true;
    protected boolean isDirty = true;
    
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        markDirty();
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        markDirty();
    }
    
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        markDirty();
    }
    
    public boolean isInBounds(double mouseX, double mouseY) {
        return isVisible && 
               mouseX >= x && mouseX <= x + width && 
               mouseY >= y && mouseY <= y + height;
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void setVisible(boolean visible) {
        this.isVisible = visible;
        markDirty();
    }
    
    public boolean isDirty() {
        return isDirty;
    }
    
    public void markDirty() {
        isDirty = true;
    }
    
    public void markClean() {
        isDirty = false;
    }
    
    // Optimized render method that only renders when dirty or forced
    public final void renderOptimized(DrawContext context, int mouseX, int mouseY, float delta, boolean forceRender) {
        if (!isVisible) return;
        
        if (forceRender || isDirty || isMouseOver(mouseX, mouseY)) {
            render(context, mouseX, mouseY, delta);
            markClean();
        }
    }
    
    protected boolean isMouseOver(int mouseX, int mouseY) {
        return isInBounds(mouseX, mouseY);
    }
    
    // Abstract methods that subclasses must implement
    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);
    public abstract boolean mouseClicked(double mouseX, double mouseY, int button);
    public abstract void tick();
    
    // Common utility methods
    protected void drawBackground(DrawContext context, int color) {
        if (isVisible) {
            context.fill(x, y, x + width, y + height, color);
        }
    }
    
    protected void drawBorder(DrawContext context, int color) {
        if (isVisible) {
            context.drawBorder(x, y, width, height, color);
        }
    }
    
    protected void drawText(DrawContext context, String text, int textX, int textY, int color) {
        if (isVisible && text != null) {
            context.drawText(MinecraftClient.getInstance().textRenderer, text, textX, textY, color, false);
        }
    }
    
    protected void drawCenteredText(DrawContext context, String text, int color) {
        if (isVisible && text != null) {
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            int textWidth = textRenderer.getWidth(text);
            int textX = x + (width - textWidth) / 2;
            int textY = y + (height - 8) / 2;
            context.drawText(textRenderer, text, textX, textY, color, false);
        }
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
