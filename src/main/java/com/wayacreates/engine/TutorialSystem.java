package com.wayacreates.engine;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Professional Tutorial System for WayaCreates Engine
 * Beginner-friendly guide with interactive tutorials and skip options
 */
public class TutorialSystem {
    private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/Tutorial");
    
    // Debug flag for verbose logging
    private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
    
    // Tutorial state
    private boolean isEnabled = true;
    private boolean isFirstTime = true;
    private TutorialStep currentStep = null;
    private int currentStepIndex = 0;
    private final Map<UUID, UserProgress> userProgress = new ConcurrentHashMap<>();
    
    // Tutorial content
    private TutorialChapter[] tutorialChapters;
    
    public TutorialSystem() {
        if (DEBUG_MODE) {
            LOGGER.debug("📚 Initializing tutorial system in DEBUG mode");
        }
        initializeTutorialContent();
    }
    
    public void initialize() {
        if (DEBUG_MODE) {
            LOGGER.debug("📚 Tutorial system initialization starting...");
            LOGGER.debug("- Loading user progress data");
            LOGGER.debug("- Checking first-time user status");
        }
        
        LOGGER.info("📚 Tutorial System initialized");
        
        // Load saved progress
        loadUserProgress();
        
        // Check if first time user
        if (isFirstTime) {
            showWelcomeMessage();
        }
        
        // Save initial progress state
        saveUserProgress();
        
        if (DEBUG_MODE) {
            LOGGER.debug("📚 Tutorial system initialization completed");
        }
    }
    
