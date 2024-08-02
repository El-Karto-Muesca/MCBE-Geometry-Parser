package me.k128.mcGeoParser.utilities;

public class Vector2f extends Vector<Float> {

    public Vector2f(float x, float y) {
        super(2);
        super.set(0, x);
        super.set(1, y);
    }

    public float getX() { return super.get(0); }
    public float getY() { return super.get(1); }
}
