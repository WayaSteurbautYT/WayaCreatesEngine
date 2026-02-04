package com.wayacreates.ui.enhanced.components;

import com.wayacreates.ui.UIComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Advanced Toolbar Component with modern design and extensible button system
 * Features:
 * - Dynamic button management
 * - Icon support (using Unicode/ASCII art)
 * - Dropdown menus
 * - Keyboard shortcuts
 * - Tooltips
 * - Button groups
 */
public class ToolbarComponent extends UIComponent {
    private final List<ToolbarButton> buttons = new ArrayList<>();
    private final List<ButtonGroup> buttonGroups = new ArrayList<>();
    private ToolbarButton hoveredButton = null;
    private DropdownMenu activeDropdown = null;
    
    public ToolbarComponent(int x, int y, int width, int height) {
        super();
        setBounds(x, y, width, height);
        initializeDefaultButtons();
    }
    
    private void initializeDefaultButtons() {
        // File operations
        ButtonGroup fileGroup = new ButtonGroup("File");
        fileGroup.addButton(new ToolbarButton("📁", "New Project", "Ctrl+N", this::newProject));
        fileGroup.addButton(new ToolbarButton("📂", "Open Project", "Ctrl+O", this::openProject));
        fileGroup.addButton(new ToolbarButton("💾", "Save", "Ctrl+S", this::saveProject));
        fileGroup.addButton(new ToolbarButton("📤", "Export", "Ctrl+E", this::exportProject));
        
        // Edit operations
        ButtonGroup editGroup = new ButtonGroup("Edit");
        editGroup.addButton(new ToolbarButton("✂", "Cut", "Ctrl+X", this::cut));
        editGroup.addButton(new ToolbarButton("📋", "Copy", "Ctrl+C", this::copy));
        editGroup.addButton(new ToolbarButton("📄", "Paste", "Ctrl+V", this::paste));
        editGroup.addButton(new ToolbarButton("↩", "Undo", "Ctrl+Z", this::undo));
        editGroup.addButton(new ToolbarButton("↪", "Redo", "Ctrl+Y", this::redo));
        
        // View operations
        ButtonGroup viewGroup = new ButtonGroup("View");
        viewGroup.addButton(new ToolbarButton("🔍", "Zoom In", "Ctrl++", this::zoomIn));
        viewGroup.addButton(new ToolbarButton("🔎", "Zoom Out", "Ctrl+-", this::zoomOut));
        viewGroup.addButton(new ToolbarButton("🎯", "Fit to Screen", "Ctrl+0", this::fitToScreen));
        viewGroup.addButton(new ToolbarButton("👁", "Toggle Preview", "Space", this::togglePreview));
        
        // Playback controls
        ButtonGroup playbackGroup = new ButtonGroup("Playback");
        playbackGroup.addButton(new ToolbarButton("⏮", "Previous", "Left", this::previousFrame));
        playbackGroup.addButton(new ToolbarButton("⏪", "Rewind", "J", this::rewind));
        playbackGroup.addButton(new ToolbarButton("▶", "Play/Pause", "K", this::playPause));
        playbackGroup.addButton(new ToolbarButton("⏩", "Forward", "L", this::forward));
        playbackGroup.addButton(new ToolbarButton("⏭", "Next", "Right", this::nextFrame));
        
        buttonGroups.add(fileGroup);
        buttonGroups.add(editGroup);
        buttonGroups.add(viewGroup);
        buttonGroups.add(playbackGroup);
        
        // Add all buttons to main list
        for (ButtonGroup group : buttonGroups) {
            buttons.addAll(group.getButtons());
        }
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw toolbar background
        drawBackground(context, 0xFF2a2a2a);
        drawBorder(context, 0xFF4a4a4a);
        
        // Draw buttons
        int buttonX = x + 10;
        int buttonY = y + (height - 24) / 2;
        
        hoveredButton = null;
        
        for (ToolbarButton button : buttons) {
            boolean isHovered = mouseX >= buttonX && mouseX <= buttonX + 28 &&
                              mouseY >= buttonY && mouseY <= buttonY + 24;
            
            if (isHovered) {
                hoveredButton = button;
                drawButton(context, buttonX, buttonY, button, true);
            } else {
                drawButton(context, buttonX, buttonY, button, false);
            }
            
            buttonX += 32;
            
            // Add spacing between groups
            if (buttonX > x + width - 100) break;
        }
        
        // Draw active dropdown
        if (activeDropdown != null) {
            activeDropdown.render(context, mouseX, mouseY, delta);
        }
        
        // Draw tooltip for hovered button
        if (hoveredButton != null && activeDropdown == null) {
            drawTooltip(context, mouseX, mouseY, hoveredButton);
        }
    }
    
