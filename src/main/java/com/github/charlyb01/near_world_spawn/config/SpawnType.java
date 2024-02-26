package com.github.charlyb01.near_world_spawn.config;

public enum SpawnType {
    VANILLA("vanilla"),
    BOX("box"),
    CIRCLE("circle");

    public final String name;

    SpawnType(final String name) {
        this.name = name;
    }
}
