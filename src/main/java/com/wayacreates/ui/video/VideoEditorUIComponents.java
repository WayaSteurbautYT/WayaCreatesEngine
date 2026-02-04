package com.wayacreates.ui.video;

import java.util.ArrayList;
import java.util.List;

import com.wayacreates.ui.UIComponent;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * Video Editor UI - Component Classes
 * Part 2 of 2 - All UI component implementations
 */
public class VideoEditorUIComponents {
    
    // Component classes
    public static class Timeline extends UIComponent {
        private final List<Track> tracks = new ArrayList<>();
        private float duration = 10.0f;
        private float currentTime = 0.0f;
        private float zoomLevel = 1.0f;
        
        public void addTrack(Track track) {
            tracks.add(track);
        }
        
        public void removeTrack(Track track) {
            tracks.remove(track);
        }
        
        public List<Track> getTracks() {
            return new ArrayList<>(tracks);
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF252525);
            drawBorder(context, 0xFF4a4a4a);
            drawText(context, Text.translatable("wayacreates.video.timeline").getString(), x + 10, y + 10, 0xFFFFFF);
            
            // Draw tracks
            int trackY = y + 30;
            for (Track track : tracks) {
                drawTrack(context, track, trackY);
                trackY += 25;
            }
            
            // Draw playhead
            int playheadX = x + (int)((currentTime / duration) * (width - 20));
            context.fill(playheadX, y + 30, playheadX + 2, y + height - 10, 0xFFff4444);
        }
        
