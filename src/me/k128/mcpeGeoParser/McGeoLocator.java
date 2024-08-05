package me.k128.mcpeGeoParser;

public class McGeoLocator {
    private final String name;
    private final Vec3f coords;

    McGeoLocator(
        String name, 
        Vec3f coords
    ) {
        this.name = name;
        this.coords = coords;
    }
    
    public String getName() {
        return name;
    }
    public Vec3f getCoords() {
        return coords;
    }
}