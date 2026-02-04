# Contributing to WayaCreates Engine

Thank you for your interest in contributing to WayaCreates Engine! This guide will help you get started with contributing to the project.

## 🤝 How to Contribute

### Reporting Bugs
- Use [GitHub Issues](https://github.com/WayaSteurbautYT/WayaCreatesEngine/issues) to report bugs
- Provide detailed information about the issue
- Include steps to reproduce the problem
- Add screenshots or videos if applicable

### Suggesting Features
- Open an issue with the "enhancement" label
- Describe the feature in detail
- Explain why it would be useful
- Consider implementation complexity

### Code Contributions
- Fork the repository
- Create a feature branch
- Make your changes
- Submit a pull request

## 🛠️ Development Setup

### Prerequisites
- **Java 17+** (Java 21 recommended)
- **Git** for version control
- **IDE** (IntelliJ IDEA or Eclipse recommended)
- **Gradle** for building

### Setting Up Your Development Environment

1. **Fork the Repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/WayaCreatesEngine.git
   cd WayaCreatesEngine/WayaCreatesEngine-1.20.1-fabric
   ```

2. **Import into IDE**
   - Open the project in IntelliJ IDEA
   - Let IDEA import the Gradle project
   - Wait for dependencies to download

3. **Build the Project**
   ```bash
   ./gradlew build
   ```

4. **Run in Debug Mode**
   - Create a run configuration for Minecraft
   - Add the mod JAR to the mods folder
   - Launch with Fabric Loader

### Project Structure
```
WayaCreatesEngine-1.20.1-fabric/
├── src/main/java/com/wayacreates/
│   ├── WayaCreatesEngine.java          # Main mod class
│   ├── engine/                         # Core engines
│   │   ├── VideoEngine.java           # Video processing
│   │   ├── RenderEngine.java          # 3D rendering
│   │   ├── AnimationEngine.java       # Animation system
│   │   ├── AudioEngine.java           # Audio processing
│   │   └── PluginManager.java         # Plugin management
│   ├── ui/                            # User interfaces
│   │   ├── VideoEditorUI.java         # Video editor
│   │   ├── ThreeDViewport.java         # 3D viewport
│   │   ├── NodeCompositorUI.java      # Node compositor
│   │   ├── AudioEditorUI.java         # Audio editor
│   │   └── OverlayUI.java             # Overlay system
│   ├── commands/                      # Command system
│   └── config/                        # Configuration
├── src/main/resources/                # Resources
└── build.gradle                       # Build configuration
```

## 📝 Coding Standards

### Java Style
- Use **camelCase** for variable and method names
- Use **PascalCase** for class names
- Use **UPPER_CASE** for constants
- Follow standard Java naming conventions

### Code Organization
- Keep classes focused on single responsibilities
- Use packages to organize related functionality
- Add comprehensive Javadoc comments
- Include examples in documentation

### Example Code Style
```java
/**
 * Video processing engine for WayaCreates Engine.
 * Handles video recording, editing, and export functionality.
 */
public class VideoEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/VideoEngine");
    
    /**
     * Starts video recording with the specified settings.
     * 
     * @param settings The recording settings to use
     * @return True if recording started successfully
     */
    public boolean startRecording(VideoSettings settings) {
        // Implementation
        return true;
    }
}
```

## 🔧 Plugin Development

### Creating a Plugin
1. Create a new Java project
2. Add WayaCreates Engine API as dependency
3. Implement the `WayaCreatesPlugin` interface
4. Create plugin manifest
5. Package as JAR

### Plugin Example
```java
public class MyPlugin implements WayaCreatesPlugin {
    @Override
    public void onInitialize(PluginContext context) {
        context.getLogger().info("MyPlugin initialized!");
    }
    
    @Override
    public void onEnable() {
        // Plugin enabled logic
    }
    