        private void drawTrack(DrawContext context, Track track, int trackY) {
            int trackColor = track.getType() == Track.Type.VIDEO ? 0xFF4a6a4a : 0xFF6a4a4a;
            context.fill(x + 10, trackY, x + width - 10, trackY + 20, trackColor);
            context.drawBorder(x + 10, trackY, width - 20, 20, 0xFF6a6a6a);
            
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawText(textRenderer, track.getName(), x + 15, trackY + 3, 0xFFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {
            if (currentTime < duration) {
                currentTime += 1.0f / 30.0f; // 30 FPS
            }
        }
        
        public void setCurrentTime(float time) {
            this.currentTime = Math.max(0, Math.min(duration, time));
        }
        
        public void setZoomLevel(float zoom) {
            this.zoomLevel = Math.max(0.1f, Math.min(10.0f, zoom));
        }
    }
    
    public static class Track {
        private final String name;
        private final Type type;
        private boolean muted = false;
        private boolean locked = false;
        
        public Track(String name, Type type) {
            this.name = name;
            this.type = type;
        }
        
        public static Track createVideoTrack(String name) {
            return new Track(name, Type.VIDEO);
        }
        
        public static Track createAudioTrack(String name) {
            return new Track(name, Type.AUDIO);
        }
        
        public enum Type {
            VIDEO, AUDIO
        }
        
        // Getters and setters
        public String getName() { return name; }
        public Type getType() { return type; }
        public boolean isMuted() { return muted; }
        public void setMuted(boolean muted) { this.muted = muted; }
        public boolean isLocked() { return locked; }
        public void setLocked(boolean locked) { this.locked = locked; }
    }
    
    public static class EffectsPanel extends UIComponent {
        private final List<VideoEffect> effects = new ArrayList<>();
        
        public EffectsPanel() {
            // Add default effects
            effects.add(new VideoEffect(Text.translatable("wayacreates.video.effect_blur").getString(), "blur"));
            effects.add(new VideoEffect(Text.translatable("wayacreates.video.effect_color").getString(), "color"));
            effects.add(new VideoEffect(Text.translatable("wayacreates.video.effect_transition").getString(), "transition"));
            effects.add(new VideoEffect(Text.translatable("wayacreates.video.effect_text").getString(), "text"));
            effects.add(new VideoEffect(Text.translatable("wayacreates.video.effect_chroma").getString(), "chroma"));
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawText(context, Text.translatable("wayacreates.video.effects").getString(), x + 10, y + 10, 0xFFFFFF);
            
            // Draw effect list
            int effectY = y + 30;
            for (VideoEffect effect : effects) {
                boolean isHovered = mouseY >= effectY && mouseY <= effectY + 25;
                int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
                
                context.fill(x + 5, effectY, x + width - 10, effectY + 25, color);
                context.drawBorder(x + 5, effectY, width - 10, 25, 0xFF6a6a6a);
                
                var textRenderer = MinecraftClient.getInstance().textRenderer;
                context.drawText(textRenderer, effect.getName(), x + 10, effectY + 5, 0xFFFFFF, false);
                
                effectY += 30;
            }
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class VideoEffect {
        private final String name;
        private final String id;
        
        public VideoEffect(String name, String id) {
            this.name = name;
            this.id = id;
        }
        
        public String getName() { return name; }
        public String getId() { return id; }
    }
    
    public static class MediaLibrary extends UIComponent {
        private final List<MediaItem> mediaItems = new ArrayList<>();
        
        public MediaLibrary() {
            // Add sample media items
            mediaItems.add(new MediaItem("video1.mp4", MediaItem.Type.VIDEO));
            mediaItems.add(new MediaItem("audio1.mp3", MediaItem.Type.AUDIO));
            mediaItems.add(new MediaItem("image1.png", MediaItem.Type.IMAGE));
        }
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawText(context, Text.translatable("wayacreates.video.media_library").getString(), x + 10, y + 10, 0xFFFFFF);
            
            // Draw media items
            int itemY = y + 30;
            for (MediaItem item : mediaItems) {
                boolean isHovered = mouseY >= itemY && mouseY <= itemY + 25;
                int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
                
                context.fill(x + 5, itemY, x + width - 10, itemY + 25, color);
                context.drawBorder(x + 5, itemY, width - 10, 25, 0xFF6a6a6a);
                
                var textRenderer = MinecraftClient.getInstance().textRenderer;
                String icon = item.getType() == MediaItem.Type.VIDEO ? "🎬" : 
                            item.getType() == MediaItem.Type.AUDIO ? "🎵" : "🖼️";
                context.drawText(textRenderer, icon + " " + item.getName(), x + 10, itemY + 5, 0xFFFFFF, false);
                
                itemY += 30;
            }
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class MediaItem {
        private final String name;
        private final Type type;
        
        public MediaItem(String name, Type type) {
            this.name = name;
            this.type = type;
        }
        
        public enum Type {
            VIDEO, AUDIO, IMAGE
        }
        
        public String getName() { return name; }
        public Type getType() { return type; }
    }
    
    public static class PreviewWindow extends UIComponent {
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF1a1a1a);
            drawBorder(context, 0xFF4a4a4a);
            drawText(context, Text.translatable("wayacreates.video.preview").getString(), x + 10, y + 10, 0xFFFFFF);
            
            // Draw preview area (simplified)
            int previewX = x + 10;
            int previewY = y + 30;
            int previewWidth = width - 20;
            int previewHeight = height - 40;
            
            context.fill(previewX, previewY, previewX + previewWidth, previewY + previewHeight, 0xFF0a0a0a);
            context.drawBorder(previewX, previewY, previewWidth, previewHeight, 0xFF3a3a3a);
            
            // Center text in preview area
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            String previewText = Text.translatable("wayacreates.video.preview_area").getString();
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
    
    public static class ToolBar extends UIComponent {
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF333333);
            
            // Draw toolbar buttons
            int buttonX = x + 10;
            String[] buttons = {
                Text.translatable("wayacreates.video.toolbar_new").getString(),
                Text.translatable("wayacreates.video.toolbar_open").getString(),
                Text.translatable("wayacreates.video.toolbar_save").getString(),
                Text.translatable("wayacreates.video.toolbar_export").getString(),
                Text.translatable("wayacreates.video.toolbar_cut").getString(),
                Text.translatable("wayacreates.video.toolbar_copy").getString(),
                Text.translatable("wayacreates.video.toolbar_paste").getString()
            };
            
            for (String button : buttons) {
                drawButton(context, buttonX, y + 5, 60, 30, button, mouseX, mouseY);
                buttonX += 70;
            }
        }
        
        private void drawButton(DrawContext context, int bx, int by, int bw, int bh, String text, int mouseX, int mouseY) {
            boolean isHovered = mouseX >= bx && mouseX <= bx + bw && mouseY >= by && mouseY <= by + bh;
            int color = isHovered ? 0xFF4a4a4a : 0xFF3a3a3a;
            
            context.fill(bx, by, bx + bw, by + bh, color);
            context.drawBorder(bx, by, bw, bh, 0xFF6a6a6a);
            
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            int textWidth = textRenderer.getWidth(text);
            int textX = bx + (bw - textWidth) / 2;
            int textY = by + (bh - 8) / 2;
            context.drawText(textRenderer, text, textX, textY, 0xFFFFFF, false);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
    
    public static class PropertiesPanel extends UIComponent {
        
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            drawBackground(context, 0xFF2a2a2a);
            drawText(context, Text.translatable("wayacreates.video.properties").getString(), x + 10, y + 10, 0xFFFFFF);
            
            // Draw property fields
            drawText(context, Text.translatable("wayacreates.video.resolution").getString(), x + 10, y + 30, 0xCCCCCC);
            drawText(context, Text.translatable("wayacreates.video.value_resolution").getString(), x + 80, y + 30, 0xFFFFFF);
            
            drawText(context, Text.translatable("wayacreates.video.frame_rate").getString(), x + 10, y + 45, 0xCCCCCC);
            drawText(context, Text.translatable("wayacreates.video.value_frame_rate").getString(), x + 80, y + 45, 0xFFFFFF);
            
            drawText(context, Text.translatable("wayacreates.video.duration").getString(), x + 10, y + 60, 0xCCCCCC);
            drawText(context, Text.translatable("wayacreates.video.value_duration").getString(), x + 80, y + 60, 0xFFFFFF);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInBounds(mouseX, mouseY);
        }
        
        @Override
        public void tick() {}
    }
}
