package me.k128.mcGeoParser;

import me.k128.mcGeoParser.utilities.Vector3f;

public record McGeoBone(
    String name,
    McGeoBone parent,
    Vector3f pivot,
    McGeoCube[] cubes
) {}