    private void initializeTutorialContent() {
        tutorialChapters = new TutorialChapter[] {
            new TutorialChapter("Getting Started", 
                new TutorialStep[] {
                    new TutorialStep("Welcome to WayaCreates Engine", 
                        """
                        Welcome to WayaCreates Engine! This is a professional Minecraft animation and video editing system.
                        
                        Let's take a quick tour to get you started with the basics.
                        
                        You can skip this tutorial at any time by pressing ESC.""", 
                        TutorialStep.Type.WELCOME),
                    
                    new TutorialStep("Main Interface Overview", 
                        """
                        The WayaCreates Engine has several main components:
                        
                        • 🎬 Video Editor - Professional video editing with timeline
                        • 🎭 3D Viewport - Blender-style 3D scene editor
                        • 🎨 Node Compositor - After Effects style node editor
                        • 🎵 Audio Editor - Professional audio mixing
                        • 📹 Recording & Livestream - Capture and broadcast
                        
                        You can switch between these using the tab keys or the main menu.""", 
                        TutorialStep.Type.INTERFACE),
                    
                    new TutorialStep("Basic Controls", 
                        """
                        Here are the essential controls:
                        
                        • F1 - Open/Close WayaCreates Engine
                        • Tab - Switch between editor tabs
                        • Space - Play/Pause in any editor
                        • Ctrl+S - Save current project
                        • Ctrl+Z - Undo (where supported)
                        • ESC - Close current dialog/tutorial
                        
                        Each editor has its own specific controls which we'll cover next.""" , 
                        TutorialStep.Type.CONTROLS)
                }),
            
            new TutorialChapter("Video Editor Basics",
                new TutorialStep[] {
                    new TutorialStep("Video Editor Introduction", 
                        """
                        The Video Editor is your main workspace for creating professional Minecraft videos.
                        
                        It features:
                        • Multi-track timeline for video, audio, and effects
                        • Real-time preview window
                        • Professional effects library
                        • Color grading tools
                        • Export options for various formats
                        
                        Let's explore the key components.""", 
                        TutorialStep.Type.VIDEO_EDITOR),
                    
                    new TutorialStep("Timeline Navigation", 
                        """
                        The timeline is where you'll spend most of your time:
                        
                        • Click and drag to scrub through your video
                        • Use mouse wheel to zoom in/out
                        • Right-click to add clips or effects
                        • Drag clips to rearrange them
                        • Double-click clips to edit properties
                        
                        The timeline supports multiple tracks for video, audio, and effects.""" , 
                        TutorialStep.Type.TIMELINE),
                    
                    new TutorialStep("Adding Effects", 
                        """
                        Effects can transform your videos:
                        
                        1. Open the Effects panel on the right
                        2. Browse categories like Color, Blur, Transition
                        3. Drag an effect onto a clip in the timeline
                        4. Adjust effect parameters in the Properties panel
                        
                        Popular effects include:
                        • Color Correction - Fix colors and exposure
                        • Blur/Gaussian - Soften or create depth
                        • Transitions - Smooth clip changes
                        • Text/Title - Add text overlays""", 
                        TutorialStep.Type.EFFECTS)
                }),
            
            new TutorialChapter("3D Viewport",
                new TutorialStep[] {
                    new TutorialStep("3D Viewport Overview", 
                        """
                        The 3D Viewport lets you create and manipulate 3D scenes:
                        
                        • Blender-style navigation
                        • Real-time rendering with shaders
                        • Character rig integration
                        • Animation timeline
                        • Camera controls
                        
                        This is perfect for creating custom animations and scenes.""", 
                        TutorialStep.Type.VIEWPORT),
                    
                    new TutorialStep("3D Navigation", 
                        """
                        Navigate the 3D space like a pro:
                        
                        • Left Mouse - Select objects
                        • Right Mouse + Drag - Rotate view
                        • Middle Mouse + Drag - Pan view
                        • Scroll - Zoom in/out
                        
                        Practice these controls to become proficient!
                        
                        View Modes:
                        1 - Solid (default)
                        2 - Wireframe
                        3 - Material preview
                        4 - Full render""", 
                        TutorialStep.Type.NAVIGATION),
                    
                    new TutorialStep("Working with Rigs", 
                        """
                        The WayaCreates Engine includes custom character rigs:
                        
                        • Based on professional Blender rigs
                        • Fresh Animations integration
                        • Custom pose library
                        • Keyframe animation
                        
                        To animate:
                        1. Select a character rig
                        2. Set keyframes on the timeline
                        3. Adjust poses between keyframes
                        4. Preview animation in real-time""", 
                        TutorialStep.Type.RIGGING)
                }),
            
            new TutorialChapter("Node Compositor",
                new TutorialStep[] {
                    new TutorialStep("Node Compositor Introduction", 
                        """
                        The Node Compositor offers professional compositing:
                        
                        • After Effects style node workflow
                        • Real-time preview
                        • Advanced color grading
                        • Custom effects creation
                        • Plugin support
                        
                        Perfect for complex visual effects and color grading.""", 
                        TutorialStep.Type.NODES),
                    
                    new TutorialStep("Creating Node Networks", 
                        """
                        Build powerful effects with nodes:
                        
                        • Drag nodes from the library
                        • Connect ports to create effects
                        • Adjust parameters in Properties
                        • Preview results in real-time
                        
                        Common workflows:
                        • Color Correction: Input → Color Grade → Output
                        • Effects: Input → Effect → Output
                        • Compositing: Multiple inputs → Composite → Output""", 
                        TutorialStep.Type.NODE_WORKFLOW)
                }),
            
            new TutorialChapter("Advanced Features",
                new TutorialStep[] {
                    new TutorialStep("Recording & Livestream", 
                        """
                        Capture and share your creations:
                        
                        Recording:
                        • High-quality video capture
                        • Multiple resolution options
                        • Audio synchronization
                        • Real-time effects
                        
                        Livestream:
                        • Direct streaming to platforms
                        • Overlay support
                        • Chat integration
                        • Real-time effects""", 
                        TutorialStep.Type.RECORDING),
                    
                    new TutorialStep("Audio Editor", 
                        """
                        Professional audio editing:
                        
                        • Multi-track audio mixing
                        • Audio effects and filters
                        • Noise reduction
                        • Volume automation
                        • Audio synchronization
                        
                        Perfect for adding music, sound effects, and voiceovers.""", 
                        TutorialStep.Type.AUDIO),
                    
                    new TutorialStep("Export & Sharing", 
                        "Share your creations with the world:\n\n" +
                        "Export Formats:\n" +
                        "• MP4 - Most compatible\n" +
                        "• MOV - High quality\n" +
                        "• AVI - Uncompressed\n" +
                        "• WebM - Web optimized\n\n" +
                        "Quality Options:\n" +
                        "• Draft - Fast preview\n" +
                        "• Standard - Good balance\n" +
                        "• High - Professional quality\n" +
                        "• Ultra - Maximum quality", 
                        TutorialStep.Type.EXPORT)
                })
        };
    }
    
    public void showWelcomeMessage() {
        TutorialStep welcomeStep = new TutorialStep(
            "Welcome to WayaCreates Engine!",
            "🎬 Welcome to WayaCreates Engine! 🎬\n\n" +
            "This is a professional Minecraft animation and video editing system designed for creators like you.\n\n" +
            "✨ Key Features:\n" +
            "• Professional video editor with timeline\n" +
            "• 3D viewport with Blender-style controls\n" +
            "• Node-based compositor for effects\n" +
            "• Audio editing and mixing\n" +
            "• Recording and livestream capabilities\n" +
            "• Fresh Animations integration\n\n" +
            "Would you like to take a quick tutorial to learn the basics?\n\n" +
            "Press 'Y' for tutorial, 'N' to skip, or ESC to close this message.",
            TutorialStep.Type.WELCOME
        );
        
        currentStep = welcomeStep;
        currentStepIndex = 0;
        isFirstTime = false;
    }
    
