package com.wayacreates.ui.node;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.DrawContext;

/**
 * Core node system classes for the Node Compositor
 * Contains Node, NodePort, Connection, and NodeGraph classes
 */
public class NodeSystem {
    
    public enum PortType {
        INPUT, OUTPUT
    }
    
    public static abstract class Node {
        protected String name;
        protected int x, y;
        protected int width = 150;
        protected int height = 80;
        protected List<NodePort> ports = new ArrayList<>();
        
        public Node(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
        
        public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);
        public abstract void tick();
        
        public boolean isPointInside(int px, int py) {
            return px >= x && px <= x + width && py >= y && py <= y + height;
        }
        
        // Getters
        public String getName() { return name; }
        public int getX() { return x; }
        public int getY() { return y; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
        public List<NodePort> getPorts() { return ports; }
        
        public List<NodePort> getInputPorts() {
            return ports.stream()
                .filter(port -> port.getPortType() == PortType.INPUT)
                .collect(java.util.stream.Collectors.toList());
        }
        
        public List<NodePort> getOutputPorts() {
            return ports.stream()
                .filter(port -> port.getPortType() == PortType.OUTPUT)
                .collect(java.util.stream.Collectors.toList());
        }
        
        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public static class NodePort {
        private final Node node;
        private final String name;
        private final PortType portType;
        private final int x, y;
        
        public NodePort(Node node, String name, PortType portType, int x, int y) {
            this.node = node;
            this.name = name;
            this.portType = portType;
            this.x = x;
            this.y = y;
        }
        
        // Getters
        public Node getNode() { return node; }
        public String getName() { return name; }
        public PortType getPortType() { return portType; }
        public int getX() { return x; }
        public int getY() { return y; }
    }
    
    public static class Connection {
        private final NodePort startPort;
        private final NodePort endPort;
        
        public Connection(NodePort startPort, NodePort endPort) {
            this.startPort = startPort;
            this.endPort = endPort;
        }
        
        public NodePort getStartPort() { return startPort; }
        public NodePort getEndPort() { return endPort; }
        
        public Node getFromNode() { return startPort.getNode(); }
        public Node getToNode() { return endPort.getNode(); }
    }
    
    public static class NodeGraph {
        private final List<Node> nodes = new ArrayList<>();
        private final List<Connection> connections = new ArrayList<>();
        private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
        
        public void addNode(Node node) {
            nodes.add(node);
        }
        
        public void removeNode(Node node) {
            nodes.remove(node);
            // Remove connections to this node
            connections.removeIf(conn -> 
                conn.getStartPort().getNode() == node || conn.getEndPort().getNode() == node);
        }
        
        public void addConnection(Connection connection) {
            connections.add(connection);
        }
        
        public void clear() {
            nodes.clear();
            connections.clear();
        }
        
        public void tick() {
            // Update all nodes
            for (Node node : nodes) {
                node.tick();
            }
        }
        
        public List<Node> getNodes() { return nodes; }
        public List<Connection> getConnections() { return connections; }
        
        public int getNodeCount() { return nodes.size(); }
        
        public void removeNode(String nodeId) {
            nodes.removeIf(node -> node.getName().equals(nodeId));
        }
        
        public void connectNodes(String fromNodeId, String toNodeId, String fromPort, String toPort) {
            // TODO: Implement node connection logic
            if (DEBUG_MODE) {
                System.out.println("Connecting " + fromNodeId + ":" + fromPort + " to " + toNodeId + ":" + toPort);
            }
        }
        
        public void update(float currentTime) {
            tick(); // Use existing tick method
        }
        
        public static void saveToFile(NodeGraph nodeGraph, String filePath) {
            // TODO: Implement node graph serialization to file
            // This would typically use JSON, XML, or binary format
            if (DEBUG_MODE) {
                System.out.println("Saving node graph to: " + filePath);
                System.out.println("Nodes: " + nodeGraph.getNodes().size());
                System.out.println("Connections: " + nodeGraph.getConnections().size());
            }
        }
        
        public static NodeGraph loadFromFile(String filePath) {
            // TODO: Implement node graph deserialization from file
            // This would create a new NodeGraph and populate it from the file
            if (DEBUG_MODE) {
                System.out.println("Loading node graph from: " + filePath);
            }
            NodeGraph nodeGraph = new NodeGraph();
            // TODO: Actually load and deserialize the file
            return nodeGraph;
        }
    }
}
