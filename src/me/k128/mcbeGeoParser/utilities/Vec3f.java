package me.k128.mcbeGeoParser.utilities;

public class Vec3f {

    private final float[] floats;
    
    public Vec3f() {
        this(0);
    }
    public Vec3f(float xyz) {
        this(xyz, xyz, xyz);
    }
    public Vec3f(float x, float y, float z) {
        this.floats = new float[] {x, y, z};
    }

    public float[] get() { return floats; }

    public float getX() { return floats[0]; }
    public float getY() { return floats[1]; }
    public float getZ() { return floats[2]; }
}