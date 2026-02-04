package com.wayacreates.ui.viewport;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.wayacreates.utils.DebugUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import com.wayacreates.ui.UIComponent;

/**
 * ThreeD Viewport - Component Classes
 * Part 2 of 3 - All UI component implementations
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
            DebugUtils.debug("SceneObject", "Rendering " + name + " at " + position);
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
        private Camera camera;
        private Scene scene;
        
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
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            // Draw grid lines (simplified)
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Grid", x + 10, y + 10, 0x66FFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class Gizmo extends UIComponent {
        private Vector3f position = new Vector3f();
        private float size = 1.0f;
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            // Draw 3D gizmo (simplified)
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, "Gizmo", x + 10, y + 10, 0x66FFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    // UI Components
    public static class ViewportControls extends UIComponent {
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawText(context, "Viewport Controls", x + 10, y + 10, 0xFFFFFF);
            
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
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class ObjectProperties extends UIComponent {
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawText(context, "Object Properties", x + 10, y + 10, 0xFFFFFF);
            
            // Draw property fields
            drawText(context, "Position:", x + 10, y + 30, 0xCCCCCC);
            drawText(context, "Rotation:", x + 10, y + 50, 0xCCCCCC);
            drawText(context, "Scale:", x + 10, y + 70, 0xCCCCCC);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class Timeline extends UIComponent {
        private float duration = 10.0f;
        private float currentTime = 0.0f;
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF252525);
            drawText(context, "Timeline", x + 10, y + 10, 0xFFFFFF);
            
            // Draw timeline scrubber
            int scrubberX = x + (int)((currentTime / duration) * (width - 20));
            context.fill(scrubberX, y + 30, scrubberX + 2, y + height - 10, 0xFF4a4a4a);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
        
        public void setCurrentTime(float time) {
            this.currentTime = Math.max(0, Math.min(duration, time));
        }
    }
    
    public static class AnimationControls extends UIComponent {
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawText(context, "Animation", x + 10, y + 10, 0xFFFFFF);
            
            // Draw playback controls
            drawButton(context, x + 10, y + 30, 30, 20, "▶", mouseX, mouseY);
            drawButton(context, x + 45, y + 30, 30, 20, "⏸", mouseX, mouseY);
            drawButton(context, x + 80, y + 30, 30, 20, "⏹", mouseX, mouseY);
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
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
}
