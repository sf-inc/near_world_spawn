package com.github.charlyb01.near_world_spawn.config;

public enum PlayerInfluence {
    NONE("none"),
    AREA_OFFSET("area_offset"),
    AREA_SHRINK("area_shrink");
    public final String name;

    PlayerInfluence(final String name) {
        this.name = name;
    }
}
