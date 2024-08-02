package me.k128.mcGeoParser;

import me.k128.mcGeoParser.utilities.Vector2i;
import me.k128.mcGeoParser.utilities.Vector3f;

public record McGeoCube(
    Vector3f origin,
    Vector3f size,
    Vector3f pivot,
    Vector3f rotation,
    Vector2i uv
) {}