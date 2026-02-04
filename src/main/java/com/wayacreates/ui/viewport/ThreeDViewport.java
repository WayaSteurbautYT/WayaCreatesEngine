package com.wayacreates.ui.viewport;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.wayacreates.utils.DebugUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static com.wayacreates.ui.viewport.ThreeDViewportComponents.*;

/**
 * Professional 3D Viewport with Blender-style navigation and rig integration
 * Features camera controls, object manipulation, and real-time rendering
 * Main UI class - Part 1 of 3
 */
public class ThreeDViewport extends Screen {
    private static final Text TITLE = Text.translatable("wayacreates.ui.viewport");
    
    // 3D Rendering components
    private Camera camera;
    private Scene scene;
    private Renderer renderer;
    private Grid grid;
    private Gizmo gizmo;
    
    // UI Components
    private ViewportControls viewportControls;
    private ObjectProperties objectProperties;
    private Timeline timeline;
    private AnimationControls animationControls;
    
    // Viewport state
    private boolean isInitialized = false;
    private ViewMode viewMode = ViewMode.SOLID;
    private ShadingMode shadingMode = ShadingMode.SMOOTH;
    private boolean showGrid = true;
    private boolean showGizmo = true;
    private boolean isPlaying = false;
    private float currentTime = 0.0f;
    
    // Mouse state
    private boolean isDragging = false;
    private boolean isRotating = false;
    private boolean isPanning = false;
    private double lastMouseX = Double.NaN;
    private double lastMouseY = Double.NaN;
    
    // Layout constants
    private static final int TOOLBAR_HEIGHT = 40;
    private static final int SIDEBAR_WIDTH = 250;
    private static final int TIMELINE_HEIGHT = 150;
    
    public ThreeDViewport() {
        super(TITLE);
        DebugUtils.debug("ThreeDViewport", "🎭 3D Viewport initialized in DEBUG mode");
    }
    
    @Override
    protected void init() {
        super.init();
        
        if (!isInitialized) {
            initializeComponents();
            isInitialized = true;
        }
        
        layoutComponents();
        setupDefaultScene();
    }
    
    private void initializeComponents() {
        DebugUtils.logInitialization("3D viewport components",
            "Camera: Setting up perspective projection",
            "Scene: Creating default scene",
            "Renderer: Initializing OpenGL context",
            "Grid: Setting up coordinate system");
        
        camera = new Camera();
        scene = new Scene();
        renderer = new Renderer();
        grid = new Grid();
        gizmo = new Gizmo();
        
        viewportControls = new ViewportControls();
        objectProperties = new ObjectProperties();
        timeline = new Timeline();
        animationControls = new AnimationControls();
        
        DebugUtils.logCompletion("3D viewport components");
    }
    
    private void layoutComponents() {
        int panelWidth = this.width;
        int panelHeight = this.height;
        
        // Layout 3D viewport (main area)
        int viewportX = SIDEBAR_WIDTH;
        int viewportY = TOOLBAR_HEIGHT;
        int viewportWidth = panelWidth - SIDEBAR_WIDTH;
        int viewportHeight = panelHeight - TOOLBAR_HEIGHT - TIMELINE_HEIGHT;
        
        renderer.setBounds(viewportX, viewportY, viewportWidth, viewportHeight);
        
        // Layout sidebar (right)
        objectProperties.setBounds(panelWidth - SIDEBAR_WIDTH, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, 
                                 panelHeight - TOOLBAR_HEIGHT - TIMELINE_HEIGHT);
        
        // Layout viewport controls (bottom-left)
        viewportControls.setBounds(SIDEBAR_WIDTH, panelHeight - TIMELINE_HEIGHT, 200, TIMELINE_HEIGHT);
        
        // Layout animation controls (bottom-center)
        animationControls.setBounds(SIDEBAR_WIDTH + 210, panelHeight - TIMELINE_HEIGHT, 200, TIMELINE_HEIGHT);
        
        // Layout timeline (bottom-right)
        timeline.setBounds(panelWidth - 400, panelHeight - TIMELINE_HEIGHT, 380, TIMELINE_HEIGHT);
    }
    
    private void setupDefaultScene() {
        DebugUtils.debug("ThreeDViewport", "Setting up default 3D scene");
        
        // Add default objects to scene
        scene.addObject(new SceneObject("Cube", new Vector3f(0, 0, 0), SceneObject.Type.CUBE));
        scene.addObject(new SceneObject("Sphere", new Vector3f(2, 0, 0), SceneObject.Type.SPHERE));
        scene.addObject(new SceneObject("Light", new Vector3f(5, 5, 5), SceneObject.Type.LIGHT));
        
        // Setup camera position
        camera.setPosition(new Vector3f(5, 3, 5));
        camera.setTarget(new Vector3f(0, 0, 0));
        
        DebugUtils.debug("ThreeDViewport", "Default scene setup complete");
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background
        renderBackground(context);
        
        // Draw 3D viewport
        renderer.render(context, mouseX, mouseY, delta);
        
        // Draw grid if enabled
        if (showGrid) {
            grid.render(context, mouseX, mouseY, delta);
        }
        
        // Draw gizmo if enabled
        if (showGizmo) {
            gizmo.render(context, mouseX, mouseY, delta);
        }
        
        // Draw UI components
        viewportControls.render(context, mouseX, mouseY, delta);
        objectProperties.render(context, mouseX, mouseY, delta);
        timeline.render(context, mouseX, mouseY, delta);
        animationControls.render(context, mouseX, mouseY, delta);
        
        // Draw toolbar
        renderToolbar(context, mouseX, mouseY);
        
        // Draw status bar
        renderStatusBar(context);
    }
    
