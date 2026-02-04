# WayaCreates Engine Wiki Documentation

## 📚 Table of Contents

### 🚀 Getting Started
- [Installation Guide](#installation-guide)
- [Quick Start Tutorial](#quick-start-tutorial)
- [First Time Setup](#first-time-setup)
- [Basic Configuration](#basic-configuration)

### 🎬 Core Features
- [Video Editor](#video-editor)
- [3D Viewport](#3d-viewport)
- [Node Compositor](#node-compositor)
- [Audio Editor](#audio-editor)
- [Recording & Livestream](#recording--livestream)

### 🔧 Advanced Features
- [Shader Support](#shader-support)
- [Entity Model Features](#entity-model-features)
- [Fresh Animations Integration](#fresh-animations-integration)
- [Plugin Development](#plugin-development)
- [Debug Commands](#debug-commands)

### 📖 Reference
- [Command Reference](#command-reference)
- [Keyboard Shortcuts](#keyboard-shortcuts)
- [Configuration Options](#configuration-options)
- [Troubleshooting](#troubleshooting)

---

## 🚀 Getting Started

### Installation Guide

#### Prerequisites
- **Minecraft**: 1.20.1 with Fabric Loader 0.14.21+
- **Java**: 17 or higher (Java 21 recommended)
- **RAM**: 8GB+ recommended for heavy projects

#### Required Mods
1. **Fabric API** - Core modding framework
2. **Mod Menu** - UI navigation
3. **Sodium** - Performance optimization (recommended)
4. **Iris** - Shader support (recommended)
5. **Entity Texture Features** - For Fresh Animations
6. **Fresh Animations** - Enhanced character animations

#### Installation Steps
1. Install Fabric Loader for Minecraft 1.20.1
2. Download required dependencies from Modrinth
3. Download WayaCreates Engine v2.1.0
4. Place all JAR files in `.minecraft/mods`
5. Launch with Fabric profile

### Quick Start Tutorial

#### Step 1: First Launch
1. Join any Minecraft world (Creative mode recommended)
2. Press **F1** to open WayaCreates Engine
3. Complete the interactive tutorial (5 minutes)

#### Step 2: Basic Recording
1. Press **F1** → Video Editor
2. Click **Record** button or press **R**
3. Record some gameplay footage
4. Press **R** again to stop recording

#### Step 3: Basic Editing
1. Use the timeline to trim clips
2. Add transitions between clips
3. Import audio files or record voiceover
4. Export your video

### First Time Setup

#### Configuration File Location
```
config/wayacreates-engine.json
```

#### Recommended Settings
```json
{
  "video": {
    "defaultResolution": "1920x1080",
    "defaultFrameRate": 30,
    "hardwareAcceleration": true
  },
  "audio": {
    "sampleRate": 48000,
    "bitRate": 320000
  },
  "ui": {
    "theme": "dark",
    "autoSave": true
  }
}
```

---

## 🎬 Core Features

### Video Editor

#### Interface Overview
- **Timeline**: Multi-track editing interface
- **Preview Window**: Real-time video preview
- **Effects Panel**: Video effects and transitions
- **Properties Panel**: Clip settings and adjustments

#### Basic Editing
1. **Import Media**: Drag files or use **I** key
2. **Trim Clips**: Drag clip edges on timeline
3. **Split Clips**: Position cursor and press **S**
4. **Add Transitions**: Select transition from panel
5. **Export**: Press **E** or click Export button

#### Advanced Features
- **Keyframe Animation**: Animate properties over time
- **Color Grading**: Professional color correction
- **Speed Control**: Slow motion or time-lapse
- **Multi-angle Editing**: Switch between camera angles

### 3D Viewport

#### Navigation Controls
- **Orbit**: Right-click + drag
- **Pan**: Middle-click + drag
- **Zoom**: Scroll wheel
- **Focus**: **F** key on selected object

#### View Modes
- **Solid**: Basic 3D view
- **Wireframe**: See mesh structure
- **Material**: Textured view
- **Rendered**: Full quality preview

#### Animation Tools
- **Timeline**: Keyframe-based animation
- **Graph Editor**: Fine-tune animation curves
- **Pose Library**: Pre-made character poses
- **Camera Animation**: Animate camera movement

### Node Compositor

#### Node Types
- **Input**: Media sources
- **Effects**: Visual effects and filters
- **Transform**: Position, scale, rotation
- **Output**: Final render output

#### Creating Effects
1. Add input nodes for media
2. Connect nodes with drag-and-drop
3. Adjust node parameters
4. Preview in real-time
5. Export final result

### Audio Editor

#### Track Management
- **Multi-track**: Multiple audio layers
- **Volume Automation**: Animate volume changes
- **Effects**: Reverb, delay, EQ, compression
- **Mixing**: Balance between tracks

#### Audio Features
- **Noise Reduction**: Clean up background noise
- **Audio Sync**: Perfect timing with video
- **Format Support**: MP3, WAV, FLAC, AAC
- **Real-time Preview**: Hear changes instantly

---

## 🔧 Advanced Features

### Shader Support

#### Iris Integration
WayaCreates Engine fully supports Iris shader loader:

```bash
/wce shaders          # Check shader status
/wce shaders reload  # Reload shaders
```

#### Supported Shader Features
- **Real-time Preview**: See shader effects in viewport
- **Custom Shaders**: Use any Iris-compatible shader
- **Performance**: GPU-accelerated rendering
- **Compatibility**: Works with Sodium

#### Recommended Shaders
- **SEUS PTGI**: High-quality ray tracing
- **BSL Shaders**: Balanced performance and quality
- **Sildur's Vibrant**: Colorful and vibrant

### Entity Model Features

#### Integration
Complete support for Entity Texture Features and Entity Model Features:

```bash
/wce entities         # Check entity features status
/wce entities reload  # Reload entity features
```

#### Features
- **Custom Models**: Use OptiFine CEM models
- **Texture Variations**: Random and custom textures
- **Emissive Textures**: Glowing effects
- **Animation Support**: Enhanced character animations

### Fresh Animations Integration

#### Setup
1. Install Fresh Animations resource pack
2. Enable in resource pack menu
3. Restart Minecraft
4. Use `/wce entities reload` to refresh

#### Features
- **Enhanced Animations**: Smoother character movements
- **Custom Poses**: Access to extensive pose library
- **Real-time Preview**: See animations in viewport
- **Export Options**: Save animations for other projects

### Plugin Development

#### Creating a Plugin
1. Create new Java project
2. Add WayaCreates Engine API dependency
3. Implement `WayaCreatesPlugin` interface
4. Create plugin manifest
5. Package as JAR and place in plugins folder

#### Example Plugin
```java
public class MyPlugin implements WayaCreatesPlugin {
    @Override
    public void onInitialize(PluginContext context) {
        // Plugin initialization code
    }
    
    @Override
    public void onEnable() {
        // Enable plugin functionality
    }
    
    @Override
    public void onEvent(String eventName, Object... args) {
        // Handle events
    }
}
```

#### Plugin Manifest
```json
{
  "id": "my-plugin",
  "name": "My Custom Plugin",
  "version": "1.0.0",
  "author": "Your Name",
  "description": "A custom plugin for WayaCreates Engine",
  "mainClass": "com.example.MyPlugin",
  "apiVersion": "2.1.0",
  "license": "MIT"
}
```

### Debug Commands

#### System Commands
```bash
/wce debug            # Show system status and debug info
/wce status           # Check mod status and health
/wce reload           # Reload all UI components
```

#### Feature Testing
```bash
/wce ui               # Test UI components
/wce shaders          # Test shader functionality
/wce entities         # Test entity model features
/wce audio            # Test audio system
/wce video            # Test video system
```

#### Development Commands
```bash
/wce logs             # Show recent logs
/wce config           # Show current configuration
/wce performance      # Show performance stats
/wce reset            # Reset UI to default state
```

---

## 📖 Reference

### Command Reference

#### Video Commands
```bash
/wayacreates video editor          # Open video editor
/wayacreates video record          # Start recording
/wayacreates video stop            # Stop recording
/wayacreates video project <name>  # Create new project
/wayacreates video export          # Export current project
```

#### 3D Commands
```bash
/wayacreates viewport open         # Open 3D viewport
/wayacreates viewport mode <mode>  # Set view mode
/wayacreates viewport shader <shader>  # Set shader
/wayacreates viewport reset        # Reset camera
```

#### Audio Commands
```bash
/wayacreates audio editor          # Open audio editor
/wayacreates audio record          # Start audio recording
/wayacreates audio import <file>   # Import audio file
/wayacreates audio export          # Export audio
```

#### System Commands
```bash
/wayacreates tutorial start       # Start tutorial
/wayacreates tutorial skip        # Skip tutorial
/wayacreates config reload        # Reload configuration
/wayacreates plugin list          # List plugins
```

### Keyboard Shortcuts

#### Global Shortcuts
- **F1**: Toggle WayaCreates Engine
- **Tab**: Switch between editor tabs
- **Space**: Play/Pause
- **Ctrl+S**: Save project
- **Ctrl+Z**: Undo
- **Ctrl+Y**: Redo
- **ESC**: Close dialog

#### Video Editor
- **I**: Import media
- **E**: Export project
- **R**: Record
- **S**: Split clip
- **T**: Add transition
- **M**: Add marker
- **Delete**: Remove selected item

#### 3D Viewport
- **1-5**: Switch view modes
- **G**: Toggle grid
- **N**: Toggle gizmo
- **Shift+D**: Duplicate selection
- **Ctrl+R**: Reset camera
- **F**: Focus on selection

#### Audio Editor
- **R**: Record audio
- **I**: Import audio
- **M**: Mute/unmute track
- **S**: Solo track
- **V**: Volume automation

### Configuration Options

#### Video Settings
```json
{
  "video": {
    "defaultResolution": "1920x1080",
    "defaultFrameRate": 30,
    "defaultBitrate": 8000000,
    "hardwareAcceleration": true,
    "previewQuality": "HIGH",
    "maxConcurrentEffects": 4
  }
}
```

#### Audio Settings
```json
{
  "audio": {
    "sampleRate": 48000,
    "bitRate": 320000,
    "channels": 2,
    "bufferSize": 1024,
    "enableEffects": true
  }
}
```

#### UI Settings
```json
{
  "ui": {
    "theme": "dark",
    "autoSave": true,
    "autoSaveInterval": 300,
    "showTooltips": true,
    "enableAnimations": true,
    "uiScale": 100
  }
}
```

#### Performance Settings
```json
{
  "performance": {
    "maxMemoryUsage": 2048,
    "threadCount": 4,
    "cacheSize": 512,
    "enableMultithreading": true,
    "gpuAcceleration": true
  }
}
```

### Troubleshooting

#### Common Issues

**Mod Won't Load**
- Verify Fabric Loader installation
- Check Minecraft version (1.20.1)
- Ensure all dependencies are installed
- Check for conflicting mods

**Performance Issues**
- Install Sodium for performance
- Lower video quality settings
- Reduce render distance
- Close background applications

**Recording Problems**
- Check disk space availability
- Lower recording resolution
- Enable hardware acceleration
- Update graphics drivers

**Audio Issues**
- Check system audio settings
- Verify microphone permissions
- Adjust sample rate settings
- Test different audio formats

**Shader Issues**
- Verify Iris installation
- Check shader compatibility
- Update graphics drivers
- Try different shader packs

#### Getting Help
- **Discord**: [Join our community](https://discord.gg/wayacreates)
- **GitHub Issues**: [Report problems](https://github.com/WayaSteurbautYT/WayaCreatesEngine/issues)
- **Wiki**: [Full documentation](https://github.com/WayaSteurbautYT/WayaCreatesEngine/wiki)

---

## 🎯 Tips and Best Practices

### For Beginners
1. **Start Simple**: Begin with basic video editing
2. **Use Tutorials**: Complete the interactive tutorial
3. **Experiment**: Try different effects and transitions
4. **Save Often**: Use auto-save or manual saves
5. **Join Community**: Get help and share creations

### For Advanced Users
1. **Master Shortcuts**: Learn keyboard shortcuts
2. **Custom Settings**: Optimize configuration for your needs
3. **Plugin Development**: Create custom functionality
4. **Performance Tuning**: Optimize for large projects
5. **Collaboration**: Share projects and plugins

### Project Ideas
- **Machinima**: Create animated stories
- **Tutorials**: Record and edit tutorial videos
- **Cinematics**: Make cinematic Minecraft scenes
- **Music Videos**: Combine gameplay with music
- **Let's Play**: Enhance gameplay recordings

---

**Happy Creating with WayaCreates Engine! 🎮🎬✨**

*For the Minecraft Community, by the Minecraft Community* ❤️
