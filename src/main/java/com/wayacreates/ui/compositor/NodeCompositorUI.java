package com.wayacreates.ui.compositor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import com.wayacreates.ui.node.NodeSystem;
import com.wayacreates.ui.node.NodeTypes;

import static com.wayacreates.ui.compositor.NodeCompositorUIComponents.*;

/**
 * Node-based Compositor UI
 * After Effects style node editor for video effects, color grading, and compositing
 * Main UI class - Part 1 of 2
 */
public class NodeCompositorUI extends Screen {
    private static final Text TITLE = Text.translatable("wayacreates.ui.compositor");
    
    // Debug flag for verbose logging
    private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
    
    // Node system components
    private NodeSystem.NodeGraph nodeSystem;
    private NodeLibrary nodeLibrary;
    private NodeEditor nodeEditor;
    private NodeProperties nodeProperties;
    private NodePreview nodePreview;
    
    // UI state
    private boolean isInitialized = false;
    private final boolean isPlaying = false;
    private float currentTime = 0.0f;
    private String selectedNode = null;
    
    // Layout constants
    private static final int TOOLBAR_HEIGHT = 40;
    private static final int SIDEBAR_WIDTH = 250;
    private static final int PREVIEW_HEIGHT = 200;
    
    public NodeCompositorUI() {
        super(TITLE);
        if (DEBUG_MODE) {
            System.out.println(" Node Compositor UI initialized in DEBUG mode");
        }
    }
    
    @Override
    protected void init() {
        super.init();
        
        if (!isInitialized) {
            initializeComponents();
            isInitialized = true;
        }
        
        layoutComponents();
        loadDefaultNodes();
    }
    
    private void initializeComponents() {
        nodeSystem = new NodeSystem.NodeGraph();
        nodeLibrary = new NodeLibrary();
        nodeEditor = new NodeEditor();
        nodeProperties = new NodeProperties();
        nodePreview = new NodePreview();
    }
    
    private void layoutComponents() {
        int panelWidth = this.width;
        int panelHeight = this.height;
        
        // Layout node library (left sidebar)
        nodeLibrary.setBounds(0, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, panelHeight - TOOLBAR_HEIGHT - PREVIEW_HEIGHT);
        
        // Layout node editor (main area)
        int editorX = SIDEBAR_WIDTH;
        int editorY = TOOLBAR_HEIGHT;
        int editorWidth = panelWidth - SIDEBAR_WIDTH;
        int editorHeight = panelHeight - TOOLBAR_HEIGHT - PREVIEW_HEIGHT;
        nodeEditor.setBounds(editorX, editorY, editorWidth, editorHeight);
        
        // Layout node properties (right sidebar)
        nodeProperties.setBounds(panelWidth - SIDEBAR_WIDTH, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, 
                              panelHeight - TOOLBAR_HEIGHT - PREVIEW_HEIGHT);
        
        // Layout node preview (bottom)
        nodePreview.setBounds(SIDEBAR_WIDTH, panelHeight - PREVIEW_HEIGHT, 
                            panelWidth - SIDEBAR_WIDTH, PREVIEW_HEIGHT);
    }
    
    private void loadDefaultNodes() {
        // Load default node types
        nodeLibrary.addNodeType(NodeTypes.createInputNode());
        nodeLibrary.addNodeType(NodeTypes.createOutputNode());
        nodeLibrary.addNodeType(NodeTypes.createColorGradeNode());
        nodeLibrary.addNodeType(NodeTypes.createBlurNode());
        nodeLibrary.addNodeType(NodeTypes.createTransformNode());
        nodeLibrary.addNodeType(NodeTypes.createCompositeNode());
        
        if (DEBUG_MODE) {
            System.out.println("🎨 Loaded " + nodeLibrary.getNodeTypeCount() + " node types");
        }
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background
        renderBackground(context);
        
        // Draw toolbar
        renderToolbar(context, mouseX, mouseY);
        
        // Draw main components
        nodeLibrary.render(context, mouseX, mouseY, delta);
        nodeEditor.render(context, mouseX, mouseY, delta);
        nodeProperties.render(context, mouseX, mouseY, delta);
        nodePreview.render(context, mouseX, mouseY, delta);
        
        // Draw status bar
        renderStatusBar(context);
    }
    
