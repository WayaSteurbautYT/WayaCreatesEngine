package com.wayacreates.ui.enhanced.components;

import com.wayacreates.ui.UIComponent;
import com.wayacreates.shader.ShaderManager;
import com.wayacreates.entity.EntityModelManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced UI Component with LDLib-inspired features
 * Provides advanced UI elements with animations, transitions, and modern design
 */
public class EnhancedUIComponent extends UIComponent {
    private final List<UIElement> elements = new ArrayList<>();
    private boolean animated = true;
    private float animationProgress = 0.0f;
    private int hoverTimer = 0;
    private UIElement hoveredElement = null;
    
    public EnhancedUIComponent(int x, int y, int width, int height) {
        super();
        setBounds(x, y, width, height);
        initializeElements();
    }
    
    private void initializeElements() {
        // Status panel
        elements.add(new StatusPanel(10, 10, 200, 120));
        
        // Quick actions panel
        elements.add(new QuickActionsPanel(220, 10, 180, 120));
        
        // Shader controls
        elements.add(new ShaderControlsPanel(10, 140, 200, 100));
        
        // Entity features panel
        elements.add(new EntityFeaturesPanel(220, 140, 180, 100));
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Update animation
        if (animated) {
            animationProgress += delta * 0.02f;
            if (animationProgress > 1.0f) animationProgress = 1.0f;
        }
        
        // Draw background with gradient effect
        drawGradientBackground(context);
        
        // Update hover state
        updateHoverState(mouseX, mouseY);
        
        // Draw all elements
        for (UIElement element : elements) {
            element.render(context, mouseX, mouseY, delta, animationProgress);
        }
        
        // Draw main title
        drawTitle(context);
    }
    
    private void drawGradientBackground(DrawContext context) {
        int alpha = (int) (40 * animationProgress);
        context.fill(x, y, x + width, y + height, 0x2a000000 + (alpha << 24));
        context.drawBorder(x, y, width, height, 0xFF4a4a4a);
    }
    
    private void drawTitle(DrawContext context) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        String title = "WayaCreates Engine - Enhanced UI";
        int titleWidth = textRenderer.getWidth(title);
        int titleX = x + (width - titleWidth) / 2;
        int titleY = y + 5;
        
