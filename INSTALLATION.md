# Installation Guide

## 📋 System Requirements

### Minimum Requirements
- **Minecraft**: 1.20.1
- **Java**: 17 or higher
- **RAM**: 4GB minimum (8GB recommended)
- **GPU**: OpenGL 3.3 compatible (for 3D viewport)

### Recommended Setup
- **Minecraft**: 1.20.1 with Fabric Loader 0.14.21+
- **Java**: 17+ (Java 21 recommended)
- **RAM**: 8GB+ (16GB for heavy projects)
- **Mods**: Sodium, Iris, Fresh Animations

## 🚀 Step-by-Step Installation

### 1. Install Fabric Loader
1. Download [Fabric Installer](https://fabricmc.net/use/installer/)
2. Run the installer
3. Select "Minecraft 1.20.1"
4. Create a new Fabric profile

### 2. Download Required Mods
Download these mods from Modrinth or CurseForge:

#### Essential Dependencies
- **Fabric API** (Required)
- **Mod Menu** (Required for UI navigation)

#### Performance Enhancements
- **Sodium** (Recommended for performance)
- **Iris** (For shader support)
- **Entity Texture Features** (For Fresh Animations)

#### Animation Support
- **Fresh Animations** (Enhanced character animations)
- **Continuity** (Connected textures for better visuals)

### 3. Install WayaCreates Engine
1. Download the latest `wayacreates-engine-2.1.0.jar` from [GitHub Releases](https://github.com/WayaSteurbautYT/WayaCreatesEngine/releases)
2. Place the JAR file in your `.minecraft/mods` folder
3. Launch Minecraft with your Fabric profile

### 4. First Launch Setup
1. Join any Minecraft world (creative mode recommended)
2. Press **F1** to open WayaCreates Engine
3. Follow the interactive tutorial
4. Configure your preferred settings

## 🔧 Configuration

### Basic Settings
Access settings via: `config/wayacreates-engine.json`

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

### Performance Optimization
For better performance, adjust these settings:

```json
{
  "video": {
    "previewQuality": "MEDIUM",
    "maxConcurrentEffects": 4
  },
  "viewport": {
    "renderDistance": 64,
    "shadows": false
  }
}
```

## 🎮 Quick Start Guide

### Opening the Interface
- **F1**: Toggle WayaCreates Engine
- **Tab**: Switch between editor tabs
- **ESC**: Close current dialog

### Debug Commands (New in v2.1.0)
Use these commands for debugging and testing:

```bash
/wce debug                    # Show system status and debug info
/wce reload                   # Reload all UI components
/wce ui                       # Test UI components
/wce shaders                  # Test shader functionality
/wce entities                 # Test entity model features
/wce status                   # Check mod status
```

### Basic Workflow
1. **Create Project**: Use `/wayacreates project new <name>`
2. **Record Footage**: Press F1 → Video Editor → Record
3. **Edit Video**: Use timeline, effects, and transitions
4. **Add Audio**: Import audio or record voiceover
5. **Export**: Choose your preferred format and quality

## 🔌 Plugin Installation

### Installing Plugins
1. Download plugin JAR files
2. Place in `.minecraft/wayacreates-plugins/`
3. Restart Minecraft
4. Enable plugins via `/wayacreates plugin list`

### Recommended Starter Plugins
- **Transition Pack**: Additional video transitions
- **Color Presets**: Professional color grading presets
- **Audio Effects**: Reverb, delay, EQ effects
- **Shader Pack**: Enhanced visual effects

## 🐛 Troubleshooting

### Common Issues

#### Mod Won't Load
**Symptoms**: Game crashes on startup or mod doesn't appear
**Solutions**:
1. Verify Fabric Loader is installed correctly
2. Check you're using Minecraft 1.20.1
3. Ensure all dependencies are installed
4. Check for conflicting mods

#### Performance Issues
**Symptoms**: Lag, low FPS, stuttering
**Solutions**:
1. Install Sodium for performance boost
2. Lower video quality settings
3. Reduce render distance in viewport
4. Close unnecessary background applications

#### Recording Problems
**Symptoms**: Recording fails, poor quality
**Solutions**:
1. Check available disk space
2. Lower recording resolution
3. Ensure hardware acceleration is enabled
4. Update graphics drivers

#### Audio Issues
**Symptoms**: No sound, poor quality
**Solutions**:
1. Check system audio settings
2. Verify microphone permissions
3. Adjust audio sample rate settings
4. Test with different audio formats

### Getting Help
- **Discord**: [Join our community](https://discord.gg/wayacreates)
- **GitHub Issues**: [Report problems](https://github.com/WayaSteurbautYT/WayaCreatesEngine/issues)
- **Wiki**: [Detailed documentation](https://github.com/WayaSteurbautYT/WayaCreatesEngine/wiki)

## 📱 Keyboard Shortcuts

### Global Shortcuts
- **F1**: Toggle WayaCreates Engine
- **Tab**: Switch between editors
- **Space**: Play/Pause
- **Ctrl+S**: Save project
- **Ctrl+Z**: Undo
- **Ctrl+Y**: Redo
- **ESC**: Close dialog

### Video Editor
- **I**: Import media
- **E**: Export project
- **R**: Start/stop recording
- **T**: Add transition
- **M**: Add marker

### 3D Viewport
- **1-5**: Switch view modes
- **G**: Toggle grid
- **N**: Toggle gizmo
- **Shift+D**: Duplicate selection
- **Ctrl+R**: Reset camera

### Audio Editor
- **R**: Record audio
- **I**: Import audio
- **M**: Mute/unmute track
- **S**: Solo track
- **V**: Volume automation

## 🎯 Tips for Beginners

### Getting Started
1. **Start Simple**: Begin with basic video editing
2. **Use Tutorials**: Complete the interactive tutorial first
3. **Experiment**: Try different effects and transitions
4. **Save Often**: Use auto-save or manual saves frequently

### Best Practices
- **Organize Projects**: Use descriptive project names
- **Backup Files**: Keep copies of important projects
- **Learn Shortcuts**: Master keyboard shortcuts for efficiency
- **Join Community**: Get help and share your creations

### Project Ideas
- **Machinima**: Create animated stories
- **Tutorials**: Record and edit tutorial videos
- **Cinematics**: Make cinematic Minecraft scenes
- **Music Videos**: Combine gameplay with music
- **Let's Play**: Enhance your gameplay recordings

---

**Need more help?** Check our [Wiki](https://github.com/WayaSteurbautYT/WayaCreatesEngine/wiki) or join our [Discord](https://discord.gg/wayacreates)!
