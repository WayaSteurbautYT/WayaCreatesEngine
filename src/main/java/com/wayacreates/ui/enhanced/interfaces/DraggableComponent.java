package com.wayacreates.ui.enhanced.interfaces;

/**
 * Interface for UI components that support dragging functionality
 */
public interface DraggableComponent {
    /**
     * Called when the mouse is dragged on this component
     * @param mouseX Current mouse X position
     * @param mouseY Current mouse Y position
     * @param button Mouse button being dragged
     * @param deltaX Horizontal movement since last drag event
     * @param deltaY Vertical movement since last drag event
     * @return true if the drag was handled, false otherwise
     */
    boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY);
}
