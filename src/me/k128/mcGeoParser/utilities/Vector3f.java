package me.k128.mcGeoParser.utilities;

public class Vector3f extends Vector<Float> {

    public Vector3f(float x, float y, float z) {
        super(3);
        super.set(0, x);
        super.set(1, y);
        super.set(2, z);
    }

    public float getX() { return super.get(0); }
    public float getY() { return super.get(1); }
    public float getZ() { return super.get(2); }
}
