package com.wayacreates.ui.node;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import com.wayacreates.ui.compositor.NodeCompositorUIComponents;

/**
 * Specific node types for the Node Compositor
 * Contains InputNode, OutputNode, ColorCorrectionNode, and EffectNode
 */
public class NodeTypes {
    
    public static class SimpleInputNode extends NodeSystem.Node {
        public SimpleInputNode(String name, int x, int y) {
            super(name, x, y);
            getPorts().add(new NodeSystem.NodePort(this, "Output", NodeSystem.PortType.OUTPUT, getWidth() - 10, getHeight() / 2));
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF2a4a2a);
            context.drawBorder(getX(), getY(), getWidth(), getHeight(), 0xFF4a6a4a);
            context.drawText(MinecraftClient.getInstance().textRenderer, getName(), getX() + 5, getY() + 5, 0xFFFFFF, false);
        }

        @Override
        public void tick() {}
    }
    
    public static class InputNode extends NodeSystem.Node implements AutoCloseable {
        private final InputStream inputStream;
        private final Closeable closeable;

        public InputNode(String name, int x, int y, InputStream inputStream, Closeable closeable) {
            super(name, x, y);
            this.inputStream = inputStream;
            this.closeable = closeable;
            getPorts().add(new NodeSystem.NodePort(this, "Output", NodeSystem.PortType.OUTPUT, getWidth() - 10, getHeight() / 2));
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF2a4a2a);
            context.drawBorder(getX(), getY(), getWidth(), getHeight(), 0xFF4a6a4a);
            context.drawText(MinecraftClient.getInstance().textRenderer, getName(), getX() + 5, getY() + 5, 0xFFFFFF, false);
        }

        @Override
        public void tick() {}

        @Override
        public void close() throws IOException {
            if (closeable != null) {
                closeable.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
    
    public static class OutputNode extends NodeSystem.Node {
        public OutputNode(String name, int x, int y) {
            super(name, x, y);
            getPorts().add(new NodeSystem.NodePort(this, "Input", NodeSystem.PortType.INPUT, 10, getHeight() / 2));
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF4a2a2a);
            context.drawBorder(getX(), getY(), getWidth(), getHeight(), 0xFF6a4a4a);
            context.drawText(MinecraftClient.getInstance().textRenderer, getName(), getX() + 5, getY() + 5, 0xFFFFFF, false);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class ColorCorrectionNode extends NodeSystem.Node {
        public ColorCorrectionNode(String name, int x, int y) {
            super(name, x, y);
            getPorts().add(new NodeSystem.NodePort(this, "Input", NodeSystem.PortType.INPUT, 10, getHeight() / 2));
            getPorts().add(new NodeSystem.NodePort(this, "Output", NodeSystem.PortType.OUTPUT, getWidth() - 10, getHeight() / 2));
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF3a3a4a);
            context.drawBorder(getX(), getY(), getWidth(), getHeight(), 0xFF5a5a6a);
            context.drawText(MinecraftClient.getInstance().textRenderer, getName(), getX() + 5, getY() + 5, 0xFFFFFF, false);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class EffectNode extends NodeSystem.Node {
        public EffectNode(String name, int x, int y) {
            super(name, x, y);
            getPorts().add(new NodeSystem.NodePort(this, "Input", NodeSystem.PortType.INPUT, 10, getHeight() / 2));
            getPorts().add(new NodeSystem.NodePort(this, "Output", NodeSystem.PortType.OUTPUT, getWidth() - 10, getHeight() / 2));
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF4a3a3a);
            context.drawBorder(getX(), getY(), getWidth(), getHeight(), 0xFF6a5a5a);
            context.drawText(MinecraftClient.getInstance().textRenderer, getName(), getX() + 5, getY() + 5, 0xFFFFFF, false);
        }
        
        @Override
        public void tick() {}
    }
    
    // Factory methods for creating node types
    public static NodeCompositorUIComponents.NodeType createInputNode() {
        return new NodeCompositorUIComponents.NodeType("Input", "I/O", "Input node for media sources");
    }
    
    public static NodeCompositorUIComponents.NodeType createOutputNode() {
        return new NodeCompositorUIComponents.NodeType("Output", "I/O", "Output node for final render");
    }
    
    public static NodeCompositorUIComponents.NodeType createColorGradeNode() {
        return new NodeCompositorUIComponents.NodeType("Color Grade", "Color", "Color correction and grading");
    }
    
    public static NodeCompositorUIComponents.NodeType createBlurNode() {
        return new NodeCompositorUIComponents.NodeType("Blur", "Effect", "Gaussian blur effect");
    }
    
    public static NodeCompositorUIComponents.NodeType createTransformNode() {
        return new NodeCompositorUIComponents.NodeType("Transform", "Transform", "Scale, rotate, position");
    }
    
    public static NodeCompositorUIComponents.NodeType createCompositeNode() {
        return new NodeCompositorUIComponents.NodeType("Composite", "Composite", "Layer blending and compositing");
    }
}
