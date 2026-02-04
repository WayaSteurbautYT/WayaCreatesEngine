package com.wayacreates.ui.compositor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import com.wayacreates.ui.UIComponent;
import com.wayacreates.ui.node.NodeSystem;

/**
 * Node Compositor UI - Component Classes
 * Part 2 of 2 - All UI component implementations
 */
public class NodeCompositorUIComponents {
    
    // Component classes
    public static class NodeLibrary extends UIComponent {
        private final List<NodeType> nodeTypes = new ArrayList<>();
        
        public void addNodeType(NodeType nodeType) {
            nodeTypes.add(nodeType);
        }
        
        public int getNodeTypeCount() {
            return nodeTypes.size();
        }
        
        public NodeSystem.Node createNode(String nodeTypeName) {
            for (NodeType type : nodeTypes) {
                if (type.getName().equals(nodeTypeName)) {
                    return type.createInstance();
                }
            }
            return null;
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawText(context, "Node Library", x + 10, y + 10, 0xFFFFFF);
            
            // Draw node types
            int nodeY = y + 30;
            for (NodeType nodeType : nodeTypes) {
                boolean isHovered = mouseY >= nodeY && mouseY <= nodeY + 25;
                int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
                
                context.fill(x + 5, nodeY, x + width - 10, nodeY + 25, color);
                context.drawBorder(x + 5, nodeY, width - 10, 25, 0xFF6a6a6a);
                
                var textRenderer = MinecraftClient.getInstance().textRenderer;
                context.drawText(textRenderer, nodeType.getName(), x + 10, nodeY + 5, 0xFFFFFF, false);
                
                nodeY += 30;
            }
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class NodeType {
        private final String name;
        private final String category;
        private final String description;
        
        public NodeType(String name, String category, String description) {
            this.name = name;
            this.category = category;
            this.description = description;
        }
        
        public NodeSystem.Node createInstance() {
            return new NodeSystem.Node(name, 0, 0) {
                @Override
                public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                    // Basic node rendering
                }
                
                @Override
                public void tick() {
                    // Basic node ticking
                }
            };
        }
        
        public String getName() { return name; }
        public String getCategory() { return category; }
        public String getDescription() { return description; }
    }
    
    public static class NodeEditor extends UIComponent {
        private final List<NodeSystem.Node> nodes = new ArrayList<>();
        private final List<NodeSystem.Connection> connections = new ArrayList<>();
        
        public void addNode(NodeSystem.Node node) {
            nodes.add(node);
        }
        
        public void removeNode(NodeSystem.Node node) {
            nodes.remove(node);
        }
        
        public void addConnection(NodeSystem.Connection connection) {
            connections.add(connection);
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF1a1a1a);
            drawBorder(context, 0xFF4a4a4a);
            
            // Draw grid
            drawGrid(context);
            
            // Draw connections
            for (NodeSystem.Connection connection : connections) {
                drawConnection(context, connection);
            }
            
            // Draw nodes
            for (NodeSystem.Node node : nodes) {
                drawNode(context, node, mouseX, mouseY);
            }
        }
        
        private void drawGrid(DrawContext context) {
            int gridSize = 20;
            
            // Draw vertical lines
            for (int gx = x; gx < x + width; gx += gridSize) {
                context.fill(gx, y, gx + 1, y + height, 0xFF2a2a2a);
            }
            
            // Draw horizontal lines
            for (int gy = y; gy < y + height; gy += gridSize) {
                context.fill(x, gy, x + width, gy + 1, 0xFF2a2a2a);
            }
        }
        
