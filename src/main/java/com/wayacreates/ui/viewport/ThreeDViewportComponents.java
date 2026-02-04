package com.wayacreates.ui.viewport;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.wayacreates.ui.UIComponent;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * ThreeD Viewport - Component Classes using LDLib
 * Part 2 of 3 - All UI component implementations with advanced layout and event system
 */
public class ThreeDViewportComponents {
    
    // 3D Rendering Components
    public static class Camera {
        private Vector3f position = new Vector3f(0, 0, 5);
        private Vector3f target = new Vector3f(0, 0, 0);
        private Vector3f up = new Vector3f(0, 1, 0);
        private float fov = 45.0f;
        private float near = 0.1f;
        private float far = 1000.0f;
        
        public void setPosition(Vector3f position) {
            this.position.set(position);
        }
        
        public void setTarget(Vector3f target) {
            this.target.set(target);
        }
        
        public Matrix4f getViewMatrix() {
            Matrix4f view = new Matrix4f();
            view.lookAt(position, target, up);
            return view;
        }
        
        public Matrix4f getProjectionMatrix(float aspectRatio) {
            Matrix4f projection = new Matrix4f();
            projection.perspective(fov, aspectRatio, near, far);
            return projection;
        }
    }
    
    public static class Scene {
        private final List<SceneObject> objects = new ArrayList<>();
        
        public void addObject(SceneObject object) {
            objects.add(object);
        }
        
        public void removeObject(SceneObject object) {
            objects.remove(object);
        }
        
        public List<SceneObject> getObjects() {
            return new ArrayList<>(objects);
        }
        
        public void render(DrawContext context, Matrix4f viewMatrix, Matrix4f projectionMatrix) {
            for (SceneObject object : objects) {
                object.render(context, viewMatrix, projectionMatrix);
            }
        }
        
        public int getObjectCount() {
            return objects.size();
        }
    }
    
    public static class SceneObject {
        private final String name;
        private Vector3f position;
        private final Type type;
        private Vector3f rotation = new Vector3f();
        private Vector3f scale = new Vector3f(1, 1, 1);
        
        public SceneObject(String name, Vector3f position, Type type) {
            this.name = name;
            this.position = position;
            this.type = type;
        }
        
        public void render(DrawContext context, Matrix4f viewMatrix, Matrix4f projectionMatrix) {
            // Simplified rendering - would use OpenGL in real implementation
            // For now, just log the rendering action
            System.out.println("[3D Viewport] Rendering " + name + " at " + position);
        }
        
        public enum Type {
            CUBE, SPHERE, CYLINDER, LIGHT, CAMERA
        }
        
        // Getters and setters
        public String getName() { return name; }
        public Vector3f getPosition() { return position; }
        public Type getType() { return type; }
        public Vector3f getRotation() { return rotation; }
        public Vector3f getScale() { return scale; }
    }
    
    public static class Renderer extends UIComponent {
        private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/Renderer");
        private Camera camera;
        private Scene scene;
        
        public Renderer() {
            super();
        }
        
        public void setScene(Camera camera, Scene scene) {
            this.camera = camera;
            this.scene = scene;
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF1a1a2a);
            drawBorder(context, 0xFF4a4a4a);
            
            if (camera != null && scene != null) {
                // Calculate view and projection matrices
                float aspectRatio = (float) width / height;
                Matrix4f viewMatrix = camera.getViewMatrix();
                Matrix4f projectionMatrix = camera.getProjectionMatrix(aspectRatio);
                
                // Render scene
                scene.render(context, viewMatrix, projectionMatrix);
                
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("🎬 Rendering 3D scene with aspect ratio: {}", aspectRatio);
                }
            }
            
