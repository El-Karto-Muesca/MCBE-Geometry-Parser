package me.k128.mcpeGeoParser;

public class Vec3f {

    private final float[] floats;
    
    public Vec3f(float x, float y, float z) {
        this.floats = new float[] {x, y, z};
    }

    public float[] get() { return floats; }

    public float getX() { return floats[0]; }
    public float getY() { return floats[1]; }
    public float getZ() { return floats[2]; }
}
