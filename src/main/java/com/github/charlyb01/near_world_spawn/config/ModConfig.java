package com.github.charlyb01.near_world_spawn.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "near_world_spawn")
public class ModConfig implements ConfigData {
    public boolean changingArea = true;
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public AreaShape areaShape = AreaShape.BOX;
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public PlayerInfluence playerInfluence = PlayerInfluence.AREA_OFFSET;
    @ConfigEntry.BoundedDiscrete(min = 0, max = 10000)
    public int expand = 250;
    public boolean fixedLoadedChunks = true;

    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
    public static void save() {
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
