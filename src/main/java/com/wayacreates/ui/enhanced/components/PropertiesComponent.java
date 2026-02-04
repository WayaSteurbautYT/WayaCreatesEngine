package com.wayacreates.ui.enhanced.components;

import com.wayacreates.ui.UIComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.Color;

/**
 * Properties component for displaying and editing properties
 * Shows video properties, settings, and metadata
 */
public class PropertiesComponent extends UIComponent {
    
    public PropertiesComponent(int x, int y, int width, int height) {
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
        context.drawText(textRenderer, Text.literal("Properties").formatted(Formatting.WHITE),
            x + 5, y + 5, Color.WHITE.getRGB(), false);
        
        // Draw placeholder properties
        String[] properties = {
            "Resolution: 1920x1080",
            "Frame Rate: 30 fps",
            "Duration: 00:10:00",
            "Codec: H.264",
            "Bitrate: 5 Mbps"
        };
        
        int yOffset = 25;
        for (String property : properties) {
            context.drawText(textRenderer, Text.literal(property).formatted(Formatting.GRAY),
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
        // Update properties state
    }
}
