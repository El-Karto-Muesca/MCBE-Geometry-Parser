package me.k128.mcpeGeoParser;

public class McGeo {
    private final String formatVersion;
    private final McGeoDescription description;
    private final McGeoBone[] bones;

    private final int cubeCount;

    McGeo(String formatVersion, McGeoDescription description, McGeoBone[] bones) {
        this.formatVersion = formatVersion;
        this.description = description;
        this.bones = bones;
        // counts the cubes of each bone and adds them up
        int cubeCount = 0;
        for (McGeoBone bone : bones) cubeCount += bone.getCubes().length;
        this.cubeCount = cubeCount;
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

    public int getBoneCount() {
        return bones.length;
    }
    public int getCubeCount() {
        return cubeCount;
    }
}