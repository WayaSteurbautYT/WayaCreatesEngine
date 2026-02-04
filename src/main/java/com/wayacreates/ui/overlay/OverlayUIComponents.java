package com.wayacreates.ui.overlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import com.wayacreates.ui.UIComponent;

/**
 * Overlay UI - Component Classes
 * Part 3 of 3 - All UI component implementations
 */
public class OverlayUIComponents {
    
    // Component classes
    public static class OverlayManager {
        private final Map<String, Overlay> overlays = new ConcurrentHashMap<>();
        private int x, y, width, height;
        
        public void addOverlay(Overlay overlay) {
            overlays.put(overlay.getId(), overlay);
        }
        
        public void removeOverlay(String overlayId) {
            overlays.remove(overlayId);
        }
        
        public Overlay getOverlay(String overlayId) {
            return overlays.get(overlayId);
        }
        
        public Overlay getOverlayAt(double mouseX, double mouseY) {
            for (Overlay overlay : overlays.values()) {
                if (overlay.isPointInside(mouseX, mouseY)) {
                    return overlay;
                }
            }
            return null;
        }
        
        public void updateOverlay(DraggableOverlay draggedOverlay) {
            Overlay overlay = overlays.get(draggedOverlay.getId());
            if (overlay != null) {
                overlay.setPosition(draggedOverlay.getIntX(), draggedOverlay.getIntY());
            }
        }
        
        public int getOverlayCount() { return overlays.size(); }
        
        public void setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xFF1a1a1a);
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Overlay Editor", x + 10, y + 10, 0xFFFFFF, false);
            
            // Render overlays
            for (Overlay overlay : overlays.values()) {
                if (overlay.isEnabled()) {
                    overlay.render(context, mouseX, mouseY, delta);
                }
            }
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        public void tick() {}
    }
    
    public static class Overlay {
        private final String id;
        private final String name;
        private int x, y;
        private boolean enabled;
        
        public Overlay(String id, String name, int x, int y, boolean enabled) {
            this.id = id;
            this.name = name;
            this.x = x;
            this.y = y;
            this.enabled = enabled;
        }
        
        public boolean isPointInside(double mouseX, double mouseY) {
            return mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 30;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            boolean isHovered = isPointInside(mouseX, mouseY);
            int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
            
            context.fill(x, y, x + 100, y + 30, color);
            context.drawBorder(x, y, 100, 30, 0xFF6a6a6a);
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, name, x + 5, y + 8, 0xFFFFFF, false);
        }
        
        // Getters and setters
        public String getId() { return id; }
        public String getName() { return name; }
        public boolean isEnabled() { return enabled; }
        
        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
    
    public static class DraggableOverlay {
        private final String id;
        private final String name;
        private double x, y;
        
        public DraggableOverlay(Overlay overlay, double x, double y) {
            this.id = overlay.getId();
            this.name = overlay.getName();
            this.x = x;
            this.y = y;
        }
        
        public void updatePosition(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public int getIntX() { return (int) x; }
        public int getIntY() { return (int) y; }
    }
    
    public static class PlayerTagManager extends UIComponent {
        private final boolean showPlayerTags = true;
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawText(context, "Player Tags", x + 10, y + 10, 0xFFFFFF);
            
            // Draw toggle
            String toggleText = showPlayerTags ? "ON" : "OFF";
            int toggleColor = showPlayerTags ? 0xFF4a7a4a : 0xFF7a4a4a;
            drawText(context, toggleText, x + 10, y + 30, toggleColor);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class ParticleManager extends UIComponent {
        private final boolean showParticles = true;
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawText(context, "Particles", x + 10, y + 10, 0xFFFFFF);
            
            // Draw toggle
            String toggleText = showParticles ? "ON" : "OFF";
            int toggleColor = showParticles ? 0xFF4a7a4a : 0xFF7a4a4a;
            drawText(context, toggleText, x + 10, y + 30, toggleColor);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class LivestreamOverlay {
        private final List<LivestreamElement> elements = new ArrayList<>();
        
        public void addOverlay(String content, int x, int y) {
            elements.add(new LivestreamElement(content, x, y));
        }
        
        public void removeOverlay(String content) {
            elements.removeIf(element -> element.getContent().equals(content));
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            for (LivestreamElement element : elements) {
                element.render(context, mouseX, mouseY, delta);
            }
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return false;
        }
        
        public void tick() {}
    }
    
    public static class LivestreamElement {
        private final String content;
        private final int x, y;
        
        public LivestreamElement(String content, int x, int y) {
            this.content = content;
            this.x = x;
            this.y = y;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + 150, y + 30, 0xFF4a2a2a);
            context.drawBorder(x, y, 150, 30, 0xFF6a4a4a);
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, content, x + 5, y + 8, 0xFFFFFF, false);
        }
        
        public String getContent() { return content; }
    }
    
    public static class DragDropOverlay extends UIComponent {
        private boolean isActive = false;
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            if (!isActive) return;
            
            // Draw drag & drop zone
            drawBackground(context, 0xFF3a4a3a);
            drawBorder(context, 0xFF5a6a6a);
            
            drawText(context, "Drag & Drop Zone", x + 10, y + 10, 0xFFFFFF);
            
            // Draw drop hint
            String hint = "Drop overlays here";
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            int hintWidth = textRenderer.getWidth(hint);
            int hintX = x + (width - hintWidth) / 2;
            context.drawText(textRenderer, hint, hintX, y + height / 2, 0xAAAAAA, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isInBounds(mouseX, mouseY)) {
                isActive = !isActive;
                return true;
            }
            return false;
        }
        
        @Override
        public void tick() {
            // Update drag & drop state if needed
        }
        
        public boolean isActive() { return isActive; }
    }
}
