package me.k128.mcpeGeoParser;

public class McGeoGeometry {
    private final McGeoDescription description;
    private final McGeoBone[] bones;

    private final int cubeCount;

    McGeoGeometry(McGeoDescription description, McGeoBone[] bones) {
        this.description = description;
        this.bones = bones;
        // counts the cubes of each bone and adds them up
        int cubeCount = 0;
        for (McGeoBone bone : bones) cubeCount += bone.getCubes().length;
        this.cubeCount = cubeCount;
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
