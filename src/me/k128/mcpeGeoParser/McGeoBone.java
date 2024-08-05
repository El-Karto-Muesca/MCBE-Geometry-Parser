package me.k128.mcpeGeoParser;

public class McGeoBone {
    private final String name;
    private final String parentName;
    private McGeoBone parent = null;
    private final Vec3f pivot;
    private final McGeoCube[] cubes;
    private final McGeoLocator[] locators;

    McGeoBone(
        String name,
        String parentName,
        Vec3f pivot,
        McGeoCube[] cubes,
        McGeoLocator[] locators
    ) {
        this.name = name;
        this.parentName = parentName;
        this.pivot = pivot;
        this.cubes = cubes;
        this.locators = locators;
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
    public Vec3f getPivot() {
        return pivot;
    }
    public McGeoCube[] getCubes() {
        return cubes;
    }
    public McGeoLocator[] getLocators() {
        return locators;
    }
}