    @Override
    public void renderBackground(DrawContext context) {
        // Dark gradient background
        int bgWidth = this.width;
        int bgHeight = this.height;
        
        for (int y = 0; y < bgHeight; y++) {
            float alpha = (float) y / bgHeight;
            int color = interpolateColor(0xFF1a1a1a, 0xFF0a0a0a, alpha);
            context.fill(0, y, bgWidth, y + 1, color);
        }
    }
    
    private void renderToolbar(DrawContext context, int mouseX, int mouseY) {
        int toolbarY = 0;
        context.fill(0, toolbarY, this.width, TOOLBAR_HEIGHT, 0xFF333333);
        
        // Draw toolbar buttons
        int buttonX = 10;
        drawToolbarButton(context, buttonX, toolbarY + 5, 60, 30, "New", mouseX, mouseY);
        buttonX += 70;
        drawToolbarButton(context, buttonX, toolbarY + 5, 60, 30, "Open", mouseX, mouseY);
        buttonX += 70;
        drawToolbarButton(context, buttonX, toolbarY + 5, 60, 30, "Save", mouseX, mouseY);
        buttonX += 70;
        drawToolbarButton(context, buttonX, toolbarY + 5, 60, 30, "Export", mouseX, mouseY);
        
        // Draw title
        var textRenderer = this.textRenderer;
        context.drawText(textRenderer, "Node Compositor", this.width / 2 - 60, toolbarY + 12, 0xFFFFFF, false);
    }
    
    private void renderStatusBar(DrawContext context) {
        int statusY = this.height - 25;
        context.fill(0, statusY, this.width, statusY + 25, 0xFF2a2a2a);
        
        String statusText = this.isPlaying ? "Playing" : "Ready";
        var textRenderer = this.textRenderer;
        context.drawText(textRenderer, statusText, 10, statusY + 5, 0xFFFFFF, false);
        
        String nodeCount = String.format("Nodes: %d", nodeSystem.getNodeCount());
        context.drawText(textRenderer, nodeCount, 100, statusY + 5, 0xFFFFFF, false);
        
        String timeText = String.format("Time: %.2fs", this.currentTime);
        context.drawText(textRenderer, timeText, 250, statusY + 5, 0xFFFFFF, false);
        
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
    
    private void drawToolbarButton(DrawContext context, int x, int y, int width, int height, 
                                  String text, int mouseX, int mouseY) {
        boolean isHovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
        
        context.fill(x, y, x + width, y + height, color);
        context.drawBorder(x, y, width, height, 0xFF6a6a6a);
        
        var textRendererInstance = this.textRenderer;
        int textWidth = textRendererInstance.getWidth(text);
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - 8) / 2;
        context.drawText(textRendererInstance, text, textX, textY, 0xFFFFFF, false);
    }
    
    // Public API methods
    public void addNode(String nodeType, int x, int y) {
        NodeSystem.Node node = nodeLibrary.createNode(nodeType);
        if (node != null) {
            node.setPosition(x, y);
            nodeSystem.addNode(node);
            if (DEBUG_MODE) {
                System.out.println("🎨 Added node: " + node.getName() + " at (" + x + ", " + y + ")");
            }
        }
    }
    
    public void removeNode(String nodeId) {
        nodeSystem.removeNode(nodeId);
        if (DEBUG_MODE) {
            System.out.println("🎨 Removed node: " + nodeId);
        }
    }
    
    public void connectNodes(String fromNodeId, String toNodeId, String fromPort, String toPort) {
        nodeSystem.connectNodes(fromNodeId, toNodeId, fromPort, toPort);
        if (DEBUG_MODE) {
            System.out.println("🎨 Connected nodes: " + fromNodeId + " -> " + toNodeId);
        }
    }
    
    public void tick() {
        if (isPlaying) {
            currentTime += 1.0f / 30.0f; // 30 FPS
            nodeSystem.update(currentTime);
        }
        
        // Update components
        nodeEditor.tick();
        nodeProperties.tick();
        nodePreview.tick();
    }
}
