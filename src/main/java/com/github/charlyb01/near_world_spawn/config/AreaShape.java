package com.github.charlyb01.near_world_spawn.config;

public enum AreaShape {
    BOX("box"),
    INNER_CIRCLE("inner_circle"),
    OUTER_CIRCLE("outer_circle");

    public final String name;

    AreaShape(final String name) {
        this.name = name;
    }

    public boolean isCircle() {
        return this.equals(INNER_CIRCLE) || this.equals(OUTER_CIRCLE);
    }
}
