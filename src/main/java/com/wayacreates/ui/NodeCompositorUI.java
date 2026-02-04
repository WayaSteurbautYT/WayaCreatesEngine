package com.wayacreates.ui;

import java.util.ArrayList;
import java.util.List;

import java.awt.FileDialog;
import java.awt.Frame;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import com.wayacreates.ui.node.NodeSystem;
import com.wayacreates.ui.node.NodeTypes;
import com.wayacreates.ui.node.NodeComponents;

/**
 * Node-based Compositor UI
 * After Effects style node editor for video effects, color grading, and compositing
 */
public class NodeCompositorUI extends Screen {
    private static final Text TITLE = Text.translatable("wayacreates.ui.compositor");
    
    // Debug flag for verbose logging
    private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
    
    // Node system components
    private NodeSystem.NodeGraph nodeGraph;
    private NodeComponents.NodeEditor nodeEditor;
    private NodeComponents.PropertyPanel propertyPanel;
    private NodeComponents.NodeLibrary nodeLibrary;
    private NodeComponents.PreviewPanel previewPanel;
    private NodeComponents.Camera camera = new NodeComponents.Camera();
    
    // UI state
    private boolean isInitialized = false;
    private NodeSystem.Node selectedNode = null;
    private NodeSystem.Connection connectingConnection = null;
    private boolean isConnecting = false;
    private float zoomLevel = 1.0f;
    
    // Mouse state
    private boolean isDragging = false;
    private boolean isPanning = false;
    
    // Layout constants
    private static final int SIDEBAR_WIDTH = 250;
    private static final int TOOLBAR_HEIGHT = 40;
    private static final int PREVIEW_HEIGHT = 200;
    