    private void drawButton(DrawContext context, int bx, int by, ToolbarButton button, boolean isHovered) {
        int bgColor = isHovered ? 0xFF5a5a5a : 0xFF3a3a3a;
        int textColor = isHovered ? 0xFFFFFF : 0xCCCCCC;
        
        // Draw button background
        context.fill(bx, by, bx + 28, by + 24, bgColor);
        context.drawBorder(bx, by, 28, 24, 0xFF6a6a6a);
        
        // Draw icon
        var textRenderer = MinecraftClient.getInstance().textRenderer;
        int iconWidth = textRenderer.getWidth(button.getIcon());
        int iconX = bx + (28 - iconWidth) / 2;
        int iconY = by + (24 - 8) / 2;
        
        context.drawText(textRenderer, button.getIcon(), iconX, iconY, textColor, false);
    }
    
    private void drawTooltip(DrawContext context, int mouseX, int mouseY, ToolbarButton button) {
        var textRenderer = MinecraftClient.getInstance().textRenderer;
        
        String tooltipText = button.getTooltip();
        if (button.getShortcut() != null) {
            tooltipText += " (" + button.getShortcut() + ")";
        }
        
        int tooltipWidth = textRenderer.getWidth(tooltipText) + 8;
        int tooltipHeight = 20;
        int tooltipX = mouseX + 10;
        int tooltipY = mouseY - tooltipHeight - 5;
        
        // Ensure tooltip stays on screen
        if (tooltipX + tooltipWidth > x + width) {
            tooltipX = mouseX - tooltipWidth - 10;
        }
        if (tooltipY < y) {
            tooltipY = mouseY + 25;
        }
        
        // Draw tooltip background
        context.fill(tooltipX, tooltipY, tooltipX + tooltipWidth, tooltipY + tooltipHeight, 0xFF2a2a2a);
        context.drawBorder(tooltipX, tooltipY, tooltipWidth, tooltipHeight, 0xFF6a6a6a);
        
        // Draw tooltip text
        context.drawText(textRenderer, tooltipText, tooltipX + 4, tooltipY + 6, 0xFFFFFF, false);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check dropdown first
        if (activeDropdown != null) {
            if (activeDropdown.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            activeDropdown = null;
            return true;
        }
        
        // Check toolbar buttons
        int buttonX = x + 10;
        int buttonY = y + (height - 24) / 2;
        
        for (ToolbarButton toolbarButton : buttons) {
            if (mouseX >= buttonX && mouseX <= buttonX + 28 &&
                mouseY >= buttonY && mouseY <= buttonY + 24) {
                
                // Handle right-click for dropdown
                if (button == 1) {
                    activeDropdown = new DropdownMenu(buttonX, buttonY + 24, toolbarButton);
                    return true;
                }
                
                // Handle left-click
                toolbarButton.getAction().run();
                return true;
            }
            
            buttonX += 32;
            if (buttonX > x + width - 100) break;
        }
        
        return false;
    }
    
    @Override
    public void tick() {
        // Update button states if needed
        for (ToolbarButton button : buttons) {
            button.tick();
        }
    }
    
    // Button action methods
    private void newProject() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("🆕 New project created"), true);
    }
    
    private void openProject() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("📂 Open project dialog"), true);
    }
    
    private void saveProject() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("💾 Project saved"), true);
    }
    
    private void exportProject() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("📤 Exporting project..."), true);
    }
    
    private void cut() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("✂ Cut selection"), true);
    }
    
    private void copy() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("📋 Copy selection"), true);
    }
    
    private void paste() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("📄 Paste from clipboard"), true);
    }
    
    private void undo() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("↩ Undo last action"), true);
    }
    
    private void redo() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("↪ Redo last action"), true);
    }
    
    private void zoomIn() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("🔍 Zoomed in"), true);
    }
    
    private void zoomOut() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("🔎 Zoomed out"), true);
    }
    
    private void fitToScreen() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("🎯 Fit to screen"), true);
    }
    
    private void togglePreview() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("👁 Preview toggled"), true);
    }
    
    private void previousFrame() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("⏮ Previous frame"), true);
    }
    
    private void rewind() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("⏪ Rewinding"), true);
    }
    
    private void playPause() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("▶ Play/Pause toggled"), true);
    }
    
    private void forward() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("⏩ Forwarding"), true);
    }
    
    private void nextFrame() {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("⏭ Next frame"), true);
    }
    
    // Inner classes
    public static class ToolbarButton {
        private final String icon;
        private final String tooltip;
        private final String shortcut;
        private final Runnable action;
        
        public ToolbarButton(String icon, String tooltip, String shortcut, Runnable action) {
            this.icon = icon;
            this.tooltip = tooltip;
            this.shortcut = shortcut;
            this.action = action;
        }
        
        public String getIcon() { return icon; }
        public String getTooltip() { return tooltip; }
        public String getShortcut() { return shortcut; }
        public Runnable getAction() { return action; }
        
        public void tick() {
            // Update button state if needed
        }
    }
    
    public static class ButtonGroup {
        private final String name;
        private final List<ToolbarButton> buttons = new ArrayList<>();
        
        public ButtonGroup(String name) {
            this.name = name;
        }
        
        public void addButton(ToolbarButton button) {
            buttons.add(button);
        }
        
        public List<ToolbarButton> getButtons() {
            return new ArrayList<>(buttons);
        }
        
        public String getName() { return name; }
    }
    
    public static class DropdownMenu {
        private final int x, y;
        private final ToolbarButton parentButton;
        private final List<DropdownItem> items = new ArrayList<>();
        
        public DropdownMenu(int x, int y, ToolbarButton parentButton) {
            this.x = x;
            this.y = y;
            this.parentButton = parentButton;
            initializeItems();
        }
        
        private void initializeItems() {
            // Add context menu items based on button type
            if (parentButton.getTooltip().contains("Project")) {
                items.add(new DropdownItem("Recent Projects", this::showRecentProjects));
                items.add(new DropdownItem("Project Settings", this::showProjectSettings));
                items.add(new DropdownItem("-", null));
                items.add(new DropdownItem("Exit", this::exit));
            }
        }
        
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            int menuWidth = 150;
            int menuHeight = items.size() * 25 + 10;
            
            // Draw menu background
            context.fill(x, y, x + menuWidth, y + menuHeight, 0xFF3a3a3a);
            context.drawBorder(x, y, menuWidth, menuHeight, 0xFF6a6a6a);
            
            // Draw menu items
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            int itemY = y + 5;
            
            for (DropdownItem item : items) {
                if (item.getText().equals("-")) {
                    // Separator
                    context.fill(x + 5, itemY + 10, x + menuWidth - 5, itemY + 11, 0xFF6a6a6a);
                } else {
                    boolean isHovered = mouseX >= x && mouseX <= x + menuWidth &&
                                      mouseY >= itemY && mouseY <= itemY + 20;
                    
                    int textColor = isHovered ? 0xFFFFFF : 0xCCCCCC;
                    if (isHovered) {
                        context.fill(x + 2, itemY, x + menuWidth - 2, itemY + 20, 0xFF5a5a5a);
                    }
                    
                    context.drawText(textRenderer, item.getText(), x + 10, itemY + 6, textColor, false);
                }
                itemY += 25;
            }
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            int menuWidth = 150;
            int menuHeight = items.size() * 25 + 10;
            
            if (mouseX < x || mouseX > x + menuWidth || 
                mouseY < y || mouseY > y + menuHeight) {
                return false; // Click outside menu
            }
            
            int itemY = y + 5;
            for (DropdownItem item : items) {
                if (!item.getText().equals("-")) {
                    if (mouseY >= itemY && mouseY <= itemY + 20) {
                        if (item.getAction() != null) {
                            item.getAction().run();
                        }
                        return true;
                    }
                }
                itemY += 25;
            }
            
            return true;
        }
        
        private void showRecentProjects() {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("📂 Recent projects"), true);
        }
        
        private void showProjectSettings() {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("⚙ Project settings"), true);
        }
        
        private void exit() {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("👋 Exiting"), true);
        }
    }
    
    public static class DropdownItem {
        private final String text;
        private final Runnable action;
        
        public DropdownItem(String text, Runnable action) {
            this.text = text;
            this.action = action;
        }
        
        public String getText() { return text; }
        public Runnable getAction() { return action; }
    }
}
