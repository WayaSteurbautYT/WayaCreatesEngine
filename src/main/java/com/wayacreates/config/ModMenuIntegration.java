package com.wayacreates.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

/**
 * Mod Menu Integration for WayaCreates Engine
 * Provides configuration screen integration with Mod Menu
 */
public class ModMenuIntegration implements ModMenuApi {
    
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::getConfigScreen;
    }
    
    private Screen getConfigScreen(Screen parent) {
        // TODO: Create configuration screen
        // For now, return null which means no config screen
        return null;
    }
}
