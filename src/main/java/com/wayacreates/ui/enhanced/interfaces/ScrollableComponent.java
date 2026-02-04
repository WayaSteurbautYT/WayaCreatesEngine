package com.wayacreates.ui.enhanced.interfaces;

/**
 * Interface for UI components that support scrolling functionality
 */
public interface ScrollableComponent {
    /**
     * Called when the mouse wheel is scrolled on this component
     * @param mouseX Current mouse X position
     * @param mouseY Current mouse Y position
     * @param horizontalAmount Horizontal scroll amount
     * @param verticalAmount Vertical scroll amount
     * @return true if the scroll was handled, false otherwise
     */
    boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount);
    
    /**
     * Sets the scroll position for this component
     * @param scrollX Horizontal scroll position
     * @param scrollY Vertical scroll position
     */
    void setScrollPosition(double scrollX, double scrollY);
    
    /**
     * Gets the current horizontal scroll position
     * @return Current horizontal scroll position
     */
    double getScrollX();
    
    /**
     * Gets the current vertical scroll position
     * @return Current vertical scroll position
     */
    double getScrollY();
}
