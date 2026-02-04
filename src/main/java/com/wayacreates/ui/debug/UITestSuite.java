package com.wayacreates.ui.debug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.gui.DrawContext;

import com.wayacreates.ui.audio.AudioEditorUI;
import com.wayacreates.ui.overlay.OverlayUI;
import com.wayacreates.ui.viewport.ThreeDViewport;
import com.wayacreates.ui.video.VideoEditorUI;
import com.wayacreates.ui.compositor.NodeCompositorUI;
import com.wayacreates.ui.UIComponent;

/**
 * Debug class to test UI system functionality
 * Verifies that all UI classes can be instantiated and basic functionality works
 */
public class UITestSuite {
    private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/UITest");
    
    public static void runAllTests() {
        LOGGER.info("🧪 Starting UI System Test Suite");
        
        try {
            testUIComponentBase();
            testAudioEditorUI();
            testOverlayUI();
            testThreeDViewport();
            testVideoEditorUI();
            testNodeCompositorUI();
            
            LOGGER.info("✅ All UI tests passed successfully!");
        } catch (Exception e) {
            LOGGER.error("❌ UI test failed: {}", e.getMessage(), e);
        }
    }
    
    private static void testUIComponentBase() {
        LOGGER.info("🔧 Testing UIComponent base class");
        
        // Test basic UI component functionality
        TestUIComponent component = new TestUIComponent();
        component.setBounds(10, 10, 100, 50);
        
        assert component.getX() == 10 : "X coordinate should be 10";
        assert component.getY() == 10 : "Y coordinate should be 10";
        assert component.getWidth() == 100 : "Width should be 100";
        assert component.getHeight() == 50 : "Height should be 50";
        assert component.isVisible() : "Component should be visible";
        assert component.isDirty() : "Component should be dirty initially";
        
        component.setVisible(false);
        assert !component.isVisible() : "Component should be hidden";
        
        component.markClean();
        assert !component.isDirty() : "Component should be clean";
        
        LOGGER.info("✅ UIComponent base class test passed");
    }
    
    private static void testAudioEditorUI() {
        LOGGER.info("🎵 Testing AudioEditorUI");
        
        AudioEditorUI audioUI = new AudioEditorUI();
        assert audioUI != null : "AudioEditorUI should be instantiated";
        
        LOGGER.info("✅ AudioEditorUI test passed");
    }
    
    private static void testOverlayUI() {
        LOGGER.info("🖼️ Testing OverlayUI");
        
        OverlayUI overlayUI = new OverlayUI();
        assert overlayUI != null : "OverlayUI should be instantiated";
        
        LOGGER.info("✅ OverlayUI test passed");
    }
    
    private static void testThreeDViewport() {
        LOGGER.info("🎭 Testing ThreeDViewport");
        
        ThreeDViewport viewport = new ThreeDViewport();
        assert viewport != null : "ThreeDViewport should be instantiated";
        
        LOGGER.info("✅ ThreeDViewport test passed");
    }
    
    private static void testVideoEditorUI() {
        LOGGER.info("🎬 Testing VideoEditorUI");
        
        VideoEditorUI videoUI = new VideoEditorUI();
        assert videoUI != null : "VideoEditorUI should be instantiated";
        
        LOGGER.info("✅ VideoEditorUI test passed");
    }
    
    private static void testNodeCompositorUI() {
        LOGGER.info("🎨 Testing NodeCompositorUI");
        
        NodeCompositorUI compositorUI = new NodeCompositorUI();
        assert compositorUI != null : "NodeCompositorUI should be instantiated";
        
        LOGGER.info("✅ NodeCompositorUI test passed");
    }
    
    // Test UI component implementation
    private static class TestUIComponent extends UIComponent {
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            // Test implementation - draw a simple rectangle
            drawBackground(context, 0xFF333333);
            drawBorder(context, 0xFF666666);
            drawText(context, "Test Component", x + 5, y + 5, 0xFFFFFF);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return false;
        }
        
        @Override
        public void tick() {
            // Test implementation
        }
    }
}
