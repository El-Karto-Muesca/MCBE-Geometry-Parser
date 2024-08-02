package me.k128.mcGeoParser;

public class McGeo {
    private final String formatVersion;
    private final McGeoDescription description;
    private final McGeoBone[] bones;

    public McGeo(String formatVersion, McGeoDescription description, McGeoBone[] bones) {
        this.formatVersion = formatVersion;
        this.description = description;
        this.bones = bones;
    }

    public String getFormatVersion() {
        return formatVersion;
    }
    public McGeoDescription getDescription() {
        return description;
    }
    public McGeoBone[] getBones() {
        return bones;
    }
}