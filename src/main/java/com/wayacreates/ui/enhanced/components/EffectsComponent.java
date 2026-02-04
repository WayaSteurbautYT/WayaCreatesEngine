package com.wayacreates.ui.enhanced.components;

import com.wayacreates.ui.UIComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.Color;

/**
 * Effects component for video effects and filters
 * Shows available effects and their parameters
 */
public class EffectsComponent extends UIComponent {
    
    public EffectsComponent(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background
        context.fill(x, y, x + width, y + height, Color.DARK_GRAY.getRGB());
        
        // Draw border
        drawBorder(context, Color.GRAY.getRGB());
        
        // Draw title
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.drawText(textRenderer, Text.literal("Effects").formatted(Formatting.WHITE),
            x + 5, y + 5, Color.WHITE.getRGB(), false);
        
        // Draw placeholder effects
        String[] effects = {
            "Brightness",
            "Contrast",
            "Saturation",
            "Blur",
            "Sharpen",
            "Color Correction"
        };
        
        int yOffset = 25;
        for (String effect : effects) {
            context.drawText(textRenderer, Text.literal(effect).formatted(Formatting.GRAY),
                x + 10, y + yOffset, Color.GRAY.getRGB(), false);
            yOffset += 15;
        }
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }
    
    @Override
    public void tick() {
        // Update effects state
    }
}