    @Override
    public void onEvent(String eventName, Object... args) {
        if ("video.record.start".equals(eventName)) {
            // Handle recording start
        }
    }
}
```

### Plugin Manifest
```json
{
  "id": "my-plugin",
  "name": "My Custom Plugin",
  "version": "1.0.0",
  "author": "Your Name",
  "description": "A custom plugin for WayaCreates Engine",
  "mainClass": "com.example.MyPlugin",
  "apiVersion": "2.0.0",
  "license": "MIT",
  "type": "wayacreates-plugin"
}
```

## 🧪 Testing

### Unit Tests
- Write tests for core functionality
- Use JUnit for testing framework
- Test edge cases and error conditions
- Maintain test coverage above 80%

### Integration Tests
- Test mod functionality in Minecraft
- Verify UI interactions work correctly
- Test plugin loading and unloading
- Validate export/import functionality

### Testing Guidelines
```java
@Test
public void VideoEngineTest {
    private VideoEngine videoEngine;
    
    @BeforeEach
    void setUp() {
        videoEngine = new VideoEngine();
    }
    
    @Test
    void shouldStartRecording() {
        VideoSettings settings = new VideoSettings();
        boolean result = videoEngine.startRecording(settings);
        assertTrue(result);
    }
}
```

## 📚 Documentation

### Code Documentation
- Add Javadoc comments to all public methods
- Document parameter types and return values
- Include usage examples
- Document exceptions that may be thrown

### README Updates
- Update README.md for new features
- Add installation instructions for new dependencies
- Update configuration examples
- Add troubleshooting information

### Wiki Contributions
- Create detailed guides for complex features
- Add video tutorials for visual learners
- Document API changes
- Translate documentation to other languages

## 🔄 Pull Request Process

### Before Submitting
1. **Test your changes** thoroughly
2. **Update documentation** if needed
3. **Run all tests** and ensure they pass
4. **Check code style** follows guidelines
5. **Update CHANGELOG.md** for significant changes

### Pull Request Template
```markdown
## Description
Brief description of changes made.

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing completed

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] CHANGELOG.md updated
```

### Review Process
1. **Automated checks** run on all PRs
2. **Code review** by maintainers
3. **Testing verification** in test environment
4. **Approval** and merge to main branch

## 🚀 Release Process

### Version Bumping
- **Patch** (x.x.1): Bug fixes
- **Minor** (x.1.0): New features
- **Major** (1.0.0): Breaking changes

### Release Checklist
- [ ] All tests pass
- [ ] Documentation updated
- [ ] CHANGELOG.md updated
- [ ] Version number updated
- [ ] Release notes written
- [ ] Assets prepared

## 🤝 Community Guidelines

### Code of Conduct
- Be respectful and inclusive
- Welcome newcomers and help them learn
- Focus on constructive feedback
- Avoid personal attacks or harassment

### Communication
- Use clear and descriptive titles for issues and PRs
- Provide context for changes
- Ask questions if anything is unclear
- Share knowledge and help others

### Recognition
- Contributors are credited in the project
- Significant contributions may earn maintainer status
- Community achievements are celebrated

## 📞 Getting Help

### Discord Community
- Join our [Discord server](https://discord.gg/wayacreates)
- Ask questions in #development channel
- Share your progress and get feedback

### GitHub Discussions
- Use [GitHub Discussions](https://github.com/WayaSteurbautYT/WayaCreatesEngine/discussions) for questions
- Share ideas and proposals
- Get help from the community

### Maintainer Contact
- For urgent issues, contact maintainers directly
- Report security vulnerabilities privately
- Discuss major changes before implementation

## 🏆 Recognition

### Contributors
All contributors are recognized in:
- README.md contributors section
- Release notes
- Annual community highlights

### Maintainer Criteria
- Consistent quality contributions
- Community engagement
- Technical expertise
- Commitment to project goals

---

Thank you for contributing to WayaCreates Engine! Your help makes this project better for everyone. 🎉