    @Override
    public void renderBackground(DrawContext context) {
        // Dark gradient background for 3D viewport
        int bgWidth = this.width;
        int bgHeight = this.height;
        
        for (int y = 0; y < bgHeight; y++) {
            float alpha = (float) y / bgHeight;
            int color = interpolateColor(0xFF2a2a3a, 0xFF1a1a2a, alpha);
            context.fill(0, y, bgWidth, y + 1, color);
        }
    }
    
    private void renderToolbar(DrawContext context, int mouseX, int mouseY) {
        int toolbarY = 0;
        context.fill(0, toolbarY, this.width, TOOLBAR_HEIGHT, 0xFF333333);
        
        // Draw view mode buttons
        int buttonX = 10;
        var textRenderer = this.textRenderer;
        for (ViewMode mode : ViewMode.values()) {
            boolean isSelected = viewMode == mode;
            int color = isSelected ? 0xFF4a6a4a : 0xFF3a4a3a;
            
            context.fill(buttonX, toolbarY + 5, buttonX + 60, toolbarY + 35, color);
            context.drawBorder(buttonX, toolbarY + 5, 60, 30, 0xFF6a6a6a);
            
            String modeText = mode.toString().substring(0, 1) + mode.toString().substring(1).toLowerCase();
            context.drawText(textRenderer, modeText, buttonX + 5, toolbarY + 12, 0xFFFFFF, false);
            
            buttonX += 70;
        }
        
        // Draw shading mode buttons
        buttonX += 20;
        for (ShadingMode mode : ShadingMode.values()) {
            boolean isSelected = shadingMode == mode;
            int color = isSelected ? 0xFF6a4a4a : 0xFF4a3a3a;
            
            context.fill(buttonX, toolbarY + 5, buttonX + 70, toolbarY + 35, color);
            context.drawBorder(buttonX, toolbarY + 5, 70, 30, 0xFF6a6a6a);
            
            String modeText = mode.toString().substring(0, 1) + mode.toString().substring(1).toLowerCase();
            context.drawText(textRenderer, modeText, buttonX + 5, toolbarY + 12, 0xFFFFFF, false);
            
            buttonX += 80;
        }
        
        // Draw toggle buttons
        drawToggleButton(context, this.width - 200, toolbarY + 5, "Grid", showGrid, mouseX, mouseY);
        drawToggleButton(context, this.width - 130, toolbarY + 5, "Gizmo", showGizmo, mouseX, mouseY);
        
        // Draw title
        context.drawText(textRenderer, "3D Viewport", this.width / 2 - 40, toolbarY + 12, 0xFFFFFF, false);
    }
    
    private void renderStatusBar(DrawContext context) {
        int statusY = this.height - 25;
        context.fill(0, statusY, this.width, statusY + 25, 0xFF2a2a2a);
        
        String statusText = isPlaying ? "Playing" : "Ready";
        var textRenderer = this.textRenderer;
        context.drawText(textRenderer, statusText, 10, statusY + 5, 0xFFFFFF, false);
        
        String viewModeText = "View: " + viewMode.toString();
        context.drawText(textRenderer, viewModeText, 100, statusY + 5, 0xFFFFFF, false);
        
        String shadingText = "Shading: " + shadingMode.toString();
        context.drawText(textRenderer, shadingText, 250, statusY + 5, 0xFFFFFF, false);
        
        String timeText = String.format("Time: %.2fs", currentTime);
        context.drawText(textRenderer, timeText, 400, statusY + 5, 0xFFFFFF, false);
        
        String fpsText = String.format("FPS: %d", MinecraftClient.getInstance().getCurrentFps());
        context.drawText(textRenderer, fpsText, this.width - 100, statusY + 5, 0xFFFFFF, false);
    }
    
    // Utility methods
    private int interpolateColor(int color1, int color2, float alpha) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;
        
        int r = (int) (r1 + (r2 - r1) * alpha);
        int g = (int) (g1 + (g2 - g1) * alpha);
        int b = (int) (b1 + (b2 - b1) * alpha);
        
        return 0xFF << 24 | r << 16 | g << 8 | b;
    }
    
    private void drawToggleButton(DrawContext context, int x, int y, String text, boolean enabled, int mouseX, int mouseY) {
        boolean isHovered = mouseX >= x && mouseX <= x + 60 && mouseY >= y && mouseY <= y + 30;
        int color = enabled ? (isHovered ? 0xFF4a7a4a : 0xFF3a5a3a) : (isHovered ? 0xFF7a4a4a : 0xFF5a3a3a);
        
        context.fill(x, y, x + 60, y + 30, color);
        context.drawBorder(x, y, 60, 30, 0xFF6a6a6a);
        
        var textRenderer = this.textRenderer;
        context.drawText(textRenderer, text, x + 5, y + 8, 0xFFFFFF, false);
    }
    
    // Enums
    public enum ViewMode {
        SOLID, WIREFRAME, TEXTURED, RENDERED
    }
    
    public enum ShadingMode {
        FLAT, SMOOTH, TOON, NORMALS
    }
}