    public NodeCompositorUI() {
        super(TITLE);
        if (DEBUG_MODE) {
            System.out.println("🎨 Node Compositor UI initialized in DEBUG mode");
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
        setupDefaultNodes();
    }
    
    private void initializeComponents() {
        nodeGraph = new NodeSystem.NodeGraph();
        nodeEditor = new NodeComponents.NodeEditor();
        propertyPanel = new NodeComponents.PropertyPanel();
        nodeLibrary = new NodeComponents.NodeLibrary();
        previewPanel = new NodeComponents.PreviewPanel();
    }
    
    private void layoutComponents() {
        int panelWidth = this.width;
        int panelHeight = this.height;
        
        // Layout node editor (main area)
        int nodeEditorX = SIDEBAR_WIDTH;
        int nodeEditorY = TOOLBAR_HEIGHT;
        int nodeEditorWidth = panelWidth - SIDEBAR_WIDTH;
        int nodeEditorHeight = panelHeight - TOOLBAR_HEIGHT - PREVIEW_HEIGHT;
        
        nodeEditor.setBounds(nodeEditorX, nodeEditorY, nodeEditorWidth, nodeEditorHeight);
        
        // Layout node library (left sidebar)
        nodeLibrary.setBounds(0, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, 
                              panelHeight - TOOLBAR_HEIGHT - PREVIEW_HEIGHT);
        
        // Layout property panel (right sidebar)
        propertyPanel.setBounds(panelWidth - SIDEBAR_WIDTH, TOOLBAR_HEIGHT, SIDEBAR_WIDTH, 
                               panelHeight - TOOLBAR_HEIGHT - PREVIEW_HEIGHT);
        
        // Layout preview panel (bottom)
        previewPanel.setBounds(SIDEBAR_WIDTH, panelHeight - PREVIEW_HEIGHT, 
                              panelWidth - SIDEBAR_WIDTH * 2, PREVIEW_HEIGHT);
    }
    
    private void setupDefaultNodes() {
        // Add default nodes to the graph
        nodeGraph.addNode(new NodeTypes.SimpleInputNode("Input", 100, 100));
        nodeGraph.addNode(new NodeTypes.ColorCorrectionNode("Color Correction", 300, 100));
        nodeGraph.addNode(new NodeTypes.EffectNode("Effect", 500, 100));
        nodeGraph.addNode(new NodeTypes.OutputNode("Output", 700, 100));
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background
        renderBackground(context);
        
        // Draw toolbar
        renderToolbar(context, mouseX, mouseY);
        
        // Draw main components
        nodeEditor.render(context, mouseX, mouseY, delta);
        nodeLibrary.render(context, mouseX, mouseY, delta);
        propertyPanel.render(context, mouseX, mouseY, delta);
        previewPanel.render(context, mouseX, mouseY, delta);
        
        // Draw connection preview
        if (isConnecting && connectingConnection != null) {
            renderConnectionPreview(context, mouseX, mouseY);
        }
        
        // Draw status bar
        renderStatusBar(context);
    }
    
    @Override
    public void renderBackground(DrawContext context) {
        int bgWidth = this.width;
        int bgHeight = this.height;
        
        // Render background gradient
        for (int bgY = 0; bgY < bgHeight; bgY++) {
            float alpha = (float) bgY / bgHeight;
            int color = interpolateColor(0xFF1a1a1a, 0xFF0a0a0a, alpha);
            context.fill(0, bgY, bgWidth, bgY + 1, color);
        }
    }
    
    private void renderToolbar(DrawContext context, int mouseX, int mouseY) {
        int toolbarY = 0;
        context.fill(0, toolbarY, this.width, TOOLBAR_HEIGHT, 0xFF333333);
        
        // Draw toolbar buttons
        int buttonX = 10;
        drawToolbarButton(context, buttonX, toolbarY + 5, 80, 30, "New", mouseX, mouseY);
        buttonX += 90;
        drawToolbarButton(context, buttonX, toolbarY + 5, 80, 30, "Open", mouseX, mouseY);
        buttonX += 90;
        drawToolbarButton(context, buttonX, toolbarY + 5, 80, 30, "Save", mouseX, mouseY);
        buttonX += 90;
        drawToolbarButton(context, buttonX, toolbarY + 5, 80, 30, "Export", mouseX, mouseY);
        
        // Draw title
        drawText(context, this.width / 2 - 100, toolbarY + 12, 
                "Node Compositor", 0xFFFFFF);
    }
    
    private void renderConnectionPreview(DrawContext context, int mouseX, int mouseY) {
        if (connectingConnection != null) {
            NodeSystem.NodePort startPort = connectingConnection.getStartPort();
            int startX = startPort.getNode().getX() + startPort.getX();
            int startY = startPort.getNode().getY() + startPort.getY();
            
            // Draw line as a series of points (simplified)
            context.fill(Math.min(startX, mouseX), Math.min(startY, mouseY), 
                        Math.max(startX, mouseX) + 1, Math.max(startY, mouseY) + 1, 0xFF00FF00);
        }
    }
    
    private void renderStatusBar(DrawContext context) {
        int statusY = this.height - 25;
        context.fill(0, statusY, this.width, statusY + 25, 0xFF2a2a2a);
        
        String statusText = selectedNode != null ? 
            "Selected: " + selectedNode.getName() : "Ready";
        drawText(context, 10, statusY + 5, statusText, 0xFFFFFF);
        
        String zoomText = String.format("Zoom: %.0f%%", zoomLevel * 100);
        drawText(context, this.width - 100, statusY + 5, zoomText, 0xFFFFFF);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Handle node editor clicks
        if (nodeEditor.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        
        // Handle node library clicks
        if (nodeLibrary.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        
        // Handle property panel clicks
        if (propertyPanel.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        
        // Handle preview panel clicks
        if (previewPanel.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        
        // Handle toolbar clicks
        if (mouseY <= TOOLBAR_HEIGHT) {
            handleToolbarClick(mouseX, mouseY);
            return true;
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // End connection creation
        if (isConnecting) {
            completeConnection(mouseX, mouseY);
            isConnecting = false;
            connectingConnection = null;
        }
        
        isDragging = false;
        isPanning = false;
        
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isPanning) {
            // Panning functionality
            camera.pan((float) deltaX * 0.01f, (float) deltaY * 0.01f);
        } else if (isDragging && selectedNode != null) {
            // Move selected node
            int newX = (int) (selectedNode.getX() + deltaX / zoomLevel);
            int newY = (int) (selectedNode.getY() + deltaY / zoomLevel);
            selectedNode.setPosition(newX, newY);
        }
        
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        // Zoom node editor
        if (nodeEditor.isInBounds((int) mouseX, (int) mouseY)) {
            zoomLevel += amount * 0.1f;
            zoomLevel = Math.max(0.1f, Math.min(3.0f, zoomLevel));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Handle keyboard shortcuts
        return switch (keyCode) {
            case GLFW.GLFW_KEY_DELETE -> {
                if (selectedNode != null) {
                    nodeGraph.removeNode(selectedNode);
                    selectedNode = null;
                }
                yield true;
            }
            case GLFW.GLFW_KEY_A -> {
                if ((modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
                    // Add new node
                    addNewNode();
                    yield true;
                }
                yield false;
            }
            case GLFW.GLFW_KEY_S -> {
                if ((modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
                    // Save node graph
                    saveNodeGraph();
                    yield true;
                }
                yield false;
            }
            default -> super.keyPressed(keyCode, scanCode, modifiers);
        };
    }
    
    private void handleToolbarClick(double mouseX, double mouseY) {
        // Only handle clicks within the toolbar area
        if (mouseY < 0 || mouseY > TOOLBAR_HEIGHT) {
            return;
        }
        
        int buttonX = 10;
        int buttonWidth = 80;
        
        if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth) {
            // New button
            newNodeGraph();
        } else if (mouseX >= buttonX + 90 && mouseX <= buttonX + 90 + buttonWidth) {
            // Open button
            openNodeGraph();
        } else if (mouseX >= buttonX + 180 && mouseX <= buttonX + 180 + buttonWidth) {
            // Save button
            saveNodeGraph();
        } else if (mouseX >= buttonX + 270 && mouseX <= buttonX + 270 + buttonWidth) {
            // Export button
            exportComposition();
        }
    }
    
    private void completeConnection(double mouseX, double mouseY) {
        // Find target port and complete connection
        NodeSystem.NodePort targetPort = nodeEditor.findPortAt((int) mouseX, (int) mouseY);
        if (targetPort != null && connectingConnection != null) {
            NodeSystem.NodePort startPort = connectingConnection.getStartPort();
            
            // Validate connection
            if (canConnect(startPort, targetPort)) {
                NodeSystem.Connection connection = new NodeSystem.Connection(startPort, targetPort);
                nodeGraph.addConnection(connection);
            }
        }
    }
    
    private boolean canConnect(NodeSystem.NodePort startPort, NodeSystem.NodePort targetPort) {
        return startPort.getPortType() != targetPort.getPortType() &&
               startPort.getNode() != targetPort.getNode();
    }
    
    private void addNewNode() {
        // Add a new node at center of view
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        NodeTypes.EffectNode newNode = new NodeTypes.EffectNode("New Effect", centerX, centerY);
        nodeGraph.addNode(newNode);
        selectedNode = newNode;
    }
    
    private void newNodeGraph() {
        nodeGraph.clear();
        setupDefaultNodes();
    }
    
    private void openNodeGraph() {
        // Implement file dialog for opening node graphs
        // This would typically open a file chooser dialog
        if (DEBUG_MODE) {
            System.out.println(" Open node graph functionality would be implemented here");
        }
        // TODO: Add actual file dialog implementation
    }
    
    private void saveNodeGraph() {
        // Implement save functionality for node graphs
        // This would serialize the node graph to a file
        if (DEBUG_MODE) {
            System.out.println(" Saving node graph with " + nodeGraph.getNodes().size() + " nodes");
        }
        // TODO: Add actual file saving implementation
    }
    
    private void exportComposition() {
        // Implement export functionality for compositions
        // This would render the node graph to a video file
        if (DEBUG_MODE) {
            System.out.println(" Exporting composition with " + nodeGraph.getConnections().size() + " connections");
        }
        // TODO: Add actual export implementation
    }
    
    public void tick() {
        // Update node graph with null check
        if (nodeGraph != null) {
            nodeGraph.tick();
        }
        
        // Update components with null checks
        if (nodeEditor != null) {
            nodeEditor.tick();
        }
        if (propertyPanel != null) {
            propertyPanel.tick();
        }
        if (previewPanel != null) {
            previewPanel.tick();
        }
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
    
    private void drawText(DrawContext context, int x, int y, String text, int color) {
        context.drawText(this.textRenderer, text, x, y, color, false);
    }
    
    private void drawToolbarButton(DrawContext context, int x, int y, int width, int height, 
                                  String text, int mouseX, int mouseY) {
        boolean isHovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
        
        context.fill(x, y, x + width, y + height, color);
        context.drawBorder(x, y, width, height, 0xFF6a6a6a);
        
        int textWidth = this.textRenderer.getWidth(text);
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - 8) / 2;
        drawText(context, textX, textY, text, 0xFFFFFF);
    }
    
    private void drawBezierCurve(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        // Simple bezier curve approximation
        int cx1 = x1 + 100;
        int cy1 = y1;
        int cx2 = x2 - 100;
        int cy2 = y2;
        
        // Draw curve as line segments
        for (int t = 0; t <= 20; t++) {
            float ft = (float) t / 20.0f;
            float ftn = (float) (t + 1) / 20.0f;
            
            int px1 = (int) bezierPoint(x1, cx1, cx2, x2, ft);
            int py1 = (int) bezierPoint(y1, cy1, cy2, y2, ft);
            int px2 = (int) bezierPoint(x1, cx1, cx2, x2, ftn);
            int py2 = (int) bezierPoint(y1, cy1, cy2, y2, ftn);
            
            // Draw line as a thin rectangle
            if (Math.abs(px2 - px1) > Math.abs(py2 - py1)) {
                // Horizontal line
                int y = Math.min(py1, py2);
                context.fill(px1, y, px2, y + 1, color);
            } else {
                // Vertical line
                int x = Math.min(px1, px2);
                context.fill(x, py1, x + 1, py2, color);
            }
        }
    }
    
    private float bezierPoint(float p0, float p1, float p2, float p3, float t) {
        float u = 1 - t;
        return u*u*u*p0 + 3*u*u*t*p1 + 3*u*t*t*p2 + t*t*t*p3;
    }
}