            // Draw viewport info
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "3D Viewport", x + 10, y + 10, 0xFFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class Grid extends UIComponent {
        private int gridSize = 10;
        private float cellSize = 1.0f;
        
        public Grid() {
            super();
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawBorder(context, 0xFF3a3a3a);
            
            // Draw grid info
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Grid (" + gridSize + "x" + gridSize + ")", x + 10, y + 10, 0x66FFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
        
        public void setGridSize(int size) {
            this.gridSize = size;
        }
        
        public void setCellSize(float size) {
            this.cellSize = size;
        }
    }
    
    public static class Gizmo extends UIComponent {
        private Vector3f position = new Vector3f();
        private float size = 1.0f;
        
        public Gizmo() {
            super();
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawBorder(context, 0xFF3a3a3a);
            
            // Draw gizmo info
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Gizmo", x + 10, y + 10, 0x66FFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
        
        public void setPosition(Vector3f position) {
            this.position.set(position);
        }
        
        public void setSize(float size) {
            this.size = size;
        }
    }
    
    // UI Components
    public static class ViewportControls extends UIComponent {
        private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/ViewportControls");
        
        public ViewportControls() {
            super();
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawBorder(context, 0xFF3a3a3a);
            
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Viewport Controls", x + 10, y + 10, 0xFFFFFF, false);
            
            // Draw control buttons
            drawButton(context, x + 10, y + 30, 40, 20, "Top", mouseX, mouseY);
            drawButton(context, x + 55, y + 30, 40, 20, "Front", mouseX, mouseY);
            drawButton(context, x + 100, y + 30, 40, 20, "Side", mouseX, mouseY);
        }
        
        private void drawButton(DrawContext context, int bx, int by, int bw, int bh, String text, int mouseX, int mouseY) {
            boolean isHovered = mouseX >= bx && mouseX <= bx + bw && mouseY >= by && mouseY <= by + bh;
            int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
            
            context.fill(bx, by, bx + bw, by + bh, color);
            context.drawBorder(bx, by, bw, bh, 0xFF6a6a6a);
            
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            int textWidth = textRenderer.getWidth(text);
            int textX = bx + (bw - textWidth) / 2;
            int textY = by + (bh - 8) / 2;
            context.drawText(textRenderer, text, textX, textY, 0xFFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            // Check button clicks
            if (mouseX >= x + 10 && mouseX <= x + 50 && mouseY >= y + 30 && mouseY <= y + 50) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("📐 Top view selected");
                }
                return true;
            }
            if (mouseX >= x + 55 && mouseX <= x + 95 && mouseY >= y + 30 && mouseY <= y + 50) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("📐 Front view selected");
                }
                return true;
            }
            if (mouseX >= x + 100 && mouseX <= x + 140 && mouseY >= y + 30 && mouseY <= y + 50) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("📐 Side view selected");
                }
                return true;
            }
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class ObjectProperties extends UIComponent {
        private Vector3f position = new Vector3f();
        private Vector3f rotation = new Vector3f();
        private Vector3f scale = new Vector3f(1, 1, 1);
        
        public ObjectProperties() {
            super();
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawBorder(context, 0xFF3a3a3a);
            
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Object Properties", x + 10, y + 10, 0xFFFFFF, false);
            
            // Draw property fields
            context.drawText(textRenderer, "Position: " + String.format("%.1f, %.1f, %.1f", position.x, position.y, position.z), x + 10, y + 30, 0xCCCCCC, false);
            context.drawText(textRenderer, "Rotation: " + String.format("%.0f°, %.0f°, %.0f°", rotation.x, rotation.y, rotation.z), x + 10, y + 50, 0xCCCCCC, false);
            context.drawText(textRenderer, "Scale: " + String.format("%.1f, %.1f, %.1f", scale.x, scale.y, scale.z), x + 10, y + 70, 0xCCCCCC, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
        
        public void updateProperties(Vector3f position, Vector3f rotation, Vector3f scale) {
            this.position.set(position);
            this.rotation.set(rotation);
            this.scale.set(scale);
        }
    }
    
    public static class Timeline extends UIComponent {
        private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/Timeline");
        private final float duration = 10.0f;
        private float currentTime = 0.0f;
        
        public Timeline() {
            super();
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF252525);
            drawBorder(context, 0xFF3a3a3a);
            
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Timeline", x + 10, y + 10, 0xFFFFFF, false);
            
            // Draw timeline scrubber
            int scrubberX = x + (int)((currentTime / duration) * (width - 20));
            context.fill(scrubberX, y + 30, scrubberX + 2, y + height - 10, 0xFF4a4a4a);
            
            // Draw time
            String timeStr = String.format("%02d:%02d", (int)(currentTime / 60), (int)(currentTime % 60));
            context.drawText(textRenderer, timeStr, x + 10, y + height - 25, 0xFFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isInBounds(mouseX, mouseY) && mouseY >= y + 30 && mouseY <= y + height - 10) {
                // Update timeline position based on click
                float relativeX = (float)(mouseX - x - 10) / (width - 20);
                currentTime = Math.max(0, Math.min(duration, relativeX * duration));
                
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("⏱ Timeline position: {}", currentTime);
                }
                return true;
            }
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
        
        public void setCurrentTime(float time) {
            this.currentTime = Math.max(0, Math.min(duration, time));
        }
        
        public float getCurrentTime() {
            return currentTime;
        }
    }
    
    public static class AnimationControls extends UIComponent {
        private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/AnimationControls");
        private boolean isPlaying = false;
        private boolean isPaused = false;
        
        public AnimationControls() {
            super();
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawBorder(context, 0xFF3a3a3a);
            
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Animation", x + 10, y + 10, 0xFFFFFF, false);
            
            // Draw playback controls
            drawButton(context, x + 10, y + 30, 30, 20, "▶", mouseX, mouseY);
            drawButton(context, x + 45, y + 30, 30, 20, "⏸", mouseX, mouseY);
            drawButton(context, x + 80, y + 30, 30, 20, "⏹", mouseX, mouseY);
            
            // Draw status
            String status = isPlaying ? "Playing" : (isPaused ? "Paused" : "Stopped");
            context.drawText(textRenderer, "Status: " + status, x + 10, y + 60, 0xCCCCCC, false);
        }
        
        private void drawButton(DrawContext context, int bx, int by, int bw, int bh, String text, int mouseX, int mouseY) {
            boolean isHovered = mouseX >= bx && mouseX <= bx + bw && mouseY >= by && mouseY <= by + bh;
            int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
            
            context.fill(bx, by, bx + bw, by + bh, color);
            context.drawBorder(bx, by, bw, bh, 0xFF6a6a6a);
            
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            int textWidth = textRenderer.getWidth(text);
            int textX = bx + (bw - textWidth) / 2;
            int textY = by + (bh - 8) / 2;
            context.drawText(textRenderer, text, textX, textY, 0xFFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            // Check button clicks
            if (mouseX >= x + 10 && mouseX <= x + 40 && mouseY >= y + 30 && mouseY <= y + 50) {
                isPlaying = true;
                isPaused = false;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("▶️ Animation started");
                }
                return true;
            }
            if (mouseX >= x + 45 && mouseX <= x + 75 && mouseY >= y + 30 && mouseY <= y + 50) {
                isPaused = true;
                isPlaying = false;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("⏸️ Animation paused");
                }
                return true;
            }
            if (mouseX >= x + 80 && mouseX <= x + 110 && mouseY >= y + 30 && mouseY <= y + 50) {
                isPlaying = false;
                isPaused = false;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("⏹️ Animation stopped");
                }
                return true;
            }
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
        
        public boolean isPlaying() {
            return isPlaying;
        }
        
        public boolean isPaused() {
            return isPaused;
        }
    }
}
