package com.github.charlyb01.near_world_spawn.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "near_world_spawn")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public SpawnType type = SpawnType.BOX;
    public boolean offset = true;
    @ConfigEntry.BoundedDiscrete(min = 0, max = 10000)
    public int expand = 250;

    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
    public static void save() {
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
