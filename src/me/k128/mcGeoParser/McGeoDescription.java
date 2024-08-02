package me.k128.mcGeoParser;

import me.k128.mcGeoParser.utilities.Vector2f;

public record McGeoDescription(
    String identifier,
    int textureWidth,
    int textureHeight,
    float visibleBoundsWidth,
    float visibleBoundsHeight,
    Vector2f visibleBoundsOffset
) {}