        private void drawNode(DrawContext context, NodeSystem.Node node, int mouseX, int mouseY) {
            int nodeX = x + (int)node.getX();
            int nodeY = y + (int)node.getY();
            int nodeWidth = 120;
            int nodeHeight = 60;
            
            boolean isHovered = mouseX >= nodeX && mouseX <= nodeX + nodeWidth && 
                               mouseY >= nodeY && mouseY <= nodeY + nodeHeight;
            int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
            
            context.fill(nodeX, nodeY, nodeX + nodeWidth, nodeY + nodeHeight, color);
            context.drawBorder(nodeX, nodeY, nodeWidth, nodeHeight, 0xFF6a6a6a);
            
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, node.getName(), nodeX + 5, nodeY + 5, 0xFFFFFF, false);
            context.drawText(textRenderer, "Node", nodeX + 5, nodeY + 20, 0xCCCCCC, false);
            
            // Draw ports
            drawPorts(context, node, nodeX, nodeY, nodeWidth, nodeHeight);
        }
        
        private void drawPorts(DrawContext context, NodeSystem.Node node, int nodeX, int nodeY, int nodeWidth, int nodeHeight) {
            // Input ports (left)
            for (int i = 0; i < node.getInputPorts().size(); i++) {
                int portY = nodeY + 30 + i * 10;
                context.fill(nodeX - 5, portY, nodeX, portY + 5, 0xFF6a6a4a);
            }
            
            // Output ports (right)
            for (int i = 0; i < node.getOutputPorts().size(); i++) {
                int portY = nodeY + 30 + i * 10;
                context.fill(nodeX + nodeWidth, portY, nodeX + nodeWidth + 5, portY + 5, 0xFF4a6a6a);
            }
        }
        
        private void drawConnection(DrawContext context, NodeSystem.Connection connection) {
            // Simplified connection drawing
            NodeSystem.Node fromNode = connection.getFromNode();
            NodeSystem.Node toNode = connection.getToNode();
            
            int fromX = x + (int)fromNode.getX() + 120;
            int fromY = y + (int)fromNode.getY() + 35;
            int toX = x + (int)toNode.getX();
            int toY = y + (int)toNode.getY() + 35;
            
            // Draw bezier curve (simplified as straight line)
            context.fill(Math.min(fromX, toX), Math.min(fromY, toY), 
                        Math.max(fromX, toX) + 1, Math.max(fromY, toY) + 1, 0xFF4a4a4a);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class NodeProperties extends UIComponent {
        private NodeSystem.Node selectedNode = null;
        
        public void setSelectedNode(NodeSystem.Node node) {
            this.selectedNode = node;
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawText(context, "Node Properties", x + 10, y + 10, 0xFFFFFF);
            
            if (selectedNode != null) {
                drawText(context, "Name:", x + 10, y + 30, 0xCCCCCC);
                drawText(context, selectedNode.getName(), x + 60, y + 30, 0xFFFFFF);
                
                drawText(context, "Category:", x + 10, y + 45, 0xCCCCCC);
                drawText(context, selectedNode.getName(), x + 60, y + 45, 0xFFFFFF);
                
                drawText(context, "Position:", x + 10, y + 60, 0xCCCCCC);
                String posText = String.format("%d, %d", selectedNode.getX(), selectedNode.getY());
                drawText(context, posText, x + 60, y + 60, 0xFFFFFF);
            } else {
                drawText(context, "No node selected", x + 10, y + 30, 0x66FFFFFF);
            }
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class NodePreview extends UIComponent {
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF252525);
            drawBorder(context, 0xFF4a4a4a);
            drawText(context, "Node Preview", x + 10, y + 10, 0xFFFFFF);
            
            // Draw preview area
            int previewX = x + 10;
            int previewY = y + 30;
            int previewWidth = width - 20;
            int previewHeight = height - 40;
            
            context.fill(previewX, previewY, previewX + previewWidth, previewY + previewHeight, 0xFF0a0a0a);
            context.drawBorder(previewX, previewY, previewWidth, previewHeight, 0xFF3a3a3a);
            
            // Center text
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            String previewText = "Node Output Preview";
            int textWidth = textRenderer.getWidth(previewText);
            int textX = previewX + (previewWidth - textWidth) / 2;
            int textY = previewY + (previewHeight - 8) / 2;
            context.drawText(textRenderer, previewText, textX, textY, 0x66FFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
}