    public void startTutorial(UUID playerId) {
        UserProgress progress = userProgress.computeIfAbsent(playerId, k -> new UserProgress());
        progress.reset();
        
        currentStepIndex = 0;
        currentStep = tutorialChapters[0].steps[0];
        progress.currentChapter = 0;
        progress.currentStep = 0;
        progress.isActive = true;
        
        LOGGER.info("📚 Started tutorial for player: {}", playerId);
    }
    
    public void nextStep(UUID playerId) {
        if (currentStep == null) return;
        
        currentStepIndex++;
        
        // Find the next step
        int chapterIndex = 0;
        int stepIndex = currentStepIndex;
        
        for (int i = 0; i < tutorialChapters.length; i++) {
            if (stepIndex < tutorialChapters[i].steps.length) {
                chapterIndex = i;
                break;
            }
            stepIndex -= tutorialChapters[i].steps.length;
        }
        
        if (chapterIndex < tutorialChapters.length && stepIndex < tutorialChapters[chapterIndex].steps.length) {
            currentStep = tutorialChapters[chapterIndex].steps[stepIndex];
            
            // Update user progress
            UserProgress progress = userProgress.get(playerId);
            if (progress != null) {
                progress.currentChapter = chapterIndex;
                progress.currentStep = stepIndex;
            }
        } else {
            // Tutorial completed
            completeTutorial(playerId);
        }
    }
    
    public void previousStep(UUID playerId) {
        if (currentStepIndex > 0) {
            currentStepIndex--;
            
            // Find the previous step
            int chapterIndex = 0;
            int stepIndex = currentStepIndex;
            
            for (int i = 0; i < tutorialChapters.length; i++) {
                if (stepIndex < tutorialChapters[i].steps.length) {
                    chapterIndex = i;
                    break;
                }
                stepIndex -= tutorialChapters[i].steps.length;
            }
            
            if (chapterIndex < tutorialChapters.length && stepIndex >= 0) {
                currentStep = tutorialChapters[chapterIndex].steps[stepIndex];
                
                // Update user progress
                UserProgress progress = userProgress.get(playerId);
                if (progress != null) {
                    progress.currentChapter = chapterIndex;
                    progress.currentStep = stepIndex;
                }
            }
        }
    }
    
    public void skipTutorial(UUID playerId) {
        UserProgress progress = userProgress.get(playerId);
        if (progress != null) {
            progress.skipped = true;
            progress.isActive = false;
        }
        
        currentStep = null;
        LOGGER.info("⏭️ Tutorial skipped by player: {}", playerId);
    }
    
    public void completeTutorial(UUID playerId) {
        UserProgress progress = userProgress.get(playerId);
        if (progress != null) {
            progress.completed = true;
            progress.isActive = false;
            progress.completionTime = System.currentTimeMillis();
        }
        
        currentStep = null;
        LOGGER.info("🎉 Tutorial completed by player: {}", playerId);
    }
    
    public void tick() {
        // Update tutorial system
        // Handle timed tutorials, auto-advance, etc.
    }
    
    public TutorialStep getCurrentStep() {
        return currentStep;
    }
    
    public boolean isInTutorial() {
        return currentStep != null;
    }
    
    public boolean isEnabled() {
        return isEnabled;
    }
    
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
    
    private void loadUserProgress() {
        // TODO: Load user progress from file
    }
    
    private void saveUserProgress() {
        // TODO: Save user progress to file
        
        // Call this method when tutorial progress changes to avoid unused warning
        if (DEBUG_MODE) {
            LOGGER.debug("💾 Saving user progress for {} users", userProgress.size());
        }
    }
    
    // Tutorial data classes
    public static class TutorialChapter {
        public final String title;
        public final TutorialStep[] steps;
        
        public TutorialChapter(String title, TutorialStep[] steps) {
            this.title = title;
            this.steps = steps;
        }
    }
    
    public static class TutorialStep {
        public final String title;
        public final String content;
        public final Type type;
        
        public TutorialStep(String title, String content, Type type) {
            this.title = title;
            this.content = content;
            this.type = type;
        }
        
        public enum Type {
            WELCOME,
            INTERFACE,
            CONTROLS,
            VIDEO_EDITOR,
            TIMELINE,
            EFFECTS,
            VIEWPORT,
            NAVIGATION,
            RIGGING,
            NODES,
            NODE_WORKFLOW,
            RECORDING,
            AUDIO,
            EXPORT
        }
    }
    
    public static class UserProgress {
        public int currentChapter = 0;
        public int currentStep = 0;
        public boolean isActive = false;
        public boolean completed = false;
        public boolean skipped = false;
        public long startTime = 0;
        public long completionTime = 0;
        
        public void reset() {
            currentChapter = 0;
            currentStep = 0;
            isActive = true;
            completed = false;
            skipped = false;
            startTime = System.currentTimeMillis();
            completionTime = 0;
        }
    }
}
