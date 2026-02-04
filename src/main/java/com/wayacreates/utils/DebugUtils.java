package com.wayacreates.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralized Debug Utilities for WayaCreates Engine
 * Provides consistent debug logging patterns across all components
 */
public class DebugUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger("WayaCreates/Debug");
    private static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("wayacreates.debug", "false"));
    
    /**
     * Log debug message with component context
     */
    public static void debug(String component, String message) {
        if (DEBUG_MODE) {
            LOGGER.debug("[{}] {}", component, message);
        }
    }
    
    /**
     * Log debug message with component context and parameters
     */
    public static void debug(String component, String message, Object... params) {
        if (DEBUG_MODE) {
            LOGGER.debug("[{}] " + message, component, params);
        }
    }
    
    /**
     * Log TODO implementation tracking
     */
    public static void trackTodo(String component, String feature, String details) {
        if (DEBUG_MODE) {
            LOGGER.debug("[{}] TODO: {} - {}", component, feature, details);
        }
    }
    
    /**
     * Check if debug mode is enabled
     */
    public static boolean isDebugEnabled() {
        return DEBUG_MODE;
    }
    
    /**
     * Log component initialization
     */
    public static void logInitialization(String component, String... details) {
        if (DEBUG_MODE) {
            LOGGER.debug("🔧 Initializing {}...", component);
            for (String detail : details) {
                LOGGER.debug("- {}", detail);
            }
        }
    }
    
    /**
     * Log component completion
     */
    public static void logCompletion(String component) {
        if (DEBUG_MODE) {
            LOGGER.debug("✅ {} initialization completed", component);
        }
    }
}