        // Animated title color
        int color = interpolateColor(0xFF666666, 0xFFAAAAAA, animationProgress);
        context.drawText(textRenderer, title, titleX, titleY, color, false);
    }
    
    private void updateHoverState(int mouseX, int mouseY) {
        hoveredElement = null;
        for (UIElement element : elements) {
            if (element.isHovered(mouseX, mouseY)) {
                hoveredElement = element;
                hoverTimer++;
                break;
            }
        }
        if (hoveredElement == null) {
            hoverTimer = 0;
        }
    }
    
    private int interpolateColor(int color1, int color2, float progress) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;
        
        int r = (int) (r1 + (r2 - r1) * progress);
        int g = (int) (g1 + (g2 - g1) * progress);
        int b = (int) (b1 + (b2 - b1) * progress);
        
        return (r << 16) | (g << 8) | b;
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (UIElement element : elements) {
            if (element.isHovered((int)mouseX, (int)mouseY)) {
                return element.mouseClicked(mouseX, mouseY, button);
            }
        }
        return false;
    }
    
    @Override
    public void tick() {
        for (UIElement element : elements) {
            element.tick();
        }
    }
    
    // Inner classes for UI elements
    public abstract static class UIElement {
        protected int x, y, width, height;
        protected float localAnimation = 0.0f;
        
        public UIElement(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public abstract void render(DrawContext context, int mouseX, int mouseY, float delta, float globalAnimation);
        public abstract boolean mouseClicked(double mouseX, double mouseY, int button);
        public abstract void tick();
        
        public boolean isHovered(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + width &&
                   mouseY >= y && mouseY <= y + height;
        }
    }
    
    public static class StatusPanel extends UIElement {
        public StatusPanel(int x, int y, int width, int height) {
            super(x, y, width, height);
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta, float globalAnimation) {
            // Draw panel background
            int bgColor = 0xFF333333;
            context.fill(x, y, x + width, y + height, bgColor);
            context.drawBorder(x, y, width, height, 0xFF555555);
            
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            int textY = y + 10;
            
            // Draw status information
            context.drawText(textRenderer, "Engine Status", x + 10, textY, 0xFFFFFF, false);
            textY += 15;
            
            context.drawText(textRenderer, "• Shaders: " + (ShaderManager.isShaderSupportEnabled() ? "✅" : "❌"), 
                           x + 10, textY, 0xAAAAAA, false);
            textY += 12;
            
            context.drawText(textRenderer, "• Entity Models: " + (EntityModelManager.isEMFPresent() ? "✅" : "❌"), 
                           x + 10, textY, 0xAAAAAA, false);
            textY += 12;
            
            context.drawText(textRenderer, "• Fresh Animations: " + (EntityModelManager.isFreshAnimationsCompatible() ? "✅" : "❌"), 
                           x + 10, textY, 0xAAAAAA, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return false;
        }
        
        @Override
        public void tick() {
            localAnimation += 0.01f;
            if (localAnimation > 1.0f) localAnimation = 1.0f;
        }
    }
    
    public static class QuickActionsPanel extends UIElement {
        public QuickActionsPanel(int x, int y, int width, int height) {
            super(x, y, width, height);
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta, float globalAnimation) {
            // Draw panel background
            int bgColor = isHovered(mouseX, mouseY) ? 0xFF3a3a3a : 0xFF333333;
            context.fill(x, y, x + width, y + height, bgColor);
            context.drawBorder(x, y, width, height, 0xFF555555);
            
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Quick Actions", x + 10, y + 10, 0xFFFFFF, false);
            
            // Draw action buttons
            String[] actions = {"Reload All", "Reset UI", "Toggle Animations"};
            for (int i = 0; i < actions.length; i++) {
                int buttonY = y + 30 + i * 25;
                boolean hovered = mouseX >= x + 10 && mouseX <= x + width - 10 &&
                                 mouseY >= buttonY && mouseY <= buttonY + 20;
                
                int buttonColor = hovered ? 0xFF5a5a5a : 0xFF4a4a4a;
                context.fill(x + 10, buttonY, x + width - 10, buttonY + 20, buttonColor);
                
                context.drawText(textRenderer, actions[i], x + 15, buttonY + 6, 0xFFFFFF, false);
            }
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0 && isHovered((int)mouseX, (int)mouseY)) {
                // Handle quick action clicks
                int relativeY = (int)mouseY - y;
                if (relativeY >= 30 && relativeY <= 50) {
                    // Reload All
                    ShaderManager.reloadShaders();
                    EntityModelManager.reloadEntityFeatures();
                } else if (relativeY >= 55 && relativeY <= 75) {
                    // Reset UI (placeholder)
                    MinecraftClient.getInstance().player.sendMessage(
                        Text.literal("🔄 UI Reset").formatted(Formatting.YELLOW), true);
                } else if (relativeY >= 80 && relativeY <= 100) {
                    // Toggle Animations (placeholder)
                    MinecraftClient.getInstance().player.sendMessage(
                        Text.literal("🎬 Animations toggled").formatted(Formatting.GREEN), true);
                }
                return true;
            }
            return false;
        }
        
        @Override
        public void tick() {
            localAnimation += 0.02f;
            if (localAnimation > 1.0f) localAnimation = 1.0f;
        }
    }
    
    public static class ShaderControlsPanel extends UIElement {
        public ShaderControlsPanel(int x, int y, int width, int height) {
            super(x, y, width, height);
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta, float globalAnimation) {
            context.fill(x, y, x + width, y + height, 0xFF333333);
            context.drawBorder(x, y, width, height, 0xFF555555);
            
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Shader Controls", x + 10, y + 10, 0xFFFFFF, false);
            
            String currentShader = ShaderManager.getCurrentShaderPack();
            context.drawText(textRenderer, "Current: " + currentShader, x + 10, y + 30, 0xAAAAAA, false);
            
            // Reload button
            boolean reloadHovered = mouseX >= x + 10 && mouseX <= x + 80 &&
                                   mouseY >= y + 50 && mouseY <= y + 70;
            int reloadColor = reloadHovered ? 0xFF5a5a5a : 0xFF4a4a4a;
            context.fill(x + 10, y + 50, x + 80, y + 70, reloadColor);
            context.drawText(textRenderer, "Reload", x + 25, y + 56, 0xFFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0 && mouseX >= x + 10 && mouseX <= x + 80 &&
                mouseY >= y + 50 && mouseY <= y + 70) {
                ShaderManager.reloadShaders();
                return true;
            }
            return false;
        }
        
        @Override
        public void tick() {
            localAnimation += 0.015f;
            if (localAnimation > 1.0f) localAnimation = 1.0f;
        }
    }
    
    public static class EntityFeaturesPanel extends UIElement {
        public EntityFeaturesPanel(int x, int y, int width, int height) {
            super(x, y, width, height);
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta, float globalAnimation) {
            context.fill(x, y, x + width, y + height, 0xFF333333);
            context.drawBorder(x, y, width, height, 0xFF555555);
            
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Entity Features", x + 10, y + 10, 0xFFFFFF, false);
            
            context.drawText(textRenderer, "EMF: " + (EntityModelManager.isEMFPresent() ? "✅" : "❌"), 
                           x + 10, y + 30, 0xAAAAAA, false);
            context.drawText(textRenderer, "ETF: " + (EntityModelManager.isETFPresent() ? "✅" : "❌"), 
                           x + 10, y + 45, 0xAAAAAA, false);
            
            // Reload button
            boolean reloadHovered = mouseX >= x + 10 && mouseX <= x + 80 &&
                                   mouseY >= y + 65 && mouseY <= y + 85;
            int reloadColor = reloadHovered ? 0xFF5a5a5a : 0xFF4a4a4a;
            context.fill(x + 10, y + 65, x + 80, y + 85, reloadColor);
            context.drawText(textRenderer, "Reload", x + 25, y + 71, 0xFFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0 && mouseX >= x + 10 && mouseX <= x + 80 &&
                mouseY >= y + 65 && mouseY <= y + 85) {
                EntityModelManager.reloadEntityFeatures();
                return true;
            }
            return false;
        }
        
        @Override
        public void tick() {
            localAnimation += 0.015f;
            if (localAnimation > 1.0f) localAnimation = 1.0f;
        }
    }
}
