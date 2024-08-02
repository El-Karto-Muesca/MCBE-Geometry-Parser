package me.k128.mcGeoParser;

public record McGeo(
    String formatVersion,
    McGeoDescription description,
    McGeoBone[] bones
) {}