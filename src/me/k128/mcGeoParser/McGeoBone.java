package me.k128.mcGeoParser;

import me.k128.mcGeoParser.utilities.Vector3f;

public class McGeoBone {
    private final String name;
    private final String parentName;
    private McGeoBone parent = null;
    private final Vector3f pivot;
    private final McGeoCube[] cubes;

    McGeoBone(
        String name,
        String parentName,
        Vector3f pivot,
        McGeoCube[] cubes
    ) {
        this.name = name;
        this.parentName = parentName;
        this.pivot = pivot;
        this.cubes = cubes;
    }

    public String getName() {
        return name;
    }
    public McGeoBone getParent() {
        return parent;
    }
    void setParent(McGeoBone parent) {
        this.parent = parent;
    }
    String getParentName() {
        return parentName;
    }
    public Vector3f getPivot() {
        return pivot;
    }
    public McGeoCube[] getCubes() {
        return cubes;
    }
}
