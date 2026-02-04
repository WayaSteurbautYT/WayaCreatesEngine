package com.wayacreates.ui.node;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * UI component classes for the Node Compositor
 * Contains NodeEditor, PropertyPanel, NodeLibrary, PreviewPanel, and Camera classes
 */
public class NodeComponents {
    
    public static class NodeEditor {
        private int x, y, width, height;
        private final List<NodeSystem.Node> nodes = new ArrayList<>();
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF1a1a1a);
            
            context.drawText(MinecraftClient.getInstance().textRenderer, "Node Editor", x + 10, y + 10, 0xFFFFFF, false);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds((int) mouseX, (int) mouseY);
        }
        
        public boolean isInBounds(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public NodeSystem.NodePort findPortAt(int mouseX, int mouseY) {
            // TODO: Implement port finding logic when nodeGraph is accessible
            // This would check all nodes and their ports to find one at the given position
            return null;
        }
        
        public void tick() {}
    }
    
    public static class PropertyPanel {
        private int x, y, width, height;
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF2a2a2a);
            context.drawText(MinecraftClient.getInstance().textRenderer, "Properties", x + 10, y + 10, 0xFFFFFF, false);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void tick() {}
    }
    
    public static class NodeLibrary {
        private int x, y, width, height;
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF2a2a2a);
            context.drawText(MinecraftClient.getInstance().textRenderer, "Node Library", x + 10, y + 10, 0xFFFFFF, false);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
    }
    
    public static class PreviewPanel {
        private int x, y, width, height;
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF1a1a1a);
            context.drawText(MinecraftClient.getInstance().textRenderer, "Preview", x + 10, y + 10, 0xFFFFFF, false);
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void tick() {}
    }
    
    public static class Camera {
        private float offsetX = 0.0f;
        private float offsetY = 0.0f;
        
        public void pan(float deltaX, float deltaY) {
            offsetX += deltaX;
            offsetY += deltaY;
        }
        
        public float getOffsetX() { return offsetX; }
        public float getOffsetY() { return offsetY; }
        
        public void reset() {
            offsetX = 0.0f;
            offsetY = 0.0f;
        }
    }
}
