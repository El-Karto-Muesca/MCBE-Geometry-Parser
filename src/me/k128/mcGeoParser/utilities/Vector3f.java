package me.k128.mcGeoParser.utilities;

public class Vector3f {

    private final float[] floats;
    
    public Vector3f(float x, float y, float z) {
        this.floats = new float[] {x, y, z};
    }

    public float getX() { return floats[0]; }
    public float getY() { return floats[1]; }
    public float getZ() { return floats[2]; }
}
